package CarbookApp.controllerTests;

import CarbookApp.controller.ReservaController;
import CarbookApp.dto.ProductoDTO;
import CarbookApp.dto.ReservaDTO;
import CarbookApp.dto.UsuarioDTO;
import CarbookApp.entity.*;
import CarbookApp.security.AppUserService;
import CarbookApp.security.Jwt.JwtEntryPoint;
import CarbookApp.security.Jwt.JwtProvider;
import CarbookApp.service.ReservaService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(ReservaController.class)
@ActiveProfiles("test")
public class ReservaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private ReservaDTO createValidReservationDTO() {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(1L);
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);

        ReservaDTO reserva = new ReservaDTO();
        reserva.setFechaIngreso(LocalDate.of(2025, 07, 01));
        reserva.setFechaFinal(LocalDate.of(2025, 07, 05));
        reserva.setEstado("Confirmada");
        reserva.setProducto(producto);
        reserva.setUsuario(usuario);

        return reserva;
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfReservationsByProduct()throws Exception{
        List<ReservaDTO> reservasxproducto = List.of(createValidReservationDTO());
        Mockito.when(reservaService.filterByProductId(1L,false)).thenReturn(reservasxproducto);

        mockMvc.perform(get("/bookings/product/1/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fechaIngreso").value("2025-07-01"))
                .andExpect(jsonPath("$[0].fechaFinal").value("2025-07-0" +
                        "5"))
                .andExpect(jsonPath("$[0].estado").value("Confirmada"));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfReservationsByUser()throws Exception{
        List<ReservaDTO> reservasxusuario = List.of(createValidReservationDTO());
        Mockito.when(reservaService.filterByUserId(1L,false)).thenReturn(reservasxusuario);

        mockMvc.perform(get("/bookings/user/1/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fechaIngreso").value("2025-07-01"))
                .andExpect(jsonPath("$[0].fechaFinal").value("2025-07-05"))
                .andExpect(jsonPath("$[0].estado").value("Confirmada"));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnReservationById()throws Exception{
        ReservaDTO reserva = createValidReservationDTO();
        Mockito.when(reservaService.findDtoById(1L)).thenReturn(reserva);

        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaIngreso").value("2025-07-01"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewReservation()throws Exception{
        ReservaDTO reserva = createValidReservationDTO();
        Mockito.when(reservaService.add(Mockito.any(Reserva.class))).thenReturn(reserva);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        post("/bookings/add")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reserva)))
                                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateReservation()throws Exception {
        ReservaDTO reserva = createValidReservationDTO();
        Mockito.when(reservaService.findDtoById(reserva.getId())).thenReturn(reserva);
        Mockito.when(reservaService.update(Mockito.any())).thenReturn(reserva);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(
                        put("/bookings/update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteReservation()throws Exception{
        ReservaDTO reserva = createValidReservationDTO();
        Mockito.when(reservaService.findDtoById(1L)).thenReturn(reserva);

        mockMvc.perform(delete("/bookings/cancel/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva cancelada con id: 1"));

    }
}
