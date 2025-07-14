package Grupo7.Autitos.serviceTests;

import Grupo7.Autitos.entity.Ciudad;
import Grupo7.Autitos.entity.Imagen;
import Grupo7.Autitos.repository.ImagenRepository;
import Grupo7.Autitos.service.ImagenService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImagenServiceTests {

    @Spy
    @InjectMocks
    private ImagenService imagenService;

    @Mock
    private ImagenRepository imagenRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testUpdateImagenValida(){
        Imagen existing = new Imagen("Old Imagen","Old url");
        existing.setId(1L);

        Imagen input = new Imagen("New Imagen","New Url");
        input.setId(1L);

        when(imagenRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(imagenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Imagen result = imagenService.update(input);

        assertEquals("New Imagen", result.getTitulo());
        assertEquals("New Url", result.getUrl());

        verify(imagenRepository).save(existing);
    }
    @Test
    void testUpdateImagenNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            imagenService.update(null);
        });
    }
    @Test
    public void testDeleteImagenSuccess() throws Exception {
        Long id = 5L;
        Imagen imagen = new Imagen("Titulo", "Url");
        imagen.setId(id);

        when(imagenRepository.findById(id)).thenReturn(Optional.of(imagen));

        String result = imagenService.delete(id);

        assertEquals("Imagen eliminada con id: " + id, result);
        verify(imagenRepository).deleteById(id);
    }
    @Test
    public void testDeleteImagenNotFound() {
        Long id = 10L;
        when(imagenRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> imagenService.delete(id)
        );

        assertEquals("Imagen con id " + id + " no encontrada", thrown.getMessage());
        verify(imagenRepository, never()).deleteById(any());
    }
}
