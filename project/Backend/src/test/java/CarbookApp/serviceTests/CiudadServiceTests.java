package CarbookApp.serviceTests;

import CarbookApp.entity.Ciudad;
import CarbookApp.repository.CiudadRepository;
import CarbookApp.service.CiudadService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CiudadServiceTests {
    @Spy
    @InjectMocks
    private CiudadService ciudadService;

    @Mock
    private CiudadRepository ciudadRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddCiudadValida(){
        Ciudad input = new Ciudad("example_titulo","example_pais");

        when(ciudadRepository.findAll()).thenReturn(List.of());
        when(ciudadRepository.save(any(Ciudad.class))).thenAnswer(invocation ->{
            Ciudad c = invocation.getArgument(0);
            c.setId(5L);
            return c;
        } );
        Ciudad result = ciudadService.add(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("example_titulo", result.getTitulo());
        assertEquals("example_pais", result.getPais());

    }
    @Test
    void testAddCiudadConTituloDuplicado(){
        Ciudad input = new Ciudad("Mendoza","Argentina");
        Ciudad existente = new Ciudad("Mendoza","Argentina");

        when(ciudadRepository.findAll()).thenReturn(List.of(existente));

        Ciudad result = ciudadService.add(input);

        assertNull(result);
        verify(ciudadRepository,never()).save(any());
    }
    @Test
    void testAddCiudadNull(){
        Ciudad input = new Ciudad();
        input.setTitulo(null);
        input.setPais(null);

        Ciudad result = ciudadService.add(input);

        assertNull(result);
    }
    @Test
    void testUpdateCiudadValida(){
        Ciudad existing = new Ciudad("Old title","Old country");
        existing.setId(1L);

        Ciudad input = new Ciudad("New Title","New Country");
        input.setId(1L);

        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(ciudadRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Ciudad result = ciudadService.update((input));

        assertEquals("New Title", result.getTitulo());
        assertEquals("New Country", result.getPais());

        verify(ciudadRepository).save(existing);
    }
    @Test
    void testUpdateSoloTitulo(){
        Ciudad existing = new Ciudad("Titulo Original","Pais original");
        existing.setId(2L);

        Ciudad input = new Ciudad("Titulo Nuevo",null);
        input.setId(2L);

        when(ciudadRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(ciudadRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Ciudad result = ciudadService.update(input);

        assertEquals("Pais original", result.getPais());
        assertEquals("Titulo Nuevo", result.getTitulo());
    }
    @Test
    void testUpdateCiudadNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            ciudadService.update(null);
        });
    }
    @Test
    public void testDeleteCiudadSuccess() throws Exception {
        Long id = 5L;
        Ciudad ciudad = new Ciudad("Titulo", "Pais");
        ciudad.setId(id);

        when(ciudadRepository.findById(id)).thenReturn(Optional.of(ciudad));

        String result = ciudadService.delete(id);

        assertEquals("Ciudad eliminada con id: " + id, result);
        verify(ciudadRepository).deleteById(id);
    }
    @Test
    public void testDeleteCiudadNotFound() {
        Long id = 10L;
        when(ciudadRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> ciudadService.delete(id)
        );

        assertEquals("Ciudad con id " + id + " no encontrada", thrown.getMessage());
        verify(ciudadRepository, never()).deleteById(any());
    }
}
