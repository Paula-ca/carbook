package Grupo7.Autitos.controllerTests;

import Grupo7.Autitos.controller.ProductoController;
import Grupo7.Autitos.entity.*;
import Grupo7.Autitos.security.AppUserService;
import Grupo7.Autitos.security.Jwt.JwtEntryPoint;
import Grupo7.Autitos.security.Jwt.JwtProvider;
import Grupo7.Autitos.service.ImagenService;
import Grupo7.Autitos.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Set;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(ProductoController.class)
class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;
    @MockBean
    private ImagenService imagenService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private Producto createValidProduct() {
        Producto p = new Producto();
        p.setId(1L);
        p.setTitulo("Toyota Corolla");
        p.setDescripcion("Reliable sedan");
        p.setUbicacion("New York");
        p.setRating(4);
        p.setPrecio(100);

        Ciudad ciudad = new Ciudad();
        ciudad.setTitulo("New York");
        p.setCiudad(ciudad);

        Categoria categoria = new Categoria();
        categoria.setTitulo("Sedan");
        p.setCategoria(categoria);

        p.setCaracteristicas(List.of(new Caracteristica()));
        p.setPoliticas(List.of(new Politica()));
        p.setImagenes(List.of(new Imagen()));

        return p;
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfProducts() throws Exception {
        List<Producto> products = List.of(createValidProduct());
        Mockito.when(productoService.list()).thenReturn(products);

        mockMvc.perform(get("/products/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Toyota Corolla"))
                .andExpect(jsonPath("$[0].categoria.titulo").value("Sedan"))
                .andExpect(jsonPath("$[0].ciudad.titulo").value("New York"));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnProductById() throws Exception {
        Producto producto = createValidProduct();
        Mockito.when(productoService.find(1L)).thenReturn(producto);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Toyota Corolla"));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnProductsByCity() throws Exception {
        Producto producto = createValidProduct();
        Mockito.when(productoService.cityFilter(10L)).thenReturn(Set.of(producto));

        mockMvc.perform(get("/products/list/city-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ciudad.titulo").value("New York"));
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewProduct() throws Exception {
        Producto producto = createValidProduct();
        Mockito.when(productoService.add(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(
                        post("/products/add")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateProduct() throws Exception {
        Producto producto = createValidProduct();
        Mockito.when(productoService.find(producto.getId())).thenReturn(producto);
        Mockito.when(productoService.update(Mockito.any())).thenReturn(producto);

        mockMvc.perform(put("/products/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(producto)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteProduct() throws Exception {
        Producto producto = createValidProduct();
        Mockito.when(productoService.find(1L)).thenReturn(producto);

        mockMvc.perform(delete("/products/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto eliminado con id: 1"));

    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldFilterByDateAndCity() throws Exception {
        Producto producto = createValidProduct();
        Reserva reserva = new Reserva(); // mock basic reserva
        reserva.setProducto(producto);
        Mockito.when(productoService.dateAndCityFilter(Mockito.any(), Mockito.any(), Mockito.eq(1L)))
                .thenReturn(List.of(producto));

        mockMvc.perform(get("/products/date-filter")
                        .with(csrf())
                        .param("inicio", "2025-07-01")
                        .param("fin", "2025-07-10")
                        .param("ciudad", "1"))
                .andExpect(status().isOk());
    }

}





