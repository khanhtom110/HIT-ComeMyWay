import { Router } from 'express';
import { createClinicPostRouter } from './clinic-post.route.js';

export function createApiRouter(dependencies) {
  const router = Router();
  router.use(createClinicPostRouter(dependencies));
  return router;
}
