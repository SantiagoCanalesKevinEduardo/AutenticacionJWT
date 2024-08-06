package com.pe.curso.spring.security;


import com.pe.curso.spring.models.Rol;
import com.pe.curso.spring.models.Usuario;
import com.pe.curso.spring.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    //Inyectamos el repositorio de usuarios
    @Autowired
    public CustomUserDetailService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    //Este metodo se encarga de mapear los roles de un usuario a los roles de Spring Security
    public Collection<GrantedAuthority> mapToAuthorities(List<Rol> roles){
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
    }

    //Este metodo se encarga de buscar un usuario por su nombre de usuario y devolver un objeto UserDetails para que Spring Security pueda hacer la autenticacion
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuario.getUsername(),usuario.getPassword(), mapToAuthorities(usuario.getRoles()));
    }

}
