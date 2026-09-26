import { Router } from 'express';
import { validate } from '../middlewares/validate.middleware.js';
import { catchAsync } from '../utils/catchAsync.js';
import { createClinicPostValidation } from '../validations/clinic-post.validation.js';

export function createClinicPostRouter({ controller, authenticateClinic }) {
  const router = Router();
  router.post('/clinic/posts', authenticateClinic, validate(createClinicPostValidation),
    catchAsync(controller.create));
  router.get('/clinic/posts', authenticateClinic, catchAsync(controller.listByClinic));
  router.get('/public/clinic-posts', catchAsync(controller.listPublic));
  return router;
}
