package com.ems.EmployeeMS.endpoints;

import com.ems.EmployeeMS.jwt.JwtAuthProvider;
import com.grpc.AuthServiceGrpc;
import com.grpc.Schema;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@GrpcService
public class AuthGrpcServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    @Value("${jwt.signing.key}")
    String jwtSecretKey;

    public final JwtAuthProvider jwtAuthProvider;

    public AuthGrpcServiceImpl(JwtAuthProvider jwtAuthProvider) {
        this.jwtAuthProvider = jwtAuthProvider;
    }

    @Override
    public void authenticate(Schema.JwtRequest request, StreamObserver<Schema.JwToken> responseObserver) {
        Authentication authenticate = jwtAuthProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);
        String authorities = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());

       responseObserver.onNext( Schema.JwToken.newBuilder().setJwtToken(Jwts.builder()
                .setSubject((String) authenticate.getPrincipal())
                .claim("auth", authorities)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512,jwtSecretKey)
                .compact()).build());
       responseObserver.onCompleted();
    }
}
