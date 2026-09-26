import { createServer } from 'node:http';
import { fileURLToPath } from 'node:url';
import dotenv from 'dotenv';
import { createApp } from './app.js';
import { createDatabasePool } from './configs/db.config.js';
import { loadConfig } from './configs/env.config.js';
import { createClinicPostModel } from './models/clinic-post.model.js';
import { createClinicModel } from './models/clinic.model.js';

dotenv.config({ path: fileURLToPath(new URL('../.env', import.meta.url)), quiet: true });
const config = loadConfig();
const pool = createDatabasePool(config.database);

try {
  await pool.query('SELECT 1');
  const app = createApp({
    clinicPostModel: createClinicPostModel(pool),
    clinicModel: createClinicModel(pool),
    jwtSecret: config.jwtSecret,
    corsOrigins: config.corsOrigins,
    checkReadiness: async () => {
      await pool.query('SELECT 1');
      return true;
    },
  });
  const server = createServer(app);
  await new Promise((resolve, reject) => {
    server.once('error', reject);
    server.listen(config.server.port, config.server.host, () => {
      server.off('error', reject);
      resolve();
    });
  });
  console.log(`Backend listening on ${config.server.host}:${config.server.port}`);

  let shuttingDown = false;
  const shutdown = () => {
    if (shuttingDown) return;
    shuttingDown = true;
    server.close(() => {
      pool.end().catch(() => { process.exitCode = 1; });
    });
  };
  process.once('SIGINT', shutdown);
  process.once('SIGTERM', shutdown);
} catch (error) {
  console.error('Backend startup failed:', error.code ?? error.name);
  await pool.end();
  process.exitCode = 1;
}
