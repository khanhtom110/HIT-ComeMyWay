import { ApiError } from '../../utils/ApiError.js';
import { catchAsync } from '../../utils/catchAsync.js';
import { verifyAccessToken } from '../../utils/jwt.js';

export function createAdminAuth({ adminStatisticsModel, jwtSecret }) {
  return catchAsync(async (request, response, next) => {
    const claims = verifyAccessToken(request.headers.authorization, jwtSecret, 'ADMIN');
    if (!claims) throw new ApiError(401, 'Token không hợp lệ');

    const admin = await adminStatisticsModel.findAdminByUsername(claims.sub, claims.jti);
    if (!admin) throw new ApiError(403, 'Chỉ quản trị viên được thực hiện thao tác này');
    request.admin = admin;
    next();
  });
}
