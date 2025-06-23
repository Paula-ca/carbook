package Grupo7.Autitos.repositoryTests;

import Grupo7.Autitos.entity.Categoria;
import Grupo7.Autitos.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoriaTests {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testFindAllCategoriesWithBorradoNull() {
        Categoria activeCategoria = new Categoria("SUV", "Descripción", "url_suv", null);
        Categoria deletedCategoria = new Categoria("Deportivo", "Descripción", "url_deportivo", LocalDate.of(2025, 6, 22));

        categoriaRepository.save(activeCategoria);
        categoriaRepository.save(deletedCategoria);

        HashSet<Categoria> result = categoriaRepository.findAllCategories();

        assertThat(result).contains(activeCategoria);
        assertThat(result).doesNotContain(deletedCategoria);
    }
}
