package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.RegistroControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Registro;

@Component
public class RegistroModelAssembler implements RepresentationModelAssembler<Registro, EntityModel<Registro>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Registro> toModel(Registro registro){
        return EntityModel.of(registro,
                linkTo(methodOn(RegistroControllerV2.class).getRegistroById(registro.getId())).withSelfRel(),
                linkTo(methodOn(RegistroControllerV2.class).getAllRegistros()).withRel("registros"),
                linkTo(methodOn(RegistroControllerV2.class).updateRegistro(registro.getId(), registro)).withRel("actualizar"),
                linkTo(methodOn(RegistroControllerV2.class).patchRegistro(registro.getId(), registro)).withRel("actualizar-parcial"),
                linkTo(methodOn(RegistroControllerV2.class).deleteRegistro(registro.getId())).withRel("eliminar")
        );
    }
}
