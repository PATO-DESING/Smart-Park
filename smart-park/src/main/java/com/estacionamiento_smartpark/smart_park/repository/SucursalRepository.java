package com.estacionamiento_smartpark.smart_park.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.model.Sucursal;
import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByDireccion(String direccio);

    List<Sucursal> findByNombre(String nombre);

    List<Sucursal> findByComunaId(Long comunaId);

    List<Sucursal> findByNombreAndComunaId(String nombre, Long comunaId);

    //para poder eliminar por cascada
    Sucursal findByComuna(Comuna comuna);
    void deleteByComuna(Comuna comuna);

}
