import { createApp } from './app.js';
import { createDatabasePool } from './config/database.js';
import { loadConfig } from './config/env.js';
import { createClinicPostRepository } from './modules/clinic-posts/clinic-post.repository.js';

const config = loadConfig();
const pool = createDatabasePool(config.database);
const repository = createClinicPostRepository(pool);
const app = createApp({ repository, secret: config.jwtSecret });

app.listen(config.server.port, config.server.host, () => {
  console.log(`Clinic posts service listening on ${config.server.host}:${config.server.port}`);
});
