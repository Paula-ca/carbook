package Grupo7.Autitos.controller;

import Grupo7.Autitos.entity.Imagen;
import Grupo7.Autitos.entity.Producto;
import Grupo7.Autitos.entity.Reserva;
import Grupo7.Autitos.service.ReservaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bookings")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    public static final Logger logger = Logger.getLogger(ReservaController.class);

    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Reserva reserva) {
        logger.debug("Agregando reserva...");

        if (reserva == null) {
            logger.error("La reserva es nulo");
            return new ResponseEntity("Reserva nulo", HttpStatus.BAD_REQUEST);
        }
        Reserva reserva1 = reservaService.add(reserva);

        if (reserva1 != null) {

            logger.info("Reserva agregada con id: " + reserva1.getId());
            return new ResponseEntity<>(reserva1, HttpStatus.OK);
        } else {
            logger.error("La reserva ya existe o no pudo guardarse");
            return new ResponseEntity("Reserva existente o error al guardar", HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> bookingById(@PathVariable Long id) {
        logger.debug("Buscando reserva con id: {}" + id);

        if (id == null) {
            logger.error("ID es nulo");
            return new ResponseEntity<>("El ID no puede ser nulo", HttpStatus.BAD_REQUEST);
        }

        Reserva reserva = reservaService.find(id);

        if (reserva != null) {
            logger.info("Reserva encontrada con id: {}"+ id);
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } else {
            logger.warn("No se encontró ninguna reserva con id: {}"+ id);
            return new ResponseEntity<>("No se encontró ninguna reserva con id " + id, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/product/{id}/{cancelada}")
    public ResponseEntity<List<?>> listBookingsByProductId(@PathVariable Long id, @PathVariable Boolean cancelada) {
        logger.debug("Buscando reservas por producto id: "+id);

        List<Reserva> lista = reservaService.filterByProductId(id, cancelada);

        if(!lista.isEmpty()){
            logger.info("Reservas del producto con id: "+ id);
            return new ResponseEntity(lista,HttpStatus.OK);
        }else{
            logger.error("No se encontraron reservas para el producto con id  " + id);
            return new ResponseEntity("No se encontraron reservas para el producto con id " + id, HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/user/{id}/{borrada}")
    public ResponseEntity<List<?>> listBookingsByUserId(@PathVariable Long id, @PathVariable Boolean borrada) {
        logger.debug("Buscando reservas por usuario id: "+id);

        List<Reserva> lista = reservaService.filterByUserId(id, borrada);

        if(!lista.isEmpty()){
            logger.info("Reservas del usuario con id: "+ id);
            return new ResponseEntity(lista,HttpStatus.OK);
        }else{
            logger.error("No se encontraron reservas para el usuario con id  " + id);
            return new ResponseEntity("No se encontraron reservas para el usuario con id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateReserva(@RequestBody Reserva r) {
        try {
            Reserva actualizada = reservaService.update(r);
            logger.info("Reserva actualizado con id: " + r.getId());
            return ResponseEntity.ok(actualizada);
        } catch (EntityNotFoundException e) {
            logger.error("Reserva no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Error al actualizar la reserva");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @DeleteMapping("cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        try {
            String mensaje = reservaService.cancel(id);
            return ResponseEntity.ok(mensaje);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cancelar la reserva.");
        }
    }


}