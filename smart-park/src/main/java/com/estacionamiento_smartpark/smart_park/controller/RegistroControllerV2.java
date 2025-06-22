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

import com.estacionamiento_smartpark.smart_park.assemblers.RegistroModelAssembler;
import com.estacionamiento_smartpark.smart_park.model.Registro;
import com.estacionamiento_smartpark.smart_park.service.RegistroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/registros")
@Tag(name = "Registros-controller-v-2", description = "Operaciones relacionadas con los registros") 

public class RegistroControllerV2 {

    @Autowired
    private RegistroService registroService;

    @Autowired
    private RegistroModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los registros", description = "Obtiene una lista de todos los registros")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se puede listar los registros") 
    }) 
    public ResponseEntity<CollectionModel<EntityModel<Registro>>> getAllRegistros() {
        List<EntityModel<Registro>> registros = registroService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (registros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                registros,
                linkTo(methodOn(RegistroControllerV2.class).getAllRegistros()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener registro", description = "Obtiene registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })  
    public ResponseEntity<EntityModel<Registro>> getRegistroById(@PathVariable Long id) {
        Registro registro = registroService.findById(id);
        if (registro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(registro));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crea un nuevo registro", description = "Crea registro")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "No se pudo crear el registro") 
    }) 
    public ResponseEntity<EntityModel<Registro>> createRegistro(@RequestBody Registro registro) {
        Registro newRegistro = registroService.save(registro);
        return ResponseEntity
                .created(linkTo(methodOn(RegistroControllerV2.class).getRegistroById(newRegistro.getId())).toUri())
                .body(assembler.toModel(newRegistro));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar registro", description = "Actualizar registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })
    public ResponseEntity<EntityModel<Registro>> updateRegistro(@PathVariable Long id, @RequestBody Registro registro) {
        registro.setId(id);
        Registro updatedRegistro = registroService.save(registro);
        return ResponseEntity.ok(assembler.toModel(updatedRegistro));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcial", description = "Actualizar registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })
    public ResponseEntity<EntityModel<Registro>> patchRegistro(@PathVariable Long id, @RequestBody Registro registro) {
        Registro updatedRegistro = registroService.patchRegistro(id, registro);
        if (updatedRegistro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updatedRegistro));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un registro", description = "Elimina registro por id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
        @ApiResponse(responseCode = "404", description = "Registro no encontrado") 
    })
    public ResponseEntity<Void> deleteRegistro(@PathVariable Long id) {
        Registro registro = registroService.findById(id);
        if (registro == null) {
            return ResponseEntity.notFound().build();
        }
        registroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
