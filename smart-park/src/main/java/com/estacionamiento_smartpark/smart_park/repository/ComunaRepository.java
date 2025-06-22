package com.estacionamiento_smartpark.smart_park.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.model.Region;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    Comuna findByCodigo(Long codigo);

    List<Comuna> findByNombreAndRegionId(String nombre, Long regionId);

    //para poder eliminar por cascada
    void deleteByRegion(Region region);
    Comuna findByRegion(Region region);
    


}
