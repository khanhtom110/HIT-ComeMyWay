import { Router } from 'express';
import { catchAsync } from '../utils/catchAsync.js';

export function createHealthRouter(controller) {
  const router = Router();
  router.get('/', controller.live);
  router.get('/live', controller.live);
  router.get('/ready', catchAsync(controller.ready));
  return router;
}
