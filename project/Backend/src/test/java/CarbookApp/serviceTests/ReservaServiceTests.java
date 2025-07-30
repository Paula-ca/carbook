package CarbookApp.serviceTests;

import CarbookApp.Interface.ReservaMapper;
import CarbookApp.dto.ReservaDTO;
import CarbookApp.entity.*;

import CarbookApp.repository.ReservaRepository;
import CarbookApp.service.ProductoService;
import CarbookApp.service.ReservaService;
import CarbookApp.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ReservaServiceTests {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ProductoService productoService;

    @Mock
    private UsuarioService usuarioService;

    private ReservaMapper reservaMapper = Mappers.getMapper(ReservaMapper.class);

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

        ReservaDTO result = reservaService.add(reserva);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    public void testAddReservaConCamposFaltantes() {
        Reserva reserva = new Reserva();

        ReservaDTO result = reservaService.add(reserva);

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

        ReservaDTO result = reservaService.add(reserva);

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

        ReservaDTO dto1 = reservaMapper.toDto(existing);

        List<ReservaDTO> reservasProducto = List.of(dto1);

        Reserva updateData = new Reserva();
        updateData.setId(reservaId);
        updateData.setFecha_ingreso(LocalDate.of(2025, 6, 22));
        updateData.setFecha_final(LocalDate.of(2025, 6, 27));
        updateData.setHora_comienzo(LocalTime.of(10,00));
        updateData.setEstado("CONFIRMADA");
        updateData.setEstado_pago("PAGADO");
        updateData.setPago_id(35574L);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(existing));
        when(reservaRepository.findBookingsForProduct(producto.getId())).thenReturn(reservasProducto);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        ReservaDTO updated = reservaService.update(updateData);

        assertEquals(updateData.getFecha_ingreso(), updated.getFechaIngreso());
        assertEquals(updateData.getFecha_final(), updated.getFechaFinal());
        assertEquals(updateData.getEstado(), updated.getEstado());
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

        ReservaDTO dto1 = reservaMapper.toDto(existing);
        ReservaDTO dto2 = reservaMapper.toDto(otraReserva);

        List<ReservaDTO> reservasProducto = List.of(dto1, dto2);

        Reserva updateData = new Reserva();
        updateData.setId(reservaId);
        updateData.setFecha_ingreso(LocalDate.of(2025, 6, 24));
        updateData.setFecha_final(LocalDate.of(2025, 6, 26));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(existing));
        when(reservaRepository.findBookingsForProduct(producto.getId())).thenReturn(reservasProducto);

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



