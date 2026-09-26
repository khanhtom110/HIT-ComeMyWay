import mysql from 'mysql2/promise';
import { createApp } from './app.js';
import { createRepository } from './repository.js';

const secret = process.env.JWT_SECRET;
if (!secret) throw new Error('JWT_SECRET is required and must match the Spring Boot backend');

const pool = mysql.createPool({
  host: process.env.DB_HOST ?? 'localhost',
  port: Number(process.env.DB_PORT ?? 3306),
  user: process.env.DB_USERNAME ?? 'root',
  password: process.env.DB_PASSWORD ?? 'root',
  database: process.env.DB_NAME ?? 'pet_heartbeat_db',
  waitForConnections: true,
  connectionLimit: 10,
  timezone: 'Z',
});

const port = Number(process.env.PORT ?? 3001);
const host = process.env.HOST ?? '127.0.0.1';
createApp({ repository: createRepository(pool), secret }).listen(port, host, () => {
  console.log(`Clinic posts service listening on ${host}:${port}`);
});
