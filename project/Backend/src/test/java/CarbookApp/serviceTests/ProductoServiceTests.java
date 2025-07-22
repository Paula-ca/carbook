package CarbookApp.serviceTests;

import CarbookApp.entity.*;
import CarbookApp.repository.ProductoRepository;
import CarbookApp.service.ProductoService;
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
public class ProductoServiceTests {
    @Spy
    @InjectMocks
    private ProductoService productoService;
    @Mock
    private ProductoRepository productoRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Producto createValidProduct() {
        Producto p = new Producto();
        p.setTitulo("Title example");
        p.setDescripcion("Description example");
        p.setUbicacion("Location example");
        p.setRating(4);
        p.setPrecio(100);
        p.setCiudad(new Ciudad());
        p.setCategoria(new Categoria());
        p.setCaracteristicas(List.of(new Caracteristica()));
        p.setPoliticas(List.of(new Politica()));
        p.setImagenes(List.of(new Imagen()));
        return p;
    }
    @Test
    void testAddProductoValido() {
        Producto p = createValidProduct();
        when(productoRepository.findAll()).thenReturn(List.of());
        when(productoRepository.save(any(Producto.class))).thenReturn(p);

        Producto result = productoService.add(p);
        assertNotNull(result);
        assertEquals("Title example", result.getTitulo());
        verify(productoRepository).save(p);
    }
    @Test
    void testAddProductoNull() {
        Producto p = new Producto(); // Missing fields
        Producto result = productoService.add(p);
        assertNull(result);
    }

    @Test
    void testAddProductoDuplicado() {
        Producto p = createValidProduct();
        Producto existing = createValidProduct();
        List<Producto> existingList = List.of(existing);

        when(productoRepository.findAll()).thenReturn(existingList);

        Producto result = productoService.add(p);
        assertNull(result);
        verify(productoRepository,never()).save(any());
    }
    @Test
    void testUpdateProductoValido(){
        Producto existing = createValidProduct();
        existing.setId(2L);
        Producto p = new Producto();
        p.setTitulo("New Title");
        p.setDescripcion("New Description");
        p.setUbicacion("New Location");
        p.setRating(5);
        p.setPrecio(550);
        p.setCiudad(new Ciudad());
        p.setCategoria(new Categoria());
        p.setCaracteristicas(List.of(new Caracteristica()));
        p.setPoliticas(List.of(new Politica()));
        p.setImagenes(List.of(new Imagen()));
        p.setId(2L);

        when(productoRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(productoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Producto result = productoService.update((p));
        assertEquals("New Title", result.getTitulo());
        assertEquals("New Description", result.getDescripcion());
        assertEquals("New Location",result.getUbicacion());
        assertEquals(5,result.getRating());
        assertEquals(550,result.getPrecio());
        verify(productoRepository).save(existing);
    }
    @Test
    void testUpdateProductoNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            productoService.update(null);
        });
    }

    @Test
    void testDeleteProductoSuccess() throws Exception {
        Long id = 2L;
        Producto p = createValidProduct();
        p.setId(id);
        when(productoRepository.findById(id)).thenReturn(Optional.of(p));

        String result = productoService.delete(id);
        assertEquals("Producto eliminado con id: " + id, result);
    }
    @Test
    void testDeleteProductoNotFound(){
        Long idNotFound = 99L;
        when(productoRepository.findById(idNotFound)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productoService.delete(idNotFound));
        assertEquals("Producto con id " + idNotFound + " no encontrado", exception.getMessage());
        verify(productoService, never()).update(any());
    }
}
