import assert from 'node:assert/strict';
import { createHmac } from 'node:crypto';
import { after, before, test } from 'node:test';
import { createApp } from '../src/app.js';

const secret = 'test-secret-at-least-32-characters-long';
const posts = [];
const repository = {
  async findClinicByUsername(username, jti) {
    return username === 'clinic' && jti !== 'revoked' ? { id: 7 } : null;
  },
  async create(clinicId, title, content) {
    const post = { id: posts.length + 1, clinicId, title, content };
    posts.push(post);
    return post;
  },
  async listByClinic(clinicId) { return posts.filter(post => post.clinicId === clinicId); },
  async listPublic() { return posts; },
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
before(async () => {
  server = createApp({ repository, secret });
  await new Promise(resolve => server.listen(0, '127.0.0.1', resolve));
  baseUrl = `http://127.0.0.1:${server.address().port}`;
});
after(() => server.close());

test('clinic publishes title and content; its posts are readable', async () => {
  const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: '  Lịch tiêm phòng  ', content: '  Có lịch mới  ' }),
  });
  assert.equal(response.status, 201);
  assert.deepEqual((await response.json()).data, {
    id: 1, clinicId: 7, title: 'Lịch tiêm phòng', content: 'Có lịch mới',
  });
  const feed = await fetch(`${baseUrl}/api/v1/public/clinic-posts`);
  assert.equal((await feed.json()).data.length, 1);
});

test('rejects empty fields and unauthorized tokens', async () => {
  for (const authorization of [undefined, `Bearer ${token({ authorities: 'USER' })}`,
    `Bearer ${token({ isRefresh: true })}`, `Bearer ${token({ jti: 'revoked' })}`]) {
    const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
      method: 'POST', headers: authorization ? { authorization } : {},
      body: JSON.stringify({ title: 'x', content: 'y' }),
    });
    assert.ok([401, 403].includes(response.status));
  }
  const response = await fetch(`${baseUrl}/api/v1/clinic/posts`, {
    method: 'POST', headers: { authorization: `Bearer ${token()}` },
    body: JSON.stringify({ title: '  ', content: 'text' }),
  });
  assert.equal(response.status, 400);
});
