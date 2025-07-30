package CarbookApp.repositoryTests;

import CarbookApp.dto.ReservaDTO;
import CarbookApp.entity.*;
import CarbookApp.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ReservaRepositoryTests {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CiudadRepository ciudadRepository;

    @Test
    public void testFindBookingsForUser() {
        Usuario user = new Usuario( "Juan","Perez","juan@gmail.com","contraseniaJuan","Mendoza");
        usuarioRepository.save(user);

        Reserva activa = new Reserva(LocalDate.now(), LocalDate.of(2025, 6, 22), null); // no cancelada
        Reserva cancelada = new Reserva(LocalDate.now(), LocalDate.of(2025, 6, 22), LocalDate.of(2025, 6, 20)); // cancelada

        activa.setUsuario(user);
        cancelada.setUsuario(user);

        reservaRepository.save(activa);
        reservaRepository.save(cancelada);

        List<ReservaDTO> canceladas = reservaRepository.findCancelBookingsForUser(user.getId());

        assertThat(canceladas).hasSize(1);
        assertThat(canceladas.get(0).getBorrado()).isNotNull();
        assertThat(canceladas.get(0).getUsuario().getEmail()).isEqualTo("juan@gmail.com");

        List<ReservaDTO> activas = reservaRepository.findBookingsForUser(user.getId());
        assertThat(activas).hasSize(1);
        assertThat(activas.get(0).getBorrado()).isNull();
        assertThat(activas.get(0).getUsuario().getEmail()).isEqualTo("juan@gmail.com");
    }
    @Test
    public void testFindBookingsForProduct() {

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

        Reserva reservaCancelada = new Reserva(LocalDate.now(), LocalDate.of(2025, 6, 30), LocalDate.of(2025, 6, 23));
        Reserva reservaActiva = new Reserva(LocalDate.now(), LocalDate.of(2025, 6, 30), null);
        reservaCancelada.setProducto(product);
        reservaActiva.setProducto(product);

        reservaRepository.save(reservaCancelada);
        reservaRepository.save(reservaActiva);

        List<ReservaDTO> canceladas = reservaRepository.findCancelBookingsForProduct(product.getId());

        assertThat(canceladas).hasSize(1);
        assertThat(canceladas.get(0).getBorrado()).isNotNull();
        assertThat(canceladas.get(0).getProducto().getId()).isEqualTo(product.getId());

        List<ReservaDTO> activas = reservaRepository.findBookingsForProduct(product.getId());

        assertThat(activas).hasSize(1);
        assertThat(activas.get(0).getBorrado()).isNull();
        assertThat(activas.get(0).getProducto().getId()).isEqualTo(product.getId());
    }
    @Test
    public void testFindById() {
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

        Reserva reservaCancelada = new Reserva(LocalDate.now(), LocalDate.of(2025, 6, 30), LocalDate.of(2025, 6, 23));
        Reserva reservaActiva = new Reserva(LocalDate.now(), LocalDate.of(2025, 6, 30), null);
        reservaCancelada.setProducto(product);
        reservaActiva.setProducto(product);

        reservaRepository.save(reservaCancelada);
        reservaRepository.save(reservaActiva);

        Optional<Reserva> reserva = reservaRepository.findById(reservaActiva.getId());

        assertThat(reserva.get().getBorrado()).isNull();
        assertThat(reserva.get().getProducto().getId()).isEqualTo(product.getId());
    }

    }

