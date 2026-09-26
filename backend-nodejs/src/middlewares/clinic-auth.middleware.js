import { HttpError } from '../http/http-error.js';
import { verifyClinicToken } from '../security/jwt.js';

export function createClinicAuth({ repository, secret }) {
  return async function authenticateClinic(request) {
    const claims = verifyClinicToken(request.headers.authorization, secret);
    if (!claims) throw new HttpError(401, 'Token không hợp lệ');

    const clinic = await repository.findClinicByUsername(claims.sub, claims.jti);
    if (!clinic) throw new HttpError(403, 'Chỉ phòng khám được đăng tin');
    return clinic;
  };
}
