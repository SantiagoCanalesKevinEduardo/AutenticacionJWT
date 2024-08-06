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
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
//La funcion de esta clase será validar el token que se envía en la cabecera de la petición
//Y si es correcto permitir el acceso a la aplicación
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private CustomUserDetailService customUserDetailService;
    private JwtGenerator jwtGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        logger.info("Token: "+token);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(jwtGenerator.getUsernameFromToken(token));
        if(StringUtils.hasText(token) && jwtGenerator.validateToken(token,userDetails)){
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            if(roles.contains("USER") || roles.contains("ADMIN")){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request,response);
            }

        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.info("Bearer Token: "+bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
