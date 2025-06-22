package com.estacionamiento_smartpark.smart_park.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Registro;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Registro r WHERE r.auto.patente = :patente AND r.horaSalida IS NULL")
    boolean estaEstacionado(@Param("patente") String patente);

    List<Registro> findByAuto_Patente(String patente);

    List<Registro> findByHoraLlegadaBetween(LocalDateTime inicio, LocalDateTime fin);

    Optional<Registro> findByAutoAndHoraSalidaIsNull(Auto auto);

    @Query("SELECT r FROM Registro r WHERE r.horaSalida IS NULL")
    List<Registro> findRegistrosActivos();

    @Query("SELECT r FROM Registro r WHERE DATE(r.horaLlegada) = :fecha")
    List<Registro> findByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT r.id, a.patente, r.horaLlegada, r.horaSalida FROM Registro r JOIN r.auto a")
    List<Object[]> findEntradaysalida();

    //para poder eliminar por cascada
    void deleteByAuto(Auto auto);
    List<Registro> findByAuto(Auto auto);
    


}
