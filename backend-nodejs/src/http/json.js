import { HttpError } from './http-error.js';

const MAX_BODY_BYTES = 64 * 1024;

export function sendJson(response, statusCode, data, message = 'OK') {
  response.writeHead(statusCode, { 'Content-Type': 'application/json; charset=utf-8' });
  response.end(JSON.stringify({ statusCode, message, data, timestamp: new Date().toISOString() }));
}

export async function readJson(request) {
  const chunks = [];
  let byteLength = 0;
  for await (const chunk of request) {
    byteLength += chunk.length;
    if (byteLength > MAX_BODY_BYTES) {
      throw new HttpError(413, 'Nội dung yêu cầu quá lớn');
    }
    chunks.push(chunk);
  }
  try {
    return JSON.parse(Buffer.concat(chunks).toString('utf8'));
  } catch {
    throw new HttpError(400, 'JSON không hợp lệ');
  }
}
