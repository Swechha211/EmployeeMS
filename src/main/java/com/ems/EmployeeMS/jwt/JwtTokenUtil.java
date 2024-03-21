//package com.ems.EmployeeMS.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.SignatureException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//public class JwtTokenUtil {
//    SecretKey key = Jwts.SIG.HS512.key().build();
//   @Value("${jwt.expiration}")
//    private long expiration;
//
//    public String generateToken(Authentication authentication) {
//        String authorities=authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
//
//        return Jwts.builder()
//                .subject((String) authentication.getPrincipal())
//                .claim("auth", authorities)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .signWith(key)
//                .compact();
//    }
//
//    public Claims extractAllClaims(String token) throws SignatureException {
//        return Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    public String extractUsername(String token) throws SignatureException {
//        return extractAllClaims(token).getSubject();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) throws SignatureException {
//        return userDetails.getUsername().equals(extractUsername(token));
//    }
//
//    public boolean isTokenExpired(String token) throws SignatureException {
//        return extractAllClaims(token).getExpiration().before(new Date());
//    }
//
//}
