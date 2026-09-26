import Joi from 'joi';

const envSchema = Joi.object({
  NODE_ENV: Joi.string().valid('development', 'production', 'test').default('development'),
  HOST: Joi.string().default('127.0.0.1'),
  PORT: Joi.number().integer().min(1).max(65535).default(3001),
  JWT_SECRET: Joi.string().min(32).required(),
  DB_HOST: Joi.string().default('localhost'),
  DB_PORT: Joi.number().integer().min(1).max(65535).default(3306),
  DB_NAME: Joi.string().default('pet_heartbeat_db'),
  DB_USERNAME: Joi.string().required(),
  DB_PASSWORD: Joi.string().allow('').required(),
  CORS_ORIGINS: Joi.string().allow('').default(''),
}).unknown(true);

export function loadConfig(env = process.env) {
  const { value, error } = envSchema.validate(env, { abortEarly: false });
  if (error) {
    const fields = error.details.map(detail => detail.path.join('.')).join(', ');
    throw new Error(`Invalid environment variables: ${fields}`);
  }

  const origins = value.CORS_ORIGINS.split(',').map(origin => origin.trim()).filter(Boolean);
  const originsSchema = Joi.array().items(Joi.string().uri({ scheme: ['http', 'https'] }));
  if (originsSchema.validate(origins).error) throw new Error('Invalid environment variable: CORS_ORIGINS');

  return {
    nodeEnv: value.NODE_ENV,
    server: { host: value.HOST, port: value.PORT },
    jwtSecret: value.JWT_SECRET,
    corsOrigins: origins.map(origin => new URL(origin).origin),
    database: {
      host: value.DB_HOST,
      port: value.DB_PORT,
      user: value.DB_USERNAME,
      password: value.DB_PASSWORD,
      database: value.DB_NAME,
    },
  };
}
