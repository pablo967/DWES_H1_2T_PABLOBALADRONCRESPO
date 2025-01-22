package org.example.hito_2_fasttrackcourier.repositories;

import org.example.hito_2_fasttrackcourier.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositories extends JpaRepository<Usuario, String>{
}
