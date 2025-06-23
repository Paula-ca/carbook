package Grupo7.Autitos.repository;

import Grupo7.Autitos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCiudadId(Long ciudad);

    List<Producto> findByCategoriaId(Long categoria);

    @Query(value = "SELECT p.id,p.coordenadas,p.descripcion,p.disponibilidad,p.rating,p.titulo,p.ubicacion,p.id_categoria,p.id_ciudad,p.precio "+
            " FROM productos p "+
            " WHERE NOT EXISTS ( " +
            " SELECT 1 FROM reservas r "+
                    " WHERE r.id_producto = p.id" +
                    " AND r.fecha_inicial <= ?2 "+
                    " AND r.fecha_final >= ?1" +
                    ")",
            nativeQuery = true)
    List<Producto> findProductosByDate(LocalDate fecha_inicial, LocalDate fecha_final);

    @Query(value = "SELECT p.id,p.coordenadas,p.descripcion,p.disponibilidad,p.rating,p.titulo,p.ubicacion,p.id_categoria,p.id_ciudad,p.precio "+
            " FROM productos p "+
            " WHERE p.id_ciudad = ?3 " +
            " AND NOT EXISTS ( " +
            " SELECT 1 FROM reservas r "+
            " WHERE r.id_producto = p.id" +
            " AND r.fecha_inicial <= ?2 "+
            " AND r.fecha_final >= ?1" +
            ")",
            nativeQuery = true)
    List<Producto> findProductosByDateAndCity(LocalDate fecha_inicial, LocalDate fecha_final, Long id_ciudad);


}

