package CarbookApp.controller;

import CarbookApp.entity.Categoria;
import CarbookApp.service.CategoriaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    public static final Logger logger = Logger.getLogger(CategoriaController.class);

    @PostMapping("/add")
    public ResponseEntity<Categoria> add(@RequestBody Categoria categoria){

        logger.debug("Agregando categoria...");
        if (categoria == null) {
            logger.error("La categoria es nula");
            return new ResponseEntity("Categoria nula", HttpStatus.BAD_REQUEST);
        }
        Categoria categoria1 = categoriaService.add(categoria);

        if (categoria1 != null) {
            logger.info("Categoria agregada con id: " + categoria1.getId());
            return new ResponseEntity<>(categoria1, HttpStatus.OK);
        } else {
            logger.error("La categoria ya existe o no pudo guardarse");
            return new ResponseEntity("Categoria existente o error al guardar", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list/{borrados}")
    public ResponseEntity<List<Categoria>> categoryList(@PathVariable Boolean borrados) {
        ResponseEntity response = null;

        Set<Categoria> lista = categoriaService.list(borrados);

        logger.debug("Listando categorias...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Categorias listadas");
        } else {
            response = new ResponseEntity("No se encontraron categorias", HttpStatus.NOT_FOUND);
            logger.error("Error al listar categorias");
        }

        return response;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Categoria categoria){
        try{
            logger.debug("Actualizando categoria...");
            Categoria catExist = categoriaService.find(categoria.getId());
            if (catExist == null) {
                logger.error("Categoria con id " + categoria.getId() + " no encontrada");
                return new ResponseEntity("Error al intentar actualizatar, categoria con id " +categoria.getId()+ " no encontrada", HttpStatus.NOT_FOUND);
            }
            logger.info("Categoria actualizada con id: " + categoria.getId());
            return ResponseEntity.ok(categoriaService.update(categoria));
        }catch (
                DataIntegrityViolationException ex) {
            logger.error("Error al actualizar categoria");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede actualizar la categoria porque está relacionado con otros datos");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
    try{
        Categoria categoria = categoriaService.find(id);
        if (categoria == null) {
            logger.error("Categoría con id " + id + " no encontrada");
            return new ResponseEntity("Error al intentar eliminar, categoría con id " +id+ " no encontrada", HttpStatus.NOT_FOUND);
        }
        categoriaService.delete(id);
        return ResponseEntity.ok("Categoria eliminada con id: "+id);
    } catch (
    DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar el producto porque está relacionado con otros datos");
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
    }

    @DeleteMapping("/strongDelete/{id}")
    public ResponseEntity<String> strongDelete(@PathVariable Long id) throws Exception {
        Categoria categoria = categoriaService.find(id);
        if (categoria == null) {
            logger.error("Categoría con id " + id + " no encontrada");
            return new ResponseEntity("Error al intentar eliminar, categoría con id " +id+ " no encontrada", HttpStatus.NOT_FOUND);
        }
        categoriaService.hardDelete(id);
        return ResponseEntity.ok("Categoria eliminada con id: "+id);
    }

}
