import { createHmac, timingSafeEqual } from 'node:crypto';

function decodeBase64Url(value) {
  return Buffer.from(value, 'base64url');
}

export function verifyClinicToken(authorization, secret) {
  if (typeof authorization !== 'string' || !authorization.startsWith('Bearer ')) return null;
  const token = authorization.slice(7);
  const parts = token.split('.');
  if (parts.length !== 3 || !parts.every(Boolean)) return null;

  try {
    const header = JSON.parse(decodeBase64Url(parts[0]).toString('utf8'));
    if (header.alg !== 'HS256' || (header.typ != null && header.typ !== 'JWT')) return null;
    const expected = createHmac('sha256', secret).update(`${parts[0]}.${parts[1]}`).digest();
    const actual = decodeBase64Url(parts[2]);
    if (actual.length !== expected.length || !timingSafeEqual(actual, expected)) return null;

    const claims = JSON.parse(decodeBase64Url(parts[1]).toString('utf8'));
    const now = Math.floor(Date.now() / 1000);
    if (claims.exp == null || !Number.isInteger(claims.exp) || claims.exp <= now ||
        (claims.nbf != null && claims.nbf > now) || claims.isRefresh !== false ||
        claims.authorities !== 'CLINIC' || typeof claims.sub !== 'string' ||
        !claims.sub || typeof claims.jti !== 'string' || !claims.jti) return null;
    return claims;
  } catch {
    return null;
  }
}
