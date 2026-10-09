import { Router } from 'express';
import Joi from 'joi';
import { validate } from '../../middlewares/index.js';
import { deleteClinicPostValidation } from '../../validations/index.js';
import { catchAsync } from '../../utils/catchAsync.js';
import { sendResponse } from '../../utils/response.js';

export function createAdminClinicPostRouter({ clinicPostService, authenticateAdmin }) {
  const router = Router();
  router.get('/admin/clinic-posts', authenticateAdmin, validate({
    query: Joi.object({
      status: Joi.string().valid('PENDING', 'APPROVED', 'REJECTED').default('PENDING'),
      limit: Joi.number().integer().min(1).max(50).default(50),
      beforeId: Joi.number().integer().positive().max(Number.MAX_SAFE_INTEGER),
    }),
  }), catchAsync(async (request, response) => {
    const { status, limit, beforeId } = request.validated.query;
    const { items, pagination } = await clinicPostService.listForAdmin(status, { limit, beforeId });
    return sendResponse(response, 200, items, 'OK', pagination);
  }));
  router.get('/admin/clinic-posts/counts', authenticateAdmin, catchAsync(async (request, response) => {
    return sendResponse(response, 200, await clinicPostService.countsForAdmin());
  }));
  router.get('/admin/clinic-posts/:id', authenticateAdmin,
    validate(deleteClinicPostValidation), catchAsync(async (request, response) => {
      return sendResponse(response, 200,
        await clinicPostService.detailForAdmin(request.validated.params.id));
    }));
  router.patch('/admin/clinic-posts/:id/approve', authenticateAdmin,
    validate(deleteClinicPostValidation), catchAsync(async (request, response) => {
      return sendResponse(response, 200,
        await clinicPostService.approve(request.validated.params.id, request.admin.id), 'Đã duyệt bài đăng');
    }));
  router.patch('/admin/clinic-posts/:id/reject', authenticateAdmin,
    validate(deleteClinicPostValidation), catchAsync(async (request, response) => {
      return sendResponse(response, 200,
        await clinicPostService.reject(request.validated.params.id), 'Đã từ chối duyệt bài đăng');
    }));
  return router;
}
