import { CLINIC_POST_LIMITS } from '../../constants/index.js';
import { ApiError } from '../../utils/ApiError.js';

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
    async listForAdmin(status, { beforeId, limit = CLINIC_POST_LIMITS.LIST_SIZE } = {}) {
      const rows = await clinicPostModel.listForAdmin(status, { beforeId, limit });
      const hasMore = rows.length > limit;
      const items = rows.slice(0, limit);
      return { items, pagination: { limit, hasMore, nextBeforeId: hasMore ? items.at(-1).id : null } };
    },
    async approve(id, adminId) {
      const post = await clinicPostModel.approve(id, adminId);
      if (!post) throw new ApiError(404, 'Không tìm thấy bài đăng');
      if (post.status !== 'APPROVED') throw new ApiError(409, 'Bài đăng đã bị từ chối duyệt');
      return post;
    },
    async reject(id) {
      const post = await clinicPostModel.reject(id);
      if (!post) throw new ApiError(404, 'Không tìm thấy bài đăng');
      if (post.status !== 'REJECTED') throw new ApiError(409, 'Bài đăng đã được duyệt');
      return post;
    },
    async remove(clinicId, id) {
      const deleted = await clinicPostModel.deleteByClinic(id, clinicId);
      if (!deleted) throw new ApiError(404, 'Không tìm thấy bài đăng');
      return { id };
    },
  };
}
