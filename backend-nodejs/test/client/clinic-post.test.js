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
    const post = { id: posts.length + 1, clinicId, clinicThumbnailUrl: 'https://example.com/clinic-avatar.jpg',
      title, content, imageUrls,
      status: 'APPROVED', approvedBy: null, approvedAt: new Date().toISOString() };
    posts.push(post);
    return post;
  },
  async listByClinic(clinicId, { status, beforeId, limit }) {
    return posts.filter(post => post.clinicId === clinicId && (status === 'ALL' || post.status === status)
      && (beforeId === undefined || post.id < beforeId)).sort((a, b) => b.id - a.id).slice(0, limit + 1);
  },
  async listPublic() {
    if (failPublicQuery) throw new Error('Sensitive database error');
    return posts.filter(post => post.status === 'APPROVED');
  },
  async findForAdmin(id) { return posts.find(post => post.id === id); },
  async countByStatus(clinicId) {
    return Object.fromEntries(['PENDING', 'APPROVED', 'REJECTED'].map(status => [status, posts.filter(post => post.status === status && (clinicId === undefined || post.clinicId === clinicId)).length]));
  },
  async findPublic(id) { return posts.find(post => post.id === id && post.status === 'APPROVED'); },
  async listForAdmin(status, { beforeId, limit }) {
    return posts.filter(post => post.status === status && (beforeId === undefined || post.id < beforeId))
      .sort((a, b) => b.id - a.id).slice(0, limit + 1);
  },
  async approve(id, adminId) {
    const post = posts.find(post => post.id === id);
    if (post && post.status === 'PENDING') Object.assign(post, { status: 'APPROVED', approvedBy: adminId, approvedAt: new Date().toISOString() });
    return post;
  },
  async reject(id) {
    const post = posts.find(post => post.id === id);
    if (post && post.status === 'PENDING') Object.assign(post, { status: 'REJECTED', approvedBy: null, approvedAt: null });
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
  assert.deepEqual(post, {
    id: 1, clinicId: 7, clinicThumbnailUrl: 'https://example.com/clinic-avatar.jpg',
    title: 'Lịch tiêm phòng', content: 'Có lịch mới', imageUrls: [],
    status: 'APPROVED', approvedBy: null, approvedAt: post.approvedAt,
  });
  assert.equal(Number.isNaN(Date.parse(post.approvedAt)), false);
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.equal((await feed.json()).data.some(item => item.id === post.id), true);
  assert.equal(post.clinicThumbnailUrl, 'https://example.com/clinic-avatar.jpg');
  const ownPosts = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    headers: { Authorization: `Bearer ${token()}` },
  });
  assert.equal(ownPosts.status, 200);
  assert.deepEqual((await ownPosts.json()).data, [...posts].sort((a, b) => b.id - a.id));
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

test('new posts are automatically approved and public without admin action', async () => {
  const imageUrls = ['https://example.com/clinic.jpg', 'https://example.com/service.png'];
  const created = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST', headers: { authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: 'Có ảnh', content: 'Nội dung', imageUrls, status: 'PENDING', approvedBy: 7, approvedAt: '2000-01-01T00:00:00Z' }),
  });
  assert.equal(created.status, 201);
  const post = (await created.json()).data;
  assert.equal(post.status, 'APPROVED');
  assert.equal(post.approvedBy, null);
  assert.equal(Number.isNaN(Date.parse(post.approvedAt)), false);
  assert.notEqual(post.approvedAt, '2000-01-01T00:00:00Z');
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
  assert.equal(Number.isNaN(Date.parse(approvedPost.approvedAt)), false);
  const detail = await fetch(`${baseUrl}/api/v1/public/clinic-posts/${post.id}`);
  assert.equal(detail.status, 200);
  const detailPost = (await detail.json()).data;
  assert.deepEqual(detailPost.imageUrls, imageUrls);
  assert.equal(detailPost.clinicThumbnailUrl, 'https://example.com/clinic-avatar.jpg');
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.ok((await feed.json()).data.some(item => item.id === post.id && item.status === 'APPROVED'));
  const repeated = await fetch(url, { method: 'PATCH', headers });
  assert.equal(repeated.status, 200);
  assert.deepEqual((await repeated.json()).data, approvedPost);
  const pendingAfter = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?status=PENDING`, { headers });
  assert.equal((await pendingAfter.json()).data.some(item => item.id === post.id), false);
  const approvedList = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?status=APPROVED`, { headers });
  assert.ok((await approvedList.json()).data.some(item => item.id === post.id && item.approvedBy === null));
  const ownerList = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    headers: { authorization: `Bearer ${token()}` },
  });
  assert.equal((await ownerList.json()).data.find(item => item.id === post.id).status, 'APPROVED');
  const legacyPost = { id: 900001, clinicId: 7, clinicThumbnailUrl: null,
    title: 'Tin cũ', content: 'Chờ duyệt',
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

test('only admin can reject pending posts; rejected posts stay private', async () => {
  const post = { id: 900002, clinicId: 7, title: 'Tin cũ bị từ chối', content: 'Nội dung',
    imageUrls: [], status: 'PENDING', approvedBy: null, approvedAt: null };
  posts.push(post);
  assert.equal(post.status, 'PENDING');
  const url = `${baseUrl}/api/v1/admin/clinic-posts/${post.id}/reject`;
  assert.equal((await fetch(url, { method: 'PATCH' })).status, 401);
  for (const claims of [{}, { authorities: 'USER' }, { authorities: 'ADMIN', sub: 'admin', jti: 'revoked' },
    { authorities: 'ADMIN', sub: 'admin', isRefresh: true }]) {
    const denied = await fetch(url, { method: 'PATCH', headers: { authorization: `Bearer ${token(claims)}` } });
    assert.ok([401, 403].includes(denied.status));
  }
  const headers = { authorization: `Bearer ${token({ authorities: 'ADMIN', sub: 'admin' })}` };
  const rejected = await fetch(url, { method: 'PATCH', headers });
  assert.equal(rejected.status, 200);
  const rejectedPost = (await rejected.json()).data;
  assert.equal(rejectedPost.status, 'REJECTED');
  assert.equal(rejectedPost.approvedBy, null);
  assert.equal(rejectedPost.approvedAt, null);
  const repeated = await fetch(url, { method: 'PATCH', headers });
  assert.equal(repeated.status, 200);
  assert.deepEqual((await repeated.json()).data, rejectedPost);
  assert.equal((await fetch(`${baseUrl}/api/v1/public/clinic-posts/${post.id}`)).status, 404);
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.equal((await feed.json()).data.some(item => item.id === post.id), false);
  const rejectedList = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?status=REJECTED`, { headers });
  assert.equal(rejectedList.status, 200);
  assert.ok((await rejectedList.json()).data.some(item => item.id === post.id));
  const pending = await fetch(`${baseUrl}/api/v1/admin/clinic-posts`, { headers });
  assert.equal((await pending.json()).data.some(item => item.id === post.id), false);
  const ownPosts = await fetch(`${baseUrl}/api/v1/clinic/posts`, { headers: { authorization: `Bearer ${token()}` } });
  assert.equal((await ownPosts.json()).data.find(item => item.id === post.id).status, 'REJECTED');
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/${post.id}/approve`, { method: 'PATCH', headers })).status, 409);
  const approvedPost = posts.find(item => item.status === 'APPROVED');
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/${approvedPost.id}/reject`, { method: 'PATCH', headers })).status, 409);
  assert.equal(approvedPost.status, 'APPROVED');
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/999999/reject`, { method: 'PATCH', headers })).status, 404);
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/no/reject`, { method: 'PATCH', headers })).status, 400);
});

test('admin can browse more than 50 posts without skipping posts after moderation', async () => {
  const headers = { authorization: `Bearer ${token({ authorities: 'ADMIN', sub: 'admin' })}` };
  const fixtures = [];
  for (const [index, status] of ['PENDING', 'APPROVED', 'REJECTED'].entries()) {
    for (let i = 1; i <= 55; i++) fixtures.push({ id: 1000000 + index * 100 + i, clinicId: 7,
      title: 'Pagination fixture', content: 'Test', imageUrls: [], status, approvedBy: null, approvedAt: null });
  }
  posts.push(...fixtures);
  try {
    for (const status of ['PENDING', 'APPROVED', 'REJECTED']) {
      const url = `${baseUrl}/api/v1/admin/clinic-posts?status=${status}`;
      const first = await fetch(url, { headers });
      assert.equal(first.status, 200);
      const firstBody = await first.json();
      assert.equal(firstBody.data.length, 50);
      assert.deepEqual(firstBody.pagination, { limit: 50, hasMore: true, nextBeforeId: firstBody.data.at(-1).id });
      assert.ok(firstBody.data.every(post => post.status === status));
      const counts = await fetch(`${baseUrl}/api/v1/admin/clinic-posts/counts`, { headers });
      assert.equal((await counts.json()).data[status], posts.filter(post => post.status === status).length);
      if (status === 'PENDING') {
        const approved = await fetch(`${baseUrl}/api/v1/admin/clinic-posts/${firstBody.data[0].id}/approve`, { method: 'PATCH', headers });
        assert.equal(approved.status, 200);
      }
      const next = await fetch(`${url}&beforeId=${firstBody.pagination.nextBeforeId}`, { headers });
      assert.equal(next.status, 200);
      const nextBody = await next.json();
      const actual = [...firstBody.data, ...nextBody.data].filter(post => post.id >= (status === 'PENDING' ? 1000000 : status === 'APPROVED' ? 1000100 : 1000200)
        && post.id < (status === 'PENDING' ? 1000100 : status === 'APPROVED' ? 1000200 : 1000300)).map(post => post.id);
      assert.equal(actual.length, 55);
      assert.equal(new Set(actual).size, 55);
      assert.equal(nextBody.pagination.hasMore, false);
      assert.equal(nextBody.pagination.nextBeforeId, null);
    }
    const small = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?limit=10`, { headers });
    const smallBody = await small.json();
    assert.equal(smallBody.data.length, 10);
    assert.equal(smallBody.pagination.limit, 10);
    const empty = await fetch(`${baseUrl}/api/v1/admin/clinic-posts?beforeId=1`, { headers });
    const emptyBody = await empty.json();
    assert.deepEqual(emptyBody.data, []);
    assert.deepEqual(emptyBody.pagination, { limit: 50, hasMore: false, nextBeforeId: null });
    for (const query of ['limit=0', 'limit=51', 'limit=1.5', 'beforeId=0', 'beforeId=abc', 'beforeId=9007199254740992']) {
      assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts?${query}`, { headers })).status, 400);
    }
  } finally {
    for (let i = posts.length - 1; i >= 0; i--) if (posts[i].id >= 1000000) posts.splice(i, 1);
  }
});

test('admin sees details of every status and counts update after moderation', async () => {
  posts.push({ id: 900005, clinicId: 7, title: 'Legacy pending detail', content: 'Test',
    imageUrls: [], status: 'PENDING', approvedBy: null, approvedAt: null });
  const headers = { authorization: `Bearer ${token({ authorities: 'ADMIN', sub: 'admin' })}` };
  const expectedCounts = () => Object.fromEntries(['PENDING', 'APPROVED', 'REJECTED'].map(status => [status, posts.filter(post => post.status === status).length]));
  const getCounts = async () => {
    const response = await fetch(`${baseUrl}/api/v1/admin/clinic-posts/counts`, { headers });
    assert.equal(response.status, 200);
    return (await response.json()).data;
  };
  assert.deepEqual(await getCounts(), expectedCounts());
  for (const status of ['PENDING', 'APPROVED', 'REJECTED']) {
    const post = posts.find(item => item.status === status);
    const detail = await fetch(`${baseUrl}/api/v1/admin/clinic-posts/${post.id}`, { headers });
    assert.equal(detail.status, 200);
    assert.deepEqual((await detail.json()).data, post);
    if (status !== 'APPROVED') assert.equal((await fetch(`${baseUrl}/api/v1/public/clinic-posts/${post.id}`)).status, 404);
  }
  for (const suffix of ['counts', String(posts[0].id)]) {
    const url = `${baseUrl}/api/v1/admin/clinic-posts/${suffix}`;
    assert.equal((await fetch(url)).status, 401);
    for (const claims of [{}, { authorities: 'USER' }, { authorities: 'ADMIN', sub: 'admin', jti: 'revoked' },
      { authorities: 'ADMIN', sub: 'admin', isRefresh: true }]) {
      assert.ok([401, 403].includes((await fetch(url, { headers: { authorization: `Bearer ${token(claims)}` } })).status));
    }
  }
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/999999`, { headers })).status, 404);
  assert.equal((await fetch(`${baseUrl}/api/v1/admin/clinic-posts/abc`, { headers })).status, 400);
  for (const action of ['approve', 'reject']) {
    const id = action === 'approve' ? 900003 : 900004;
    posts.push({ id, clinicId: 7, title: 'Legacy count fixture', content: 'Test',
      imageUrls: [], status: 'PENDING', approvedBy: null, approvedAt: null });
    const beforeCounts = await getCounts();
    const result = await fetch(`${baseUrl}/api/v1/admin/clinic-posts/${id}/${action}`, { method: 'PATCH', headers });
    assert.equal(result.status, 200);
    const afterCounts = await getCounts();
    assert.equal(afterCounts.PENDING, beforeCounts.PENDING - 1);
    assert.equal(afterCounts[action === 'approve' ? 'APPROVED' : 'REJECTED'], beforeCounts[action === 'approve' ? 'APPROVED' : 'REJECTED'] + 1);
    assert.deepEqual(afterCounts, expectedCounts());
  }
});

test('admin SQL details include private posts and counts cover all rows', async () => {
  const calls = [];
  const model = createClinicPostModel({ async execute(sql, params) {
    calls.push({ sql, params });
    if (sql.includes('COUNT(*)')) return [[{ status: 'PENDING', total: '75' }, { status: 'REJECTED', total: 3 }]];
    return [[{ id: 42, status: 'REJECTED', imageUrls: '["https://example.com/a.jpg"]' }]];
  } });
  const post = await model.findForAdmin(42);
  assert.equal(post.status, 'REJECTED');
  assert.deepEqual(post.imageUrls, ['https://example.com/a.jpg']);
  assert.deepEqual(calls[0].params, [42]);
  assert.doesNotMatch(calls[0].sql, /p.status = 'APPROVED'/);
  assert.deepEqual(await model.countByStatus(), { PENDING: 75, APPROVED: 0, REJECTED: 3 });
  assert.match(calls[1].sql, /GROUP BY p.status/);
  assert.doesNotMatch(calls[1].sql, /LIMIT/);
});

test('clinic tabs, badge and pagination expose only the signed-in clinic posts', async () => {
  const headers = { authorization: `Bearer ${token()}` };
  const url = `${baseUrl}/api/v1/clinic/posts`;
  const fixtures = Array.from({ length: 60 }, (_, i) => ({ id: 2000000 + i, clinicId: 7,
    title: 'Clinic fixture', content: 'Test', imageUrls: [], status: ['PENDING', 'APPROVED', 'REJECTED'][i % 3] }));
  fixtures.push({ id: 3000000, clinicId: 8, title: 'Private', content: 'Other clinic', status: 'REJECTED', imageUrls: [] });
  posts.push(...fixtures);
  try {
    const first = await fetch(url, { headers });
    assert.equal(first.status, 200);
    const firstBody = await first.json();
    assert.equal(firstBody.data.length, 50);
    assert.equal(firstBody.pagination.hasMore, true);
    assert.ok(firstBody.data.every(post => post.clinicId === 7));
    const next = await fetch(`${url}?beforeId=${firstBody.pagination.nextBeforeId}`, { headers });
    const nextBody = await next.json();
    const ids = [...firstBody.data, ...nextBody.data].filter(post => post.id >= 2000000).map(post => post.id);
    assert.equal(ids.length, 60);
    assert.equal(new Set(ids).size, 60);
    assert.equal(nextBody.pagination.hasMore, false);
    for (const status of ['ALL', 'PENDING', 'APPROVED', 'REJECTED']) {
      const result = await fetch(`${url}?status=${status}&limit=10&clinicId=8`, { headers });
      assert.equal(result.status, 200);
      const body = await result.json();
      assert.equal(body.pagination.limit, 10);
      assert.ok(body.data.every(post => post.clinicId === 7 && (status === 'ALL' || post.status === status)));
    }
    const counts = await fetch(`${url}/counts?clinicId=8`, { headers });
    assert.equal(counts.status, 200);
    const expected = Object.fromEntries(['PENDING', 'APPROVED', 'REJECTED'].map(status => [status,
      posts.filter(post => post.clinicId === 7 && post.status === status).length]));
    assert.deepEqual((await counts.json()).data, { ...expected, ALL: Object.values(expected).reduce((a,b) => a+b,0) });
    const other = await fetch(`${url}/counts`, { headers: { authorization: `Bearer ${token({ sub: 'other-clinic' })}` } });
    assert.deepEqual((await other.json()).data, { PENDING: 0, APPROVED: 0, REJECTED: 1, ALL: 1 });
    assert.equal((await fetch(`${url}/counts`)).status, 401);
    assert.equal((await fetch(`${url}/counts`, { headers: { authorization: `Bearer ${token({ authorities: 'USER' })}` } })).status, 401);
    for (const query of ['status=INVALID', 'limit=0', 'limit=51', 'beforeId=abc']) {
      assert.equal((await fetch(`${url}?${query}`, { headers })).status, 400);
    }
  } finally {
    for (let i=posts.length-1;i>=0;i--) if(posts[i].id>=2000000) posts.splice(i,1);
  }
});

test('clinic list and count SQL always filter by clinic identity', async () => {
  const calls = [];
  const model = createClinicPostModel({ async execute(sql, params) { calls.push({ sql, params }); return [[]]; } });
  await model.listByClinic(7, { status: 'REJECTED', beforeId: 100, limit: 10 });
  assert.deepEqual(calls[0].params, [7, 'REJECTED', 100]);
  assert.match(calls[0].sql, /p.clinic_id = \? AND p.status = \? AND p.id < \?/);
  assert.match(calls[0].sql, /LIMIT 11/);
  await model.listByClinic(7);
  assert.deepEqual(calls[1].params, [7]);
  assert.doesNotMatch(calls[1].sql, /p.status = \?/);
  assert.match(calls[1].sql, /LIMIT 51/);
  assert.deepEqual(await model.countByStatus(7), { PENDING: 0, APPROVED: 0, REJECTED: 0 });
  assert.deepEqual(calls[2].params, [7]);
  assert.match(calls[2].sql, /WHERE p.clinic_id = \? GROUP BY p.status/);
  assert.doesNotMatch(calls[2].sql, /LIMIT/);
});

test('admin SQL pagination applies status and cursor before limiting rows', async () => {
  const calls = [];
  const model = createClinicPostModel({ async execute(sql, params) { calls.push({ sql, params }); return [[]]; } });
  await model.listForAdmin('PENDING', { beforeId: 123, limit: 10 });
  assert.deepEqual(calls[0].params, ['PENDING', 123]);
  assert.match(calls[0].sql, /p.status = \? AND p.id < \?\s+ORDER BY p.id DESC LIMIT 11/);
  await model.listForAdmin('REJECTED');
  assert.deepEqual(calls[1].params, ['REJECTED']);
  assert.match(calls[1].sql, /p.status = \?\s+ORDER BY p.id DESC LIMIT 51/);
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

test('SQL model auto-approves new posts and supports legacy moderation', async () => {
  const calls = [];
  const model = createClinicPostModel({ async execute(sql, params) {
    calls.push({ sql, params });
    if (sql.startsWith('INSERT')) return [{ insertId: 42 }];
    if (sql.startsWith('UPDATE')) return [{ affectedRows: 1 }];
    return [[{ id: 42, clinicThumbnailUrl: 'https://example.com/clinic-avatar.jpg',
      imageUrls: '["https://example.com/a.jpg"]' }]];
  } });
  assert.deepEqual((await model.create(7, 'Title', 'Content', ['https://example.com/a.jpg'])).imageUrls,
    ['https://example.com/a.jpg']);
  assert.match(calls[1].sql, /c\.thumbnail_url AS clinicThumbnailUrl/);
  assert.deepEqual(calls[0].params, [7, 'Title', 'Content', '["https://example.com/a.jpg"]']);
  assert.match(calls[0].sql, /'APPROVED', NULL, UTC_TIMESTAMP\(3\), UTC_TIMESTAMP\(3\)/);
  assert.match(calls[0].sql, /approved_by, approved_at, created_at/);
  await model.listPublic();
  assert.match(calls.at(-1).sql, /WHERE p.status = 'APPROVED'/);
  await model.findPublic(42);
  assert.match(calls.at(-1).sql, /p.id = \? AND p.status = 'APPROVED'/);
  await model.approve(42, 99);
  assert.deepEqual(calls.at(-2).params, [99, 42]);
  assert.match(calls.at(-2).sql, /WHERE id = \? AND status = 'PENDING'/);
  await model.reject(42);
  assert.deepEqual(calls.at(-2).params, [42]);
  assert.match(calls.at(-2).sql, /SET status = 'REJECTED', approved_by = NULL, approved_at = NULL/);
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
  assert.deepEqual(spec.paths['/api/v1/clinic/posts'].get.parameters[0].schema.enum, ['ALL', 'PENDING', 'APPROVED', 'REJECTED']);
  assert.deepEqual(spec.paths['/api/v1/clinic/posts/counts'].get.security, [{ clinicBearer: [] }]);
  assert.deepEqual(spec.paths['/api/v1/clinic/posts/{id}'].delete.security, [{ clinicBearer: [] }]);
  assert.deepEqual(spec.components.schemas.CreateClinicPost.required, ['title', 'content']);
  assert.equal(spec.components.schemas.CreateClinicPost.properties.imageUrls.maxItems, 10);
  assert.equal(spec.components.schemas.ClinicPost.properties.clinicThumbnailUrl.nullable, true);
  assert.deepEqual(spec.components.schemas.ClinicPost.properties.status.enum, ['PENDING', 'APPROVED', 'REJECTED']);
  assert.deepEqual(spec.paths['/api/v1/admin/clinic-posts/{id}/approve'].patch.security, [{ adminBearer: [] }]);
  assert.deepEqual(spec.paths['/api/v1/admin/clinic-posts/{id}/reject'].patch.security, [{ adminBearer: [] }]);
  assert.deepEqual(spec.paths['/api/v1/admin/clinic-posts/{id}'].get.security, [{ adminBearer: [] }]);
  assert.deepEqual(spec.paths['/api/v1/admin/clinic-posts/counts'].get.security, [{ adminBearer: [] }]);
  assert.deepEqual(Object.keys(spec.paths['/api/v1/admin/clinic-posts/{id}'].get.responses[200].content['application/json'].examples), ['PENDING', 'APPROVED', 'REJECTED']);
  assert.ok(spec.paths['/api/v1/admin/clinic-posts/{id}/reject'].patch.responses[409]);
  assert.equal(spec.paths['/api/v1/clinic/posts'].post.responses[201].content['application/json'].example.data.status, 'APPROVED');
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
