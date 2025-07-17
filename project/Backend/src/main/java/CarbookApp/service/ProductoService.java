package CarbookApp.service;

import CarbookApp.entity.*;
import CarbookApp.repository.ProductoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    public static final Logger logger = Logger.getLogger(ProductoService.class);

    public Producto add(Producto p) {
        if (p.getTitulo() == null ||
                p.getDescripcion() == null ||
                p.getUbicacion() == null ||
                p.getRating() <= 0 || p.getRating() > 5 ||
                p.getPrecio() <= 0 ||
                p.getCiudad() == null ||
                p.getCategoria() == null ||
                p.getCaracteristicas() == null ||
                p.getPoliticas() == null ||
                p.getImagenes() == null || p.getImagenes().isEmpty()) {
            return null;
        }
        List<Producto> productos = productoRepository.findAll();
        for (Producto producto : productos) {
            if (p.getTitulo().equals(producto.getTitulo()) ||
                    p.getUbicacion().equals(producto.getUbicacion())) {
                return null;
            }
        }
        return productoRepository.save(p);
    }


    public List<Producto> list() {
        return productoRepository.findAll();
    }


    public Set<Producto> cityFilter(Long id){
        return new HashSet<>(productoRepository.findByCiudadId(id));
    }

    public Set<Producto> categoryFilter(Long id){
        return new HashSet<>(productoRepository.findByCategoriaId(id));
    }



    public Producto update(Producto p) {
        if (p == null || p.getId() == null) {
            throw new IllegalArgumentException("Producto or ID cannot be null");
        }
        Producto producto  = productoRepository.findById(p.getId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + p.getId()));

        Optional.ofNullable(p.getTitulo()).ifPresent(producto::setTitulo);
        Optional.ofNullable(p.getDescripcion()).ifPresent(producto::setDescripcion);
        Optional.ofNullable(p.getUbicacion()).ifPresent(producto::setUbicacion);
        Optional.ofNullable(p.getCoordenadas()).ifPresent(producto::setCoordenadas);
        Optional.of(p.getRating()).ifPresent(producto::setRating);
        Optional.ofNullable(p.getDisponibilidad()).ifPresent(producto::setDisponibilidad);
        Optional.of(p.getPrecio()).ifPresent(producto::setPrecio);
        Optional.ofNullable(p.getCaracteristicas()).ifPresent(producto::setCaracteristicas);
        Optional.ofNullable(p.getCategoria()).ifPresent(producto::setCategoria);
        Optional.ofNullable(p.getCiudad()).ifPresent(producto::setCiudad);
        Optional.ofNullable(p.getPoliticas()).ifPresent(producto::setPoliticas);
        Optional.ofNullable(p.getImagenes()).ifPresent(producto::setImagenes);


        return productoRepository.save(producto);

    }

    public String delete(Long id) throws Exception {
        logger.debug("Eliminando producto con id: "+ id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con id " + id + " no encontrado"));

        productoRepository.deleteById(id);
        logger.info("Producto eliminado con id: "+ id);
        return "Producto eliminado con id: " + id;
    }

    public Producto find(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> dateFilter(LocalDate inicio, LocalDate fin) {
        return productoRepository.findProductosByDate(inicio, fin);
    }

    public List<Producto> dateAndCityFilter(LocalDate inicio, LocalDate fin, Long id_ciudad) {
        return productoRepository.findProductosByDateAndCity(inicio, fin, id_ciudad);
    }

}
