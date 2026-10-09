export function sendResponse(response, statusCode, data, message = 'OK', pagination) {
  return response.status(statusCode).json({
    statusCode, message, data, timestamp: new Date().toISOString(),
    ...(pagination ? { pagination } : {}),
  });
}
