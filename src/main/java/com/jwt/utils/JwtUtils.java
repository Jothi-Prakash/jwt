package com.jwt.utils;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import com.jwt.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final SecretKey REFRESH_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static long expiryDuration = 30; // 30 seconds for Access Token
    private static long refreshExpiryDuration = 24 * 60 * 60; // 24 hours for Refresh Token

    // Generate Access Token
    public String generateJwt(UserModel userModel) {
        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + expiryDuration * 1000;
        Date issuedAt = new Date(milliTime);
        Date expiryAt = new Date(expiryTime);

        Claims claims = Jwts.claims()
                .setIssuer(userModel.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt);

        claims.put("userId", userModel.getId());
        claims.put("type", userModel.getUserType());
        claims.put("name", userModel.getName());
        claims.put("emailId", userModel.getEmailId());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Generate Refresh Token
    public String generateRefreshToken(UserModel userModel) {
        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + refreshExpiryDuration * 1000;
        Date issuedAt = new Date(milliTime);
        Date expiryAt = new Date(expiryTime);

        Claims claims = Jwts.claims()
                .setIssuer(userModel.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt);

        claims.put("userId", userModel.getId());
        claims.put("type", userModel.getUserType());
        claims.put("name", userModel.getName());
        claims.put("emailId", userModel.getEmailId());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(REFRESH_SECRET_KEY)
                .compact();
    }

    // Verify Access Token
    public Claims verify(String authorization) throws Exception {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(authorization)
                .getBody();
    }

    // Verify Refresh Token
    public Claims verifyRefreshToken(String refreshToken) throws Exception {
        return Jwts.parser()
                .setSigningKey(REFRESH_SECRET_KEY)
                .parseClaimsJws(refreshToken)
                .getBody();
    }
}
