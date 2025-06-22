package com.estacionamiento_smartpark.smart_park.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.estacionamiento_smartpark.smart_park.model.Registro;
import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;
import com.estacionamiento_smartpark.smart_park.repository.AutoRepository;
import com.estacionamiento_smartpark.smart_park.repository.EstacionamientoRepository;
import com.estacionamiento_smartpark.smart_park.repository.RegistroRepository;

@SpringBootTest
public class RegistroServiceTest {

    @Autowired
    private RegistroService registroService;

    @MockBean
    private AutoRepository autoRepository;

    @MockBean
    private EstacionamientoRepository estacionamientoRepository;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private RegistroRepository registroRepository;

    private Registro createRegistro() {
        return new Registro(
            1L, 
            LocalDateTime.now(),
            LocalDateTime.now(),
            new Auto()
        );
    }

    @Test
    public void testFindAll() {
        when(registroRepository.findAll()).thenReturn(List.of(createRegistro()));
        List<Registro> registros = registroService.findAll();
        assertNotNull(registros);
        assertEquals(1, registros.size());
    }

    @Test
    public void testFindById() {
        when(registroRepository.findById(1L)).thenReturn(java.util.Optional.of(createRegistro()));
        Registro registro = registroService.findById(1L);
        assertNotNull(registro);
        assertEquals(1L, registro.getId());
    }

    @Test
    public void testSave() {
        Registro registro = createRegistro();
        when(registroRepository.save(registro)).thenReturn(registro);
        Registro savedRegistro = registroService.save(registro);
        assertNotNull(savedRegistro);
        assertEquals(1L, savedRegistro.getId());
    }

    @Test
    public void testPatchRegistro() {
        Registro existingRegistro = createRegistro();
        Registro patchData = new Registro();
        patchData.setId(1L);

        when(registroRepository.findById(1L)).thenReturn(java.util.Optional.of(existingRegistro));
        when(registroRepository.save(any(Registro.class))).thenReturn(existingRegistro);

        Registro patchedRegistro = registroService.patchRegistro(1L, patchData);
        assertNotNull(patchedRegistro);
        assertEquals(1L, patchedRegistro.getId());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(registroRepository).deleteById(1L);
        registroService.deleteById(1L);
        verify(registroRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testObtenerRegistrosPorPatente() {
        when(registroRepository.findByAuto_Patente("ABC123")).thenReturn(List.of(createRegistro()));

        List<Registro> resultado = registroService.obtenerRegistrosPorPatente("ABC123");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerRegistrosEntreFechas() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now();

        when(registroRepository.findByHoraLlegadaBetween(inicio, fin)).thenReturn(List.of(createRegistro()));

        List<Registro> resultado = registroService.obtenerRegistrosEntreFechas(inicio, fin);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerRegistrosActivos() {
        when(registroRepository.findRegistrosActivos()).thenReturn(List.of(createRegistro()));

        List<Registro> resultado = registroService.obtenerRegistrosActivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerRegistrosPorFecha() {
        when(registroRepository.findByFecha(any())).thenReturn(List.of(createRegistro()));

        List<Registro> resultado = registroService.obtenerRegistrosPorFecha(LocalDateTime.now().toLocalDate());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerEntradasYSalidas() {
        Object[] fila = new Object[] { 1L, "08:00", "10:00" };

        List<Object[]> lista = new java.util.ArrayList<>();
        lista.add(fila);

        when(registroRepository.findEntradaysalida()).thenReturn(lista);

        List<Object[]> resultado = registroService.obtenerEntradasYSalidas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("08:00", resultado.get(0)[1]);
        assertEquals("10:00", resultado.get(0)[2]);
    }

    @Test
    public void testRegistrarEntrada() {
        Auto auto = new Auto();
        Estacionamiento estacionamiento = new Estacionamiento();
        estacionamiento.setOcupado(false);

        when(registroRepository.estaEstacionado("ABC123")).thenReturn(false);
        when(autoRepository.findByPatente("ABC123")).thenReturn(java.util.Optional.of(auto));
        when(estacionamientoRepository.findByNumero(10)).thenReturn(java.util.Optional.of(estacionamiento));
        when(estacionamientoRepository.save(any())).thenReturn(estacionamiento);
        when(registroRepository.save(any())).thenReturn(new Registro());

        Estacionamiento resultado = registroService.registrarEntrada("ABC123", 10);

        assertNotNull(resultado);
        assertTrue(resultado.isOcupado());
    }

    @Test
    public void testRegistrarSalida() {
        Auto auto = new Auto();
        Registro registro = new Registro();
        Estacionamiento estacionamiento = new Estacionamiento();
        estacionamiento.setOcupado(true);
        estacionamiento.setAuto(auto);

        when(autoRepository.findByPatente("ABC123")).thenReturn(java.util.Optional.of(auto));
        when(registroRepository.findByAutoAndHoraSalidaIsNull(auto)).thenReturn(java.util.Optional.of(registro));
        when(estacionamientoRepository.findByAutoPatente("ABC123")).thenReturn(java.util.Optional.of(estacionamiento));
        when(registroRepository.save(any())).thenReturn(registro);
        when(estacionamientoRepository.save(any())).thenReturn(estacionamiento);

        Estacionamiento resultado = registroService.registrarSalida("ABC123");

        assertNotNull(resultado);
        assertFalse(resultado.isOcupado());
    }

    @Test
    public void testDeleteByAuto() {
        Auto auto = new Auto();
        Registro registro = createRegistro();

        when(registroRepository.findByAuto(auto)).thenReturn(List.of(registro));
        doNothing().when(pagoService).deleteByRegistro(any());
        doNothing().when(registroRepository).deleteByAuto(auto);

        registroService.deleteByAuto(auto);

        verify(pagoService, times(1)).deleteByRegistro(registro);
        verify(registroRepository, times(1)).deleteByAuto(auto);
    }

}

