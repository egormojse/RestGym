package Rest.Gym.RestGym.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Duration expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        // Generate a 256-bit (32-byte) secret key using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(hash);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);
        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = (List<String>) claims.get("roles");
        return roles != null ? roles : Collections.emptyList();
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        List<String> roles = extractRoles(token);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean hasRole(String token, String role) {
        List<String> roles = extractRoles(token);
        return roles.contains(role);
    }

    public boolean hasAnyRole(String token, String... roles) {
        List<String> userRoles = extractRoles(token);
        return Arrays.stream(roles)
                .anyMatch(userRoles::contains);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}