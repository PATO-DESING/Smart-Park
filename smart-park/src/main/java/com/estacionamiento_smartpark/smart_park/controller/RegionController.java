package com.estacionamiento_smartpark.smart_park.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamiento_smartpark.smart_park.model.Region;
import com.estacionamiento_smartpark.smart_park.service.RegionService;

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
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "Operaciones relacionadas con las regiones") 
public class RegionController {
    @Autowired
    private RegionService regionService;

    @GetMapping
    @Operation(summary = "Obtener todas las regiones", description = "Obtiene una lista de todas las regiones")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar las regiones") 
    }) 
    public ResponseEntity<List<Region>> listar(){
        List<Region> regiones = regionService.findAll();
        if (regiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(regiones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener región por ID", description = "Retorna una región según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región encontrada"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        Region region = regionService.findById(id);
        return ResponseEntity.ok(region);
    }

    @PostMapping
    @Operation(summary = "Crea una nueva región", description = "Crea región")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear la región") 
    }) 
    public ResponseEntity<Region> guardar(@RequestBody Region region) {
        Region regionNueva = regionService.save(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(regionNueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una región", description = "Actualiza todos los datos de la región")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Región no actualizada") 
    }) 
    public ResponseEntity<Region> actualizar(@PathVariable Long id, @RequestBody Region region) {
        try {
            Region regionActualizado = regionService.updateRegion(id, region);
            return ResponseEntity.ok(regionActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar una región", description = "Actualiza algunos datos de la región")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Región no actualizada") 
    })
    public ResponseEntity<Region> actualizarParcial(@PathVariable Long id, @RequestBody Region parcialRegion) {
        Region regionActualizado = regionService.patchRegion(id, parcialRegion);
        if (regionActualizado != null) {
            return ResponseEntity.ok(regionActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una región", description = "Elimina región por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Región no encontrada") 
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            regionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar región por nombre", description = "Retorna una región si existe el nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región encontrada"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    public ResponseEntity<Region> obtenerPorNombre(@RequestParam String nombre) {
        Region region = regionService.findByNombre(nombre);
        if (region != null) {
            return ResponseEntity.ok(region);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
