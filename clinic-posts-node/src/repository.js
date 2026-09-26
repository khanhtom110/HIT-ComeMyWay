export function createRepository(pool) {
  return {
    async findClinicByUsername(username, jti) {
      const [rows] = await pool.execute(
        `SELECT c.id FROM clinics c JOIN users u ON u.id = c.user_id
         WHERE u.username = ? AND u.role = 'CLINIC' AND u.status = 'ACTIVE'
           AND NOT EXISTS (SELECT 1 FROM invalidated_token t WHERE t.id = ?)
         LIMIT 1`,
        [username, jti],
      );
      return rows[0] ?? null;
    },
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
         WHERE p.clinic_id = ? ORDER BY p.id DESC LIMIT 50`,
        [clinicId],
      );
      return rows;
    },
    async listPublic() {
      const [rows] = await pool.execute(
        `SELECT p.id, p.clinic_id AS clinicId, c.name AS clinicName, p.title,
                p.content, p.created_at AS createdAt
         FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id
         ORDER BY p.id DESC LIMIT 50`,
      );
      return rows;
    },
  };
}
