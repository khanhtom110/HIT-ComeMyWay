export function loadConfig(env = process.env) {
  if (!env.JWT_SECRET) {
    throw new Error('JWT_SECRET is required and must match the Spring Boot backend');
  }

  return {
    server: { host: env.HOST ?? '127.0.0.1', port: Number(env.PORT ?? 3001) },
    jwtSecret: env.JWT_SECRET,
    database: {
      host: env.DB_HOST ?? 'localhost',
      port: Number(env.DB_PORT ?? 3306),
      user: env.DB_USERNAME ?? 'root',
      password: env.DB_PASSWORD ?? 'root',
      database: env.DB_NAME ?? 'pet_heartbeat_db',
    },
  };
}
