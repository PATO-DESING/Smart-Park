package com.estacionamiento_smartpark.smart_park.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamiento_smartpark.smart_park.model.Pago;
import com.estacionamiento_smartpark.smart_park.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos") 
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    @Operation(summary = "Crea un nuevo pago", description = "Crea pago")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el pago") 
    }) 
    public ResponseEntity<Pago> crearPago(@RequestParam Long registroId,
                                          @RequestParam String metodoPago) {
        return ResponseEntity.ok(pagoService.crearPago(registroId, metodoPago));
    }
    @GetMapping
    @Operation(summary = "Obtener todos los pagos", description = "Obtiene una lista de todos los pagos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los pagos") 
    }) 
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodosLosPagos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago", description = "Obtiene pago por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no encontrado") 
    })  
    public ResponseEntity<Pago> getPagoById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/metodo/{metodo}")
    @Operation(summary = "Obtener pagos por metodo", description = "Obtiene una lista de todos los pagos por metodo")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los pagos") 
    }) 
    public ResponseEntity<List<Pago>> obtenerPorMetodo(@PathVariable String metodo) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorMetodo(metodo));
    }

    @GetMapping("/rango")
    @Operation(summary = "Obtener pagos por rango", description = "Obtiene una lista de todos los pagos en rango de fechas")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los pagos") 
    }) 
    public ResponseEntity<List<Pago>>obtenerPagosPorFechas(@RequestParam String inicio,@RequestParam String fin){
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return ResponseEntity.ok(pagoService.obtenerPagosEntreFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/registro/{registroId}")
    @Operation(summary = "Obtener pago por registro", description = "Obtiene pago por el id del registro")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no encontrado") 
    }) 
    public ResponseEntity<Pago> obtenerPorRegistroId(@PathVariable Long registroId){
        Optional<Pago> pago = pagoService.obtenerPagoPorRegistroId(registroId);
        return pago.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }   

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago", description = "Elimina pago por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no encontrado") 
    })
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id){
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(summary = "Actualizar un pago", description = "Actualiza todos los datos del pago")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no actualizado") 
    }) 
    public ResponseEntity<Pago> actualizarPago(@RequestBody Pago pago){
        return ResponseEntity.ok(pagoService.actualizarPago(pago));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcial un pago", description = "Actualiza algunos datos del pago")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no actualizado") 
    }) 
    public ResponseEntity<Pago> patchPago(@PathVariable Long id, @RequestBody Pago parcialPago) {
        try {
            Pago pagoActualizado = pagoService.patchPago(id, parcialPago);
            return ResponseEntity.ok(pagoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/detalles-registro")
    @Operation(summary = "Listar pagos con detalles de registro", description = "Devuelve fecha de pago, monto, hora de llegada y hora de salida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontraron registros")
    })
    public List<Object[]> obtenerPagosConDetallesRegistro() {
        return pagoService.obtenerPagosConDetallesRegistro();
    }
}
