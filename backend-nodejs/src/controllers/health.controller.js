import { sendResponse } from '../utils/response.js';

export function createHealthController(checkReadiness) {
  return {
    live(request, response) {
      return sendResponse(response, 200, { status: 'ok' });
    },
    async ready(request, response) {
      let ready = false;
      try {
        ready = await checkReadiness();
      } catch {
        // Readiness must fail when the required database is unavailable.
      }
      return sendResponse(response, ready ? 200 : 503,
        { status: ready ? 'ready' : 'not_ready' }, ready ? 'OK' : 'Database unavailable');
    },
  };
}
