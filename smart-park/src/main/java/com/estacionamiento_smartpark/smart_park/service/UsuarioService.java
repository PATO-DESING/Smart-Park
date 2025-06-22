package com.estacionamiento_smartpark.smart_park.service;


import com.estacionamiento_smartpark.smart_park.model.Usuario;
import com.estacionamiento_smartpark.smart_park.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AutoService autoService;

    public Usuario crearUsuario(Usuario usuario){
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())){
            throw new IllegalStateException("El Correo ya esta registrado.");
        }
        if (usuarioRepository.existsByRun(usuario.getRun())){
            throw new IllegalStateException("El Run ya esta registrado.");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
    }


    public List<Usuario> buscarPorNombre(String nombre){
        return usuarioRepository.findByNombreCompleto(nombre);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalStateException("Usuario no encontrado");
        }
        Usuario usuario = usuarioRepository.findById(id).get();
        autoService.deleteByUsuario(usuario);
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));

        if (usuarioActualizado.getCorreo() == null || usuarioActualizado.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }

        if (usuarioActualizado.getRun() == null || usuarioActualizado.getRun().isBlank()) {
            throw new IllegalArgumentException("El RUN no puede estar vacío.");
        }

        if (usuarioActualizado.getNombreCompleto() == null || usuarioActualizado.getNombreCompleto().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        if (!usuarioExistente.getCorreo().equals(usuarioActualizado.getCorreo()) &&
            usuarioRepository.existsByCorreo(usuarioActualizado.getCorreo())) {
            throw new IllegalStateException("El correo ya está registrado por otro usuario.");
        }

        if (!usuarioExistente.getRun().equals(usuarioActualizado.getRun()) &&
            usuarioRepository.existsByRun(usuarioActualizado.getRun())) {
            throw new IllegalStateException("El RUN ya está registrado por otro usuario.");
        }

        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setRun(usuarioActualizado.getRun());
        usuarioExistente.setNombreCompleto(usuarioActualizado.getNombreCompleto());

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario patchUsuario(Long id, Usuario parcialUsuario) {
        return usuarioRepository.findById(id).map(usuarioToUpdate -> {

            if (parcialUsuario.getCorreo() != null) {
                usuarioToUpdate.setCorreo(parcialUsuario.getCorreo());
            }

            if (parcialUsuario.getRun() != null) {
                usuarioToUpdate.setRun(parcialUsuario.getRun());
            }

            if (parcialUsuario.getNombreCompleto() != null) {
                usuarioToUpdate.setNombreCompleto(parcialUsuario.getNombreCompleto());
            }

            return usuarioRepository.save(usuarioToUpdate);
        }).orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
    }


}
