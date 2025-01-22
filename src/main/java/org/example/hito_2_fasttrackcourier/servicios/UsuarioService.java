package org.example.hito_2_fasttrackcourier.servicios;

import org.example.hito_2_fasttrackcourier.modelo.Usuario;
import org.example.hito_2_fasttrackcourier.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllConductores() {
        return usuarioRepository.findByRol("conductor");
    }

    public Usuario getUsuarioByDni(String dni) {
        return usuarioRepository.findById(dni).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}