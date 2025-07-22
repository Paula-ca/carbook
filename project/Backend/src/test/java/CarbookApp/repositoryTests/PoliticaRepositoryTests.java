package CarbookApp.repositoryTests;

import CarbookApp.entity.Politica;
import CarbookApp.repository.PoliticaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PoliticaRepositoryTests {

    @Autowired
    private PoliticaRepository politicaRepository;

    @Test
    void testFindPoliticaByTitulo() {
        Politica p1 = new Politica("Política de cancelación", "Texto 1");
        Politica p2 = new Politica("Política de seguridad", "Texto 2");
        Politica p3 = new Politica("Política de cancelación", "Texto 3");

        politicaRepository.save(p1);
        politicaRepository.save(p2);
        politicaRepository.save(p3);

        List<Politica> politicas = politicaRepository.findPoliticaByTitulo("Política de cancelación");

        assertThat(politicas).hasSize(2);
        assertThat(politicas)
                .extracting(Politica::getDescripcion)
                .containsExactlyInAnyOrder("Texto 1", "Texto 3");
    }
}

