package Grupo7.Autitos.controller;

import Grupo7.Autitos.entity.Politica;
import Grupo7.Autitos.service.PoliticaService;
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
@RequestMapping("/policies")
public class PoliticaController {

    @Autowired
    PoliticaService politicaService;

    public static final Logger logger = Logger.getLogger(PoliticaController.class);

    @PostMapping("/add")
    public ResponseEntity<Politica> add(@RequestBody Politica politica){
        logger.debug("Agregando politica...");
        if (politica == null) {
            logger.error("La politica es nula");
            return new ResponseEntity("Politica nula", HttpStatus.BAD_REQUEST);
        }

        Politica politica1 = politicaService.add(politica);

        if (politica1 != null) {
            logger.info("Politica agregada con id: " + politica1.getId());
            return new ResponseEntity<>(politica1, HttpStatus.OK);
        } else {
            logger.error("La politica ya existe o no pudo guardarse");
            return new ResponseEntity("Politica existente o error al guardar", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Politica>> categoryList() {
        ResponseEntity response = null;

        Set<Politica> lista = politicaService.list();

        logger.debug("Listando politicas...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Politicas listadas");
        } else {
            response = new ResponseEntity("No se encontraron politicas", HttpStatus.NOT_FOUND);
            logger.error("Error al listar politicas");
        }

        return response;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Politica politica){
        try{
            logger.debug("Actualizando politica...");
            Politica politicaExist = politicaService.find(politica.getId());
            if (politicaExist == null) {
                logger.error("Politica con id " + politica.getId() + " no encontrada");
                return new ResponseEntity("Error al intentar actualizatar, politica con id " +politica.getId()+ " no encontrada", HttpStatus.NOT_FOUND);
            }
            logger.info("Politica actualizada con id: " + politica.getId());
            return ResponseEntity.ok(politicaService.update(politica));
        }catch (
                DataIntegrityViolationException ex) {
            logger.error("Error al actualizar politica");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede actualizar la politica porque está relacionado con otros datos");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        try{
            Politica politica = politicaService.find(id);
            if (politica == null) {
                logger.error("Politica con id " + id + " no encontrada");
                return new ResponseEntity("Error al intentar eliminar, politica con id " +id+ " no encontrada", HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok(politicaService.delete(id));
    } catch (
    DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar la politica porque está relacionado con otros datos");
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
    }

    @GetMapping("/list-{titulo}")
    public ResponseEntity<Politica> getById (@PathVariable String titulo){
        ResponseEntity response = null;

        List<Politica> lista = politicaService.findByTitulo(titulo);

        logger.debug("Buscando politicas...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista,HttpStatus.OK);
            logger.info("Politicas encontradas con titulo " + titulo);
        }else{
            response = new ResponseEntity("No se encontraron politicas con titulo " + titulo, HttpStatus.NOT_FOUND);
            logger.error("No se encontraron el politicas con titulo " + titulo);
        }
        return response;
    }

}
