package Grupo7.Autitos.service;

import Grupo7.Autitos.entity.*;
import Grupo7.Autitos.repository.ProductoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Producto producto  = this.find(p.getId());
        if(p.getTitulo() != null) {
            producto.setTitulo(p.getTitulo());
        }
        if(p.getDescripcion() != null){
            producto.setDescripcion(p.getDescripcion());
        }
        if(p.getUbicacion() != null){
            producto.setUbicacion(p.getUbicacion());
        }
        if(p.getCoordenadas() != null){
            producto.setCoordenadas(p.getCoordenadas());
        }
        if(p.getRating() > 0 && p.getRating() <= 5){
            producto.setRating(p.getRating());
        }
        if(p.getDisponibilidad() != null){
            producto.setDisponibilidad(p.getDisponibilidad());
        }
        if(p.getPrecio()>0){
            producto.setPrecio(p.getPrecio());
        }
        if(p.getCaracteristicas() != null){
            producto.setCaracteristicas(p.getCaracteristicas());
        }
        if(p.getCategoria() != null){
            producto.setCategoria(p.getCategoria());
        }
        if(p.getCiudad() != null){
            producto.setCiudad(p.getCiudad());
        }
        if(p.getPoliticas() != null){
            producto.setPoliticas(p.getPoliticas());
        }
        if(p.getImagenes() != null){
            producto.setImagenes(p.getImagenes());
        }

        productoRepository.save(producto);
        return producto;
    }

    public String delete(Long id) throws Exception {
        logger.debug("Eliminando producto...");
        Producto p = this.find(id);
        if(p != null){
            logger.info("Producto eliminado con id: " + id);
            productoRepository.deleteById(id);
            return "Producto eliminado con id: " + id;
        } else {
            logger.error("Producto con id " + id + " no encontrado");
            throw new Exception("Producto con id " + id + " no encontrado");
        }
    }

    public Producto find(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> dateFilter(LocalDate inicio, LocalDate fin) {
        List<Producto> lista = productoRepository.findProductosByDate(inicio, fin);
        List<Producto> listaNueva = new ArrayList<>();

        for (Producto producto : lista) {
            boolean agregable = true;

            for (Reserva r : producto.getReservas()) {
                boolean reservaActiva = r.getBorrado() == null;

                // Condición de solapamiento: si hay superposición de rangos
                boolean haySolapamiento = !(r.getFecha_final().isBefore(inicio) || r.getFecha_ingreso().isAfter(fin));

                if (reservaActiva && haySolapamiento) {
                    agregable = false;
                    break; // Ya sabemos que no se puede agregar, salimos del loop
                }
            }

            if (agregable) {
                listaNueva.add(producto);
            }
        }

        return listaNueva;
    }


    public List<Producto> dateAndCityFilter(LocalDate inicio, LocalDate fin, Long id_ciudad) {
        List<Producto> lista = productoRepository.findProductosByDateAndCity(inicio, fin, id_ciudad);
        List<Producto> listaNueva = new ArrayList<>();

        for (Producto producto : lista) {
            boolean agregable = true;

            for (Reserva r : producto.getReservas()) {
                boolean reservaActiva = (r.getBorrado() == null);

                boolean fechaIngresoEnRango =
                        r.getFecha_ingreso().isEqual(inicio) ||
                                r.getFecha_ingreso().isEqual(fin) ||
                                (r.getFecha_ingreso().isAfter(inicio) && r.getFecha_ingreso().isBefore(fin));

                boolean fechaFinalEnRango =
                        r.getFecha_final().isEqual(inicio) ||
                                r.getFecha_final().isEqual(fin) ||
                                (r.getFecha_final().isAfter(inicio) && r.getFecha_final().isBefore(fin));

                if (reservaActiva && !(r.getFecha_final().isBefore(inicio) || r.getFecha_ingreso().isAfter(fin))) {
                    agregable = false;
                    break;
                }
            }

            if (agregable) {
                listaNueva.add(producto);
            }
        }

        return listaNueva;
    }


}
