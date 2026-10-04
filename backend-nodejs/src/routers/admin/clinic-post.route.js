import { Router } from 'express';
import Joi from 'joi';
import { validate } from '../../middlewares/index.js';
import { deleteClinicPostValidation } from '../../validations/index.js';
import { catchAsync } from '../../utils/catchAsync.js';
import { sendResponse } from '../../utils/response.js';

export function createAdminClinicPostRouter({ clinicPostService, authenticateAdmin }) {
  const router = Router();
  router.get('/admin/clinic-posts', authenticateAdmin, validate({
    query: Joi.object({ status: Joi.string().valid('PENDING', 'APPROVED').default('PENDING') }),
  }), catchAsync(async (request, response) => {
    return sendResponse(response, 200,
      await clinicPostService.listForAdmin(request.validated.query.status));
  }));
  router.patch('/admin/clinic-posts/:id/approve', authenticateAdmin,
    validate(deleteClinicPostValidation), catchAsync(async (request, response) => {
      return sendResponse(response, 200,
        await clinicPostService.approve(request.validated.params.id, request.admin.id), 'Đã duyệt bài đăng');
    }));
  return router;
}
