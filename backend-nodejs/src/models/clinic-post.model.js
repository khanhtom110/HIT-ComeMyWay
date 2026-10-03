import { CLINIC_POST_LIMITS } from '../constants/clinic-post.constant.js';

export function createClinicPostModel(pool) {
  const select = `SELECT p.id, p.clinic_id AS clinicId, c.name AS clinicName, p.title,
    p.content, p.image_urls AS imageUrls, p.created_at AS createdAt
    FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id`;
  const decode = row => ({ ...row, imageUrls: typeof row.imageUrls === 'string'
    ? JSON.parse(row.imageUrls) : (row.imageUrls ?? []) });
  return {
    async create(clinicId, title, content, imageUrls = []) {
      const [result] = await pool.execute(
        'INSERT INTO clinic_posts (clinic_id, title, content, image_urls, created_at) VALUES (?, ?, ?, ?, UTC_TIMESTAMP(3))',
        [clinicId, title, content, JSON.stringify(imageUrls)],
      );
      const [rows] = await pool.execute(
        `${select} WHERE p.id = ?`,
        [result.insertId],
      );
      return decode(rows[0]);
    },
    async listByClinic(clinicId) {
      const [rows] = await pool.execute(
        `${select} WHERE p.clinic_id = ? ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`,
        [clinicId],
      );
      return rows.map(decode);
    },
    async listPublic() {
      const [rows] = await pool.execute(
        `${select} ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`,
      );
      return rows.map(decode);
    },
    async findPublic(id) {
      const [rows] = await pool.execute(`${select} WHERE p.id = ?`, [id]);
      return rows[0] ? decode(rows[0]) : null;
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
