package CarbookApp.controllerTests;

import CarbookApp.controller.ReservaController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(ReservaController.class)
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

    private Reserva createValidReservation() {
        Producto producto = new Producto();
        producto.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Reserva reserva = new Reserva();
        reserva.setFecha_ingreso(LocalDate.of(2025, 07, 01));
        reserva.setFecha_final(LocalDate.of(2025, 07, 05));
        reserva.setPrecio(1000);
        reserva.setHora_comienzo(LocalTime.of(12,00,00));
        reserva.setProducto(producto);
        reserva.setUsuario(usuario);

        return reserva;
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfReservationsByProduct()throws Exception{
        List<Reserva> reservasxproducto = List.of(createValidReservation());
        Mockito.when(reservaService.filterByProductId(1L,false)).thenReturn(reservasxproducto);

        mockMvc.perform(get("/bookings/product/1/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fecha_ingreso").value("2025-07-01"))
                .andExpect(jsonPath("$[0].fecha_final").value("2025-07-0" +
                        "5"))
                .andExpect(jsonPath("$[0].precio").value(1000));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnListOfReservationsByUser()throws Exception{
        List<Reserva> reservasxusuario = List.of(createValidReservation());
        Mockito.when(reservaService.filterByUserId(1L,false)).thenReturn(reservasxusuario);

        mockMvc.perform(get("/bookings/user/1/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fecha_ingreso").value("2025-07-01"))
                .andExpect(jsonPath("$[0].fecha_final").value("2025-07-05"))
                .andExpect(jsonPath("$[0].precio").value(1000));
    }
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnReservationById()throws Exception{
        Reserva reserva = createValidReservation();
        Mockito.when(reservaService.find(1L)).thenReturn(reserva);

        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fecha_ingreso").value("2025-07-01"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAddNewReservation()throws Exception{
        Reserva reserva = createValidReservation();
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
        Reserva reserva = createValidReservation();
        Mockito.when(reservaService.find(reserva.getId())).thenReturn(reserva);
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
        Reserva reserva = createValidReservation();
        Mockito.when(reservaService.find(1L)).thenReturn(reserva);

        mockMvc.perform(delete("/bookings/cancel/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva cancelada con id: 1"));

    }
}
