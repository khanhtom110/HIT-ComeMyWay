import assert from 'node:assert/strict';
import { createHmac } from 'node:crypto';
import { after, before, test } from 'node:test';
import { createApp } from '../../src/app.js';
import { openApiDocument } from '../../src/docs/openapi.js';
import { createAdminStatisticsModel } from '../../src/models/index.js';

const secret = 'test-secret-at-least-32-characters-long';
const statistics = { activeClinics: 3, inactiveClinics: 4, totalUsers: 12 };
const adminStatisticsModel = {
  async findAdminByUsername(username, jti) {
    return username === 'admin' && jti !== 'revoked' ? { id: 1 } : null;
  },
  async getStatistics() { return statistics; },
};

function token(overrides = {}) {
  const header = Buffer.from(JSON.stringify({ alg: 'HS256' })).toString('base64url');
  const claims = Buffer.from(JSON.stringify({
    sub: 'admin', authorities: 'ADMIN', isRefresh: false,
    jti: 'valid', exp: Math.floor(Date.now() / 1000) + 60, ...overrides,
  })).toString('base64url');
  const signature = createHmac('sha256', secret).update(`${header}.${claims}`).digest('base64url');
  return `${header}.${claims}.${signature}`;
}

let server;
let baseUrl;
before(async () => {
  const app = createApp({
    clinicPostModel: {}, clinicModel: {}, adminStatisticsModel, jwtSecret: secret,
  });
  await new Promise(resolve => { server = app.listen(0, '127.0.0.1', resolve); });
  baseUrl = `http://127.0.0.1:${server.address().port}`;
});

after(async () => {
  server.closeAllConnections();
  await new Promise(resolve => server.close(resolve));
});

test('admin receives the three account counts', async () => {
  const response = await fetch(`${baseUrl}/api/v1/admin/statistics`, {
    headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(response.status, 200);
  assert.deepEqual((await response.json()).data, statistics);
});

test('statistics rejects missing, clinic, refresh, and revoked credentials', async () => {
  for (const authorization of [undefined, `Bearer ${token({ authorities: 'CLINIC' })}`,
    `Bearer ${token({ isRefresh: true })}`, `Bearer ${token({ jti: 'revoked' })}`]) {
    const response = await fetch(`${baseUrl}/api/v1/admin/statistics`, {
      headers: authorization ? { Authorization: authorization } : {},
    });
    assert.ok([401, 403].includes(response.status));
  }
});

test('statistics counts changed-password clinics as active even before profile completion', async () => {
  let sql;
  const model = createAdminStatisticsModel({
    async execute(query) { sql = query; return [[statistics]]; },
  });
  assert.deepEqual(await model.getStatistics(), statistics);
  assert.match(sql, /role = 'CLINIC' AND status IN \('PENDING_PROFILE', 'ACTIVE'\) THEN 1 END\) AS activeClinics/);
  assert.match(sql, /role = 'CLINIC' AND \(status IS NULL OR status NOT IN \('PENDING_PROFILE', 'ACTIVE'\)\) THEN 1 END\) AS inactiveClinics/);
  assert.match(sql, /role = 'USER'/);
});

test('OpenAPI documents the admin route, bearer role, and response fields', () => {
  const operation = openApiDocument.paths['/api/v1/admin/statistics'].get;
  assert.deepEqual(operation.security, [{ adminBearer: [] }]);
  assert.match(openApiDocument.components.securitySchemes.adminBearer.description, /ADMIN/);
  assert.deepEqual(openApiDocument.components.schemas.AdminStatistics.required,
    ['activeClinics', 'inactiveClinics', 'totalUsers']);
});
