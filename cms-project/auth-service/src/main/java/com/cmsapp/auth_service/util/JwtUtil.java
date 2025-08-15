package com.cmsapp.auth_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
@Slf4j  // log özelliği ekler (log.info() log.error() gibi)
public class JwtUtil {


    @Value("${jwt.secret}")
    private String jwtSecret;
    // gizli anahtar, milisaniye geçerli
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // Bu anahtar token oluştururken ve token doğrularken kullanılır
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)          // Token'ın sahibi
                .claim("role", role)        // Kullanıcı rolü
                .setIssuedAt(now)              // Oluşturulma zamanı
                .setExpiration(expiryDate)     // Son geçerlilik zamanı
                .signWith(getSigningKey())     // İmzalama
                .compact();                    // String'e çevirme

        // log.info("JWT token generated for user: {}", username);
    }


    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            throw e;
        }
    }


    //  Token içindeki "role" alanını string olarak döner.
    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()  //tokeni çözmek için parser oluşturuyor
                    .setSigningKey(getSigningKey()) //
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("role", String.class);
        } catch (JwtException e) {
            log.error("Error extracting role from token: {}", e.getMessage());
            throw e;
        }
    }


    // Token geçerli mi, süresi dolmuş mu, imza doğru mu gibi kontrolleri yapar.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.debug("Token validation successful");
            return true;

        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("JWT token is malformed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT token validation error: {}", e.getMessage());
            return false;
        }
    }


    // Token’ın tam olarak hangi tarihte geçersiz olacağını döner.
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration();
        } catch (JwtException e) {
            log.error("Error extracting expiration from token: {}", e.getMessage());
            throw e;
        }
    }


    public long getTimeToExpiration(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        Date now = new Date();
        return expirationDate.getTime() - now.getTime();
    }


    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // "Bearer " = 7 karakter
        }
        return authHeader;
    }
}