package com.estacionamiento_smartpark.smart_park.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.estacionamiento_smartpark.smart_park.model.Usuario;
import com.estacionamiento_smartpark.smart_park.repository.UsuarioRepository;

@SpringBootTest
public class UsuarioServiceTest {
    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private Usuario createUsuario(){
        return new Usuario(
            1L,
            "22222222", 
            "pedro juan", 
            "pedrojuan@gmail", 
            "123456");
    }

    @Test
    public void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(createUsuario()));
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    public void findById() {
        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(createUsuario()));
        Usuario usuario = usuarioService.obtenerPorId(1L);
        assertNotNull(usuario);
        assertEquals("pedro juan", usuario.getNombreCompleto());
    }

    @Test
    public void testSave() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario savedUsuario = usuarioService.crearUsuario(usuario);
        assertNotNull(savedUsuario);
        assertEquals("pedro juan", savedUsuario.getNombreCompleto());
    }

    @Test
    public void testPatchUsuario() {
        Usuario existingUsuario = createUsuario();
        Usuario patchData = new Usuario();
        patchData.setNombreCompleto("pedro Actualizado");

        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existingUsuario);

        Usuario patchedUsuario = usuarioService.patchUsuario(1L, patchData);
        assertNotNull(patchedUsuario);
        assertEquals("pedro Actualizado", patchedUsuario.getNombreCompleto());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1L);
        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testBuscarPorNombre() {
        List<Usuario> usuarios = usuarioService.buscarPorNombre("pedro juan");
        assertNotNull(usuarios);
    }

    @Test
    public void testActualizarUsuario() {
        Usuario existente = createUsuario();
        Usuario actualizado = new Usuario();
        actualizado.setCorreo("nuevo@gmail.com");
        actualizado.setRun("22222222");
        actualizado.setNombreCompleto("Pedro Nuevo");

        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
        when(usuarioRepository.existsByCorreo("nuevo@gmail.com")).thenReturn(false);
        when(usuarioRepository.existsByRun("22222222")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);

        Usuario resultado = usuarioService.actualizarUsuario(1L, actualizado);

        assertNotNull(resultado);
        assertEquals("nuevo@gmail.com", resultado.getCorreo());
        assertEquals("Pedro Nuevo", resultado.getNombreCompleto());
    }

    @Test
    public void testCrearUsuarioCorreoDuplicado() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(true);

        try {
            usuarioService.crearUsuario(usuario);
        } catch (IllegalStateException e) {
            assertEquals("El Correo ya esta registrado.", e.getMessage());
        }
    }

    @Test
    public void testCrearUsuarioRunDuplicado() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(usuarioRepository.existsByRun(usuario.getRun())).thenReturn(true);

        try {
            usuarioService.crearUsuario(usuario);
        } catch (IllegalStateException e) {
            assertEquals("El Run ya esta registrado.", e.getMessage());
        }
    }


}
