package Grupo7.Autitos.serviceTests;

import Grupo7.Autitos.entity.*;
import Grupo7.Autitos.repository.UsuarioRepository;
import Grupo7.Autitos.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicioTests {
    @Spy
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Usuario createUsuarioValido() {
        Rol userRole = new Rol(TipoRol.ROLE_USER);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Nombre");
        usuario.setApellido("Apellido");
        usuario.setContrasenia("ContraseniaEncoded");
        usuario.setCiudad("Ciudad");
        usuario.setEmail("email_example@gmail.com");
        usuario.setRol(userRole);
        return usuario;
    }
    @Test
    void testAddUsuarioValido() {
        Usuario usuario = createUsuarioValido();
        when(usuarioRepository.findAll()).thenReturn(List.of());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.add(usuario);
        assertNotNull(result);
        assertEquals("email_example@gmail.com", result.getEmail());
        verify(usuarioRepository).save(usuario);
    }
    @Test
    void testAddUsuarioNull() {
        Usuario usuarioNull = new Usuario(); // Missing fields
        Usuario result = usuarioService.add(usuarioNull);
        assertNull(result);
    }

    @Test
    void testAddUsuarioDuplicado() {
        Usuario usuarioNuevo = createUsuarioValido();
        Usuario existing = createUsuarioValido();
        List<Usuario> existingList = List.of(existing);

        when(usuarioRepository.findAll()).thenReturn(existingList);

        Usuario result = usuarioService.add(usuarioNuevo);
        assertNull(result);
        verify(usuarioRepository,never()).save(any());
    }
}
