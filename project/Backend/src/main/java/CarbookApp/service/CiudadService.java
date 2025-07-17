package CarbookApp.service;


import CarbookApp.entity.Ciudad;
import CarbookApp.repository.CiudadRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CiudadService {

    @Autowired
    CiudadRepository ciudadRepository;

    public static final Logger logger = Logger.getLogger(CiudadService.class);

    public Ciudad add(Ciudad c){
        if(c.getTitulo() == null
                || c.getPais() == null
                ) {
            return null;
        } List<Ciudad> ciudads = ciudadRepository.findAll();
        for (Ciudad ciudad : ciudads) {
            if (c.getTitulo().equals(ciudad.getTitulo())) {
                return null;
            }
        }
        return ciudadRepository.save(c);
    }

    public Set<Ciudad> list() {
        return new HashSet<>(ciudadRepository.findAll());

    }

    public Ciudad update(Ciudad c) {
        if(c==null || c.getId()==null){
            throw new IllegalArgumentException("Ciudad or ID cannot be null");
        }
        Ciudad existingCiudad = ciudadRepository.findById(c.getId())
                .orElseThrow(() -> new EntityNotFoundException("Ciudad not found with ID: " + c.getId()));

        Optional.ofNullable(c.getTitulo()).ifPresent(existingCiudad::setTitulo);
        Optional.ofNullable(c.getPais()).ifPresent(existingCiudad::setPais);

        return ciudadRepository.save(existingCiudad);
    }



    public String delete(Long id) throws Exception {
        logger.debug("Eliminando ciudad con id: "+ id);

        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ciudad con id " + id + " no encontrada"));

        ciudadRepository.deleteById(id);
        logger.info("Ciudad eliminada con id: "+ id);
        return "Ciudad eliminada con id: " + id;
    }

    public Ciudad find(Long id){
        return ciudadRepository.findById(id).orElse(null);
    }

    public List<Ciudad> findByPais(String pais){
        return ciudadRepository.findCiudadByPais(pais);
    }

}
