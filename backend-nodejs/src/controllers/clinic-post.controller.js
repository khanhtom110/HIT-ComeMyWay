import { sendResponse } from '../utils/response.js';

export function createClinicPostController(service) {
  return {
    async create(request, response) {
      const post = await service.create(request.clinic.id, request.validated.body);
      return sendResponse(response, 201, post, 'Đăng tin thành công');
    },
    async listByClinic(request, response) {
      return sendResponse(response, 200, await service.listByClinic(request.clinic.id));
    },
    async listPublic(request, response) {
      return sendResponse(response, 200, await service.listPublic());
    },
    async remove(request, response) {
      const result = await service.remove(request.clinic.id, request.validated.params.id);
      return sendResponse(response, 200, result, 'Xóa bài đăng thành công');
    },
  };
}
