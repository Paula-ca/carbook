package CarbookApp.controllerTests;

import CarbookApp.controller.CategoriaController;
import CarbookApp.entity.Categoria;
import CarbookApp.security.AppUserService;
import CarbookApp.security.Jwt.JwtEntryPoint;
import CarbookApp.security.Jwt.JwtProvider;
import CarbookApp.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private Categoria createValidCategoria(){
        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setTitulo("Deportivo");
        cat.setDescripcion("Categoria deportivo");
        cat.setUrlImagen("Imagen deportivo");
        return cat;
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfActiveCategories()throws Exception{
        Set<Categoria> categorias = Set.of(createValidCategoria());
        Mockito.when(categoriaService.list(false)).thenReturn(categorias);

        mockMvc.perform(get("/categories/list/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Deportivo"))
                .andExpect(jsonPath("$[0].descripcion").value("Categoria deportivo"
                ));
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewCategory()throws Exception{
        Categoria cat = createValidCategoria();
        Mockito.when(categoriaService.add(Mockito.any(Categoria.class))).thenReturn(cat);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        post("/categories/add")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateCategory()throws Exception{
        Categoria cat = createValidCategoria();
        Mockito.when(categoriaService.find(cat.getId())).thenReturn(cat);
        Mockito.when(categoriaService.update(Mockito.any())).thenReturn(cat);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        put("/categories/update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteCategory()throws Exception{
        Categoria cat = createValidCategoria();
        Mockito.when(categoriaService.find(1L)).thenReturn(cat);

        mockMvc.perform(delete("/categories/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Categoria eliminada con id: 1"));

    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldStrongDeleteCategory()throws Exception{
        Categoria cat = createValidCategoria();
        Mockito.when(categoriaService.find(1L)).thenReturn(cat);

        mockMvc.perform(delete("/categories/strongDelete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Categoria eliminada con id: 1"));

    }
}
