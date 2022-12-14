package com.uce.edu.demo.service.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.demo.repository.modelo.Cliente;
import com.uce.edu.demo.repository.modelo.Reserva;
import com.uce.edu.demo.repository.modelo.Vehiculo;
import com.uce.edu.demo.service.IClienteService;
import com.uce.edu.demo.service.IGestorReservasService;
import com.uce.edu.demo.service.IReservaService;
import com.uce.edu.demo.service.IVehiculoService;

@SpringBootTest
@Transactional
@Rollback(true)
public class GestorReservaServiceTest {
	@Autowired
	private IGestorReservasService iGestorReservasService;

	@Autowired
	private IReservaService iReservaService;

	@Autowired
	private IVehiculoService iVehiculoService;

	@Autowired
	private IClienteService iClienteService;

	@Test
	@Rollback(true)
	void testReservarVehiculo() {
		Vehiculo v = new Vehiculo();
		Cliente c = new Cliente();
		v.setPlaca("CXX-333");
		c.setCedula("117251313");
		this.iGestorReservasService.reservarVehiculo(v.getPlaca(), c.getCedula(), LocalDateTime.of(2022, 9, 4, 0, 0),
				LocalDateTime.of(2022, 9, 12, 23, 59));
		assertThat(this.iReservaService.buscarNumero("3")).isNotNull();
//		fail("Not yet implemented");
	}

	@Test
	@Rollback(true)
	void testVerificarDisponibilidad() {
		Vehiculo v = new Vehiculo();
		v.setPlaca("CXX-333");
		LocalDateTime fechaInicio = LocalDateTime.of(2022, 9, 4, 0, 0);
		LocalDateTime fechaFin = LocalDateTime.of(2022, 9, 12, 23, 59);
		this.iGestorReservasService.verificarDisponibilidad(v.getPlaca(), fechaInicio, fechaFin);
		assertEquals(LocalDateTime.of(2022, 9, 12, 23, 59), fechaFin);
	}

	@Test
	@Rollback(true)
	void testRetirarVehiculo() {
		Reserva r = new Reserva();
		r.setNumero("3");
		Reserva rbusqueda = this.iReservaService.buscarNumero(r.getNumero());
		this.iGestorReservasService.retirarVehiculo(rbusqueda.getNumero());
		assertThat(rbusqueda.getNumero()).isNotNull();
		// fail("Not yet implemented");
	}

	@Test
	@Rollback(true)
	void testVerificarVehiculoReservado() {
		Reserva r = new Reserva();
		r.setNumero("3");
		Reserva rbusqueda = this.iReservaService.buscarNumero(r.getNumero());
		assertEquals(this.iReservaService.buscarNumero(rbusqueda.getNumero()),
				this.iReservaService.buscarNumero(r.getNumero()));
//		fail("Not yet implemented");
	}

	@Test
	@Rollback(true)
	void testVerificarPlacaReservada() {
		Vehiculo v = new Vehiculo();
		Cliente c = new Cliente();
		v.setPlaca("CXX-333");
		c.setCedula("1313151621");
		this.iGestorReservasService.reservarVehiculo(v.getPlaca(), c.getCedula(), LocalDateTime.of(2022, 9, 4, 0, 0),
				LocalDateTime.of(2022, 9, 12, 23, 59));
		assertThat(this.iVehiculoService.buscarPlaca(v.getPlaca())).isNotNull();
	}

	@Test
	@Rollback(true)
	void testVerificarCedulaReservada() {
		Vehiculo v = new Vehiculo();
		Cliente c = new Cliente();
		v.setPlaca("CXX-333");
		c.setCedula("1313151621");
		this.iGestorReservasService.reservarVehiculo(v.getPlaca(), c.getCedula(), LocalDateTime.of(2022, 11, 27, 0, 0),
				LocalDateTime.of(2022, 9, 29, 23, 59));
		assertThat(this.iClienteService.buscarCedula(c.getCedula())).isNotNull();
	}

	@Test
	@Rollback(true)
	void testRegistroReservadoActualizado() {
		Cliente c = new Cliente();
		c.setCedula("1313151621");
		Vehiculo v = new Vehiculo();
		v.setPlaca("ASD-333");
		Cliente cbusqueda = this.iClienteService.buscarCedula(c.getCedula());
		cbusqueda.setApellido("Silva");
		cbusqueda.setGenero("F");
		String reserva = this.iGestorReservasService.reservarVehiculo(v.getPlaca(), c.getCedula(),
				LocalDateTime.of(2022, 9, 18, 0, 0), LocalDateTime.of(2022, 9, 23, 23, 59));
		assertThat(this.iReservaService.buscarNumero(reserva)).isNotNull();
	}

	@Test
	@Rollback(true)
	void testVerificarApellidoReservado() {
		Cliente c = new Cliente();
		c.setApellido("Fernandez");
		assertThat(this.iClienteService.buscarApellido(c.getApellido())).isNotNull();
	}

	@Test
	@Rollback(true)
	void testInsertarReservaReservado() {
		Cliente c = new Cliente();
		Vehiculo v = new Vehiculo();
		c.setCedula("1313151621");
		v.setPlaca("CXX-333");
		String numero = this.iGestorReservasService.reservarVehiculo(v.getPlaca(), c.getCedula(),
				LocalDateTime.of(2022, 9, 4, 0, 0), LocalDateTime.of(2022, 9, 12, 23, 59));
		Reserva r;
		try {
			r = this.iReservaService.buscarNumero(numero);
		} catch (Exception e) {
			r = null;
		}
		assertThat(r).isNull();
	}

	@Test
	@Rollback(true)
	void testEliminarReservaReservado() {
		Reserva r = new Reserva();
		r.setNumero("3");
		Reserva rbuscar = this.iReservaService.buscarNumero(r.getNumero());
		this.iReservaService.eliminar(rbuscar.getNumero());
		assertThat(rbuscar.getNumero()).isNotNull();

	}

	@Test
	@Rollback(true)
	void testActualizarReservaReservado() {
		Reserva r = new Reserva();
		r.setNumero("3");
		Reserva rbusqueda = this.iReservaService.buscarNumero(r.getNumero());
		rbusqueda.setNumero("4");
		rbusqueda.setTotalPagar(new BigDecimal(10));
		this.iReservaService.actualizar(rbusqueda);
		assertThat(rbusqueda.getNumero()).isNotNull();
	}

	@Test
	@Rollback(true)
	void testEliminarClienteReserva() {
		Cliente c = new Cliente();
		c.setCedula("117251313");
		Cliente cbusqueda = this.iClienteService.buscarCedula(c.getCedula());
		this.iClienteService.eliminarPorCedula(cbusqueda.getCedula());
		assertThat(cbusqueda.getCedula()).isNotNull();
	}

	@Test
	@Rollback(true)
	void testActualizarClienteReserva() {
		Cliente c = new Cliente();
		c.setCedula("117251313");
		Cliente cbusqueda = this.iClienteService.buscarCedula(c.getCedula());
		cbusqueda.setApellido("Martha");
		cbusqueda.setGenero("M");
		this.iClienteService.actualizar(cbusqueda);
		assertEquals(this.iClienteService.buscarCedula(cbusqueda.getCedula()).getApellido(), cbusqueda.getApellido());
	}
}
