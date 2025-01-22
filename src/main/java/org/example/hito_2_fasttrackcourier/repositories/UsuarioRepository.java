package org.example.hito_2_fasttrackcourier.repositories;

import org.example.hito_2_fasttrackcourier.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    List<Usuario> findByRol(String rol);
}
