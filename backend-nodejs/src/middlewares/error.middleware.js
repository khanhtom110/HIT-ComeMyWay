import { HttpError } from '../http/http-error.js';
import { sendJson } from '../http/json.js';

export function handleError(error, response) {
  if (error instanceof HttpError) {
    return sendJson(response, error.statusCode, null, error.message);
  }
  console.error('Request failed:', error);
  return sendJson(response, 500, null, 'Không thể xử lý yêu cầu');
}
