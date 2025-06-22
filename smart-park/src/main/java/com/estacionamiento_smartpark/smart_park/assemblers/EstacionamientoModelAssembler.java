package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.EstacionamientoControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;

@Component
public class EstacionamientoModelAssembler implements RepresentationModelAssembler<Estacionamiento, EntityModel<Estacionamiento>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Estacionamiento> toModel(Estacionamiento estacionamiento){
        return EntityModel.of(estacionamiento,
                linkTo(methodOn(EstacionamientoControllerV2.class).getEstacionamientoById(estacionamiento.getId())).withSelfRel(),
                linkTo(methodOn(EstacionamientoControllerV2.class).getAllEstacionamientos()).withRel("estacionamientos"),
                linkTo(methodOn(EstacionamientoControllerV2.class).updateEstacionamiento(estacionamiento.getId(), estacionamiento)).withRel("actualizar"),
                linkTo(methodOn(EstacionamientoControllerV2.class).patchEstacionamiento(estacionamiento.getId(), estacionamiento)).withRel("actualizar-parcial"),
                linkTo(methodOn(EstacionamientoControllerV2.class).deleteEstacionamiento(estacionamiento.getId())).withRel("eliminar")
        );
    }
}
