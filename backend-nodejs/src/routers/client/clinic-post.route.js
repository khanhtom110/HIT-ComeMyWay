import { Router } from 'express';
import { validate } from '../../middlewares/index.js';
import { catchAsync } from '../../utils/catchAsync.js';
import { createClinicPostValidation, deleteClinicPostValidation, listClinicPostsValidation } from '../../validations/index.js';

export function createClinicPostRouter({ controller, authenticateClinic }) {
  const router = Router();
  router.post('/clinic/posts', authenticateClinic, validate(createClinicPostValidation),
    catchAsync(controller.create));
  router.get('/clinic/posts', authenticateClinic, validate(listClinicPostsValidation), catchAsync(controller.listByClinic));
  router.get('/clinic/posts/counts', authenticateClinic, catchAsync(controller.countsByClinic));
  router.delete('/clinic/posts/:id', authenticateClinic, validate(deleteClinicPostValidation),
    catchAsync(controller.remove));
  router.get('/public/clinic-posts', catchAsync(controller.listPublic));
  router.get('/public/clinic-posts/:id', validate(deleteClinicPostValidation), catchAsync(controller.detailPublic));
  return router;
}
