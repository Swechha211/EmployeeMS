package com.ems.EmployeeMS.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    SecretKey key = Jwts.SIG.HS512.key().build();
//@Value("${jwt.signing.key}")
//    String jwtSecretKey;

@Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Authentication authentication) {
        String authorities=authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String token = Jwts.builder()
                .subject((String) authentication.getPrincipal())
                .claim("auth", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key)
//                .signWith(SignatureAlgorithm.HS512,jwtSecretKey)
                .compact();
        System.out.println("Generated Token: " + token);
        return  token;
    }


    public Claims extractAllClaims(String token) throws SignatureException {
        token = token.trim();

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)

                .getPayload();
    }

    public String extractUsername(String token) throws SignatureException {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws SignatureException {
        return userDetails.getUsername().equals(extractUsername(token));
    }

    public boolean isTokenExpired(String token) throws SignatureException {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}

//package com.ems.EmployeeMS.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.SignatureException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtTokenUtil {
//    @Value("${jwt.signing.key}")
//    private String jwtSecretKey;
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    private final SecretKey key;
//
//    public JwtTokenUtil(@Value("${jwt.signing.key}") String jwtSecretKey) {
//        this.key = new SecretKeySpec(jwtSecretKey.getBytes(), "HmacSHA512");
//    }
//
//    public String generateToken(Authentication authentication) {
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        return Jwts.builder()
//                .setSubject((String) authentication.getPrincipal())
//                .claim("auth", authorities)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .signWith(key) // Here, we use signWith method to specify the signing key
//                .compact();
//    }
//
//    public Claims extractAllClaims(String token) throws SignatureException {
//        // Parse the token and return the claims
//        return Jwts.parser().build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public String extractUsername(String token) throws JwtException {
//        return extractAllClaims(token).getSubject();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) throws JwtException {
//        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
//    }
//
//    public boolean isTokenExpired(String token) throws JwtException {
//        Date expirationDate = extractAllClaims(token).getExpiration();
//        return expirationDate != null && expirationDate.before(new Date());
//    }
//}
//
