import { CLINIC_POST_LIMITS } from '../constants/clinic-post.constant.js';

export function createClinicPostModel(pool) {
  return {
    async create(clinicId, title, content) {
      const [result] = await pool.execute(
        'INSERT INTO clinic_posts (clinic_id, title, content, created_at) VALUES (?, ?, ?, UTC_TIMESTAMP(3))',
        [clinicId, title, content],
      );
      const [rows] = await pool.execute(
        `SELECT p.id, p.clinic_id AS clinicId, c.name AS clinicName, p.title,
                p.content, p.created_at AS createdAt
         FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id WHERE p.id = ?`,
        [result.insertId],
      );
      return rows[0];
    },
    async listByClinic(clinicId) {
      const [rows] = await pool.execute(
        `SELECT p.id, p.clinic_id AS clinicId, c.name AS clinicName, p.title,
                p.content, p.created_at AS createdAt
         FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id
         WHERE p.clinic_id = ? ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`,
        [clinicId],
      );
      return rows;
    },
    async listPublic() {
      const [rows] = await pool.execute(
        `SELECT p.id, p.clinic_id AS clinicId, c.name AS clinicName, p.title,
                p.content, p.created_at AS createdAt
         FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id
         ORDER BY p.id DESC LIMIT ${CLINIC_POST_LIMITS.LIST_SIZE}`,
      );
      return rows;
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
