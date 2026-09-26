import mysql from 'mysql2/promise';

export function createDatabasePool(databaseConfig) {
  return mysql.createPool({
    ...databaseConfig,
    waitForConnections: true,
    connectionLimit: 10,
    timezone: 'Z',
  });
}
