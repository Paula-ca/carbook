package Grupo7.Autitos.controllerTests;

import Grupo7.Autitos.controller.CaracteristicaController;
import Grupo7.Autitos.entity.Caracteristica;
import Grupo7.Autitos.security.AppUserService;
import Grupo7.Autitos.security.Jwt.JwtEntryPoint;
import Grupo7.Autitos.security.Jwt.JwtProvider;
import Grupo7.Autitos.service.CaracteristicaService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(CaracteristicaController.class)
public class CaracteristicaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaracteristicaService caracteristicaService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private Caracteristica createValidCaracteristica(){
        Caracteristica carac = new Caracteristica();
        carac.setId(1L);
        carac.setTitulo("Airbag");
        carac.setIcono("Airbag Icono");

        return carac;
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfFeatures()throws Exception{
        Set<Caracteristica> features = Set.of(createValidCaracteristica());
        Mockito.when(caracteristicaService.list()).thenReturn(features);

        mockMvc.perform(get("/features/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Airbag"))
                .andExpect(jsonPath("$[0].icono").value("Airbag Icono"
                        ));
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewFeature()throws Exception{
        Caracteristica carac = createValidCaracteristica();
        Mockito.when(caracteristicaService.add(Mockito.any(Caracteristica.class))).thenReturn(carac);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        post("/features/add")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carac)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateFeature()throws Exception{
        Caracteristica carac = createValidCaracteristica();
        Mockito.when(caracteristicaService.find(carac.getId())).thenReturn(carac);
        Mockito.when(caracteristicaService.update(Mockito.any())).thenReturn(carac);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        put("/features/update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carac)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteFeature()throws Exception{
        Caracteristica carac = createValidCaracteristica();
        Mockito.when(caracteristicaService.find(1L)).thenReturn(carac);

        mockMvc.perform(delete("/features/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Caracteristica eliminada con id: 1"));

    }
}
