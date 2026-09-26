export function createClinicPostRoutes({ controller, authenticateClinic }) {
  function requireClinic(handler) {
    return async (request, response) => {
      const clinic = await authenticateClinic(request);
      await handler(request, response, clinic);
    };
  }

  return [
    { method: 'POST', path: '/api/v1/clinic/posts', handler: requireClinic(controller.create) },
    { method: 'GET', path: '/api/v1/clinic/posts', handler: requireClinic(controller.listByClinic) },
    { method: 'GET', path: '/api/v1/public/clinic-posts', handler: controller.listPublic },
  ];
}
