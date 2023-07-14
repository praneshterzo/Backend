package com.example.Portal.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtils {
    private final String key = "My_Secret_Key";
    public long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;

    private final List<String> blackedJwt = new ArrayList<>();

    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        blackedJwt.add(jwt);
    }

    public String generateJwt(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }


    public String extractUserName(String jwt) {
        return extractClaims(jwt, Claims::getSubject);
    }

    public Date extractExpirationDate(String jwt) {
        return extractClaims(jwt, Claims::getExpiration);
    }

    private <T> T extractClaims(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwt);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
    }

    public boolean isExpired(String jwt) {
        return extractExpirationDate(jwt).before(new Date());
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        String username = extractUserName(jwt);
        return userDetails.getUsername().equals(username) && !isExpired(jwt);
    }

    public void cleanExpired() {
        for (String jwt : blackedJwt) {
            if (isExpired(jwt)) {
                blackedJwt.remove(jwt);
                if (blackedJwt.size() == 0) {
                    return;
                }
            }
        }
    }
}
