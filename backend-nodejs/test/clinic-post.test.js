import assert from 'node:assert/strict';
import { createHmac } from 'node:crypto';
import { after, before, test } from 'node:test';
import { createApp } from '../src/app.js';
import { createClinicPostModel } from '../src/models/clinic-post.model.js';

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
  async create(clinicId, title, content) {
    const post = { id: posts.length + 1, clinicId, title, content };
    posts.push(post);
    return post;
  },
  async listByClinic(clinicId) { return posts.filter(post => post.clinicId === clinicId); },
  async listPublic() {
    if (failPublicQuery) throw new Error('Sensitive database error');
    return posts;
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
    clinicPostModel, clinicModel, jwtSecret: secret,
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
  assert.deepEqual((await response.json()).data, {
    id: 1, clinicId: 7, title: 'Lịch tiêm phòng', content: 'Có lịch mới',
  });
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.equal((await feed.json()).data.length, 1);
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
