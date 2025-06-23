package Grupo7.Autitos.repositoryTests;

import Grupo7.Autitos.entity.Usuario;
import Grupo7.Autitos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindByEmailAndExistsByEmail() {
        Usuario user = new Usuario( "Juan","Perez","juan@example.com","contraseniaJuan","Mendoza");
        usuarioRepository.save(user);

        Usuario encontrado = usuarioRepository.findByEmail("juan@example.com");
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNombre()).isEqualTo("Juan");

        Boolean existe = usuarioRepository.existsByEmail("juan@example.com");
        Boolean noExiste = usuarioRepository.existsByEmail("maria@example.com");

        assertThat(existe).isTrue();
        assertThat(noExiste).isFalse();
    }
}

