package com.pe.curso.spring.security;

import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Jwts.SIG.HS256.key().build();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("New JWT Signature Key: " + encodedKey);
    }
}