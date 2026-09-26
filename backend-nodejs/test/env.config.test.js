import assert from 'node:assert/strict';
import { test } from 'node:test';
import { loadConfig } from '../src/configs/env.config.js';

test('environment validation names invalid variables without printing their values', () => {
  const env = { JWT_SECRET: 'short-sensitive-value', DB_USERNAME: 'root', DB_PASSWORD: '' };
  assert.throws(() => loadConfig(env), error => {
    assert.match(error.message, /JWT_SECRET/);
    assert.ok(!error.message.includes(env.JWT_SECRET));
    return true;
  });
});

test('environment loads MySQL and the configured browser origin list', () => {
  const config = loadConfig({
    JWT_SECRET: 'test-secret-at-least-32-characters-long', DB_USERNAME: 'root', DB_PASSWORD: '',
    PORT: '3002', CORS_ORIGINS: 'http://localhost:3000, https://example.test/',
  });
  assert.equal(config.server.port, 3002);
  assert.equal(config.database.database, 'pet_heartbeat_db');
  assert.deepEqual(config.corsOrigins, ['http://localhost:3000', 'https://example.test']);
});
