package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.SucursalControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Sucursal;

@Component
public class SucursalModelAssembler implements RepresentationModelAssembler<Sucursal, EntityModel<Sucursal>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Sucursal> toModel(Sucursal sucursal){
        return EntityModel.of(sucursal,
                linkTo(methodOn(SucursalControllerV2.class).getSucursalById(sucursal.getId())).withSelfRel(),
                linkTo(methodOn(SucursalControllerV2.class).getAllSucursales()).withRel("sucursales"),
                linkTo(methodOn(SucursalControllerV2.class).updateSucursal(sucursal.getId(), sucursal)).withRel("actualizar"),
                linkTo(methodOn(SucursalControllerV2.class).patchSucursal(sucursal.getId(), sucursal)).withRel("actualizar-parcial"),
                linkTo(methodOn(SucursalControllerV2.class).deleteSucursal(sucursal.getId())).withRel("eliminar")
        );
    }
}
