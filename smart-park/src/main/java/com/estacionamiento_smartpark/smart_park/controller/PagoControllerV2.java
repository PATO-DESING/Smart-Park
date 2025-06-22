package com.estacionamiento_smartpark.smart_park.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
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

import com.estacionamiento_smartpark.smart_park.assemblers.PagoModelAssembler;
import com.estacionamiento_smartpark.smart_park.model.Pago;
import com.estacionamiento_smartpark.smart_park.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/pagos")
@Tag(name = "Pagos-controller-v-2", description = "Operaciones relacionadas con los pagos") 
public class PagoControllerV2 {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los pagos", description = "Obtiene una lista de todos los pagos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los pagos") 
    }) 
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> getAllPagos() {
        List<EntityModel<Pago>> pagos = pagoService.obtenerTodosLosPagos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                pagos,
                linkTo(methodOn(PagoControllerV2.class).getAllPagos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener pago", description = "Obtiene pago por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no encontrado") 
    })  
    public ResponseEntity<EntityModel<Pago>> getPagoById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(pago));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crea un nuevo pago", description = "Crea pago")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el pago") 
    }) 
    public ResponseEntity<EntityModel<Pago>> createPago(@RequestBody Pago pago) {
        Pago newPago = pagoService.actualizarPago(pago);
        return ResponseEntity
                .created(linkTo(methodOn(PagoControllerV2.class).getPagoById(newPago.getId())).toUri())
                .body(assembler.toModel(newPago));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un pago", description = "Actualiza todos los datos del pago")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no actualizado") 
    }) 
    public ResponseEntity<EntityModel<Pago>> updatePago(@PathVariable Long id, @RequestBody Pago pago) {
        pago.setId(id);
        Pago updatedPago = pagoService.actualizarPago(pago);
        return ResponseEntity.ok(assembler.toModel(updatedPago));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcial un pago", description = "Actualiza algunos datos del pago")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no actualizado") 
    }) 
    public ResponseEntity<EntityModel<Pago>> patchPago(@PathVariable Long id, @RequestBody Pago pago) {
        Pago updatedPago = pagoService.patchPago(id, pago);
        if (updatedPago == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updatedPago));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un pago", description = "Elimina pago por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Pago no encontrado") 
    })
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

}
