package Grupo7.Autitos.service;


import Grupo7.Autitos.entity.Politica;
import Grupo7.Autitos.repository.PoliticaRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PoliticaService {

    @Autowired
    PoliticaRepository politicaRepository;

    public static final Logger logger = Logger.getLogger(PoliticaService.class);

    public Politica add(Politica p){

        if(p.getTitulo() == null || p.getDescripcion() == null
        ) {
            return null;
        } List<Politica> politicas = politicaRepository.findAll();
        for (Politica politica : politicas) {
            if (p.getTitulo().equals(politica.getTitulo()) && p.getDescripcion().equals(politica.getDescripcion())) {
                return null;
            }
        }
        return politicaRepository.save(p);
    }

    public Set<Politica> list() {
        return new HashSet<>(politicaRepository.findAll());
    }

    public Politica update(Politica p) {
        if(p==null || p.getId()==null){
            throw new IllegalArgumentException("Politica or ID cannot be null");
        }
        Politica existingPolitica = politicaRepository.findById(p.getId())
                .orElseThrow(() -> new EntityNotFoundException("Politica not found with ID: " + p.getId()));

        Optional.ofNullable(p.getTitulo()).ifPresent(existingPolitica::setTitulo);
        Optional.ofNullable(p.getDescripcion()).ifPresent(existingPolitica::setDescripcion);

        return politicaRepository.save(existingPolitica);
    }

    public String delete(Long id) throws Exception {
        logger.debug("Eliminando politica con id: "+id);
        Politica politica = politicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Politica con id " + id + " no encontrada"));
        politicaRepository.deleteById(id);
        logger.info("Politica eliminada con id: "+ id);
        return "Politica eliminada con id: " + id;
    }

    public Politica find(Long id){
        return politicaRepository.findById(id).orElse(null);
    }

    public List<Politica> findByTitulo(String titulo){
        return politicaRepository.findPoliticaByTitulo(titulo);
    }

}
