package com.pe.curso.spring.security;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("New JWT Signature Key: " + encodedKey);

        // Verificar la longitud de la clave
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        if (keyBytes.length < 32) { // 32 bytes * 8 = 256 bits
            throw new IllegalArgumentException("The key must be at least 256 bits.");
        } else {
            System.out.println("The key is valid and at least 256 bits long.");
        }
    }
}