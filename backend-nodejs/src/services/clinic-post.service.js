import { ApiError } from '../utils/ApiError.js';

export function createClinicPostService(clinicPostModel) {
  return {
    create(clinicId, { title, content, imageUrls }) {
      return clinicPostModel.create(clinicId, title, content, imageUrls);
    },
    listByClinic(clinicId) {
      return clinicPostModel.listByClinic(clinicId);
    },
    listPublic() {
      return clinicPostModel.listPublic();
    },
    async detailPublic(id) {
      const post = await clinicPostModel.findPublic(id);
      if (!post) throw new ApiError(404, 'Không tìm thấy bài đăng');
      return post;
    },
    async remove(clinicId, id) {
      const deleted = await clinicPostModel.deleteByClinic(id, clinicId);
      if (!deleted) throw new ApiError(404, 'Không tìm thấy bài đăng');
      return { id };
    },
  };
}
