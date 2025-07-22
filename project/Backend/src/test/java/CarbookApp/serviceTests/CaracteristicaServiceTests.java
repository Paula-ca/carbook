package CarbookApp.serviceTests;

import CarbookApp.entity.Caracteristica;
import CarbookApp.repository.CaracteristicaRepository;
import CarbookApp.service.CaracteristicaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CaracteristicaServiceTests {

    @InjectMocks
    private CaracteristicaService caracteristicaService;
    @Mock
    private CaracteristicaRepository caracteristicaRepository;

    @Test
    void testAddCaracteristicaValida() {
        Caracteristica input = new Caracteristica("example_titulo", "example_icono");

        when(caracteristicaRepository.findAll()).thenReturn(List.of());

        when(caracteristicaRepository.save(any(Caracteristica.class))).thenAnswer(invocation -> {
            Caracteristica c = invocation.getArgument(0);
            c.setId(5L);
            return c;
        });

        Caracteristica result = caracteristicaService.add(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("example_titulo", result.getTitulo());
        assertEquals("example_icono", result.getIcono());
    }
    @Test
    void testAddCaracteristicaConTituloDuplicado() {
        Caracteristica input = new Caracteristica("A/C", "icono_ac");

        Caracteristica existente = new Caracteristica("A/C", "icono_ac");

        when(caracteristicaRepository.findAll()).thenReturn(List.of(existente));

        Caracteristica result = caracteristicaService.add(input);

        assertNull(result, "Expected null due to duplicated titulo and icono");
        verify(caracteristicaRepository, never()).save(any());
    }

    @Test
    void testAddCaracteristicaNull() {
        Caracteristica input = new Caracteristica();
        input.setTitulo(null);
        input.setIcono(null);

        Caracteristica result = caracteristicaService.add(input);

        assertNull(result);
    }
    @Test
    void testUpdateCaracteristicaValida() {
        Caracteristica existing = new Caracteristica("Old Title", "Old Icon");
        existing.setId(1L);

        Caracteristica input = new Caracteristica("New Title", "New Icon");
        input.setId(1L); // Same ID for update

        when(caracteristicaRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(caracteristicaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Caracteristica result = caracteristicaService.update(input);

        assertEquals("New Title", result.getTitulo());
        assertEquals("New Icon", result.getIcono());
        verify(caracteristicaRepository).save(existing);
    }
    @Test
    void testUpdateSoloIcono() {
        Caracteristica existing = new Caracteristica("Titulo Original", "Icono Antiguo");
        existing.setId(2L);

        Caracteristica input = new Caracteristica(null, "Icono Nuevo");
        input.setId(2L);

        when(caracteristicaRepository.findById(2L))
                .thenReturn(Optional.of(existing));
        when(caracteristicaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Caracteristica result = caracteristicaService.update(input);

        assertEquals("Titulo Original", result.getTitulo());
        assertEquals("Icono Nuevo", result.getIcono());
    }
    @Test
    void testUpdateCaracteristicaNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            caracteristicaService.update(null);
        });
    }
    @Test
    void testDeleteCaracteristicaSuccess() throws Exception {
        Long id = 1L;
        Caracteristica mockCaracteristica = new Caracteristica("Titulo", "Icono");
        mockCaracteristica.setId(id);

        when(caracteristicaRepository.findById(1L)).thenReturn(Optional.of(mockCaracteristica));

        String result = caracteristicaService.delete(id);

        assertEquals("Caracteristica eliminada con id: " + id, result);
        verify(caracteristicaRepository).deleteById(id);
    }
    @Test
    void testDeleteCaracteristicaNotFound() {
        Long id = 2L;

        when(caracteristicaRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            caracteristicaService.delete(id);
        });

        assertEquals("Caracteristica con id " + id + " no encontrada", exception.getMessage());
        verify(caracteristicaRepository, never()).deleteById(any());
    }
}
