import { createServer } from 'node:http';
import { HttpError } from './http/http-error.js';
import { sendJson } from './http/json.js';
import { createClinicAuth } from './middlewares/clinic-auth.middleware.js';
import { handleError } from './middlewares/error.middleware.js';
import { createClinicPostController } from './modules/clinic-posts/clinic-post.controller.js';
import { createClinicPostRoutes } from './modules/clinic-posts/clinic-post.routes.js';
import { createClinicPostService } from './modules/clinic-posts/clinic-post.service.js';

export function createApp({ repository, secret }) {
  if (!repository || !secret) throw new Error('Missing repository or JWT_SECRET');

  const service = createClinicPostService(repository);
  const controller = createClinicPostController(service);
  const authenticateClinic = createClinicAuth({ repository, secret });
  const routes = [
    {
      method: 'GET',
      path: '/health',
      handler: (request, response) => sendJson(response, 200, { status: 'ok' }),
    },
    ...createClinicPostRoutes({ controller, authenticateClinic }),
  ];

  return createServer(async (request, response) => {
    try {
      const path = new URL(request.url, 'http://localhost').pathname;
      const route = routes.find(item => item.method === request.method && item.path === path);
      if (!route) throw new HttpError(404, 'Không tìm thấy API');
      await route.handler(request, response);
    } catch (error) {
      handleError(error, response);
    }
  });
}
