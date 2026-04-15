package com.deliverytech.delivery_api.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = 
    "chave-super-secreta-para-jwt-delivery-2026-123456789";

    private static final long EXPIRATION = 5000;

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email){
        return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public String extractEmail(String token){
        return Jwts.parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token);
            return true;

        }catch(Exception e){
            return false;
        }
    }

    public Object extractRoles(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("roles");
    }

}
