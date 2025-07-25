package com.jobportal.JobPortal.security;

import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.repository.UserRepository;
import com.jobportal.JobPortal.service.AuthService;
import com.jobportal.JobPortal.service.RecruiterService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

//    @Value("${jwt.secret:zidio_secret_key_enhanced_with_more_security_for_production}")
    private String SECRET_KEY="TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

//    @Value("${jwt.expiration:86400000}") // 24 hours
    private long EXPIRATION_TIME=86400000;

//    @Value("${jwt.refresh.expiration:604800000}") // 7 days
    private long REFRESH_EXPIRATION_TIME=604800000;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Long id,String email, String role) {
        return generateToken(id,email, role, EXPIRATION_TIME);
    }

    public String generateRefreshToken(Long id,String email, String role) {
        return generateToken(id,email, role, REFRESH_EXPIRATION_TIME);
    }



    private String generateToken(Long userId,String email, String role, long expiration) {
        try {
            Map<String, Object> claims = new HashMap<>();
            Recruiter recruiter = recruiterRepository.findByEmail(email).orElse(null);

            claims.put("role", role);
            claims.put("type", expiration == REFRESH_EXPIRATION_TIME ? "refresh" : "access");
            claims.put("userId", userId);
            claims.put("payStat",recruiter!=null ? recruiter.getPaymentStatus() : "PENDING");


            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token: {}", e.getMessage());
            throw new RuntimeException("Token generation failed", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String extractPayStat(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("payStat",String.class);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class); // safely gets userId as Long
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
            throw new RuntimeException("Token expired");
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
            throw new RuntimeException("Token unsupported");
        } catch (MalformedJwtException e) {
            logger.warn("JWT token is malformed: {}", e.getMessage());
            throw new RuntimeException("Token malformed");
        } catch (SignatureException e) {
            logger.warn("JWT signature does not match: {}", e.getMessage());
            throw new RuntimeException("Invalid token signature");
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token compact is invalid: {}", e.getMessage());
            throw new RuntimeException("Token is invalid");
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String refreshToken(String token) {
        try {
            String username = extractUsername(token);
            String role = extractRole(token);
            Long id=extractUserId(token);
            return generateToken(id,username, role);
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            throw new RuntimeException("Token refresh failed", e);
        }
    }

    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}