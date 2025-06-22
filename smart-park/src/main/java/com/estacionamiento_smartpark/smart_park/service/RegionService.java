package com.estacionamiento_smartpark.smart_park.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.estacionamiento_smartpark.smart_park.repository.RegionRepository;
import com.estacionamiento_smartpark.smart_park.model.Region;
import java.util.List;


@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaService comunaService;

    public List<Region> findAll(){
        return regionRepository.findAll();
    }

    public Region save(Region region){
        return regionRepository.save(region);
    }

    public void delete(Long id) {
        Region region = regionRepository.findById(id).orElseThrow();
        comunaService.deleteByRegion(region);
        regionRepository.deleteById(id);
    }

    public Region updateRegion(Long id, Region region){
        Region regionToUpdate = regionRepository.findById(id).orElse(null);
        if (regionToUpdate != null) {
            regionToUpdate.setNombre(region.getNombre());
            regionToUpdate.setCodigo(region.getCodigo());
            return regionRepository.save(regionToUpdate);
        } else {
            return null;
        }
    }

    public Region patchRegion(Long id, Region parcialRegion){
        Region regionToUpdate =regionRepository.findById(id).orElse(null);
        if (regionToUpdate != null) {
            if (parcialRegion.getNombre() != null) {
                regionToUpdate.setNombre(parcialRegion.getNombre());
            }
            if (parcialRegion.getCodigo() != 0) {
                regionToUpdate.setCodigo(parcialRegion.getCodigo());
            }
            return regionRepository.save(regionToUpdate);
        } else {
            return null;
        }
    }

    public Region findById(Long id) {
        return regionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(id + "no encontrado"));
    }

    public Region findByNombre(String nombre) {
        return regionRepository.findByNombre(nombre);
    }

}
