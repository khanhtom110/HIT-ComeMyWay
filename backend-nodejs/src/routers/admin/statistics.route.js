import { Router } from 'express';
import { catchAsync } from '../../utils/catchAsync.js';

export function createAdminStatisticsRouter({ adminStatisticsController, authenticateAdmin }) {
  const router = Router();
  router.get('/admin/statistics', authenticateAdmin,
    catchAsync(adminStatisticsController.getStatistics));
  return router;
}
