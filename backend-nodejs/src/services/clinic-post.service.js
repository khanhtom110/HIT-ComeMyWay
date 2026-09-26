import { ApiError } from '../utils/ApiError.js';

export function createClinicPostService(clinicPostModel) {
  return {
    create(clinicId, { title, content }) {
      return clinicPostModel.create(clinicId, title, content);
    },
    listByClinic(clinicId) {
      return clinicPostModel.listByClinic(clinicId);
    },
    listPublic() {
      return clinicPostModel.listPublic();
    },
    async remove(clinicId, id) {
      const deleted = await clinicPostModel.deleteByClinic(id, clinicId);
      if (!deleted) throw new ApiError(404, 'Không tìm thấy bài đăng');
      return { id };
    },
  };
}
