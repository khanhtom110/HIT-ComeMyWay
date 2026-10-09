export function createAdminStatisticsModel(pool) {
  return {
    async findAdminByUsername(username, jti) {
      const [rows] = await pool.execute(
        `SELECT u.id FROM users u
         WHERE u.username = ? AND u.role = 'ADMIN'
           AND NOT EXISTS (SELECT 1 FROM invalidated_token t WHERE t.id = ?)
         LIMIT 1`,
        [username, jti],
      );
      return rows[0] ?? null;
    },
    async getStatistics() {
      const [rows] = await pool.execute(
        `SELECT
           COUNT(CASE WHEN role = 'CLINIC' AND status = 'ACTIVE' THEN 1 END) AS activeClinics,
           COUNT(CASE WHEN role = 'CLINIC' AND (status IS NULL OR status <> 'ACTIVE') THEN 1 END) AS inactiveClinics,
           COUNT(CASE WHEN role = 'USER' THEN 1 END) AS totalUsers
         FROM users`,
      );
      return rows[0];
    },
  };
}
