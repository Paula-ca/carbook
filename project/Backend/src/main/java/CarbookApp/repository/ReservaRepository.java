package CarbookApp.repository;

import CarbookApp.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NOT NULL AND r.usuario.id = ?1")
    List<Reserva> findCancelBookingsForUser(Long id);

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NULL AND r.usuario.id = ?1")
    List<Reserva> findBookingsForUser(Long id);

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NOT NULL AND r.producto.id = ?1")
    List<Reserva> findCancelBookingsForProduct(Long id);

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NULL AND r.producto.id = ?1")
    List<Reserva> findBookingsForProduct(Long id);

    List<Reserva> findByProductoId(Long producto);

}