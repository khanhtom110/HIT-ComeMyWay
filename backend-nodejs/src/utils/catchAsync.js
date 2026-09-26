export function catchAsync(handler) {
  return (request, response, next) => {
    Promise.resolve().then(() => handler(request, response, next)).catch(next);
  };
}
