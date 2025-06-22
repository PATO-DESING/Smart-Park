package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.ComunaControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Comuna;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<Comuna, EntityModel<Comuna>> {

    @Override
    public EntityModel<Comuna> toModel(Comuna comuna) {
        return EntityModel.of(comuna,
                linkTo(methodOn(ComunaControllerV2.class).getComunaById(comuna.getId())).withSelfRel(),
                linkTo(methodOn(ComunaControllerV2.class).getAllComunas()).withRel("comunas"),
                linkTo(methodOn(ComunaControllerV2.class).updateComuna(comuna.getId(), comuna)).withRel("actualizar"),
                linkTo(methodOn(ComunaControllerV2.class).patchComuna(comuna.getId(), comuna)).withRel("actualizarParcial"),
                linkTo(methodOn(ComunaControllerV2.class).deleteComuna(comuna.getId())).withRel("eliminar")
        );
    }
}
