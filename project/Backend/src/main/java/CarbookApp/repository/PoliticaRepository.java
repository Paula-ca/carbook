package CarbookApp.repository;

import CarbookApp.entity.Politica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PoliticaRepository extends JpaRepository<Politica, Long> {

    List<Politica> findPoliticaByTitulo(String titulo);

}
