package CarbookApp.repository;

import CarbookApp.dto.ReservaDTO;
import CarbookApp.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NOT NULL AND r.usuario.id = ?1")
    List<ReservaDTO> findCancelBookingsForUser(Long id);

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NULL AND r.usuario.id = ?1")
    List<ReservaDTO> findBookingsForUser(Long id);

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NOT NULL AND r.producto.id = ?1")
    List<ReservaDTO> findCancelBookingsForProduct(Long id);

    @Query("SELECT r FROM Reserva r WHERE r.borrado IS NULL AND r.producto.id = ?1")
    List<ReservaDTO> findBookingsForProduct(Long id);

    ReservaDTO findDtoById(Long id);

    Optional<Reserva> findById(Long id);


}