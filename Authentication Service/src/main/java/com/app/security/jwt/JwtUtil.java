package com.app.security.jwt;

import com.app.config.LoggerService;
import com.app.exception.common.BaseRunTimeException;
import com.app.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.app.utils.Utils.tagMethodName;

@Component
public class JwtUtil {

    private static final String TAG = "JwtUtil";
    private static final String KEY_FOLDER = "src/main/resources/keys/";
    private static final String PUBLIC_KEY_FILE = KEY_FOLDER + "public_key.pem";
    private static final String PRIVATE_KEY_FILE = KEY_FOLDER + "private_key.pem";
    private final LoggerService logger;

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Getter
    @Value("${security.jwt.expiration-ms}")
    private long jwtExpiration;

    public JwtUtil(LoggerService logger) {
        this.logger = logger;
    }

    // ------------------- KEY LOADING -------------------

    private PrivateKey readPrivateKey() throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE)))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    private PublicKey readPublicKey() throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE)))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    // ------------------- TOKEN BUILDERS -------------------

    private String buildTokenRS256(Map<String, Object> extraClaims, UserDetails user, long expiration) {
        String methodName = "buildTokenRS256";
        try {
            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(readPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to build RS256 token", e);
            return null;
        }
    }

    private String buildTokenHS256(Map<String, Object> extraClaims, UserDetails user, long expiration) {
        String methodName = "buildTokenHS256";
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            Key key = Keys.hmacShaKeyFor(keyBytes);
            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to build HS256 token", e);
            return null;
        }
    }

    // ------------------- TOKEN GENERATION -------------------

    public String generateToken(User user) {
        String methodName = "generateToken";
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("user_id", user.getId());
            claims.put("role", user.getRole());
            return buildTokenRS256(claims, user, jwtExpiration);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to generate token", e);
            return null;
        }
    }

    // ------------------- TOKEN EXTRACTION -------------------

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("user_id", Long.class));
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        String methodName = "extractAllClaims";
        try {
            PublicKey publicKey = readPublicKey();
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error(tagMethodName(TAG, methodName), "JWT expired", e);
            throw e;
        } catch (JwtException e) {
            logger.error(tagMethodName(TAG, methodName), "Invalid JWT", e);
            throw e;
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Token parsing failed", e);
            throw new BaseRunTimeException("Invalid token");
        }
    }

    // ------------------- TOKEN VALIDATION -------------------

    public boolean isTokenValid(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return extractedUsername != null && extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, "isTokenValid"), "Invalid token", e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public boolean verifyToken(String token) {
        try {
            PublicKey publicKey = readPublicKey();
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error(tagMethodName(TAG, "verifyToken"), "Invalid token", e);
            return false;
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, "verifyToken"), "Invalid token", e);
            throw new BaseRunTimeException("Token verification failed");
        }
    }

    // ------------------- EXPIRATION HELPERS -------------------

    public long getExpirationTime(String token) {
        try {
            Date expirationDate = extractExpiration(token);
            return Math.max(expirationDate.getTime() - System.currentTimeMillis(), 0);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, "getExpirationTime"), "Error reading expiration", e);
            return 0;
        }
    }

}
