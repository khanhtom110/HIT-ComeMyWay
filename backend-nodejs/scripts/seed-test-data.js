import { randomBytes } from 'node:crypto';
import { readFile, writeFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import bcrypt from 'bcryptjs';
import dotenv from 'dotenv';
import { createDatabasePool } from '../src/configs/db.config.js';
import { loadConfig } from '../src/configs/env.config.js';
import { createAdminStatisticsModel } from '../src/models/admin/statistics.model.js';

const envPath = fileURLToPath(new URL('../.env', import.meta.url));
const credentialsPath = fileURLToPath(new URL('../.env.seed.local', import.meta.url));
const seedDomain = 'example.invalid';

dotenv.config({ path: envPath, quiet: true });

async function getCredentials() {
  let values;
  try {
    values = dotenv.parse(await readFile(credentialsPath));
  } catch (error) {
    if (error.code !== 'ENOENT') throw error;
    values = {
      SEED_ADMIN_PASSWORD: randomBytes(18).toString('base64url'),
      SEED_CLINIC_PASSWORD: randomBytes(18).toString('base64url'),
      SEED_USER_PASSWORD: randomBytes(18).toString('base64url'),
    };
    const content = Object.entries(values).map(([key, value]) => `${key}=${value}`).join('\n') + '\n';
    try {
      await writeFile(credentialsPath, content, { flag: 'wx', mode: 0o600 });
    } catch (writeError) {
      if (writeError.code !== 'EEXIST') throw writeError;
      values = dotenv.parse(await readFile(credentialsPath));
    }
  }
  for (const key of ['SEED_ADMIN_PASSWORD', 'SEED_CLINIC_PASSWORD', 'SEED_USER_PASSWORD']) {
    if (!values[key]) throw new Error(`Missing ${key} in .env.seed.local`);
  }
  return values;
}

async function saveUser(connection, { username, email, fullName, role, status, password }) {
  const [existing] = await connection.execute(
    'SELECT id, username, email, role FROM users WHERE username = ? OR email = ? FOR UPDATE',
    [username, email],
  );
  if (existing.length > 1 || (existing.length === 1 &&
      (existing[0].username !== username || existing[0].email !== email ||
       existing[0].role !== role))) {
    throw new Error(`Seed account conflicts with existing data: ${username}`);
  }

  if (existing.length === 1) {
    await connection.execute(
      `UPDATE users SET password = ?, full_name = ?, home_address = ?, status = ?,
       updated_at = UTC_TIMESTAMP(3) WHERE id = ?`,
      [password, fullName, 'Địa chỉ dữ liệu kiểm thử', status, existing[0].id],
    );
    return existing[0].id;
  }

  const [result] = await connection.execute(
    `INSERT INTO users (username, email, password, full_name, home_address, role, status,
                        created_at, updated_at)
     VALUES (?, ?, ?, ?, ?, ?, ?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3))`,
    [username, email, password, fullName, 'Địa chỉ dữ liệu kiểm thử', role, status],
  );
  return result.insertId;
}

async function saveClinicProfile(connection, userId) {
  const [existing] = await connection.execute(
    'SELECT id FROM clinics WHERE user_id = ? FOR UPDATE', [userId],
  );
  const values = [
    'Phòng khám mẫu ComeMyWay', 'Số 1, Hà Nội', '0900000001',
    'https://maps.google.com/?q=21.0285,105.8542',
    'Hồ sơ phòng khám dùng để kiểm thử tài khoản ACTIVE.', 21.0285, 105.8542,
    '08:00:00', '18:00:00', 'https://example.invalid/clinic.png',
  ];
  let clinicId;
  if (existing.length) {
    clinicId = existing[0].id;
    await connection.execute(
      `UPDATE clinics SET name = ?, address = ?, phone = ?, map_link = ?, description = ?,
       latitude = ?, longitude = ?, open_time = ?, close_time = ?, thumbnail_url = ?,
       status = 1, updated_at = UTC_TIMESTAMP(3) WHERE id = ?`,
      [...values, clinicId],
    );
  } else {
    const [result] = await connection.execute(
      `INSERT INTO clinics (name, address, phone, map_link, description, latitude, longitude,
                            open_time, close_time, thumbnail_url, status, rating, user_id,
                            created_at, updated_at)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 0, ?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3))`,
      [...values, userId],
    );
    clinicId = result.insertId;
  }

  const [services] = await connection.execute(
    'SELECT name FROM services WHERE clinic_id = ?', [clinicId],
  );
  const names = new Set(services.map(service => service.name));
  for (const name of ['Khám tổng quát', 'Tiêm phòng']) {
    if (names.has(name)) continue;
    await connection.execute(
      `INSERT INTO services (clinic_id, name, price, created_at, updated_at)
       VALUES (?, ?, ?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3))`,
      [clinicId, name, name === 'Khám tổng quát' ? 150000 : 120000],
    );
  }
}

async function main() {
  const prepareOnly = process.argv.includes('--prepare');
  let config;
  if (!prepareOnly) {
    config = loadConfig();
    if (config.nodeEnv === 'production' ||
        !['localhost', '127.0.0.1', 'devmysql'].includes(config.database.host)) {
      throw new Error('Test seed is restricted to local development databases');
    }
  }
  const credentials = await getCredentials();
  if (prepareOnly) {
    console.log(`Seed credentials ready: ${credentialsPath}`);
    return;
  }

  const [adminHash, clinicHash, userHash] = await Promise.all([
    bcrypt.hash(credentials.SEED_ADMIN_PASSWORD, 10),
    bcrypt.hash(credentials.SEED_CLINIC_PASSWORD, 10),
    bcrypt.hash(credentials.SEED_USER_PASSWORD, 10),
  ]);
  const pool = createDatabasePool(config.database);
  try {
    const connection = await pool.getConnection();
    try {
      await connection.beginTransaction();
      await saveUser(connection, {
        username: 'cmw_test_admin', email: `cmw-test-admin@${seedDomain}`,
        fullName: 'Quản trị viên kiểm thử', role: 'ADMIN', status: 'ACTIVE',
        password: adminHash,
      });
      const activeClinicId = await saveUser(connection, {
        username: 'cmw_test_clinic_active', email: `cmw-test-clinic-active@${seedDomain}`,
        fullName: 'Phòng khám kiểm thử hoạt động', role: 'CLINIC', status: 'ACTIVE',
        password: clinicHash,
      });
      await saveClinicProfile(connection, activeClinicId);
      await saveUser(connection, {
        username: 'cmw_test_clinic_new', email: `cmw-test-clinic-new@${seedDomain}`,
        fullName: 'Phòng khám chưa đổi mật khẩu', role: 'CLINIC',
        status: 'PENDING_PASSWORD_CHANGE', password: clinicHash,
      });
      await saveUser(connection, {
        username: 'cmw_test_clinic_profile', email: `cmw-test-clinic-profile@${seedDomain}`,
        fullName: 'Phòng khám chưa hoàn tất hồ sơ', role: 'CLINIC',
        status: 'PENDING_PROFILE', password: clinicHash,
      });
      for (const number of [1, 2]) {
        await saveUser(connection, {
          username: `cmw_test_user_${number}`,
          email: `cmw-test-user-${number}@${seedDomain}`,
          fullName: `Người dùng kiểm thử ${number}`, role: 'USER', status: null,
          password: userHash,
        });
      }
      await connection.commit();
    } catch (error) {
      await connection.rollback();
      throw error;
    } finally {
      connection.release();
    }
    const statistics = await createAdminStatisticsModel(pool).getStatistics();
    console.log(JSON.stringify({
      adminUsername: 'cmw_test_admin',
      seededAccounts: 6,
      statistics,
      credentialsFile: credentialsPath,
    }));
  } finally {
    await pool.end();
  }
}

main().catch(error => {
  console.error('Test seed failed:', error.code ?? error.message);
  process.exitCode = 1;
});
