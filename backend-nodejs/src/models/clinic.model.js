export function createClinicModel(pool) {
  return {
    async findActiveByUsername(username, jti) {
      const [rows] = await pool.execute(
        `SELECT c.id FROM clinics c JOIN users u ON u.id = c.user_id
         WHERE u.username = ? AND u.role = 'CLINIC' AND u.status = 'ACTIVE'
           AND NOT EXISTS (SELECT 1 FROM invalidated_token t WHERE t.id = ?)
         LIMIT 1`,
        [username, jti],
      );
      return rows[0] ?? null;
    },
  };
}
