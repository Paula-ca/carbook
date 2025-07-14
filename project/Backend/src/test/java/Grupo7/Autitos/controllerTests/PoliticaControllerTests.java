package Grupo7.Autitos.controllerTests;

import Grupo7.Autitos.controller.PoliticaController;
import Grupo7.Autitos.entity.Politica;
import Grupo7.Autitos.security.AppUserService;
import Grupo7.Autitos.security.Jwt.JwtEntryPoint;
import Grupo7.Autitos.security.Jwt.JwtProvider;
import Grupo7.Autitos.service.PoliticaService;
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

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(PoliticaController.class)
public class PoliticaControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PoliticaService politicaService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private Politica createValidPolitica(){
        Politica politica = new Politica();
        politica.setId(1L);
        politica.setTitulo("Politica de cancelacion");
        politica.setDescripcion("Politica de cancelacion descripcion");

        return politica;
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfPolicies()throws Exception{
        Set<Politica> politicas = Set.of(createValidPolitica());
        Mockito.when(politicaService.list()).thenReturn(politicas);

        mockMvc.perform(get("/policies/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Politica de cancelacion"))
                .andExpect(jsonPath("$[0].descripcion").value("Politica de cancelacion descripcion"
                ));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfPoliciesByTitle()throws Exception{
        List<Politica> politicas = List.of(createValidPolitica());
        Mockito.when(politicaService.findByTitulo("Politica de cancelacion")).thenReturn(politicas);

        mockMvc.perform(get("/policies/list-Politica de cancelacion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewPolicy()throws Exception{
        Politica politica = createValidPolitica();
        Mockito.when(politicaService.add(Mockito.any(Politica.class))).thenReturn(politica);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        post("/policies/add")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(politica)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdatePolicy()throws Exception{
        Politica politica = createValidPolitica();
        Mockito.when(politicaService.find(politica.getId())).thenReturn(politica);
        Mockito.when(politicaService.update(Mockito.any())).thenReturn(politica);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        put("/policies/update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(politica)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeletePolicy()throws Exception{
        Politica politica = createValidPolitica();
        Mockito.when(politicaService.find(1L)).thenReturn(politica);

        mockMvc.perform(delete("/policies/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk());

    }
}