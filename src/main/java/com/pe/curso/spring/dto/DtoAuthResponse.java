package com.pe.curso.spring.dto;

import lombok.Data;

@Data
public class DtoAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer ";

    public DtoAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
