package Grupo7.Autitos.service;

import Grupo7.Autitos.controller.CategoriaController;
import Grupo7.Autitos.entity.Categoria;
import Grupo7.Autitos.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    public static final Logger logger = Logger.getLogger(CategoriaController.class);

    public Categoria add(Categoria c){
        if(c.getTitulo() == null
                || c.getDescripcion() == null
                || c.getUrlImagen() == null) {
            return null;
        }
        List<Categoria> categorias = categoriaRepository.findAll();
        for (Categoria categoria : categorias) {
            if (c.getTitulo().equals(categoria.getTitulo()) ||
                    c.getDescripcion().equals(categoria.getDescripcion())) {
                return null;
            }
        }
        return categoriaRepository.save(c);
    }

    public Set<Categoria> list(Boolean borrados) {
        if(borrados){
            return new HashSet<>(categoriaRepository.findAll());
        }
        else return categoriaRepository.findAllCategories();
    }

    public Categoria update(Categoria c) {
        if (c == null || c.getId() == null) {
            throw new IllegalArgumentException("Categoria or ID cannot be null");
        }

        Categoria existingCategoria = categoriaRepository.findById(c.getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria not found with ID: " + c.getId()));

        Optional.ofNullable(c.getTitulo()).ifPresent(existingCategoria::setTitulo);
        Optional.ofNullable(c.getDescripcion()).ifPresent(existingCategoria::setDescripcion);
        Optional.ofNullable(c.getUrlImagen()).ifPresent(existingCategoria::setUrlImagen);

        return categoriaRepository.save(existingCategoria);
    }

    public String delete(Long id) throws Exception {
        logger.debug("Eliminando categoria...");

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new Exception("Categoria con id " + id + " no encontrada"));

        if (Boolean.TRUE.equals(categoria.getBorrado() != null)) {
            return "La categoria id " + id + " fue eliminada anteriormente";
        }

        categoria.setBorrado(LocalDate.now());
        logger.info("Categoria eliminada con id: " + id);
        System.out.println("Categoria id " + categoria.getId() + " Categoria titulo " + categoria.getTitulo());

        this.update(categoria);

        return "Categoria eliminada con id: " + id;
    }

    public String hardDelete(Long id) throws Exception {
        logger.debug("Eliminando categoria con id: "+ id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria con id " + id + " no encontrada"));

        categoriaRepository.deleteById(id);
        logger.info("Categoria eliminada con id: {}"+ id);
        return "Categoria eliminada con id: " + id;
    }

    public Categoria find(Long id){
        return categoriaRepository.findById(id).orElse(null);
    }


}
