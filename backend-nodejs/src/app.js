import express from 'express';
import cors from 'cors';
import { ApiError } from './utils/ApiError.js';
import { createClinicAuth } from './middlewares/clinic-auth.middleware.js';
import { handleError, notFound } from './middlewares/error.middleware.js';
import { createClinicPostController } from './controllers/clinic-post.controller.js';
import { createHealthController } from './controllers/health.controller.js';
import { createApiRouter } from './routers/index.js';
import { createHealthRouter } from './routers/health.route.js';
import { createClinicPostService } from './services/clinic-post.service.js';

export function createApp({
  clinicPostModel, clinicModel, jwtSecret, corsOrigins = [], checkReadiness = async () => false,
}) {
  if (!clinicPostModel || !clinicModel || !jwtSecret) {
    throw new Error('Missing models or JWT_SECRET');
  }

  const app = express();
  app.disable('x-powered-by');
  app.use(cors({
    origin(origin, callback) {
      if (!origin || corsOrigins.includes(origin)) return callback(null, true);
      return callback(new ApiError(403, 'Origin không được phép'));
    },
  }));
  app.use(express.json({ limit: '64kb' }));

  const service = createClinicPostService(clinicPostModel);
  const controller = createClinicPostController(service);
  const authenticateClinic = createClinicAuth({ clinicModel, jwtSecret });
  app.use('/health', createHealthRouter(createHealthController(checkReadiness)));
  app.use('/api/v1', createApiRouter({ controller, authenticateClinic }));
  app.use(notFound);
  app.use(handleError);
  return app;
}
