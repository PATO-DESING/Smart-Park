package com.estacionamiento_smartpark.smart_park.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamiento_smartpark.smart_park.model.Usuario;
import com.estacionamiento_smartpark.smart_park.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios") 
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los usuarios") 
    }) 
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener usuario", description = "Obtiene usuario por nombre")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado") 
    })  
    public ResponseEntity<List<Usuario>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo usuario", description = "Crea usuario")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el usuario") 
    }) 
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioNuevo = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza todos los datos del usuario")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Usuario no actualizado") 
    })
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza algunos datos del usuario")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Usuario no actualizado") 
    })
    public ResponseEntity<Usuario> actualizarParcial(@PathVariable Long id, @RequestBody Usuario parcialUsuario) {
        Usuario usuarioActualizado = usuarioService.patchUsuario(id, parcialUsuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina usuario por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado") 
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
