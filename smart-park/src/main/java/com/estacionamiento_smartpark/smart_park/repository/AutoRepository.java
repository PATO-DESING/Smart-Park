package com.estacionamiento_smartpark.smart_park.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Usuario;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {

    Optional<Auto> findByPatente(String patente);

    List<Auto> findByUsuarioId(Long usuarioId);

    @Query("SELECT u.run, u.nombreCompleto, a.patente " +
           "FROM Auto a " +
           "JOIN a.usuario u")
    List<Object[]> findDatosAutosYUsuarios();

    //para poder eliminar por cascada
    void deleteByUsuario(Usuario usuario);
    List<Auto> findByUsuario(Usuario usuario);


}
