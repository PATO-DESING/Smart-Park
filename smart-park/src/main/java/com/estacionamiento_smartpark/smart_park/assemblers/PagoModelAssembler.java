package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.PagoControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Pago;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Pago> toModel(Pago pago){
        return EntityModel.of(pago,
                linkTo(methodOn(PagoControllerV2.class).getPagoById(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).getAllPagos()).withRel("pagos"),
                linkTo(methodOn(PagoControllerV2.class).updatePago(pago.getId(), pago)).withRel("actualizar"),
                linkTo(methodOn(PagoControllerV2.class).patchPago(pago.getId(), pago)).withRel("actualizar-parcial"),
                linkTo(methodOn(PagoControllerV2.class).deletePago(pago.getId())).withRel("eliminar")
        );
    }

}
