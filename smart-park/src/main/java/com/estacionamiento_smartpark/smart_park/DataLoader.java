package com.estacionamiento_smartpark.smart_park;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.estacionamiento_smartpark.smart_park.model.Auto;
import com.estacionamiento_smartpark.smart_park.model.Comuna;
import com.estacionamiento_smartpark.smart_park.model.Estacionamiento;
import com.estacionamiento_smartpark.smart_park.model.Pago;
import com.estacionamiento_smartpark.smart_park.model.Region;
import com.estacionamiento_smartpark.smart_park.model.Registro;
import com.estacionamiento_smartpark.smart_park.model.Sucursal;
import com.estacionamiento_smartpark.smart_park.model.Usuario;
import com.estacionamiento_smartpark.smart_park.repository.AutoRepository;
import com.estacionamiento_smartpark.smart_park.repository.ComunaRepository;
import com.estacionamiento_smartpark.smart_park.repository.EstacionamientoRepository;
import com.estacionamiento_smartpark.smart_park.repository.PagoRepository;
import com.estacionamiento_smartpark.smart_park.repository.RegionRepository;
import com.estacionamiento_smartpark.smart_park.repository.RegistroRepository;
import com.estacionamiento_smartpark.smart_park.repository.SucursalRepository;
import com.estacionamiento_smartpark.smart_park.repository.UsuarioRepository;

import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private EstacionamientoRepository estacionamientoRepository;

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        // REGIONES
        for (int i = 1; i <= 5; i++) {
            Region region = new Region();
            region.setCodigo(100 + i);
            region.setNombre("Región " + faker.address().state());
            regionRepository.save(region);
        }

        List<Region> regiones = regionRepository.findAll();

        // COMUNAS
        for (int i = 1; i <= 10; i++) {
            Comuna comuna = new Comuna();
            comuna.setCodigo(200L + i);
            comuna.setNombre(faker.address().cityName());
            comuna.setRegion(regiones.get(random.nextInt(regiones.size())));
            comunaRepository.save(comuna);
        }

        List<Comuna> comunas = comunaRepository.findAll();

        // SUCURSALES
        for (int i = 1; i <= 5; i++) {
            Sucursal sucursal = new Sucursal();
            sucursal.setNombre("Sucursal " + i);
            sucursal.setDireccion(faker.address().streetAddress());
            sucursal.setTelefono(faker.number().numberBetween(900000000, 999999999));
            sucursal.setComuna(comunas.get(random.nextInt(comunas.size())));
            sucursalRepository.save(sucursal);
        }

        List<Sucursal> sucursales = sucursalRepository.findAll();

        // USUARIOS
        for (int i = 1; i <= 20; i++) {
            Usuario usuario = new Usuario();
            usuario.setRun(faker.idNumber().valid());
            usuario.setNombreCompleto(faker.name().fullName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setContraseña(faker.internet().password(8, 15));
            usuarioRepository.save(usuario);
        }

        List<Usuario> usuarios = usuarioRepository.findAll();

        // AUTOS
        for (int i = 1; i <= 30; i++) {
            Auto auto = new Auto();
            auto.setPatente(faker.bothify("??####")); // Ej: AB1234
            auto.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
            autoRepository.save(auto);
        }

        List<Auto> autos = autoRepository.findAll();

        // ESTACIONAMIENTOS
        for (int i = 1; i <= 20; i++) {
            Estacionamiento est = new Estacionamiento();
            est.setNumero(i);
            est.setOcupado(random.nextBoolean());
            est.setAuto(random.nextBoolean() ? autos.get(random.nextInt(autos.size())) : null);
            est.setSucursal(sucursales.get(random.nextInt(sucursales.size())));
            estacionamientoRepository.save(est);
        }

        // REGISTROS
        for (int i = 1; i <= 15; i++) {
            Registro reg = new Registro();
            reg.setHoraLlegada(LocalDateTime.now().minusHours(random.nextInt(48)));
            reg.setHoraSalida(LocalDateTime.now().plusHours(random.nextInt(5)));
            reg.setAuto(autos.get(random.nextInt(autos.size())));
            registroRepository.save(reg);

            // PAGO
            Pago pago = new Pago();
            pago.setFechaPago(LocalDateTime.now());
            pago.setMonto(random.nextInt(10000) + 1000);
            pago.setMetodo(faker.options().option("Efectivo", "Débito", "Crédito", "Transferencia"));
            pago.setRegistro(reg);
            pagoRepository.save(pago);
        }
    }
}
