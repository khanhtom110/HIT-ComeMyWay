import { createServer } from 'node:http';
import { verifyClinicToken } from './auth.js';

const MAX_BODY_BYTES = 64 * 1024;

function send(response, statusCode, data, message = 'OK') {
  response.writeHead(statusCode, { 'Content-Type': 'application/json; charset=utf-8' });
  response.end(JSON.stringify({ statusCode, message, data, timestamp: new Date().toISOString() }));
}

async function readJson(request) {
  let body = '';
  for await (const chunk of request) {
    body += chunk;
    if (Buffer.byteLength(body) > MAX_BODY_BYTES) {
      const error = new Error('Nội dung yêu cầu quá lớn');
      error.statusCode = 413;
      throw error;
    }
  }
  try {
    return JSON.parse(body);
  } catch {
    const error = new Error('JSON không hợp lệ');
    error.statusCode = 400;
    throw error;
  }
}

export function createApp({ repository, secret }) {
  if (!repository || !secret) throw new Error('Missing repository or JWT_SECRET');

  return createServer(async (request, response) => {
    const path = new URL(request.url, 'http://localhost').pathname;
    try {
      if (path === '/health' && request.method === 'GET') {
        return send(response, 200, { status: 'ok' });
      }
      if (path === '/api/v1/public/clinic-posts' && request.method === 'GET') {
        return send(response, 200, await repository.listPublic());
      }
      if (path !== '/api/v1/clinic/posts' || !['GET', 'POST'].includes(request.method)) {
        return send(response, 404, null, 'Không tìm thấy API');
      }

      const claims = verifyClinicToken(request.headers.authorization, secret);
      if (!claims) return send(response, 401, null, 'Token không hợp lệ');
      const clinic = await repository.findClinicByUsername(claims.sub, claims.jti);
      if (!clinic) return send(response, 403, null, 'Chỉ phòng khám được đăng tin');

      if (request.method === 'GET') {
        return send(response, 200, await repository.listByClinic(clinic.id));
      }

      const input = await readJson(request);
      const title = typeof input?.title === 'string' ? input.title.trim() : '';
      const content = typeof input?.content === 'string' ? input.content.trim() : '';
      if (!title || title.length > 200 || !content || content.length > 10000) {
        return send(response, 400, null, 'Tiêu đề (1–200 ký tự) và nội dung (1–10000 ký tự) là bắt buộc');
      }
      const post = await repository.create(clinic.id, title, content);
      return send(response, 201, post, 'Đăng tin thành công');
    } catch (error) {
      if (error.statusCode) return send(response, error.statusCode, null, error.message);
      console.error('Clinic posts request failed:', error);
      return send(response, 500, null, 'Không thể xử lý yêu cầu');
    }
  });
}
