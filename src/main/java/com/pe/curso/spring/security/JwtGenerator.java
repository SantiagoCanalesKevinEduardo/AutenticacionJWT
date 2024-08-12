package com.pe.curso.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtGenerator {

    //Metodo para generar el token por medio de la autenticación
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentTime = new Date();
        Date expirationToken = new Date(currentTime.getTime() + ConstantSecurity.JWT_EXPIRATION_TOKEN);
        //Linea para generar el token
        return Jwts.builder()
                .subject(username)
                .issuedAt(currentTime)
                .expiration(expirationToken)
                .signWith(getSecretKey())
                .compact();
    }

    //Metodo para obtener el nombre de usuario del token
    public String getUsernameFromToken(String token){
        System.out.println("Token: "+token);
        return getClaims(token, Claims::getSubject);
    }

    private SecretKey getSecretKey() {
        String base64Key = ConstantSecurity.JWT_SIGNATURE_KEY.replace('_', '/').replace('-', '+');

        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        if (keyBytes.length < 32) { // 32 bytes * 8 = 256 bits
            throw new IllegalArgumentException("The key must be at least 256 bits.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token){
        return Jwts.parser().verifyWith(getSecretKey()).build(). parseSignedClaims(token).getPayload();
    }

    public <T> T getClaims(String token, Function<Claims,T> claimsResolver){
        final  Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Metodo para validar si el token esta expirado
    public Boolean isTokenExpired(String token){
        return getExpirationToken(token).before(new Date());
    }

    private Date getExpirationToken(String token){
        return getClaims(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));
    }

}
