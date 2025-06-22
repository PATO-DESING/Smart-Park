package com.estacionamiento_smartpark.smart_park.service;

import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;
import com.estacionamiento_smartpark.smart_park.model.Registro;
import com.estacionamiento_smartpark.smart_park.repository.AutoRepository;
import com.estacionamiento_smartpark.smart_park.repository.EstacionamientoRepository;
import com.estacionamiento_smartpark.smart_park.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private EstacionamientoRepository estacionamientoRepository;

    @Autowired
    private PagoService pagoService;

    public Registro save(Registro registro) {
        return registroRepository.save(registro);
    }

    public List<Registro> findAll() {
        return registroRepository.findAll();
    }

    public Registro findById(Long id) {
        return registroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro con ID " + id + " no encontrado"));
    }

    public void deleteById(Long id) {
        Registro r = registroRepository.findById(id).orElseThrow();
        pagoService.deleteByRegistro(r);
        registroRepository.deleteById(id);
    }

    public Registro patchRegistro(Long id, Registro parcialRegistro) {
        return registroRepository.findById(id).map(registroToUpdate -> {

            if (parcialRegistro.getHoraLlegada() != null) {
                registroToUpdate.setHoraLlegada(parcialRegistro.getHoraLlegada());
            }

            if (parcialRegistro.getHoraSalida() != null) {
                registroToUpdate.setHoraSalida(parcialRegistro.getHoraSalida());
            }

            if (parcialRegistro.getAuto() != null) {
                registroToUpdate.setAuto(parcialRegistro.getAuto());
            }

            return registroRepository.save(registroToUpdate);
        }).orElseThrow(() -> new RuntimeException("Registro con ID " + id + " no encontrado"));
    }


    public List<Registro> obtenerRegistrosPorPatente(String patente) {
        return registroRepository.findByAuto_Patente(patente);
    }

    public List<Registro> obtenerRegistrosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicial debe ser anterior a la de fin");
        }
        return registroRepository.findByHoraLlegadaBetween(inicio, fin);
    }

    public Estacionamiento registrarEntrada(String patente, int numeroEstacionamiento) {
        if (registroRepository.estaEstacionado(patente)) {
            throw new IllegalStateException("El auto con patente " + patente + " ya tiene un registro activo");
        }

        Auto auto = autoRepository.findByPatente(patente)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        Estacionamiento estacionamiento = estacionamientoRepository.findByNumero(numeroEstacionamiento)
                .orElseThrow(() -> new RuntimeException("Estacionamiento no existe"));

        if (estacionamiento.isOcupado()) {
            throw new IllegalStateException(numeroEstacionamiento + " ya está ocupado");
        }

        estacionamiento.setOcupado(true);
        estacionamiento.setAuto(auto);
        estacionamientoRepository.save(estacionamiento);

        Registro registro = new Registro();
        registro.setAuto(auto);
        registro.setHoraLlegada(LocalDateTime.now());
        registroRepository.save(registro);

        return estacionamiento;
    }

    public Estacionamiento registrarSalida(String patente) {
        Auto auto = autoRepository.findByPatente(patente)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        Registro registro = registroRepository.findByAutoAndHoraSalidaIsNull(auto)
                .orElseThrow(() -> new RuntimeException("No hay registro activo para este auto"));

        Estacionamiento estacionamiento = estacionamientoRepository.findByAutoPatente(patente)
                .orElseThrow(() -> new RuntimeException("No hay estacionamiento ocupado por un auto con patente " + patente));

        registro.setHoraSalida(LocalDateTime.now());
        registroRepository.save(registro);

        estacionamiento.setOcupado(false);
        estacionamiento.setAuto(null);
        estacionamientoRepository.save(estacionamiento);

        return estacionamiento;
    }

    public List<Registro> obtenerRegistrosActivos() {
        return registroRepository.findRegistrosActivos();
    }

    public List<Registro> obtenerRegistrosPorFecha(LocalDate fecha) {
        return registroRepository.findByFecha(fecha);
    }

    public List<Object[]> obtenerEntradasYSalidas() {
        return registroRepository.findEntradaysalida();
    }

    //para eliminar por cascada
    public void deleteByAuto(Auto auto) {
        List<Registro> registros = registroRepository.findByAuto(auto);
        for (Registro r : registros) {
            pagoService.deleteByRegistro(r);
        }
        registroRepository.deleteByAuto(auto);
    }

}
