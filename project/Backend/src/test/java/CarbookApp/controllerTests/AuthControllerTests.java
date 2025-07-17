package CarbookApp.controllerTests;

import CarbookApp.controller.AuthController;
import CarbookApp.entity.Rol;
import CarbookApp.entity.TipoRol;
import CarbookApp.entity.Usuario;
import CarbookApp.security.AppUserService;
import CarbookApp.security.Jwt.JwtEntryPoint;
import CarbookApp.security.Jwt.JwtProvider;
import CarbookApp.security.Payload.LoginRequest;
import CarbookApp.security.Payload.RegisterRequest;
import CarbookApp.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private JwtEntryPoint jwtEntryPoint;
    @MockBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEmail("juan@example.com");
        request.setContrasenia("password123");

        when(usuarioService.existsUserByEmail("juan@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        Usuario usuario = new Usuario("Juan", "Perez", "juan@example.com", "encodedPassword", "");
        usuario.setRol(new Rol(TipoRol.ROLE_USER));

        when(usuarioService.add(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void register_emailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEmail("juan@example.com");
        request.setContrasenia("password123");

        when(usuarioService.existsUserByEmail("juan@example.com")).thenReturn(true);

        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Este correo ya se encuentra registrado"));
    }

    @Test
    void login_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("juan@example.com");
        loginRequest.setContrasenia("password123");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtProvider.generateToken(authentication)).thenReturn("jwt-token");

        Usuario usuario = new Usuario("Juan", "Perez", "juan@example.com", "encoded", "Ciudad");
        usuario.setId(1L);
        usuario.setRol(new Rol(TipoRol.ROLE_USER));

        when(usuarioService.userByEmail("juan@example.com")).thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/auth/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"))
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
