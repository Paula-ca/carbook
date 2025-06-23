package Grupo7.Autitos.controller;

import Grupo7.Autitos.entity.Ciudad;
import Grupo7.Autitos.service.CiudadService;
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
@RequestMapping("/cities")
public class CiudadController {

    @Autowired
    CiudadService ciudadService;

    public static final Logger logger = Logger.getLogger(CiudadController.class);

    @PostMapping("/add")
    public ResponseEntity<Ciudad> add(@RequestBody Ciudad ciudad){

        logger.debug("Agregando ciudad...");
        if (ciudad == null) {
            logger.error("La ciudad es nula");
            return new ResponseEntity("Ciudad nula", HttpStatus.BAD_REQUEST);
        }

        Ciudad ciudad1 = ciudadService.add(ciudad);

        if (ciudad1 != null) {
            logger.info("Ciudad agregada con id: " + ciudad1.getId());
            return new ResponseEntity<>(ciudad1, HttpStatus.OK);
        } else {
            logger.error("La ciudad ya existe o no pudo guardarse");
            return new ResponseEntity("Ciudad existente o error al guardar", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Ciudad>> cityList() {
        ResponseEntity response = null;

        Set<Ciudad> lista = ciudadService.list();

        logger.debug("Listando ciudades...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista, HttpStatus.OK);
            logger.info("Ciudades listadas");
        } else {
            response = new ResponseEntity("No se encontraron ciudades", HttpStatus.NOT_FOUND);
            logger.error("Error al listar ciudades");
        }

        return response;
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Ciudad ciudad){
        try{
            logger.debug("Actualizando ciudad...");
            Ciudad ciudadExist = ciudadService.find(ciudad.getId());
            if (ciudadExist == null) {
                logger.error("Ciudad con id " + ciudad.getId() + " no encontrada");
                return new ResponseEntity("Error al intentar actualizatar, ciudad con id " +ciudad.getId()+ " no encontrada", HttpStatus.NOT_FOUND);
            }
            logger.info("Ciudad actualizada con id: " + ciudad.getId());
            return ResponseEntity.ok(ciudadService.update(ciudad));
        }catch (
                DataIntegrityViolationException ex) {
            logger.error("Error al actualizar ciudad");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede actualizar la ciudad porque está relacionado con otros datos");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
    try{
        Ciudad ciudad = ciudadService.find(id);
        if (ciudad == null) {
            logger.error("Ciudad con id " + id + " no encontrada");
            return new ResponseEntity("Error al intentar eliminar, ciudad con id " +id+ " no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ciudadService.delete(id));
    } catch (
    DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar la ciudad porque está relacionado con otros datos");
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
    }

    @GetMapping("/list-{pais}")
    public ResponseEntity<Ciudad> getById (@PathVariable String pais){
        ResponseEntity response = null;

        List<Ciudad> lista = ciudadService.findByPais(pais);

        logger.debug("Buscando ciudades...");
        if(lista.size() > 0){
            response = new ResponseEntity(lista,HttpStatus.OK);
            logger.info("Ciudades encontradas en " + pais);
        }else{
            response = new ResponseEntity("No se encontraron ciudades en " + pais, HttpStatus.NOT_FOUND);
            logger.error("No se encontraron el ciudades en " + pais);
        }
        return response;
    }

}
