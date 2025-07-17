package CarbookApp.repositoryTests;

import CarbookApp.entity.Ciudad;
import CarbookApp.repository.CiudadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CiudadRepositoryTests {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Test
    void testFindCiudadByPais() {
        Ciudad ciudad1 = new Ciudad("Buenos Aires", "Argentina");
        Ciudad ciudad2 = new Ciudad("Córdoba", "Argentina");
        Ciudad ciudad3 = new Ciudad("Santiago", "Chile");

        ciudadRepository.save(ciudad1);
        ciudadRepository.save(ciudad2);
        ciudadRepository.save(ciudad3);

        List<Ciudad> ciudadesArgentinas = ciudadRepository.findCiudadByPais("Argentina");

        assertThat(ciudadesArgentinas)
                .hasSize(2)
                .extracting(Ciudad::getTitulo)
                .containsExactlyInAnyOrder("Buenos Aires", "Córdoba");

        List<Ciudad> ciudadesChilenas = ciudadRepository.findCiudadByPais("Chile");

        assertThat(ciudadesChilenas)
                .hasSize(1)
                .extracting(Ciudad::getTitulo)
                .containsExactly("Santiago");
    }
}

