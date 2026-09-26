import { readJson, sendJson } from '../../http/json.js';

export function createClinicPostController(service) {
  return {
    async create(request, response, clinic) {
      const input = await readJson(request);
      const post = await service.create(clinic.id, input);
      sendJson(response, 201, post, 'Đăng tin thành công');
    },
    async listByClinic(request, response, clinic) {
      sendJson(response, 200, await service.listByClinic(clinic.id));
    },
    async listPublic(request, response) {
      sendJson(response, 200, await service.listPublic());
    },
  };
}
