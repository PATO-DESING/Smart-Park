package com.estacionamiento_smartpark.smart_park.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;
import com.estacionamiento_smartpark.smart_park.model.Registro;
import com.estacionamiento_smartpark.smart_park.service.RegistroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/registros")
@Tag(name = "Registros", description = "Operaciones relacionadas con los registros") 
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @GetMapping
    @Operation(summary = "Obtener todos los registros", description = "Obtiene una lista de todos los registros")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los registros") 
    }) 
    public ResponseEntity<List<Registro>> obtenerTodos() {
        return ResponseEntity.ok(registroService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro", description = "Obtiene registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })  
    public ResponseEntity<Registro> getRegistroById(@PathVariable Long id) {
        Registro registro = registroService.findById(id);
        return ResponseEntity.ok(registro);
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo registro", description = "Crea registro")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el registro") 
    }) 
    public ResponseEntity<Registro> crearRegistro(@RequestBody Registro registro) {
        return ResponseEntity.ok(registroService.save(registro));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro", description = "Elimina registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        registroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcial", description = "Actualizar registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })
    public ResponseEntity<Registro> patchRegistro(@PathVariable Long id, @RequestBody Registro parcialRegistro) {
        try {
            Registro registroActualizado = registroService.patchRegistro(id, parcialRegistro);
            return ResponseEntity.ok(registroActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/entrada")
    @Operation(summary = "Crea un nueva entrada", description = "Crea entrada")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear la entrada") 
    }) 
    public ResponseEntity<Estacionamiento> registrarEntrada(
            @RequestParam String patente,
            @RequestParam int numeroEstacionamiento) {
        return ResponseEntity.ok(registroService.registrarEntrada(patente, numeroEstacionamiento));
    }

    @PostMapping("/salida")
    @Operation(summary = "Crea un nueva salida", description = "Crea salida")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear la salida") 
    }) 
    public ResponseEntity<Estacionamiento> registrarSalida(@RequestParam String patente) {
        return ResponseEntity.ok(registroService.registrarSalida(patente));
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener registros activos", description = "Obtiene registros activos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se listaron los registros") 
    }) 
    public ResponseEntity<List<Registro>> obtenerRegistrosActivos() {
        List<Registro> registrosActivos = registroService.obtenerRegistrosActivos();
        return ResponseEntity.ok(registrosActivos);
    }

    @GetMapping("/fecha")
    @Operation(summary = "Obtener registro por fecha", description = "Obtiene registro por fecha")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    }) 
    public List<Registro> obtenerPorFecha(@RequestParam("fecha") String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        return registroService.obtenerRegistrosPorFecha(fecha);
    }

    @GetMapping("/patente/{patente}")
    @Operation(summary = "Obtener registros por patente", description = "Lista todos los registros de la patente")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No hay registros de la patente") 
    }) 
    public ResponseEntity<List<Registro>> obtenerPorPatente(@PathVariable String patente) {
        return ResponseEntity.ok(registroService.obtenerRegistrosPorPatente(patente));
    }

    @GetMapping("/rango")
    @Operation(summary = "Obtener registro por rango", description = "Obtiene registros por rango de fecha")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se listaron los registros") 
    }) 
    public ResponseEntity<List<Registro>> obtenerPorRangoFechas(
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return ResponseEntity.ok(registroService.obtenerRegistrosEntreFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/entradas-salidas")
    @Operation(summary = "Obtener entradas y salidas", description = "lista entradas y salidas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "No se listaron las entradas y salidas")
    })
    public List<Object[]> getEntradasSalidas() {
        return registroService.obtenerEntradasYSalidas();
    }
    
}