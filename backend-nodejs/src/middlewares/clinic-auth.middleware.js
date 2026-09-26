import { ApiError } from '../utils/ApiError.js';
import { catchAsync } from '../utils/catchAsync.js';
import { verifyClinicToken } from '../utils/jwt.js';

export function createClinicAuth({ clinicModel, jwtSecret }) {
  return catchAsync(async (request, response, next) => {
    const claims = verifyClinicToken(request.headers.authorization, jwtSecret);
    if (!claims) throw new ApiError(401, 'Token không hợp lệ');

    const clinic = await clinicModel.findActiveByUsername(claims.sub, claims.jti);
    if (!clinic) throw new ApiError(403, 'Chỉ phòng khám được đăng tin');
    request.clinic = clinic;
    next();
  });
}
