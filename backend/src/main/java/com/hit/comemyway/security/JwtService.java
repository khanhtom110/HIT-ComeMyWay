package com.hit.comemyway.security;

import com.hit.comemyway.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(User user) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("authorities", user.getRole())
                    .claim("userId", user.getId())
                    .claim("email", user.getEmail())
                    .build();

            //Ky
            SignedJWT signedJWT= new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),claimsSet);
            signedJWT.sign(new MACSigner(secretKey.getBytes()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Sai định dạng token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            //Kiem tra chữ ký
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            boolean isSignaltureValid = signedJWT.verify(verifier);

            //Kiem tra thoi gian
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isTokenExpired = expirationTime.before(new Date());

            //Kiem tra username
            String username = signedJWT.getJWTClaimsSet().getSubject();
            boolean isUsernameMatch = username.equals(userDetails.getUsername());

            return isSignaltureValid && !isTokenExpired && isUsernameMatch;

        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

}
