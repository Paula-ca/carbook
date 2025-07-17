package CarbookApp.controllerTests;

import CarbookApp.controller.CiudadController;
import CarbookApp.entity.Ciudad;
import CarbookApp.security.AppUserService;
import CarbookApp.security.Jwt.JwtEntryPoint;
import CarbookApp.security.Jwt.JwtProvider;
import CarbookApp.service.CiudadService;
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
@WebMvcTest(CiudadController.class)
public class CiudadControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CiudadService ciudadService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private Ciudad createValidCiudad(){
        Ciudad city = new Ciudad();
        city.setId(1L);
        city.setTitulo("Mendoza");
        city.setPais("Argentina");

        return city;
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfCities()throws Exception{
        Set<Ciudad> ciudades = Set.of(createValidCiudad());
        Mockito.when(ciudadService.list()).thenReturn(ciudades);

        mockMvc.perform(get("/cities/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Mendoza"))
                .andExpect(jsonPath("$[0].pais").value("Argentina"
                ));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfCitiesByCountry()throws Exception{
        List<Ciudad> ciudades = List.of(createValidCiudad());
        Mockito.when(ciudadService.findByPais("Argentina")).thenReturn(ciudades);

        mockMvc.perform(get("/cities/list-Argentina")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Mendoza"));

        mockMvc.perform(get("/cities/list-Brasil")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewCity()throws Exception{
        Ciudad ciudad = createValidCiudad();
        Mockito.when(ciudadService.add(Mockito.any(Ciudad.class))).thenReturn(ciudad);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        post("/cities/add")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ciudad)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateCity()throws Exception{
        Ciudad ciudad = createValidCiudad();
        Mockito.when(ciudadService.find(ciudad.getId())).thenReturn(ciudad);
        Mockito.when(ciudadService.update(Mockito.any())).thenReturn(ciudad);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        put("/cities/update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ciudad)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteCity()throws Exception{
        Ciudad ciudad = createValidCiudad();
        Mockito.when(ciudadService.find(1L)).thenReturn(ciudad);

        mockMvc.perform(delete("/cities/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Ciudad eliminada con id: 1"));

    }
}
