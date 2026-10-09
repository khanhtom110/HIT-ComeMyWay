import { createHash } from 'node:crypto';
import { readFile, readdir } from 'node:fs/promises';

const directory = new URL('../../database/migrations/', import.meta.url);
const checksum = sql => createHash('sha256').update(sql).digest('hex');

async function readSchema(connection) {
  const [columns] = await connection.execute(
    'SELECT COLUMN_NAME AS name, COLUMN_TYPE AS type FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?', ['clinic_posts']);
  const [indexes] = await connection.execute(
    'SELECT DISTINCT INDEX_NAME AS name FROM information_schema.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?', ['clinic_posts']);
  return { columns: new Map(columns.map(column => [column.name, column.type])), indexes: new Set(indexes.map(index => index.name)) };
}

// Adopt SQL already run manually; also recover a partially applied migration 003.
function pendingSql(name, sql, schema) {
  const columns = schema.columns;
  switch (name) {
    case '001_create_clinic_posts.sql':
      if (columns.size === 0) return sql;
      if (!['id', 'clinic_id', 'title', 'content', 'created_at'].every(column => columns.has(column))) {
        throw new Error('clinic_posts has an incompatible existing schema');
      }
      return null;
    case '002_add_clinic_post_images.sql':
      return columns.has('image_urls') ? null : sql;
    case '003_clinic_post_moderation.sql': {
      const additions = [];
      if (!columns.has('status')) additions.push("ADD COLUMN status ENUM('PENDING', 'APPROVED') NOT NULL DEFAULT 'PENDING'");
      if (!columns.has('approved_by')) additions.push('ADD COLUMN approved_by BIGINT NULL');
      if (!columns.has('approved_at')) additions.push('ADD COLUMN approved_at DATETIME(3) NULL');
      if (!schema.indexes.has('idx_clinic_posts_status_id')) additions.push('ADD INDEX idx_clinic_posts_status_id (status, id)');
      return additions.length ? `ALTER TABLE clinic_posts ${additions.join(', ')}` : null;
    }
    case '004_clinic_post_rejection.sql':
      return columns.get('status') === "enum('PENDING','APPROVED','REJECTED')" ? null : sql;
    default:
      return sql;
  }
}

export async function runMigrations(pool, { logger = console } = {}) {
  const names = (await readdir(directory)).filter(name => /^\d+_.+\.sql$/.test(name)).sort();
  if (names.length === 0) throw new Error('No database migration files found');
  const files = await Promise.all(names.map(async name => ({ name, sql: (await readFile(new URL(name, directory), 'utf8')).replace(/\r\n/g, '\n') })));
  const connection = await pool.getConnection();
  let lockName;
  let locked = false;
  let current;
  try {
    const [[database]] = await connection.query('SELECT DATABASE() AS name');
    if (!database?.name) throw new Error('No database selected for migrations');
    lockName = `comemyway:migrations:${checksum(database.name).slice(0, 40)}`;
    const [[lock]] = await connection.execute('SELECT GET_LOCK(?, 30) AS acquired', [lockName]);
    if (Number(lock?.acquired) !== 1) throw new Error('Could not acquire database migration lock');
    locked = true;
    await connection.query(`CREATE TABLE IF NOT EXISTS node_schema_migrations (
      name VARCHAR(255) NOT NULL PRIMARY KEY,
      checksum CHAR(64) NOT NULL,
      applied_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
    )`);
    const [history] = await connection.query('SELECT name, checksum FROM node_schema_migrations');
    const applied = new Map(history.map(row => [row.name, row.checksum]));
    for (const file of files) {
      current = file.name;
      const hash = checksum(file.sql);
      if (applied.has(file.name)) {
        if (applied.get(file.name) !== hash) throw new Error(`Applied migration was modified: ${file.name}`);
        continue;
      }
      const statement = pendingSql(file.name, file.sql, await readSchema(connection));
      if (statement) await connection.query(statement);
      await connection.execute('INSERT INTO node_schema_migrations (name, checksum) VALUES (?, ?)', [file.name, hash]);
      logger.info(`Migration ${statement ? 'applied' : 'adopted'}: ${file.name}`);
    }
  } catch (error) {
    logger.error(`Database migration failed${current ? `: ${current}` : ''}`);
    throw error;
  } finally {
    try { if (locked) await connection.execute('SELECT RELEASE_LOCK(?)', [lockName]); }
    finally { connection.release(); }
  }
}
