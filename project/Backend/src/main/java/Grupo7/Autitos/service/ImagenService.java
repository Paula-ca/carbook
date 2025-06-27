package Grupo7.Autitos.service;

import Grupo7.Autitos.entity.Imagen;
import Grupo7.Autitos.repository.ImagenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ImagenService {

    @Autowired
    ImagenRepository imagenRepository;

    public static final Logger logger = Logger.getLogger(ImagenService.class);

    public void add(Imagen i){
        imagenRepository.save(i);
    }

    public Set<Imagen> list() {
        return new HashSet<>(imagenRepository.findAll());
    }

    public Imagen update(Imagen i) {
        if(i==null || i.getId()==null){
            throw new IllegalArgumentException("Imagen or ID cannot be null");
        }
        Imagen imagen  = imagenRepository.findById(i.getId()).orElseThrow(()-> new EntityNotFoundException("Imagen not found with ID: " + i.getId()));
        Optional.ofNullable(i.getTitulo()).ifPresent(imagen::setTitulo);
        Optional.ofNullable(i.getUrl()).ifPresent(imagen::setUrl);

        return imagenRepository.save(imagen);
    }

    public String delete(Long id) throws Exception {
        logger.debug("Eliminando imagen con id: "+id);
        Imagen i = imagenRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Imagen con id " + id + " no encontrada"));
        imagenRepository.deleteById(id);
        logger.info("Imagen eliminada con id: "+ id);
        return "Imagen eliminada con id: " + id;
    }

    public Imagen find(Long id){
        return imagenRepository.findById(id).orElse(null);
    }
}
