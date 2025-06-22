package com.estacionamiento_smartpark.smart_park.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.service.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/comunas")
@Tag(name = "Comunas", description = "Operaciones relacionadas con las comunas") 
public class ComunaController {
    @Autowired
    private ComunaService comunaService;

    @GetMapping
    @Operation(summary = "Obtener todas las comunas", description = "Obtiene una lista de todas las comunas")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los autos") 
    }) 
    public ResponseEntity<List<Comuna>> listar(){
        List<Comuna> comuna = comunaService.findAll();
        if (comuna.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comuna);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener comuna", description = "Obtiene comuna por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "comuna no encontrado") 
    })  
    public ResponseEntity<Comuna> buscar(@PathVariable Long id) {
        try {
            Comuna comuna = comunaService.findById(id);
            return ResponseEntity.ok(comuna);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea una nueva comuna", description = "Crea comuna")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear la comuna") 
    }) 
    public ResponseEntity<Comuna> guardar(@RequestBody Comuna comuna) {
        Comuna comunaNueva = comunaService.save(comuna);
        return ResponseEntity.status(HttpStatus.CREATED).body(comunaNueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una comuna", description = "Actualiza todos los datos de la comuna")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Comuna no actualizada") 
    }) 
    public ResponseEntity<Comuna> actualizar(@PathVariable Long id, @RequestBody Comuna comuna) {
        try {
            Comuna comunaActualizado = comunaService.updateComuna(id, comuna);
            return ResponseEntity.ok(comunaActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar una comuna", description = "Actualiza algunos datos de la comuna")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Comuna no actualizada") 
    }) 
    public ResponseEntity<Comuna> actualizarParcial(@PathVariable Long id, @RequestBody Comuna parcialComuna) {
        Comuna comunaActualizado = comunaService.patchComuna(id, parcialComuna);
        if (comunaActualizado != null) {
            return ResponseEntity.ok(comunaActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una comuna", description = "Elimina comuna por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada") 
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            comunaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca una comuna", description = "Busca comuna por nombre y region")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<List<Comuna>> buscarPorNombreYRegion(
            @RequestParam String nombre,
            @RequestParam Long regionId) {
        List<Comuna> comunas = comunaService.findByNombreAndRegionId(nombre, regionId);


        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }


}
