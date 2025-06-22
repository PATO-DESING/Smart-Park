package com.estacionamiento_smartpark.smart_park.controller;

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

import com.estacionamiento_smartpark.smart_park.assemblers.AutoModelAssembler;
import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.service.AutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/autos")
@Tag(name = "Autos-controller-v-2", description = "Operaciones relacionadas con los autos") 
public class AutoControllerV2 {

    @Autowired
    private AutoService autoService;

    @Autowired
    private AutoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas los autos", description = "Obtiene una lista de todos los autos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los autos") 
    }) 
    public ResponseEntity<CollectionModel<EntityModel<Auto>>> getAllAutos() {
        List<EntityModel<Auto>> autos = autoService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (autos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                autos,
                linkTo(methodOn(AutoControllerV2.class).getAllAutos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener auto", description = "Obtiene auto por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no encontrado") 
    })      
    public ResponseEntity<EntityModel<Auto>> getAutoById(@PathVariable Long id) {
        Auto auto = autoService.findById(id);
        if (auto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(auto));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crea un nuevo auto", description = "Crea auto")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el auto") 
    })     
    public ResponseEntity<EntityModel<Auto>> createAuto(@RequestBody Auto auto) {
        Auto newAuto = autoService.save(auto);
        return ResponseEntity
                .created(linkTo(methodOn(AutoControllerV2.class).getAutoById(newAuto.getId())).toUri())
                .body(assembler.toModel(newAuto));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un auto", description = "Actualiza todos los datos del auto")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no actualizado") 
    })     
    public ResponseEntity<EntityModel<Auto>> updateAuto(@PathVariable Long id, @RequestBody Auto auto) {
        auto.setId(id);
        Auto updatedAuto = autoService.save(auto);
        return ResponseEntity.ok(assembler.toModel(updatedAuto));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcial auto", description = "Actualiza ciertos datos de un auto")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no actualizado") 
    })   
    public ResponseEntity<EntityModel<Auto>> patchAuto(@PathVariable Long id, @RequestBody Auto auto) {
        Auto updatedAuto = autoService.patchAuto(id, auto);
        if (updatedAuto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updatedAuto));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un auto", description = "Elimina auto por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Auto no encontrado") 
    })    
    public ResponseEntity<Void> deleteAuto(@PathVariable Long id) {
        Auto auto = autoService.findById(id);
        if (auto == null) {
            return ResponseEntity.notFound().build();
        }
        autoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
