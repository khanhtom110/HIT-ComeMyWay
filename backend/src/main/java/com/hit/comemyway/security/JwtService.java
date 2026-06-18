package com.hit.comemyway.security;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.entity.InvalidatedToken;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.InvalidatedRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long jwtAccessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpiration;

    private final InvalidatedRepository invalidatedRepository;

    public String generateToken(User user,boolean isRefresh) {
        try {
            long expirationTime = isRefresh ? jwtRefreshExpiration : jwtAccessExpiration;

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("isRefresh", isRefresh);

            if (!isRefresh) {
                claimsBuilder.claim("authorities", user.getRole())
                        .claim("userId", user.getId());
            }

            //Ky
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsBuilder.build());
            signedJWT.sign(new MACSigner(secretKey.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(400, ErrorMessage.Auth.MALFORMED_TOKEN);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            if(invalidatedRepository.existsById(jti)){
                return false;
            }

            //Kiem tra chữ ký
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            boolean isSignatureValid = signedJWT.verify(verifier);

            //Kiem tra thoi gian
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isTokenExpired = expirationTime.before(new Date());

            //Kiem tra username
            String username = signedJWT.getJWTClaimsSet().getSubject();
            boolean isUsernameMatch = username.equals(userDetails.getUsername());

            return isSignatureValid && !isTokenExpired && isUsernameMatch;

        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    //Dua token invalid vao table db
    public void invalidateToken(String token){
        try{
            SignedJWT signedJWT = SignedJWT.parse(token);

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .build();

            invalidatedRepository.save(invalidatedToken);
        } catch (ParseException e) {
            throw new AppException(400, ErrorMessage.Auth.MALFORMED_TOKEN);
        }
    }

    //Check param la access token
    public boolean isAccessToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Boolean isRefresh = signedJWT.getJWTClaimsSet().getBooleanClaim("isRefresh");
            return isRefresh == null || !isRefresh;
        } catch (ParseException e) {
            return false;
        }
    }
}
