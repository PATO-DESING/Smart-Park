package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.AutoControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Auto;

@Component
public class AutoModelAssembler implements RepresentationModelAssembler<Auto, EntityModel<Auto>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Auto> toModel(Auto auto){
        return EntityModel.of(auto,
                linkTo(methodOn(AutoControllerV2.class).getAutoById(auto.getId())).withSelfRel(),
                linkTo(methodOn(AutoControllerV2.class).getAllAutos()).withRel("autos"),
                linkTo(methodOn(AutoControllerV2.class).updateAuto(auto.getId(), auto)).withRel("actualizar"),
                linkTo(methodOn(AutoControllerV2.class).patchAuto(auto.getId(), auto)).withRel("actualizar-parcial"),
                linkTo(methodOn(AutoControllerV2.class).deleteAuto(auto.getId())).withRel("eliminar")
        );
    }

}
