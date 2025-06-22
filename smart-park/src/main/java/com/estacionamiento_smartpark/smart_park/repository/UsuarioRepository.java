package com.estacionamiento_smartpark.smart_park.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estacionamiento_smartpark.smart_park.model.Usuario;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByRun(String run);

    List<Usuario> findByNombreCompleto(String nombreCompleto);

    boolean existsByCorreo(String correo);

    boolean existsByRun(String run);

    Usuario findByCorreo(String correo);

}
