package CarbookApp.serviceTests;

import CarbookApp.entity.Categoria;
import CarbookApp.repository.CategoriaRepository;
import CarbookApp.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTests {
    @Spy
    @InjectMocks
    private CategoriaService categoriaService;
    @Mock
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategoriaValida(){
        Categoria input = new Categoria("example_titulo","example_descripcion","example_url",null);

        when(categoriaRepository.findAll()).thenReturn(List.of());
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation ->{
            Categoria cat = invocation.getArgument(0);
            cat.setId(5L);
            return cat;
        } );
        Categoria result = categoriaService.add(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("example_titulo", result.getTitulo());
        assertEquals("example_descripcion", result.getDescripcion());
        assertEquals("example_url", result.getUrlImagen());
    }
    @Test
    void testAddCategoriaConTituloDuplicado(){
        Categoria input = new Categoria("Compacto","descripcion","imagen",null);
        Categoria existente = new Categoria("Compacto","descripcion","imagen",null);

        when(categoriaRepository.findAll()).thenReturn(List.of(existente));

        Categoria result = categoriaService.add(input);

        assertNull(result);
        verify(categoriaRepository,never()).save(any());
    }
    @Test
    void testAddCategoriaNull(){
        Categoria input = new Categoria();

        Categoria result = categoriaService.add(input);

        assertNull(result);
    }
    @Test
    void testUpdateCategoriaValida(){
        Categoria existing = new Categoria("Old title","Old description","Old Imagen",null);
        existing.setId(1L);

        Categoria input = new Categoria("New Title","New Description","New Imagen",null);
        input.setId(1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Categoria result = categoriaService.update((input));

        assertEquals("New Title", result.getTitulo());
        assertEquals("New Description", result.getDescripcion());
        assertEquals("New Imagen",result.getUrlImagen());
        verify(categoriaRepository).save(existing);
    }
    @Test
    void testUpdateSoloImagen(){
        Categoria existing = new Categoria("Titulo Original","Descripcion original","Imagen Antigua",null);
        existing.setId(2L);

        Categoria input = new Categoria(null,null,"Imagen Nueva",null);
        input.setId(2L);

        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Categoria result = categoriaService.update(input);

        assertEquals("Titulo Original", result.getTitulo());
        assertEquals("Imagen Nueva", result.getUrlImagen());
    }
    @Test
    void testUpdateCategoriaNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.update(null);
        });
    }
    @Test
    void testDeleteCategoriaSuccess() throws Exception {
        Long id = 5L;
        Categoria categoria = new Categoria("Titulo", "Descripcion", "img.jpg", null);
        categoria.setId(id);

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        String result = categoriaService.delete(id);

        assertEquals("Categoria eliminada con id: " + id, result);
        assertNotNull(categoria.getBorrado());

    }
    @Test
    void testDeleteCategoriaYaEliminada() throws Exception {
        Long id = 2L;
        Categoria categoria = new Categoria("Titulo", "Desc", "img.jpg", LocalDate.now());
        categoria.setId(id);

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        String result = categoriaService.delete(id);

        assertEquals("La categoria id " + id + " fue eliminada anteriormente", result);
        verify(categoriaService, never()).update(any());
    }
    @Test
    void testDeleteCategoriaNotFound() {
        Long id = 99L;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> categoriaService.delete(id));
        assertEquals("Categoria con id " + id + " no encontrada", exception.getMessage());
        verify(categoriaService, never()).update(any());
    }
    @Test
    void testHardDeleteCategoriaSuccess() throws Exception {
        Long id = 5L;
        Categoria categoria = new Categoria("Titulo", "Descripcion", "imagen.jpg", null);
        categoria.setId(id);

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        String result = categoriaService.hardDelete(id);

        assertEquals("Categoria eliminada con id: " + id, result);
        verify(categoriaRepository).deleteById(id);
    }
    @Test
    void testHardDeleteCategoriaNotFound() {
        Long id = 10L;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> categoriaService.hardDelete(id)
        );

        assertEquals("Categoria con id " + id + " no encontrada", thrown.getMessage());
        verify(categoriaRepository, never()).deleteById(any());
    }
}
