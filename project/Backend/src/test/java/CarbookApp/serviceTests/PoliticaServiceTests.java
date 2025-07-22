package CarbookApp.serviceTests;

import CarbookApp.entity.Politica;
import CarbookApp.repository.PoliticaRepository;
import CarbookApp.service.PoliticaService;
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
public class PoliticaServiceTests {
    @Spy
    @InjectMocks
    private PoliticaService politicaService;
    @Mock
    private PoliticaRepository politicaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddPoliticaValida(){
        Politica input = new Politica("example_titulo","example_description");

        when(politicaRepository.findAll()).thenReturn(List.of());
        when(politicaRepository.save(any(Politica.class))).thenAnswer(invocation ->{
            Politica p = invocation.getArgument(0);
            p.setId(5L);
            return p;
        } );
        Politica result = politicaService.add(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("example_titulo", result.getTitulo());
        assertEquals("example_description", result.getDescripcion());
    }
    @Test
    void testAddPoliticaDuplicada(){
        Politica input = new Politica("Politica","Duplicada");
        Politica existente = new Politica("Politica","Duplicada");

        when(politicaRepository.findAll()).thenReturn(List.of(existente));

        Politica result = politicaService.add(input);

        assertNull(result);
        verify(politicaRepository,never()).save(any());
    }
    @Test
    void testAddPoliticaNull(){
        Politica input = new Politica();
        input.setTitulo(null);
        input.setDescripcion(null);

        Politica result = politicaService.add(input);

        assertNull(result);
    }
    @Test
    void testUpdatePoliticaValida(){
        Politica existing = new Politica("Old title","Old desc");
        existing.setId(1L);

        Politica input = new Politica("New Title","New desc");
        input.setId(1L);

        when(politicaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(politicaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Politica result = politicaService.update((input));

        assertEquals("New Title", result.getTitulo());
        assertEquals("New desc", result.getDescripcion());

        verify(politicaRepository).save(existing);
    }
    @Test
    void testUpdateSoloDescripcion(){
        Politica existing = new Politica("Titulo Original","Descripcion original");
        existing.setId(2L);

        Politica input = new Politica(null,"Descripcion Nueva");
        input.setId(2L);

        when(politicaRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(politicaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Politica result = politicaService.update(input);

        assertEquals("Titulo Original", result.getTitulo());
        assertEquals("Descripcion Nueva", result.getDescripcion());
    }
    @Test
    void testUpdatePoliticaNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            politicaService.update(null);
        });
    }
    @Test
    public void testDeletePoliticaSuccess() throws Exception {
        Long id = 5L;
        Politica pol = new Politica("Titulo", "Descripcion");
        pol.setId(id);

        when(politicaRepository.findById(id)).thenReturn(Optional.of(pol));

        String result = politicaService.delete(id);

        assertEquals("Politica eliminada con id: " + id, result);
        verify(politicaRepository).deleteById(id);
    }
    @Test
    public void testDeletePoliticaNotFound() {
        Long id = 10L;
        when(politicaRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> politicaService.delete(id)
        );

        assertEquals("Politica con id " + id + " no encontrada", thrown.getMessage());
        verify(politicaRepository, never()).deleteById(any());
    }
}
