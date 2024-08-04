package com.pe.curso.spring.repositories;

import com.pe.curso.spring.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Sirve para buscar un usuario por su nombre de usuario
    //Optional es un contenedor que puede o no contener un valor no nulo
    Optional<Usuario> findByUsername(String username);
    Boolean existsByUsername(String username);
}
