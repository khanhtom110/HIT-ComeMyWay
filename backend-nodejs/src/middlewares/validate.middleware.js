import { ApiError } from '../utils/ApiError.js';

export function validate(schemas) {
  return (request, response, next) => {
    const validated = {};
    for (const key of ['params', 'query', 'body']) {
      if (!schemas[key]) continue;
      const { value, error } = schemas[key].validate(request[key], {
        abortEarly: false, stripUnknown: true,
      });
      if (error) return next(new ApiError(400, error.details.map(detail => detail.message).join('; ')));
      validated[key] = value;
    }
    request.validated = validated;
    next();
  };
}
