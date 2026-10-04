import { CLINIC_POST_LIMITS } from '../../constants/index.js';

export function createClinicPostModel(pool) {
  const select = `SELECT p.id, p.clinic_id AS clinicId, c.name AS clinicName,
    p.title, p.content, p.image_urls AS imageUrls, p.status,
    p.approved_by AS approvedBy, p.approved_at AS approvedAt, p.created_at AS createdAt
    FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id`;
  const decode = row => ({ ...row, imageUrls: typeof row.imageUrls === 'string'
    ? JSON.parse(row.imageUrls) : (row.imageUrls ?? []) });
  async function find(id, approvedOnly = false) {
    const [rows] = await pool.execute(
      `${select} WHERE p.id = ?${approvedOnly ? " AND p.status = 'APPROVED'" : ''}`, [id]);
    return rows[0] ? decode(rows[0]) : null;
  }
  return {
    async create(clinicId, title, content, imageUrls = []) {
      const [result] = await pool.execute(
        "INSERT INTO clinic_posts (clinic_id, title, content, image_urls, status, created_at) VALUES (?, ?, ?, ?, 'PENDING', UTC_TIMESTAMP(3))",
        [clinicId, title, content, JSON.stringify(imageUrls)],
      );
      return find(result.insertId);
    },
    async listByClinic(clinicId) {
      const [rows] = await pool.execute(
        `${select}
         WHERE p.clinic_id = ? ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`,
        [clinicId],
      );
      return rows.map(decode);
    },
    async listPublic() {
      const [rows] = await pool.execute(
        `${select} WHERE p.status = 'APPROVED'
         ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`,
      );
      return rows.map(decode);
    },
    findPublic(id) { return find(id, true); },
    async listForAdmin(status) {
      const [rows] = await pool.execute(
        `${select} WHERE p.status = ? ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`, [status]);
      return rows.map(decode);
    },
    async approve(id, adminId) {
      await pool.execute(
        `UPDATE clinic_posts SET status = 'APPROVED', approved_by = ?, approved_at = UTC_TIMESTAMP(3)
         WHERE id = ? AND status = 'PENDING'`, [adminId, id]);
      return find(id);
    },
    async deleteByClinic(id, clinicId) {
      const [result] = await pool.execute(
        'DELETE FROM clinic_posts WHERE id = ? AND clinic_id = ?',
        [id, clinicId],
      );
      return result.affectedRows === 1;
    },
  };
}
