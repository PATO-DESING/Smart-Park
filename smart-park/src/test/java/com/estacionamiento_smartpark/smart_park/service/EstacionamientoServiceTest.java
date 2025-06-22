package com.estacionamiento_smartpark.smart_park.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;
import com.estacionamiento_smartpark.smart_park.model.Sucursal;
import com.estacionamiento_smartpark.smart_park.repository.EstacionamientoRepository;

@SpringBootTest
public class EstacionamientoServiceTest {

    @Autowired
    private EstacionamientoService estacionamientoService;

    @MockBean
    private EstacionamientoRepository estacionamientoRepository;

    @MockBean
    private AutoService autoService;

    private Estacionamiento createEstacionamieno(){
        return new Estacionamiento (
            1L,
            123,
            false,
            new Auto(),
            new Sucursal()
        );
    }

    @Test
    public void testFindAll() {
        when(estacionamientoRepository.findAll()).thenReturn(List.of(createEstacionamieno()));
        List<Estacionamiento> estacionamiento = estacionamientoService.findAll();
        assertNotNull(estacionamiento);
        assertEquals(1, estacionamiento.size());
    }

    @Test
    public void testFindById() {
        when(estacionamientoRepository.findById(1L)).thenReturn(java.util.Optional.of(createEstacionamieno()));
        Estacionamiento estacionamiento = estacionamientoService.findById(1L);
        assertNotNull(estacionamiento);
        assertEquals(123, estacionamiento.getNumero());
    }

    @Test
    public void testSave() {
        Estacionamiento estacionamiento = createEstacionamieno();
        when(estacionamientoRepository.save(estacionamiento)).thenReturn(estacionamiento);
        Estacionamiento savedEstacionamiento = estacionamientoService.save(estacionamiento);
        assertNotNull(savedEstacionamiento);
        assertEquals(123, savedEstacionamiento.getNumero());
    }

    @Test
    public void testPatchEstacionamiento() {
        Estacionamiento existingEstacionamiento = createEstacionamieno();
        Estacionamiento patchData = new Estacionamiento();
        patchData.setNumero(111);

        when(estacionamientoRepository.findById(1L)).thenReturn(java.util.Optional.of(existingEstacionamiento));
        when(estacionamientoRepository.save(any(Estacionamiento.class))).thenReturn(existingEstacionamiento);

        Estacionamiento patchedEstacionamiento = estacionamientoService.patchEstacionamiento(1L, patchData);
        assertNotNull(patchedEstacionamiento);
        assertEquals(111, patchedEstacionamiento.getNumero());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(estacionamientoRepository).deleteById(1L);
        estacionamientoService.deleteById(1L);
        verify(estacionamientoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testObtenerEstacionamientosConAutosYUsuarios() {
        List<Object[]> lista = estacionamientoService.obtenerEstacionamientosConAutosYUsuarios();
        assertNotNull(lista);
    }

    @Test
    public void testFindByNumero() {
        Estacionamiento estacionamiento = createEstacionamieno();
        when(estacionamientoRepository.findByNumero(123)).thenReturn(java.util.Optional.of(estacionamiento));

        Estacionamiento resultado = estacionamientoService.findByNumero(123).orElse(null);

        assertNotNull(resultado);
        assertEquals(123, resultado.getNumero());
    }

    @Test
    public void testFindOcupados() {
        Estacionamiento ocupado = createEstacionamieno();
        ocupado.setOcupado(true);

        when(estacionamientoRepository.findByOcupadoTrue()).thenReturn(List.of(ocupado));

        List<Estacionamiento> resultado = estacionamientoService.findOcupados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(true, resultado.get(0).isOcupado());
    }

    @Test
    public void testActualizarEstacionamiento() {
        Estacionamiento existente = createEstacionamieno();
        Estacionamiento actualizado = createEstacionamieno();
        actualizado.setNumero(999);
        actualizado.setOcupado(true);

        when(estacionamientoRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
        when(estacionamientoRepository.save(any(Estacionamiento.class))).thenReturn(actualizado);

        Estacionamiento resultado = estacionamientoService.actualizarEstacionamiento(1L, actualizado);

        assertNotNull(resultado);
        assertEquals(999, resultado.getNumero());
        assertEquals(true, resultado.isOcupado());
    }

    @Test
    public void testFindLibres() {
        Estacionamiento libre = createEstacionamieno();
        libre.setOcupado(false);

        when(estacionamientoRepository.findByOcupadoFalse()).thenReturn(List.of(libre));

        List<Estacionamiento> resultado = estacionamientoService.findLibres();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isOcupado());
    }

    @Test
    public void testFindBySucursalId() {
        Estacionamiento estacionamiento = createEstacionamieno();
        when(estacionamientoRepository.findBySucursalId(1L)).thenReturn(List.of(estacionamiento));

        List<Estacionamiento> resultado = estacionamientoService.findBySucursalId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testFindLibresBySucursalId() {
        Estacionamiento libre = createEstacionamieno();
        libre.setOcupado(false);

        when(estacionamientoRepository.findBySucursalIdAndOcupadoFalse(1L)).thenReturn(List.of(libre));

        List<Estacionamiento> resultado = estacionamientoService.findLibresBySucursalId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isOcupado());
    }


    @Test
    public void testFindByAutoPatente() {
        Estacionamiento estacionamiento = createEstacionamieno();
        when(estacionamientoRepository.findByAutoPatente("ABC123")).thenReturn(java.util.Optional.of(estacionamiento));

        Estacionamiento resultado = estacionamientoService.findByAutoPatente("ABC123").orElse(null);

        assertNotNull(resultado);
        assertEquals(123, resultado.getNumero());
    }

    @Test
    public void testDeleteByAuto() {
        Auto auto = new Auto();

        doNothing().when(estacionamientoRepository).deleteByAuto(auto);

        estacionamientoService.deleteByAuto(auto);

        verify(estacionamientoRepository, times(1)).deleteByAuto(auto);
    }

    @Test
    public void testDeleteBySucursal() {
        Sucursal sucursal = new Sucursal();
        Auto auto = new Auto();
        auto.setId(5L);

        Estacionamiento conAuto = createEstacionamieno();
        conAuto.setAuto(auto);

        when(estacionamientoRepository.findBySucursal(sucursal)).thenReturn(List.of(conAuto));
        doNothing().when(autoService).delete(5L);
        doNothing().when(estacionamientoRepository).deleteBySucursal(sucursal);

        estacionamientoService.deleteBySucursal(sucursal);

        verify(estacionamientoRepository, times(1)).findBySucursal(sucursal);
        verify(autoService, times(1)).delete(5L);
        verify(estacionamientoRepository, times(1)).deleteBySucursal(sucursal);
    }

}

