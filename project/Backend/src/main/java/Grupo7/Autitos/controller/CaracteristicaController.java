package Grupo7.Autitos.controller;

import Grupo7.Autitos.entity.Caracteristica;
import Grupo7.Autitos.service.CaracteristicaService;
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
@RequestMapping("/features")
public class CaracteristicaController {

    @Autowired
    CaracteristicaService caracteristicaService;

    public static final Logger logger = Logger.getLogger(CaracteristicaService.class);

    @PostMapping("/add")
    public ResponseEntity<Caracteristica> add(@RequestBody Caracteristica caracteristica){
        logger.debug("Agregando caracteristica...");
        if (caracteristica == null) {
            logger.error("La caracteristica es nula");
            return new ResponseEntity("Caracteristica nula", HttpStatus.BAD_REQUEST);
        }

        Caracteristica caracteristica1 = caracteristicaService.add(caracteristica);

        if (caracteristica1 != null) {
            logger.info("Caracteristica agregada con id: " + caracteristica1.getId());
            return new ResponseEntity<>(caracteristica1, HttpStatus.OK);
        } else {
            logger.error("La caracteristica ya existe o no pudo guardarse");
            return new ResponseEntity("Caracteristica existente o error al guardar", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Caracteristica>> featureList() {
        ResponseEntity response = null;

        Set<Caracteristica> lista = caracteristicaService.list();

        logger.debug("Listando caracteristicas...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Caracteristicas listadas");
        } else {
            response = new ResponseEntity("No se encontraron caracteristicas", HttpStatus.NOT_FOUND);
            logger.error("Error al listar caracteristicas");
        }

        return response;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Caracteristica caracteristica){
        try{
            logger.debug("Actualizando caracteristica...");
            Caracteristica caracExist = caracteristicaService.find(caracteristica.getId());
            if (caracExist == null) {
                logger.error("Caracteristica con id " + caracteristica.getId() + " no encontrada");
                return new ResponseEntity("Error al intentar actualizatar, caracteristica con id " +caracteristica.getId()+ " no encontrada", HttpStatus.NOT_FOUND);
            }
            logger.info("Caracteristica actualizada con id: " + caracteristica.getId());
            return ResponseEntity.ok(caracteristicaService.update(caracteristica));
        }catch (
                DataIntegrityViolationException ex) {
            logger.error("Error al actualizar caracteristica");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede actualizar la caracteristica porque está relacionado con otros datos");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
    try{
        Caracteristica caracteristica = caracteristicaService.find(id);
        if (caracteristica == null) {
            logger.error("Caracteristica con id " + id + " no encontrada");
            return new ResponseEntity("Error al intentar eliminar, caracteristica con id " +id+ " no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(caracteristicaService.delete(id));
    } catch (
    DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar la caracteristica porque está relacionado con otros datos");
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
    }
}
