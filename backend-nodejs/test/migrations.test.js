import assert from 'node:assert/strict';
import { test } from 'node:test';
import { runMigrations } from '../src/utils/migrations.js';

const basicColumns = ['id', 'clinic_id', 'title', 'content', 'created_at'];
const enumTwo = "enum('PENDING','APPROVED')";
const enumThree = "enum('PENDING','APPROVED','REJECTED')";
const logger = { info() {}, error() {} };
function fixture({ ready = false, partial = false, locked = true, failDDL = false, failRecord = false } = {}) {
  const columns = new Map((ready || partial ? basicColumns : []).map(name => [name, 'text']));
  const indexes = new Set();
  if (ready || partial) columns.set('image_urls', 'json');
  if (ready) {
    columns.set('status', enumThree);
    columns.set('approved_by', 'bigint');
    columns.set('approved_at', 'datetime(3)');
    indexes.add('idx_clinic_posts_status_id');
  }
  if (partial) columns.set('status', enumTwo);
  const history = [];
  const calls = [];
  let released = 0;
  const connection = {
    async query(sql) {
      calls.push(sql);
      if (sql === 'SELECT DATABASE() AS name') return [[{ name: 'migration_test' }]];
      if (sql === 'SELECT name, checksum FROM node_schema_migrations') return [history];
      if (sql.includes('CREATE TABLE IF NOT EXISTS clinic_posts')) basicColumns.forEach(name => columns.set(name, 'text'));
      if (sql.includes('ALTER TABLE clinic_posts')) {
        if (failDDL) throw new Error('DDL failed');
        if (sql.includes('ADD COLUMN image_urls')) columns.set('image_urls', 'json');
        if (sql.includes('ADD COLUMN status')) columns.set('status', enumTwo);
        if (sql.includes('ADD COLUMN approved_by')) columns.set('approved_by', 'bigint');
        if (sql.includes('ADD COLUMN approved_at')) columns.set('approved_at', 'datetime(3)');
        if (sql.includes('ADD INDEX')) indexes.add('idx_clinic_posts_status_id');
        if (sql.includes('MODIFY COLUMN status')) columns.set('status', enumThree);
      }
      return [[]];
    },
    async execute(sql, params) {
      calls.push(sql);
      if (sql.includes('GET_LOCK')) return [[{ acquired: locked ? 1 : 0 }]];
      if (sql.includes('information_schema.COLUMNS')) return [[...columns].map(([name, type]) => ({ name, type }))];
      if (sql.includes('information_schema.STATISTICS')) return [[...indexes].map(name => ({ name }))];
      if (sql.startsWith('INSERT INTO node_schema_migrations')) {
        if (failRecord) throw new Error('Record failed');
        history.push({ name: params[0], checksum: params[1] });
      }
      return [[]];
    },
    release() { released++; },
  };
  return { pool: { async getConnection() { return connection; } }, columns, indexes, history, calls,
    get released() { return released; }, resumeHistoryWrites() { failRecord = false; } };
}

test('fresh schema runs migrations in order; second startup performs no DDL', async () => {
  const f = fixture();
  await runMigrations(f.pool, { logger });
  assert.deepEqual(f.history.map(row => row.name), ['001_create_clinic_posts.sql', '002_add_clinic_post_images.sql', '003_clinic_post_moderation.sql', '004_clinic_post_rejection.sql']);
  assert.equal(f.columns.get('status'), enumThree);
  const before = f.calls.filter(sql => sql.includes('ALTER TABLE clinic_posts')).length;
  await runMigrations(f.pool, { logger });
  assert.equal(f.calls.filter(sql => sql.includes('ALTER TABLE clinic_posts')).length, before);
  assert.equal(f.history.length, 4);
  assert.equal(f.released, 2);
});

test('manually migrated database is adopted without changing existing post data', async () => {
  const f = fixture({ ready: true });
  await runMigrations(f.pool, { logger });
  assert.equal(f.history.length, 4);
  assert.equal(f.calls.some(sql => /ALTER TABLE clinic_posts|UPDATE clinic_posts|DELETE FROM clinic_posts/.test(sql)), false);
});

test('partial moderation migration adds only missing columns and index', async () => {
  const f = fixture({ partial: true });
  await runMigrations(f.pool, { logger });
  const sql = f.calls.find(sql => sql.includes('ADD COLUMN approved_by'));
  assert.doesNotMatch(sql, /ADD COLUMN status/);
  assert.match(sql, /ADD COLUMN approved_at/);
  assert.match(sql, /ADD INDEX/);
  assert.equal(f.columns.get('status'), enumThree);
});

test('unavailable lock stops before DDL and releases connection', async () => {
  const f = fixture({ locked: false });
  await assert.rejects(runMigrations(f.pool, { logger }), /migration lock/);
  assert.equal(f.calls.some(sql => sql.includes('CREATE TABLE')), false);
  assert.equal(f.calls.some(sql => sql.includes('RELEASE_LOCK')), false);
  assert.equal(f.released, 1);
});

test('failed DDL is not marked applied and the lock is released', async () => {
  const f = fixture({ failDDL: true });
  await assert.rejects(runMigrations(f.pool, { logger }), /DDL failed/);
  assert.equal(f.history.length, 1);
  assert.ok(f.calls.some(sql => sql.includes('RELEASE_LOCK')));
  assert.equal(f.released, 1);
});

test('existing DDL is adopted after a failed history write', async () => {
  const f = fixture({ failRecord: true });
  await assert.rejects(runMigrations(f.pool, { logger }), /Record failed/);
  assert.equal(f.history.length, 0);
  assert.ok(f.columns.has('clinic_id'));
  assert.equal(f.released, 1);
  f.resumeHistoryWrites();
  await runMigrations(f.pool, { logger });
  assert.equal(f.history.length, 4);
  assert.equal(f.calls.filter(sql => sql.includes('CREATE TABLE IF NOT EXISTS clinic_posts')).length, 1);
  assert.equal(f.released, 2);
});

test('modified applied migration fails instead of rerunning', async () => {
  const f = fixture({ ready: true });
  await runMigrations(f.pool, { logger });
  f.history[0].checksum = 'changed';
  await assert.rejects(runMigrations(f.pool, { logger }), /Applied migration was modified/);
  assert.equal(f.history.length, 4);
  assert.equal(f.released, 2);
});
