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

import com.estacionamiento_smartpark.smart_park.repository.ComunaRepository;
import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.model.Region;

@SpringBootTest
public class ComunaServiceTest {

    @Autowired
    private ComunaService comunaService;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private SucursalService sucursalService;

    private Comuna createComuna() {
        return new Comuna(
            1L, 
            1234L, 
            "Santiago",
            new Region()
        );
    }

    @Test
    public void testFindAll() {
        when(comunaRepository.findAll()).thenReturn(List.of(createComuna()));
        List<Comuna> comunas = comunaService.findAll();
        assertNotNull(comunas);
        assertEquals(1, comunas.size());
    }

    @Test
    public void testFindById() {
        when(comunaRepository.findById(1L)).thenReturn(java.util.Optional.of(createComuna()));
        Comuna comuna = comunaService.findById(1L);
        assertNotNull(comuna);
        assertEquals("Santiago", comuna.getNombre());
    }

    @Test
    public void testSave() {
        Comuna comuna = createComuna();
        when(comunaRepository.save(comuna)).thenReturn(comuna);
        Comuna savedComuna = comunaService.save(comuna);
        assertNotNull(savedComuna);
        assertEquals("Santiago", savedComuna.getNombre());
    }

    @Test
    public void testPatchComuna() {
        Comuna existingComuna = createComuna();
        Comuna patchData = new Comuna();
        patchData.setNombre("Santiago Actualizado");

        when(comunaRepository.findById(1L)).thenReturn(java.util.Optional.of(existingComuna));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existingComuna);

        Comuna patchedComuna = comunaService.patchComuna(1L, patchData);
        assertNotNull(patchedComuna);
        assertEquals("Santiago Actualizado", patchedComuna.getNombre());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(comunaRepository).deleteById(1L);
        comunaService.delete(1L);
        verify(comunaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByCodigo(){
        when(comunaRepository.findByCodigo(1234L)).thenReturn((Comuna) List.of(createComuna()));
        Comuna comuna = comunaService.findByCodigo(1234L);
        assertNotNull(comuna);
        assertEquals(1324, comuna.getCodigo());  
    }

    @Test
    public void testFindByNombreAndRegionId() {
        Comuna comuna = createComuna();
        comuna.getRegion().setId(1L);

        when(comunaRepository.findByNombreAndRegionId("Santiago", 1L)).thenReturn(List.of(comuna));

        List<Comuna> resultado = comunaService.findByNombreAndRegionId("Santiago", 1L);
        
        Comuna comunaResultado = resultado.get(0);

        assertNotNull(comunaResultado);
        assertEquals("Santiago", comunaResultado.getNombre());
        assertEquals(1L, comunaResultado.getRegion().getId());
    }

    @Test
    public void testDeleteByRegion() {
        Region region = new Region();
        region.setId(1L);

        Comuna comuna = createComuna();
        comuna.setRegion(region);

        when(comunaRepository.findByRegion(region)).thenReturn(comuna);
        doNothing().when(sucursalService).deleteByComuna(comuna);
        doNothing().when(comunaRepository).deleteByRegion(region);

        comunaService.deleteByRegion(region);

        verify(comunaRepository, times(1)).findByRegion(region);
        verify(sucursalService, times(1)).deleteByComuna(comuna);
        verify(comunaRepository, times(1)).deleteByRegion(region);
    }

}

