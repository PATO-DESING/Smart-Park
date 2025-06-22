package com.estacionamiento_smartpark.smart_park.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.model.Region;
import com.estacionamiento_smartpark.smart_park.repository.ComunaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {
    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private SucursalService sucursalService;

    public List<Comuna> findAll(){
        return comunaRepository.findAll();        
    }       

    public Comuna findById(long id) {
        return comunaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(id + "no encontrado"));
    }
    
    public Comuna save(Comuna comuna){
        return comunaRepository.save(comuna);
    }

    public void delete(Long id) {
        Comuna comuna = comunaRepository.findById(id).orElseThrow();
        sucursalService.deleteByComuna(comuna);
        comunaRepository.deleteById(id);
    }

    public Comuna updateComuna(Long id, Comuna comunaActualizada) {
        Comuna comunaExistente = comunaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(id +" no encontrado"));
        comunaExistente.setCodigo(comunaActualizada.getCodigo());
        comunaExistente.setNombre(comunaActualizada.getNombre());
        comunaExistente.setRegion(comunaActualizada.getRegion());
    
        return comunaRepository.save(comunaExistente);
    }

    public Comuna patchComuna(Long id, Comuna parcialComuna){
        Optional<Comuna> comunaOptional = comunaRepository.findById(id);
        if (comunaOptional.isPresent()) {            
            Comuna comunaToUpdate = comunaOptional.get();        
            if (parcialComuna.getCodigo() != 0) {
                comunaToUpdate.setCodigo(parcialComuna.getCodigo());   
            }
            if (parcialComuna.getNombre() != null) {
                comunaToUpdate.setNombre(parcialComuna.getNombre()); 
            }
            if (parcialComuna.getRegion() != null) {
                comunaToUpdate.setRegion(parcialComuna.getRegion()); 
            }
            return comunaRepository.save(comunaToUpdate);
        } else {
            return null;
        }
    }

    public Comuna findByCodigo(Long codigo) {
        return comunaRepository.findByCodigo(codigo);
    }

    public List<Comuna> findByNombreAndRegionId(String nombre, Long regionId){
        return comunaRepository.findByNombreAndRegionId(nombre, regionId);
    }

    public void deleteByRegion(Region region) {
        Comuna comuna = comunaRepository.findByRegion(region);
        sucursalService.deleteByComuna(comuna);
        comunaRepository.deleteByRegion(region);
    }

}
