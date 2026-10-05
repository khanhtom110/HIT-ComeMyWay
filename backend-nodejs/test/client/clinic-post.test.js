import assert from 'node:assert/strict';
import { createHmac } from 'node:crypto';
import { after, before, test } from 'node:test';
import { createApp } from '../../src/app.js';
import { createClinicPostModel } from '../../src/models/index.js';

const secret = 'test-secret-at-least-32-characters-long';
const posts = [];
const clinicModel = {
  async findActiveByUsername(username, jti) {
    if (jti === 'revoked') return null;
    if (username === 'clinic') return { id: 7 };
    if (username === 'other-clinic') return { id: 8 };
    return null;
  },
};
let failPublicQuery = false;
const clinicPostModel = {
  async create(clinicId, title, content, imageUrls) {
    const post = { id: posts.length + 1, clinicId, title, content, imageUrls,
      status: 'APPROVED', approvedBy: null, approvedAt: new Date().toISOString() };
    posts.push(post);
    return post;
  },
  async listByClinic(clinicId) { return posts.filter(post => post.clinicId === clinicId); },
  async listPublic() {
    if (failPublicQuery) throw new Error('Sensitive database error');
    return posts.filter(post => post.status === 'APPROVED');
  },
  async findPublic(id) { return posts.find(post => post.id === id && post.status === 'APPROVED'); },
  async listForAdmin(status) { return posts.filter(post => post.status === status); },
  async approve(id, adminId) {
    const post = posts.find(post => post.id === id);
    if (post && post.status === 'PENDING') Object.assign(post, { status: 'APPROVED', approvedBy: adminId });
    return post;
  },
  async deleteByClinic(id, clinicId) {
    const index = posts.findIndex(post => post.id === id && post.clinicId === clinicId);
    if (index === -1) return false;
    posts.splice(index, 1);
    return true;
  },
};

function token(overrides = {}) {
  const header = Buffer.from(JSON.stringify({ alg: 'HS256' })).toString('base64url');
  const claims = Buffer.from(JSON.stringify({
    sub: 'clinic', authorities: 'CLINIC', isRefresh: false,
    jti: 'valid', exp: Math.floor(Date.now() / 1000) + 60, ...overrides,
  })).toString('base64url');
  const signature = createHmac('sha256', secret).update(`${header}.${claims}`).digest('base64url');
  return `${header}.${claims}.${signature}`;
}

let server;
let baseUrl;
let databaseReady = true;
before(async () => {
  const app = createApp({
    clinicPostModel, clinicModel,
    adminStatisticsModel: {
      async findAdminByUsername(username, jti) { return username === 'admin' && jti !== 'revoked' ? { id: 99 } : null; },
      async getStatistics() { return { activeClinics: 0, inactiveClinics: 0, totalUsers: 0 }; },
    },
    jwtSecret: secret,
    corsOrigins: ['http://localhost:3000'],
    checkReadiness: async () => databaseReady,
  });
  await new Promise(resolve => { server = app.listen(0, '127.0.0.1', resolve); });
  baseUrl = `http://127.0.0.1:${server.address().port}`;
});
after(async () => {
  server.closeAllConnections();
  await new Promise(resolve => server.close(resolve));
});

test('clinic publishes title and content; its posts are readable', async () => {
  const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: '  Lịch tiêm phòng  ', content: '  Có lịch mới  ', clinicId: 999 }),
  });
  assert.equal(response.status, 201);
  const post = (await response.json()).data;
  assert.deepEqual({ ...post, approvedAt: null }, {
    id: 1, clinicId: 7, title: 'Lịch tiêm phòng', content: 'Có lịch mới', imageUrls: [],
    status: 'APPROVED', approvedBy: null, approvedAt: null,
  });
  assert.equal(Number.isNaN(Date.parse(post.approvedAt)), false);
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.ok((await feed.json()).data.some(item => item.id === post.id));
  const ownPosts = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(ownPosts.status, 200);
  assert.deepEqual((await ownPosts.json()).data, posts);
});

test('rejects empty fields and unauthorized tokens', async () => {
  for (const authorization of [undefined, `Bearer ${token({ authorities: 'USER' })}`,
    `Bearer ${token({ isRefresh: true })}`, `Bearer ${token({ jti: 'revoked' })}`,
    `Bearer ${token({ exp: 1 })}`, `Bearer ${token()}invalid`]) {
    const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
      method: 'POST', headers: {
        'Content-Type': 'application/json', ...(authorization ? { authorization } : {}),
      },
      body: JSON.stringify({ title: 'x', content: 'y' }),
    });
    assert.ok([401, 403].includes(response.status));
  }
  const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST', headers: { authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: '  ', content: 'text' }),
  });
  assert.equal(response.status, 400);
});

test('new posts with images publish immediately; only admin can approve legacy pending posts', async () => {
  const imageUrls = ['https://example.com/clinic.jpg', 'https://example.com/service.png'];
  const created = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST', headers: { authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: 'Có ảnh', content: 'Nội dung', imageUrls, status: 'APPROVED', approvedBy: 7 }),
  });
  assert.equal(created.status, 201);
  const post = (await created.json()).data;
  assert.equal(post.status, 'APPROVED');
  assert.equal(post.approvedBy, null);
  assert.equal(Number.isNaN(Date.parse(post.approvedAt)), false);
  assert.deepEqual(post.imageUrls, imageUrls);
  assert.equal((await fetch(`${baseUrl}/api/v1/public/clinic-posts/${post.id}`)).status, 200);
  const url = `${baseUrl}/api/v1/admin/clinic-posts/${post.id}/approve`;
  for (const claims of [{}, { authorities: 'USER' }, { authorities: 'ADMIN', sub: 'admin', jti: 'revoked' },
    { authorities: 'ADMIN', sub: 'admin', isRefresh: true }]) {
    const denied = await fetch(url, { method: 'PATCH', headers: { authorization: `Bearer ${token(claims)}` } });
    assert.ok([401, 403].includes(denied.status));
  }
  assert.equal((await fetch(url, { method: 'PATCH' })).status, 401);
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts`)).status, 401);
  const headers = { authorization: `Bearer ${token({ authorities: 'ADMIN', sub: 'admin' })}` };
  const pending = await fetch(`${baseUrl}/api/v1/admin/clinic-posts`, { headers });
  assert.equal(pending.status, 200);
  assert.equal((await pending.json()).data.some(item => item.id === post.id), false);
  const approved = await fetch(url, { method: 'PATCH', headers });
  assert.equal(approved.status, 200);
  const approvedPost = (await approved.json()).data;
  assert.equal(approvedPost.approvedBy, null);
  assert.equal(approvedPost.approvedAt, post.approvedAt);
  const detail = await fetch(`${baseUrl}/api/v1/public/clinic-posts/${post.id}`);
  assert.equal(detail.status, 200);
  assert.deepEqual((await detail.json()).data.imageUrls, imageUrls);
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.ok((await feed.json()).data.some(item => item.id === post.id && item.status === 'APPROVED'));
  assert.equal((await fetch(url, { method: 'PATCH', headers })).status, 200);
  const pendingAfter = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?status=PENDING`, { headers });
  assert.equal((await pendingAfter.json()).data.some(item => item.id === post.id), false);
  const approvedList = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?status=APPROVED`, { headers });
  assert.ok((await approvedList.json()).data.some(item => item.id === post.id && item.approvedBy === null));
  const ownerList = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    headers: { authorization: `Bearer ${token()}` },
  });
  assert.equal((await ownerList.json()).data.find(item => item.id === post.id).status, 'APPROVED');
  const legacyPost = { id: 900001, clinicId: 7, title: 'Tin cũ', content: 'Chờ duyệt',
    imageUrls: [], status: 'PENDING', approvedBy: null, approvedAt: null };
  posts.push(legacyPost);
  assert.equal((await fetch(`${baseUrl}/api/v1/public/clinic-posts/${legacyPost.id}`)).status, 404);
  const legacyApproved = await fetch(`${baseUrl}/api/v1/admin/clinic-posts/${legacyPost.id}/approve`,
    { method: 'PATCH', headers });
  assert.equal(legacyApproved.status, 200);
  assert.equal((await legacyApproved.json()).data.approvedBy, 99);
  for (const claims of [{}, { authorities: 'USER' }]) {
    const deniedList = await fetch(`${baseUrl}/api/v1/admin/clinic-posts`, {
      headers: { authorization: `Bearer ${token(claims)}` },
    });
    assert.equal(deniedList.status, 401);
  }
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/999999/approve`, { method: 'PATCH', headers })).status, 404);
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/no/approve`, { method: 'PATCH', headers })).status, 400);
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts?status=INVALID`, { headers })).status, 400);
});

test('invalid image arrays are rejected without creating posts', async () => {
  const beforeCount = posts.length;
  for (const imageUrls of ['url', [null], ['javascript:alert(1)'], ['file:///tmp/a.jpg'],
    ['https://example.com/a', 'https://example.com/a'], Array.from({ length: 11 }, (_, i) => `https://example.com/${i}`)]) {
    const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
      method: 'POST', headers: { authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: 'Tin', content: 'Nội dung', imageUrls }),
    });
    assert.equal(response.status, 400);
  }
  assert.equal(posts.length, beforeCount);
});

test('SQL model auto-publishes new posts and preserves admin approval for legacy posts', async () => {
  const calls = [];
  const model = createClinicPostModel({ async execute(sql, params) {
    calls.push({ sql, params });
    if (sql.startsWith('INSERT')) return [{ insertId: 42 }];
    if (sql.startsWith('UPDATE')) return [{ affectedRows: 1 }];
    return [[{ id: 42, imageUrls: '["https://example.com/a.jpg"]' }]];
  } });
  assert.deepEqual((await model.create(7, 'Title', 'Content', ['https://example.com/a.jpg'])).imageUrls,
    ['https://example.com/a.jpg']);
  assert.deepEqual(calls[0].params, [7, 'Title', 'Content', '["https://example.com/a.jpg"]']);
  assert.match(calls[0].sql, /'APPROVED', UTC_TIMESTAMP\(3\), UTC_TIMESTAMP\(3\)/);
  await model.listPublic();
  assert.match(calls.at(-1).sql, /WHERE p.status = 'APPROVED'/);
  await model.findPublic(42);
  assert.match(calls.at(-1).sql, /p.id = \? AND p.status = 'APPROVED'/);
  await model.approve(42, 99);
  assert.deepEqual(calls.at(-2).params, [99, 42]);
  assert.match(calls.at(-2).sql, /WHERE id = \? AND status = 'PENDING'/);
});

test('only the author clinic can delete a post', async () => {
  const created = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: 'Tin cần xóa', content: 'Nội dung' }),
  });
  assert.equal(created.status, 201);
  const id = (await created.json()).data.id;
  const url = `${baseUrl}/api/v1/clinic/posts/${id}`;

  const unauthorized = await fetch(url, { method: 'DELETE' });
  assert.equal(unauthorized.status, 401);
  const otherClinic = await fetch(url, {
    method: 'DELETE', headers: { Authorization: `Bearer ${token({ sub: 'other-clinic' })}` },
  });
  assert.equal(otherClinic.status, 404);
  assert.equal(posts.some(post => post.id === id), true);

  const invalidId = await fetch(`${baseUrl}/api/v1/clinic/posts/abc`, {
    method: 'DELETE', headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(invalidId.status, 400);
  const missing = await fetch(`${baseUrl}/api/v1/clinic/posts/999999`, {
    method: 'DELETE', headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(missing.status, 404);

  const deleted = await fetch(url, {
    method: 'DELETE', headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(deleted.status, 200);
  assert.deepEqual((await deleted.json()).data, { id });
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.equal((await feed.json()).data.some(post => post.id === id), false);
  const repeat = await fetch(url, {
    method: 'DELETE', headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(repeat.status, 404);
});

test('delete query includes both post and clinic IDs', async () => {
  const calls = [];
  const model = createClinicPostModel({
    async execute(sql, params) {
      calls.push({ sql, params });
      return [{ affectedRows: 1 }];
    },
  });
  assert.equal(await model.deleteByClinic(12, 7), true);
  assert.deepEqual(calls[0].params, [12, 7]);
  assert.match(calls[0].sql, /DELETE FROM clinic_posts WHERE id = \? AND clinic_id = \?/);
});

test('routes preserve health, authentication and JSON error responses', async () => {
  const health = await fetch(`${baseUrl}/health`);
  assert.equal(health.status, 200);
  assert.deepEqual((await health.json()).data, { status: 'ok' });

  const missing = await fetch(`${baseUrl}/not-found`);
  assert.equal(missing.status, 404);
  const unauthorized = await fetch(`${baseUrl}/api/v1/clinic/posts`);
  assert.equal(unauthorized.status, 401);

  const malformed = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' }, body: '{',
  });
  assert.equal(malformed.status, 400);
  assert.equal((await malformed.json()).message, 'JSON không hợp lệ');
});

test('Joi rejects invalid post data before writing to the model', async () => {
  const count = posts.length;
  const invalidBodies = [null, {}, { title: 123, content: 'text' },
    { title: 'x'.repeat(201), content: 'text' },
    { title: 'Tin mới', content: 'x'.repeat(10001) }];
  for (const body of invalidBodies) {
    const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });
    assert.equal(response.status, 400);
    assert.equal((await response.json()).statusCode, 400);
  }
  assert.equal(posts.length, count);
});

test('Express enforces the request body byte limit', async () => {
  const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: 'Tin mới', content: 'x'.repeat(64 * 1024) }),
  });
  assert.equal(response.status, 413);
  assert.equal((await response.json()).statusCode, 413);
});

test('health readiness reflects the database and liveness remains available', async () => {
  const ready = await fetch(`${baseUrl}/health/ready`);
  assert.equal(ready.status, 200);
  databaseReady = false;
  try {
    const unavailable = await fetch(`${baseUrl}/health/ready`);
    assert.equal(unavailable.status, 503);
    assert.equal((await unavailable.json()).data.status, 'not_ready');
    const live = await fetch(`${baseUrl}/health/live`);
    assert.equal(live.status, 200);
  } finally {
    databaseReady = true;
  }
});

test('CORS allows configured browser origins and rejects other origins', async () => {
  const allowed = await fetch(`${baseUrl}/health/live`, {
    headers: { Origin: 'http://localhost:3000' },
  });
  assert.equal(allowed.status, 200);
  assert.equal(allowed.headers.get('access-control-allow-origin'), 'http://localhost:3000');
  const denied = await fetch(`${baseUrl}/health/live`, {
    headers: { Origin: 'https://not-allowed.example' },
  });
  assert.equal(denied.status, 403);
  assert.equal((await denied.json()).statusCode, 403);
});

test('Swagger UI serves the clinic post contract and accepts same-origin requests', async () => {
  const ui = await fetch(`${baseUrl}/api-docs/`);
  assert.equal(ui.status, 200);
  assert.match(await ui.text(), /Swagger UI/);

  const specResponse = await fetch(`${baseUrl}/api-docs/openapi.json`);
  assert.equal(specResponse.status, 200);
  const spec = await specResponse.json();
  assert.equal(spec.openapi, '3.0.3');
  assert.deepEqual(spec.paths['/api/v1/clinic/posts'].post.security, [{ clinicBearer: [] }]);
  assert.deepEqual(spec.paths['/api/v1/clinic/posts/{id}'].delete.security, [{ clinicBearer: [] }]);
  assert.deepEqual(spec.components.schemas.CreateClinicPost.required, ['title', 'content']);
  assert.equal(spec.components.schemas.CreateClinicPost.properties.imageUrls.maxItems, 10);
  assert.deepEqual(spec.components.schemas.ClinicPost.properties.status.enum, ['PENDING', 'APPROVED']);
  assert.deepEqual(spec.paths['/api/v1/admin/clinic-posts/{id}/approve'].patch.security, [{ adminBearer: [] }]);
  assert.ok(spec.paths['/api/v1/public/clinic-posts/{id}'].get.responses[404]);

  const sameOrigin = await fetch(`${baseUrl}/api/v1/public/clinic-posts`, {
    headers: { Origin: baseUrl },
  });
  assert.equal(sameOrigin.status, 200);
  assert.equal(sameOrigin.headers.get('access-control-allow-origin'), baseUrl);
});

test('async model errors use the common response without leaking details', async t => {
  t.mock.method(console, 'error', () => {});
  failPublicQuery = true;
  try {
    const response = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
    assert.equal(response.status, 500);
    const body = await response.json();
    assert.equal(body.statusCode, 500);
    assert.equal(body.message, 'Không thể xử lý yêu cầu');
    assert.equal(body.data, null);
  } finally {
    failPublicQuery = false;
  }
});
