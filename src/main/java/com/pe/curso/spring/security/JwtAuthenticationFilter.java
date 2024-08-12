package com.pe.curso.spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//La funcion de esta clase será validar el token que se envía en la cabecera de la petición
//Y si es correcto permitir el acceso a la aplicación
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailService customUserDetailService;
    private final JwtGenerator jwtGenerator;

    public JwtAuthenticationFilter(CustomUserDetailService customUserDetailService, JwtGenerator jwtGenerator) {
        this.customUserDetailService = customUserDetailService;
        this.jwtGenerator = jwtGenerator;
    }


    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);
            logger.info("Token: " + token);
            if (StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
                String username = jwtGenerator.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException ex) {
            logger.error("JWT token validation error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token format.");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (token.chars().filter(ch -> ch == '.').count() == 2) {
                return token;
            } else {
                logger.error("Invalid JWT token format: " + token);
                throw new IllegalArgumentException("Invalid JWT token format.");
            }
        }
        return null;
    }



}
