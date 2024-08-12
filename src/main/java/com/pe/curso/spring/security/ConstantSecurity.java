package com.pe.curso.spring.security;

import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.Jwts;
import lombok.Getter;

public class ConstantSecurity {
     @Getter
    private static final Long JWT_EXPIRATION_TOKEN = 3000000L;
    @Getter
    private static final String JWT_SIGNATURE_KEY = generarTokenSignatureKey();

    private static String generarTokenSignatureKey(){
        Key key = Jwts.SIG.HS256.key().build();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        return encodedKey;
    }
}
