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
import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.service.AutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/autos")
@Tag(name = "Autos", description = "Operaciones relacionadas con los autos") 
public class AutoController {
    @Autowired
    private AutoService autoService;

    @GetMapping
    @Operation(summary = "Obtener todas los autos", description = "Obtiene una lista de todos los autos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los autos") 
    })      
    public ResponseEntity<List<Auto>> listar() {
        List<Auto> autos = autoService.findAll();
        if (autos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener auto", description = "Obtiene auto por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no encontrado") 
    })      
    public ResponseEntity<Auto> buscar(@PathVariable Long id) {
        try {
            Auto auto = autoService.findById(id);
            return ResponseEntity.ok(auto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo auto", description = "Crea auto")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el auto") 
    })     
    public ResponseEntity<Auto> guardar(@RequestBody Auto auto) {

        System.out.print(auto.getUsuario());
        Auto autoNuevo = autoService.save(auto);
        return ResponseEntity.status(HttpStatus.CREATED).body(autoNuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un auto", description = "Actualiza todos los datos del auto")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no actualizado") 
    })     
    public ResponseEntity<Auto> actualizar(@PathVariable Long id, @RequestBody Auto auto) {
        try {
            Auto autoActualizado = autoService.updateAuto(id, auto);
            return ResponseEntity.ok(autoActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcial auto", description = "Actualiza ciertos datos de un auto")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no actualizado") 
    })     
    public ResponseEntity<Auto> actualizarParcial(@PathVariable Long id, @RequestBody Auto parcialAuto) {
        Auto autoActualizado = autoService.patchAuto(id, parcialAuto);
        if (autoActualizado != null) {
            return ResponseEntity.ok(autoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un auto", description = "Elimina auto por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no encontrado") 
    })     
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            autoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/datos-usuarios")
    @Operation(summary = "Datos Usuario", description = "Lista los datos del usuario del auto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "No se pudo listar los datos")
    })   
    public List<Object[]> obtenerDatosAutosYUsuarios() {
        return autoService.obtenerDatosAutosYUsuarios();
    }

}
