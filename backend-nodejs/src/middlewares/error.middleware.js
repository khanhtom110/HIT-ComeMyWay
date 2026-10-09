import { ApiError } from '../utils/ApiError.js';
import { sendResponse } from '../utils/response.js';

export function notFound(request, response, next) {
  next(new ApiError(404, 'Không tìm thấy API'));
}

export function handleError(error, request, response, next) {
  if (response.headersSent) return next(error);
  if (error instanceof ApiError) {
    return sendResponse(response, error.statusCode, null, error.message);
  }
  if (error.type === 'entity.parse.failed') {
    return sendResponse(response, 400, null, 'JSON không hợp lệ');
  }
  if (error.type === 'entity.too.large') {
    return sendResponse(response, 413, null, 'Nội dung yêu cầu quá lớn');
  }
  if (error.status === 415) {
    return sendResponse(response, 415, null, 'Định dạng dữ liệu không được hỗ trợ');
  }
  console.error('Request failed:', error.code ?? error.name);
  return sendResponse(response, 500, null, 'Không thể xử lý yêu cầu');
}
