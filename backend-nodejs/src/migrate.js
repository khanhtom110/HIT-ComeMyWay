import { fileURLToPath } from 'node:url';
import dotenv from 'dotenv';
import { loadConfig } from './configs/env.config.js';
import { createDatabasePool } from './configs/db.config.js';
import { runMigrations } from './utils/migrations.js';

dotenv.config({ path: fileURLToPath(new URL('../.env', import.meta.url)), quiet: true });
const pool = createDatabasePool(loadConfig().database);
try {
  await runMigrations(pool);
} catch (error) {
  console.error('Migration command failed:', error.code ?? error.name);
  process.exitCode = 1;
} finally {
  await pool.end();
}
