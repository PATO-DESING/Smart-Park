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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamiento_smartpark.smart_park.model.Sucursal;
import com.estacionamiento_smartpark.smart_park.service.SucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Sucursales", description = "Operaciones relacionadas con las sucursales") 
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Obtiene una lista de todas las sucursales")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar las sucursales") 
    }) 
    public ResponseEntity<List<Sucursal>> listar() {
        List<Sucursal> sucursales = sucursalService.findAll();
        if (sucursales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una sucursal por ID", description = "Retorna una sucursal específica utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Sucursal> getSucursalById(@PathVariable Long id) {
        Sucursal sucursal = sucursalService.findById(id);
        return ResponseEntity.ok(sucursal);
    }

    @PostMapping
    @Operation(summary = "Crea una nueva sucursal", description = "Crea sucursal")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear la sucursal") 
    }) 
    public ResponseEntity<Sucursal> guardar(@RequestBody Sucursal sucursal) {
        Sucursal sucursalNuevo = sucursalService.save(sucursal);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalNuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal", description = "Actualiza todos los datos de la sucursal")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Sucursal no actualizada") 
    })
    public ResponseEntity<Sucursal> actualizar(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        try {
            Sucursal sucursalActualizado = sucursalService.actualizarSucursal(id, sucursal);
            return ResponseEntity.ok(sucursalActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal", description = "Actualiza algunos datos de la sucursal")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Sucursal no actualizada") 
    })
    public ResponseEntity<Sucursal> actualizarParcial(@PathVariable Long id, @RequestBody Sucursal parcialSucursal) {
        Sucursal sucursalActualizado = sucursalService.patchSucursal(id, parcialSucursal);
        if (sucursalActualizado != null) {
            return ResponseEntity.ok(sucursalActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sucursal", description = "Elimina sucursal por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada") 
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            sucursalService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/direccion")
    @Operation(summary = "Obtener sucursal por dirección", description = "Obtiene una sucursal por dirección")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se encontro la sucursal") 
    }) 
    public List<Sucursal> getByDireccion(@RequestParam String direccion) {
        return sucursalService.findByDireccion(direccion);
    }

    @GetMapping("/nombre")
    @Operation(summary = "Obtener sucursal por nombre", description = "Obtiene una sucursal por su nombre")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se encontro sucursal") 
    }) 
    public List<Sucursal> getByNombre(@RequestParam String nombre) {
        return sucursalService.findByNombre(nombre);
    }

    @GetMapping("/comuna")
    @Operation(summary = "Obtener sucursales por comuna", description = "Obtiene una lista de las sucursales de la comuna")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar las sucursales") 
    }) 
    public List<Sucursal> getByComunaId(@RequestParam Long comunaId) {
        return sucursalService.findByComunaId(comunaId);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Obtener sucursal", description = "Obtiene una sucursal por nombre y comuna")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "No se encontro la sucursal")
    })
    public ResponseEntity<List<Sucursal>> buscarPorNombreYComuna(
            @RequestParam String nombre,
            @RequestParam Long comunaId) {
        List<Sucursal> sucursales = sucursalService.findByNombreAndComunaId(nombre, comunaId);


        if (sucursales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sucursales);
    }


}
