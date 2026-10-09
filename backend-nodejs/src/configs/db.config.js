import mysql from 'mysql2/promise';

export function createDatabasePool(databaseConfig) {
  return mysql.createPool({
    ...databaseConfig,
    waitForConnections: true,
    connectionLimit: 10,
    connectTimeout: 5000,
    timezone: 'Z',
  });
}
