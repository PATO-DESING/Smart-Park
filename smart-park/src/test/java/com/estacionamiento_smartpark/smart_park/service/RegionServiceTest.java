package com.estacionamiento_smartpark.smart_park.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.estacionamiento_smartpark.smart_park.model.Region;
import com.estacionamiento_smartpark.smart_park.repository.RegionRepository;

@SpringBootTest
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private ComunaService comunaService;


    private Region createRegion() {
        return new Region(
            1L, 
            12345, 
            "Metropolitana"
        );
    }

    @Test
    public void testFindAll() {
        when(regionRepository.findAll()).thenReturn(List.of(createRegion()));
        List<Region> regiones = regionService.findAll();
        assertNotNull(regiones);
        assertEquals(1, regiones.size());
    }

    @Test
    public void testFindById() {
        when(regionRepository.findById(1L)).thenReturn(java.util.Optional.of(createRegion()));
        Region region = regionService.findById(1L);
        assertNotNull(region);
        assertEquals("Metropolitana", region.getNombre());
    }

    @Test
    public void testSave() {
        Region region = createRegion();
        when(regionRepository.save(region)).thenReturn(region);
        Region savedRegion = regionService.save(region);
        assertNotNull(savedRegion);
        assertEquals("Metropolitana", savedRegion.getNombre());
    }

    @Test
    public void testPatchRegion() {
        Region existingRegion = createRegion();
        Region patchData = new Region();
        patchData.setNombre("Metropolitana Actualizado");

        when(regionRepository.findById(1L)).thenReturn(java.util.Optional.of(existingRegion));
        when(regionRepository.save(any(Region.class))).thenReturn(existingRegion);

        Region patchedRegion = regionService.patchRegion(1L, patchData);
        assertNotNull(patchedRegion);
        assertEquals("Metropolitana Actualizado", patchedRegion.getNombre());
    }

    @Test
    public void testDelete() {
        Region region = createRegion();

        when(regionRepository.findById(1L)).thenReturn(java.util.Optional.of(region));
        doNothing().when(comunaService).deleteByRegion(region);
        doNothing().when(regionRepository).deleteById(1L);

        regionService.delete(1L);

        verify(regionRepository, times(1)).findById(1L);
        verify(comunaService, times(1)).deleteByRegion(region);
        verify(regionRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testUpdateRegion() {
        Region existingRegion = createRegion();
        Region updatedData = new Region();
        updatedData.setNombre("Actualizado");
        updatedData.setCodigo(9999);

        when(regionRepository.findById(1L)).thenReturn(java.util.Optional.of(existingRegion));
        when(regionRepository.save(any(Region.class))).thenReturn(existingRegion);

        Region result = regionService.updateRegion(1L, updatedData);

        assertNotNull(result);
        assertEquals("Actualizado", result.getNombre());
        assertEquals(9999, result.getCodigo());
    }

    @Test
    public void testFindByNombre() {
        Region region = createRegion();
        when(regionRepository.findByNombre("Metropolitana")).thenReturn(region);

        Region result = regionService.findByNombre("Metropolitana");

        assertNotNull(result);
        assertEquals("Metropolitana", result.getNombre());
    }

    

}

