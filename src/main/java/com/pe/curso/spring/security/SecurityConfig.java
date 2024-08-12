package com.pe.curso.spring.security;


import com.pe.curso.spring.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //le indica a Spring que esta clase es de seguridad al momento de arrancar la aplicación
@EnableWebSecurity //habilita la seguridad web y proporciona la integración de Spring Security con Spring MVC
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UsuarioRepository usuarioRepository;

    //esta sirve para pruebas unitarias
    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //este bena va a encargar de verificar la información de los usuarios
    @Bean
    AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encritar la contraseña de los usuarios
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este bean incorpara el filtro de seguridad de json web token que creamos
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(new CustomUserDetailService(usuarioRepository), new JwtGenerator());
    }

    //Este bean incorpara el filtro de seguridad de json web token que creamos
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/person/create").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/person/update").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/person/delete").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/person/find").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/person/list").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.disable());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

