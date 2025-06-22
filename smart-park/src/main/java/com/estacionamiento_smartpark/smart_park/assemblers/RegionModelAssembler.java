package com.estacionamiento_smartpark.smart_park.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.controller.RegionControllerV2;
import com.estacionamiento_smartpark.smart_park.model.Region;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<Region, EntityModel<Region>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Region> toModel(Region region){
        return EntityModel.of(region,
                linkTo(methodOn(RegionControllerV2.class).getRegionById(region.getId())).withSelfRel(),
                linkTo(methodOn(RegionControllerV2.class).getAllRegiones()).withRel("regiones"),
                linkTo(methodOn(RegionControllerV2.class).updateRegion(region.getId(), region)).withRel("actualizar"),
                linkTo(methodOn(RegionControllerV2.class).patchRegion(region.getId(), region)).withRel("actualizar-parcial"),
                linkTo(methodOn(RegionControllerV2.class).deleteRegion(region.getId())).withRel("eliminar")
        );
    }
}
