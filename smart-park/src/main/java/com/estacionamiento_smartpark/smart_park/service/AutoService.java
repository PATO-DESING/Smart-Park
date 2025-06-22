package com.estacionamiento_smartpark.smart_park.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Usuario;
import com.estacionamiento_smartpark.smart_park.repository.AutoRepository;
import com.estacionamiento_smartpark.smart_park.repository.EstacionamientoRepository;

import jakarta.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class AutoService {

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private RegistroService registroService;

    @Autowired
    private EstacionamientoRepository estacionamientoRepository;

    public List<Auto> findAll(){
        return autoRepository.findAll();        
    }       

    public Auto findById(Long id) {
        return autoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(id + "no encontrado"));
    }
    
    public Auto save(Auto auto) {
        return autoRepository.save(auto);
    }

    public void delete(Long id) {
        Auto auto = autoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auto con ID " + id + " no encontrado"));
        registroService.deleteByAuto(auto);
        estacionamientoRepository.deleteByAuto(auto);
        autoRepository.deleteById(id);
    }


    public Auto actualizarAuto(Long id, Auto autoActualizado) {
        Auto autoExistente = autoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(id +" no encontrado"));
        autoExistente.setPatente(autoActualizado.getPatente());
        autoExistente.setUsuario(autoActualizado.getUsuario());
    
        return autoRepository.save(autoExistente);
    }

    public Auto patchAuto(Long id, Auto parcialAuto){
        Auto autoToUpdate = autoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(id + " no encontrado"));
        if (parcialAuto.getPatente() != null) {
            autoToUpdate.setPatente(parcialAuto.getPatente()); 
        }

        if (parcialAuto.getUsuario() != null) {
            autoToUpdate.setUsuario(parcialAuto.getUsuario());
        }
        return autoRepository.save(autoToUpdate);
        }

        public List<Auto> obtenerPorUsuarioId(Long usuarioId) {
            return autoRepository.findByUsuarioId(usuarioId);
    }

    public Auto updateAuto(Long id, Auto auto){
        Auto autoToUpdate = autoRepository.findById(id).orElse(null);
        if (autoToUpdate != null) {
            autoToUpdate.setPatente(auto.getPatente());
            autoToUpdate.setUsuario(auto.getUsuario());
            return autoRepository.save(autoToUpdate);
        } else {
            return null;
        }
    }

    public List<Object[]> obtenerDatosAutosYUsuarios() {
        return autoRepository.findDatosAutosYUsuarios();
    }

    public void deleteByUsuario(Usuario usuario) {
        autoRepository.deleteByUsuario(usuario);
    }


}
