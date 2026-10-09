import express from 'express';
import cors from 'cors';
import swaggerUi from 'swagger-ui-express';
import { openApiDocument } from './docs/openapi.js';
import { ApiError } from './utils/ApiError.js';
import { createClinicAuth, createAdminAuth, handleError, notFound } from './middlewares/index.js';
import { createClinicPostController, createAdminStatisticsController,
  createHealthController } from './controllers/index.js';
import { createApiRouter, createHealthRouter } from './routers/index.js';
import { createClinicPostService } from './services/index.js';

export function createApp({
  clinicPostModel, clinicModel, adminStatisticsModel, jwtSecret,
  corsOrigins = [], checkReadiness = async () => false,
}) {
  if (!clinicPostModel || !clinicModel || !adminStatisticsModel || !jwtSecret) {
    throw new Error('Missing models or JWT_SECRET');
  }

  const app = express();
  app.disable('x-powered-by');
  app.use((request, response, next) => cors({
    origin(origin, callback) {
      const sameOrigin = `${request.protocol}://${request.get('host')}`;
      if (!origin || origin === sameOrigin || corsOrigins.includes(origin)) {
        return callback(null, true);
      }
      return callback(new ApiError(403, 'Origin không được phép'));
    },
  })(request, response, next));
  app.use(express.json({ limit: '64kb' }));

  const service = createClinicPostService(clinicPostModel);
  const controller = createClinicPostController(service);
  const authenticateClinic = createClinicAuth({ clinicModel, jwtSecret });
  const authenticateAdmin = createAdminAuth({ adminStatisticsModel, jwtSecret });
  const adminStatisticsController = createAdminStatisticsController(adminStatisticsModel);
  app.use('/health', createHealthRouter(createHealthController(checkReadiness)));
  app.use('/api/v1', createApiRouter({
    controller, authenticateClinic, adminStatisticsController, authenticateAdmin, clinicPostService: service,
  }));
  app.get('/api-docs/openapi.json', (request, response) => response.json(openApiDocument));
  app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(openApiDocument));
  app.use(notFound);
  app.use(handleError);
  return app;
}
