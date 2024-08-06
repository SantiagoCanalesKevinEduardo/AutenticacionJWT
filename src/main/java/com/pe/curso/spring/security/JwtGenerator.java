package com.pe.curso.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
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
        return getClaims(token, Claims::getSubject);
    }

    private SecretKey getSecretKey(){

        return Keys.hmacShaKeyFor(ConstantSecurity.JWT_SIGNATURE_KEY.getBytes());
    }

    private Claims getAllClaims(String token){
        return Jwts.parser().verifyWith(getSecretKey()).build().parseClaimsJws(token).getPayload();
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

    public Boolean validateToken(String token, UserDetails userDetail){
        String username = getUsernameFromToken(token);
        return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));
    }

}
