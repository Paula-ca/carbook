package Grupo7.Autitos.service;


import Grupo7.Autitos.entity.Caracteristica;
import Grupo7.Autitos.repository.CaracteristicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CaracteristicaService {

    @Autowired
    CaracteristicaRepository caracteristicaRepository;

    public static final Logger logger = Logger.getLogger(CaracteristicaService.class);

    public Caracteristica add(Caracteristica c){
        if(c.getTitulo() == null
        ) {
            return null;
        }
        List<Caracteristica> caracteristicas = caracteristicaRepository.findAll();

        for (Caracteristica caracteristica : caracteristicas) {
            if (c.getTitulo().equals(caracteristica.getTitulo()) && c.getIcono().equals(caracteristica.getIcono())) {
                return null;
            }
        }
        return caracteristicaRepository.save(c);
    }

    public Set<Caracteristica> list() {
            return new HashSet<>(caracteristicaRepository.findAll());

    }

    public Caracteristica update(Caracteristica c) {
        if(c==null || c.getId()==null){
            throw new IllegalArgumentException("Caracteristica or ID cannot be null");
        }
        Caracteristica caracteristica = caracteristicaRepository.findById(c.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Caracteristica not found with ID: " + c.getId()));

        if(c.getIcono()!= null) {
            caracteristica.setIcono(c.getIcono());
        }
        if(c.getTitulo() != null){
            caracteristica.setTitulo(c.getTitulo());
        }

        caracteristicaRepository.save(caracteristica);
        return caracteristica;
    }



    public String delete(Long id) throws Exception {
        logger.debug("Eliminando caracteristica...");
        Caracteristica c = caracteristicaRepository.findById(id)
                .orElseThrow(() -> new Exception("Caracteristica con id " + id + " no encontrada"));
        if(c != null){
            logger.info("Caracteristica eliminada con id: " + id);
            caracteristicaRepository.deleteById(id);
            return "Caracteristica eliminada con id: " + id;
        } else {
            logger.error("Caracteristica con id " + id + " no encontrada");
            throw new Exception("Caracteristica con id " + id + " no encontrada");
        }
    }

    public Caracteristica find(Long id){
        return caracteristicaRepository.findById(id).orElse(null);
    }

}
