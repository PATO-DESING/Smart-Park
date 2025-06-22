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

import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.model.Sucursal;
import com.estacionamiento_smartpark.smart_park.repository.SucursalRepository;

@SpringBootTest
public class SucursalServiceTest {
    @Autowired
    private SucursalService sucursalService;

    @MockBean
    private SucursalRepository sucursalRepository;

    @MockBean
    private EstacionamientoService estacionamientoService;

    private Sucursal createSucursal() {
        return new Sucursal(
            1L, 
            "Sucursal 1", 
            "Av Santiago 1",
            912345678,
            new Comuna()
        );
    }

    @Test
    public void testFindAll() {
        when(sucursalRepository.findAll()).thenReturn(List.of(createSucursal()));
        List<Sucursal> sucursales = sucursalService.findAll();
        assertNotNull(sucursales);
        assertEquals(1, sucursales.size());
    }

    @Test
    public void testFindById() {
        when(sucursalRepository.findById(1L)).thenReturn(java.util.Optional.of(createSucursal()));
        Sucursal sucursal = sucursalService.findById(1L);
        assertNotNull(sucursal);
        assertEquals("Sucursal 1", sucursal.getNombre());
    }

    @Test
    public void testSave() {
        Sucursal sucursal = createSucursal();
        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);
        Sucursal savedSucursal = sucursalService.save(sucursal);
        assertNotNull(savedSucursal);
        assertEquals("Sucursal 1", savedSucursal.getNombre());
    }

    @Test
    public void testPatchSucursal() {
        Sucursal existingSucursal = createSucursal();
        Sucursal patchData = new Sucursal();
        patchData.setNombre("Sucursal 1");

        when(sucursalRepository.findById(1L)).thenReturn(java.util.Optional.of(existingSucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(existingSucursal);

        Sucursal patchedSucursal = sucursalService.patchSucursal(1L, patchData);
        assertNotNull(patchedSucursal);
        assertEquals("Sucursal 1", patchedSucursal.getNombre());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(sucursalRepository).deleteById(1L);
        sucursalService.delete(1L);
        verify(sucursalRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByNombreAndComunaId() {
        List<Sucursal> sucursales = sucursalService.findByNombreAndComunaId("Sucursal X", 1L);
        assertNotNull(sucursales);
    }

    @Test
    public void testActualizarSucursal() {
        Sucursal original = createSucursal();
        Sucursal actualizada = new Sucursal();
        actualizada.setNombre("Sucursal Actualizada");
        actualizada.setDireccion("Nueva Dirección");
        actualizada.setTelefono(123456789);
        actualizada.setComuna(new Comuna());

        when(sucursalRepository.findById(1L)).thenReturn(java.util.Optional.of(original));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(actualizada);

        Sucursal resultado = sucursalService.actualizarSucursal(1L, actualizada);

        assertNotNull(resultado);
        assertEquals("Sucursal Actualizada", resultado.getNombre());
    }

    @Test
    public void testFindByDireccion() {
        when(sucursalRepository.findByDireccion("Av Santiago 1")).thenReturn(List.of(createSucursal()));

        List<Sucursal> resultado = sucursalService.findByDireccion("Av Santiago 1");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testFindByNombre() {
        when(sucursalRepository.findByNombre("Sucursal 1")).thenReturn(List.of(createSucursal()));

        List<Sucursal> resultado = sucursalService.findByNombre("Sucursal 1");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testFindByComunaId() {
        when(sucursalRepository.findByComunaId(1L)).thenReturn(List.of(createSucursal()));

        List<Sucursal> resultado = sucursalService.findByComunaId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testDeleteByComuna() {
        Comuna comuna = new Comuna();
        comuna.setId(1L);
        Sucursal sucursal = createSucursal();
        sucursal.setComuna(comuna);

        when(sucursalRepository.findByComuna(comuna)).thenReturn(sucursal);
        doNothing().when(estacionamientoService).deleteBySucursal(sucursal);
        doNothing().when(sucursalRepository).deleteByComuna(comuna);

        sucursalService.deleteByComuna(comuna);

        verify(estacionamientoService, times(1)).deleteBySucursal(sucursal);
        verify(sucursalRepository, times(1)).deleteByComuna(comuna);
    }

}

