package Grupo7.Autitos.repositoryTests;
import static org.assertj.core.api.Assertions.assertThat;

import Grupo7.Autitos.entity.*;
import Grupo7.Autitos.repository.CategoriaRepository;
import Grupo7.Autitos.repository.CiudadRepository;
import Grupo7.Autitos.repository.ProductoRepository;
import Grupo7.Autitos.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductoRepositoryTests {

    private static final Logger logger = LoggerFactory.getLogger(ProductoRepositoryTests.class);

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    @Test
    void testFindProductById() {
        List<Caracteristica> caracList = List.of(
                new Caracteristica("Motor electrico", "icono_motor_electrico"),
                new Caracteristica("A/C", "icono_a/c")
        );

        Ciudad ciudad = ciudadRepository.save(new Ciudad("Buenos Aires", "Argentina"));
        Categoria categoria = categoriaRepository.save(new Categoria("Deportivo", "deportivo_descripcion", "url_imagen", null));

        List<Imagen> imagenes = List.of(
                new Imagen("imagen_1", "imagen_1"),
                new Imagen("imagen_2", "imagen_2")
        );

        List<Politica> politicas = List.of(
                new Politica("Normas de seguridad", "normas_seguridad"),
                new Politica("Politica de cancelacion", "cancelacion")
        );

        Producto product = new Producto("Fiat Cronos", "", true, 1500, "Avenida Nueva", null,
                caracList, ciudad, categoria, imagenes, null, politicas, 3);

        productoRepository.save(product);

        Optional<Producto> foundProduct = productoRepository.findById(product.getId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getTitulo()).isEqualTo("Fiat Cronos");
        assertThat(foundProduct.get().getImagenes()).hasSize(2);
        assertThat(foundProduct.get().getPoliticas()).hasSize(2);
    }
    @Test
    void testFindByCiudadId() {
        Ciudad ciudad = ciudadRepository.save(new Ciudad("Rosario", "Argentina"));
        Categoria categoria = categoriaRepository.save(new Categoria("SUV", "SUV descripción", "url_suv", null));

        Producto producto1 = new Producto("Toyota RAV4", "", true, 2000, "Calle Uno", null,
                List.of(), ciudad, categoria, List.of(), null, List.of(), 3);
        Producto producto2 = new Producto("Honda HR-V", "", true, 1800, "Calle Dos", null,
                List.of(), ciudad, categoria, List.of(), null, List.of(), 2);

        productoRepository.saveAll(List.of(producto1, producto2));

        List<Producto> productosEncontrados = productoRepository.findByCiudadId(ciudad.getId());

        assertThat(productosEncontrados).hasSize(2);
        assertThat(productosEncontrados).extracting(Producto::getTitulo)
                .containsExactlyInAnyOrder("Toyota RAV4", "Honda HR-V");
    }
    @Test
    void testFindByCategoriaId() {
        Ciudad ciudad = ciudadRepository.save(new Ciudad("Córdoba", "Argentina"));
        Categoria categoria = categoriaRepository.save(new Categoria("Sedán", "sedán descripción", "url_sedan", null));

        Producto producto1 = new Producto("Volkswagen Vento", "", true, 1700, "Boulevard Este", null,
                List.of(), ciudad, categoria, List.of(), null, List.of(), 4);
        Producto producto2 = new Producto("Renault Logan", "", true, 1600, "Calle Sur", null,
                List.of(), ciudad, categoria, List.of(), null, List.of(), 2);

        productoRepository.saveAll(List.of(producto1, producto2));

        List<Producto> productosPorCategoria = productoRepository.findByCategoriaId(categoria.getId());

        assertThat(productosPorCategoria).hasSize(2);
        assertThat(productosPorCategoria)
                .extracting(Producto::getTitulo)
                .containsExactlyInAnyOrder("Volkswagen Vento", "Renault Logan");
    }
    @Test
    void testFindProductosByDateCity() {
        Ciudad ciudad = ciudadRepository.save(new Ciudad("Mendoza", "Argentina"));
        Ciudad ciudad1 = ciudadRepository.save(new Ciudad("Buenos Aires", "Argentina"));
        Categoria categoria = categoriaRepository.save(new Categoria("Camioneta", "camioneta descripción", "url_camioneta", null));

        Producto producto1 = new Producto("Ford Ranger", "", true, 2200, "Ruta 40", null,
                List.of(), ciudad, categoria, List.of(), null, List.of(), 4);
        producto1 = productoRepository.save(producto1);

        Producto producto2 = new Producto("Chevrolet S10", "", true, 2300, "Ruta 60", null,
                List.of(), ciudad1, categoria, List.of(), null, List.of(), 4);
        producto2 = productoRepository.save(producto2);


        Reserva reserva = new Reserva(LocalTime.of(12,00,00,00),LocalDate.of(2025, 6, 20), LocalDate.of(2025, 6, 25),null,300000,"Confirmada","Aproved", null);
        reserva.setProducto(producto1);

        reservaRepository.save(reserva);

        LocalDate searchStart = LocalDate.of(2025, 6, 22);
        LocalDate searchEnd = LocalDate.of(2025, 6, 28);

        List<Producto> disponiblesByDate = productoRepository.findProductosByDate(searchStart, searchEnd);
        assertThat(disponiblesByDate).contains(producto2);
        assertThat(disponiblesByDate).doesNotContain(producto1);

        List<Producto> disponiblesByDatecity = productoRepository.findProductosByDateAndCity(searchStart, searchEnd,ciudad1.getId());
        assertThat(disponiblesByDatecity).contains(producto2);
        assertThat(disponiblesByDatecity).doesNotContain(producto1);
    }
}

