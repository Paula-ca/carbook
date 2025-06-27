package Grupo7.Autitos.serviceTests;

import Grupo7.Autitos.entity.Producto;
import Grupo7.Autitos.entity.Reserva;
import Grupo7.Autitos.entity.Usuario;
import Grupo7.Autitos.repository.ReservaRepository;
import Grupo7.Autitos.service.ProductoService;
import Grupo7.Autitos.service.ReservaService;
import Grupo7.Autitos.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTests {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ProductoService productoService;

    @Mock
    private UsuarioService usuarioService;

    @Test
    public void testAddReservaValida() {

        Producto producto = new Producto();
        producto.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Reserva reserva = new Reserva();
        reserva.setFecha_ingreso(LocalDate.of(2025, 7, 1));
        reserva.setFecha_final(LocalDate.of(2025, 7, 5));
        reserva.setPrecio(1000);
        reserva.setHora_comienzo(LocalTime.of(12,00));
        reserva.setProducto(producto);
        reserva.setUsuario(usuario);

        when(productoService.find(1L)).thenReturn(producto);
        when(usuarioService.find(1L)).thenReturn(usuario);
        when(reservaRepository.findAll()).thenReturn(List.of());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setId(10L);
            return r;
        });
        when(reservaRepository.findById(10L)).thenReturn(Optional.of(reserva));

        Reserva result = reservaService.add(reserva);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    public void testAddReservaConCamposFaltantes() {
        Reserva reserva = new Reserva();

        Reserva result = reservaService.add(reserva);

        assertNull(result);
    }

    @Test
    public void testAddReservaDuplicada() {
        Producto producto = new Producto(); producto.setId(1L);
        Usuario usuario = new Usuario(); usuario.setId(1L);

        Reserva reserva = new Reserva();
        reserva.setFecha_ingreso(LocalDate.of(2025, 7, 1));
        reserva.setFecha_final(LocalDate.of(2025, 7, 5));
        reserva.setPrecio(1000);
        reserva.setHora_comienzo(LocalTime.of(12,00));
        reserva.setProducto(producto);
        reserva.setUsuario(usuario);

        Reserva existente = new Reserva();
        existente.setFecha_ingreso(reserva.getFecha_ingreso());
        existente.setFecha_final(reserva.getFecha_final());
        existente.setProducto(producto);
        existente.setUsuario(usuario);

        when(productoService.find(1L)).thenReturn(producto);
        when(usuarioService.find(1L)).thenReturn(usuario);
        when(reservaRepository.findAll()).thenReturn(List.of(existente));

        Reserva result = reservaService.add(reserva);

        assertNull(result);
    }
    @Test
    public void testUpdate_Success() {
        Long reservaId = 1L;
        Producto producto = new Producto();
        producto.setId(10L);

        Reserva existing = new Reserva();
        existing.setId(reservaId);
        existing.setProducto(producto);
        existing.setFecha_ingreso(LocalDate.of(2025, 6, 20));
        existing.setFecha_final(LocalDate.of(2025, 6, 25));
        existing.setBorrado(null);

        List<Reserva> reservasProducto = List.of(existing);

        Reserva updateData = new Reserva();
        updateData.setId(reservaId);
        updateData.setFecha_ingreso(LocalDate.of(2025, 6, 22));
        updateData.setFecha_final(LocalDate.of(2025, 6, 27));
        updateData.setHora_comienzo(LocalTime.of(10,00));
        updateData.setEstado("CONFIRMADA");
        updateData.setEstado_pago("PAGADO");
        updateData.setPago_id(35574L);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(existing));
        when(reservaRepository.findByProductoId(producto.getId())).thenReturn(reservasProducto);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        Reserva updated = reservaService.update(updateData);

        assertEquals(updateData.getFecha_ingreso(), updated.getFecha_ingreso());
        assertEquals(updateData.getFecha_final(), updated.getFecha_final());
        assertEquals(updateData.getHora_comienzo(), updated.getHora_comienzo());
        assertEquals(updateData.getEstado(), updated.getEstado());
        assertEquals(updateData.getEstado_pago(), updated.getEstado_pago());
        assertEquals(updateData.getPago_id(), updated.getPago_id());
    }
    @Test
    public void testUpdate_ThrowsExceptionWhenConflict() {
        Long reservaId = 1L;
        Producto producto = new Producto();
        producto.setId(10L);

        Reserva existing = new Reserva();
        existing.setId(reservaId);
        existing.setProducto(producto);
        existing.setFecha_ingreso(LocalDate.of(2025, 6, 20));
        existing.setFecha_final(LocalDate.of(2025, 6, 25));
        existing.setBorrado(null);

        Reserva otraReserva = new Reserva();
        otraReserva.setId(2L);
        otraReserva.setProducto(producto);
        otraReserva.setFecha_ingreso(LocalDate.of(2025, 6, 23));
        otraReserva.setFecha_final(LocalDate.of(2025, 6, 28));
        otraReserva.setBorrado(null);

        List<Reserva> reservasProducto = List.of(existing, otraReserva);

        Reserva updateData = new Reserva();
        updateData.setId(reservaId);
        updateData.setFecha_ingreso(LocalDate.of(2025, 6, 24));
        updateData.setFecha_final(LocalDate.of(2025, 6, 26));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(existing));
        when(reservaRepository.findByProductoId(producto.getId())).thenReturn(reservasProducto);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.update(updateData);
        });

        assertTrue(thrown.getMessage().contains("Ya hay reservas entre"));
    }
    @Test
    public void testCancel_Success() {
        Long reservaId = 1L;
        Reserva reserva = new Reserva();
        reserva.setId(reservaId);
        reserva.setBorrado(null);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        String result = reservaService.cancel(reservaId);

        assertNotNull(reserva.getBorrado());
        assertEquals("Reserva cancelada con id: " + reservaId, result);
    }

    @Test
    public void testCancel_ThrowsWhenAlreadyCancelled() {
        Long reservaId = 1L;
        Reserva reserva = new Reserva();
        reserva.setId(reservaId);
        reserva.setBorrado(LocalDate.now().minusDays(1));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            reservaService.cancel(reservaId);
        });

        assertEquals("La reserva ya fue cancelada anteriormente.", thrown.getMessage());
    }

    @Test
    public void testCancel_ThrowsWhenNotFound() {
        Long reservaId = 1L;

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            reservaService.cancel(reservaId);
        });

        assertTrue(thrown.getMessage().contains("Reserva con id " + reservaId + " no encontrada"));
    }
}



