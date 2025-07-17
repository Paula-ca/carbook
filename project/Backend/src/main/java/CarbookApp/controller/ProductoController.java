package CarbookApp.controller;

import CarbookApp.entity.Imagen;
import CarbookApp.entity.Producto;
import CarbookApp.entity.Reserva;
import CarbookApp.service.ImagenService;
import CarbookApp.service.ProductoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @Autowired
    ImagenService imagenService;

    public static final Logger logger = Logger.getLogger(ProductoController.class);

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Producto> add(@RequestBody Producto producto){
        logger.debug("Agregando producto...");

        if (producto == null) {
            logger.error("El producto es nulo");
            return new ResponseEntity("Producto nulo", HttpStatus.BAD_REQUEST);
        }

        Producto productoGuardado = productoService.add(producto);
        if (productoGuardado != null) {
            for (Imagen i : producto.getImagenes()) {
                imagenService.add(i);
            }

            logger.info("Producto agregado con id: " + productoGuardado.getId());
            return new ResponseEntity<>(productoGuardado, HttpStatus.OK);
        } else {
            logger.error("El producto ya existe o no pudo guardarse");
            return new ResponseEntity("Producto existente o error al guardar", HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/random-list")
    public ResponseEntity<List<Producto>> randomList() {
        ResponseEntity response = null;

        Set<Producto> lista = new HashSet<>(productoService.list());

        logger.debug("Listando productos...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Productos listados");
        } else {
            response = new ResponseEntity("No se encontraron productos", HttpStatus.NOT_FOUND);
            logger.error("Error al listar productos");
        }

        return response;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Producto>> productList() {
        ResponseEntity response = null;

        List<Producto> lista = productoService.list();

        logger.debug("Listando productos...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Productos listados");
        } else {
            response = new ResponseEntity("No se encontraron productos", HttpStatus.NOT_FOUND);
            logger.error("Error al listar productos");
        }

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById (@PathVariable Long id){
        ResponseEntity response = null;

        logger.debug("Buscando producto...");
        if(productoService.find(id) != null){
            response = new ResponseEntity(productoService.find(id),HttpStatus.OK);
            logger.info("Producto encontrado con id "+id );
        }else{
            response = new ResponseEntity("No se encontró el producto con id "+id, HttpStatus.NOT_FOUND);
            logger.error("No se encontró el producto con id "+id);
        }
        return response;
    }

    @GetMapping("/list/city-{city_id}")
    public ResponseEntity<List<Producto>> cityFilter(@PathVariable Long city_id) {
        ResponseEntity response = null;

        Set<Producto> lista = productoService.cityFilter(city_id);

        logger.debug("Listando productos...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Productos filtrados por ciudad");
        } else {
            response = new ResponseEntity("No se encontraron productos de la ciudad con id " + city_id, HttpStatus.NOT_FOUND);
            logger.error("Error al filtrar productos");
        }

        return response;
    }

    @GetMapping("/list/category-{category_id}")
    public ResponseEntity<List<Producto>> categoryFilter(@PathVariable Long category_id) {
        ResponseEntity response = null;

        Set<Producto> lista = productoService.categoryFilter(category_id);

        logger.debug("Listando productos...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Productos filtrados por categoria");
        } else {
            response = new ResponseEntity("No se encontraron productos de la categoria con id " + category_id, HttpStatus.NOT_FOUND);
            logger.error("Error al filtrar productos");
        }

        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Producto producto){
        try{
            logger.debug("Actualizando producto...");
            Producto prodExist = productoService.find(producto.getId());
            if (prodExist == null) {
                logger.error("Producto con id " + producto.getId() + " no encontrado");
                return new ResponseEntity("Error al intentar actualizatar, producto con id " +producto.getId()+ " no encontrado", HttpStatus.NOT_FOUND);
            }
            logger.info("Producto actualizado con id: " + producto.getId());
            return ResponseEntity.ok(productoService.update(producto));
        }catch (
                DataIntegrityViolationException ex) {
            logger.error("Error al actualizar producto");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede actualizar el producto porque está relacionado con otros datos");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        try{
        logger.debug("Eliminando producto...");
        Producto producto = productoService.find(id);
        if (producto == null) {
            logger.error("Producto con id " + id + " no encontrado");
            return new ResponseEntity("Error al intentar eliminar, producto con id " +id+ " no encontrado", HttpStatus.NOT_FOUND);
        }

        // Eliminar imágenes asociadas
        for (Imagen imagen : producto.getImagenes()) {
            imagenService.delete(imagen.getId());
        }

        // Eliminar el producto
        productoService.delete(id);
        logger.info("Producto eliminado con id: " + id);

        return ResponseEntity.ok("Producto eliminado con id: " + id);
    } catch (
    DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar la ciudad porque está relacionado con otros datos");
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
    }


    @GetMapping("/date-filter")
    public ResponseEntity<List<Reserva>> dateFilter(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
                                                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fin,
                                                                      @RequestParam(required = false) Long ciudad) {
        ResponseEntity response = null;

        logger.debug("Listando reservas segun fecha y/o ciudad");

        try {
            if (ciudad != null) {
                logger.info("Lista de reservas por fecha y ciudad");
                response = new ResponseEntity<>(productoService.dateAndCityFilter(inicio, fin, ciudad), HttpStatus.OK);
            }
            if (ciudad == null) {
                logger.info("Lista de reservas por fecha");
                response = new ResponseEntity<>(productoService.dateFilter(inicio, fin), HttpStatus.OK);
            }
        } catch (Exception e){
            response = new ResponseEntity("Error al aplicar filtro por fecha y/o ciudad", HttpStatus.NOT_FOUND);
            logger.error("Error al aplicar filtro por fecha y/o ciudad", e);
        }

        return response;
    }

}
