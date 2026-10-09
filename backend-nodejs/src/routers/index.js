import { Router } from 'express';
import { createClinicPostRouter } from './client/clinic-post.route.js';
import { createAdminStatisticsRouter } from './admin/statistics.route.js';
import { createAdminClinicPostRouter } from './admin/clinic-post.route.js';

export { createHealthRouter } from './health.route.js';

export function createApiRouter(dependencies) {
  const router = Router();
  router.use(createClinicPostRouter(dependencies));
  router.use(createAdminStatisticsRouter(dependencies));
  router.use(createAdminClinicPostRouter(dependencies));
  return router;
}
