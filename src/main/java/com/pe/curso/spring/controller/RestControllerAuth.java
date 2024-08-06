package com.pe.curso.spring.controller;

import com.pe.curso.spring.dto.DtoAuthResponse;
import com.pe.curso.spring.dto.DtoLogin;
import com.pe.curso.spring.dto.DtoRegister;
import com.pe.curso.spring.models.Rol;
import com.pe.curso.spring.models.Usuario;
import com.pe.curso.spring.repositories.RolRepository;
import com.pe.curso.spring.repositories.UsuarioRepository;
import com.pe.curso.spring.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class RestControllerAuth {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private RolRepository rolRepository;
    private UsuarioRepository usuarioRepository;
    private JwtGenerator jwtGenerator;

    @Autowired
    public RestControllerAuth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RolRepository rolRepository, UsuarioRepository usuarioRepository, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtGenerator = jwtGenerator;
    }

    //Aqui se crearan los metodos para registrar y loguear usuarios
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody DtoRegister register){
        if(usuarioRepository.existsByUsername(register.getUsername())){
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(register.getUsername());
        usuario.setPassword(passwordEncoder.encode(register.getPassword()));

        Rol rol = rolRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRoles(Collections.singletonList(rol));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado");
    }

    //Metodo para registrar un administrador
    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody DtoRegister register){
        if(usuarioRepository.existsByUsername(register.getUsername())){
            return ResponseEntity.badRequest().body("El nombre de administrador ya existe");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(register.getUsername());
        usuario.setPassword(passwordEncoder.encode(register.getPassword()));

        Rol rol = rolRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRoles(Collections.singletonList(rol));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Administrador registrado");
    }

    //Metodo para loguear un usuario
    @GetMapping("/login")
    public ResponseEntity<DtoAuthResponse> login(@RequestBody DtoLogin login){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.ok(new DtoAuthResponse(token));
    }
}
