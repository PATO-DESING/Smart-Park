package com.estacionamiento_smartpark.smart_park.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;
import com.estacionamiento_smartpark.smart_park.service.EstacionamientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/estacionamientos")
@Tag(name = "Estacionamientos", description = "Operaciones relacionadas con los estacionamientos") 
public class EstacionamientoController {

    @Autowired
    private EstacionamientoService estacionamientoService;

    @GetMapping
    @Operation(summary = "Obtener todos los estacionamientos", description = "Obtiene una lista de todos los estacionamientos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los estacionamientos") 
    }) 
    public ResponseEntity<List<Estacionamiento>> obtenerTodos() {
        return ResponseEntity.ok(estacionamientoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estacionamiento", description = "Obtiene estacionamiento por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Estacionamiento no encontrado") 
    })  
    public ResponseEntity<Estacionamiento> getEstacionamientoById(@PathVariable Long id) {
        Estacionamiento estacionamiento = estacionamientoService.findById(id);
        return ResponseEntity.ok(estacionamiento);
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Obtener estacionamiento por numero", description = "Obtiene estacionamiento por numero")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se encontro el estacionamiento") 
    }) 
    public ResponseEntity<Estacionamiento> obtenerPorNumero(@PathVariable int numero) {
        return estacionamientoService.findByNumero(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ocupados")
    @Operation(summary = "Obtener todas los estacionamientos ocupados", description = "Obtiene una lista de todos los estacionamientos ocupados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los estacionamientos ocupados") 
    }) 
    public ResponseEntity<List<Estacionamiento>> obtenerOcupados() {
        return ResponseEntity.ok(estacionamientoService.findOcupados());
    }

    @GetMapping("/libres")
    @Operation(summary = "Obtener todas los estacionamientos libres", description = "Obtiene una lista de todos los estacionamientos libres")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los estacionamientos libres") 
    }) 
    public ResponseEntity<List<Estacionamiento>> obtenerLibres() {
        return ResponseEntity.ok(estacionamientoService.findLibres());
    }

    @GetMapping("/sucursal/{sucursalId}")
    @Operation(summary = "Obtener estacionamientos por sucursal", description = "Obtiene una lista de todos los estacionamientos por sucursal")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los estacionamientos") 
    }) 
    public ResponseEntity<List<Estacionamiento>> obtenerPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(estacionamientoService.findBySucursalId(sucursalId));
    }

    @GetMapping("/sucursal/{sucursalId}/libres")
    @Operation(summary = "Obtener estacionamientos libres por sucursal", description = "Obtiene estacionamientos por sucursal")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los estacionamientos libres") 
    }) 
    public ResponseEntity<List<Estacionamiento>> obtenerLibresPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(estacionamientoService.findLibresBySucursalId(sucursalId));
    }

    @GetMapping("/patente/{patente}")
    @Operation(summary = "Obtener estacionamiento por patente", description = "Obtiene estacionamiento por patente")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Estacionamiento no encontrado") 
    }) 
    public ResponseEntity<Estacionamiento> obtenerPorPatenteAuto(@PathVariable String patente) {
        return estacionamientoService.findByAutoPatente(patente)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo estacionamiento", description = "Crea estacionamiento")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el estacionamiento") 
    }) 
    public ResponseEntity<Estacionamiento> crear(@RequestBody Estacionamiento est) {
        return ResponseEntity.ok(estacionamientoService.save(est));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estacionamiento", description = "Actualiza todos los datos del estacionamiento")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Estacionamiento no actualizado") 
    }) 
    public ResponseEntity<Estacionamiento> actualizar(@PathVariable Long id, @RequestBody Estacionamiento est) {
        return ResponseEntity.ok(estacionamientoService.actualizarEstacionamiento(id, est));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar un estacionamiento", description = "Actualiza algunos datos del estacionamiento")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Estacionamiento no actualizado") 
    }) 
    public ResponseEntity<Estacionamiento> patch(@PathVariable Long id, @RequestBody Estacionamiento parcial) {
        return ResponseEntity.ok(estacionamientoService.patchEstacionamiento(id, parcial));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estacionamiento", description = "Elimina estacionamiento por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Estacionamiento no encontrado") 
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estacionamientoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/con-autos-usuarios")
    @Operation(summary = "Lista de estacionamientos con autos y usuarios",
               description = "Obtiene el número del estacionamiento, patente del auto y nombre del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos obtenidos correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron datos")
    })
    public List<Object[]> obtenerEstacionamientosConAutosYUsuarios() {
        return estacionamientoService.obtenerEstacionamientosConAutosYUsuarios();
    }
}

