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

import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Usuario;
import com.estacionamiento_smartpark.smart_park.repository.AutoRepository;



@SpringBootTest
public class AutoServiceTest {
    @Autowired
    private AutoService autoService;

    @MockBean
    private AutoRepository autoRepository;

    private Auto createAuto() {
        return new Auto(
            1L, 
            "ABCD12", 
            new Usuario()
        );
    }

    @Test
    public void testFindAll() {
        when(autoRepository.findAll()).thenReturn(List.of(createAuto()));
        List<Auto> autos = autoService.findAll();
        assertNotNull(autos);
        assertEquals(1, autos.size());
    }

    @Test
    public void testFindById() {
        when(autoRepository.findById(1L)).thenReturn(java.util.Optional.of(createAuto()));
        Auto auto = autoService.findById(1L);
        assertNotNull(auto);
        assertEquals("ABCD12", auto.getPatente());
    }

    @Test
    public void testSave() {
        Auto auto = createAuto();
        when(autoRepository.save(auto)).thenReturn(auto);
        Auto savedAuto = autoService.save(auto);
        assertNotNull(savedAuto);
        assertEquals("ABCD12", savedAuto.getPatente());
    }

    @Test
    public void testPatchAuto() {
        Auto existingAuto = createAuto();
        Auto patchData = new Auto();
        patchData.setPatente("ABCD12 Actualizado");

        when(autoRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAuto));
        when(autoRepository.save(any(Auto.class))).thenReturn(existingAuto);

        Auto patchedAuto = autoService.patchAuto(1L, patchData);
        assertNotNull(patchedAuto);
        assertEquals("ABCD12 Actualizado", patchedAuto.getPatente());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(autoRepository).deleteById(1L);
        autoService.delete(1L);
        verify(autoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerDatosAutosYUsuarios() {
        List<Object[]> resultados = autoService.obtenerDatosAutosYUsuarios();
        assertNotNull(resultados);
    }

    @Test
    public void testDeleteByUsuario(Usuario usuario) {
        doNothing().when(autoRepository).deleteByUsuario(usuario);
        autoService.deleteByUsuario(usuario);
        verify(autoRepository,times(1)).deleteByUsuario(usuario);
    }

}

