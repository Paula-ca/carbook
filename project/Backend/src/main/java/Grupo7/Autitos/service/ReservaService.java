package Grupo7.Autitos.service;

import Grupo7.Autitos.entity.Reserva;
import Grupo7.Autitos.repository.ReservaRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public static final Logger logger = Logger.getLogger(ReservaService.class);

    public Reserva add(Reserva reserva) {
        if(reserva.getFecha_ingreso() != null
                && reserva.getFecha_final() != null
                && reserva.getProducto() != null
                && reserva.getUsuario() != null) {
            return reservaRepository.save(reserva);
        } else {
            return null;
        }
    }

    public Reserva update(Reserva r) {
        Reserva reserva = reservaRepository.findById(r.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + r.getId()));

        List<Reserva> lista = reservaRepository.findByProductoId(reserva.getProducto().getId());
        List<Reserva> otrasReservas = lista.stream()
                .filter(res -> !res.getId().equals(reserva.getId()))
                .collect(Collectors.toList());

        LocalDate inicio = (r.getFecha_ingreso() != null) ? r.getFecha_ingreso() : reserva.getFecha_ingreso();
        LocalDate fin = (r.getFecha_final() != null) ? r.getFecha_final() : reserva.getFecha_final();

        boolean hayConflicto = otrasReservas.stream().anyMatch(resExistente ->
                resExistente.getBorrado() == null &&
                        resExistente.getFecha_ingreso().isBefore(fin) &&
                        resExistente.getFecha_final().isAfter(inicio)
        );

        if (hayConflicto) {
            throw new IllegalArgumentException("Ya hay reservas entre " + inicio + " y " + fin);
        }

        if (r.getHora_comienzo() != null) reserva.setHora_comienzo(r.getHora_comienzo());
        reserva.setFecha_ingreso(inicio);
        reserva.setFecha_final(fin);
        if (r.getEstado() != null) reserva.setEstado(r.getEstado());
        if (r.getEstado_pago() != null) reserva.setEstado_pago(r.getEstado_pago());
        if (r.getPago_id() != null) reserva.setPago_id(r.getPago_id());

        return reservaRepository.save(reserva);
    }


    public List<Reserva> filterByProductId(Long id, Boolean borrada) {
        if (borrada){
            return reservaRepository.findCancelBookingsForProduct(id);
        } else return reservaRepository.findBookingsForProduct(id);
    }

    public List<Reserva> filterByUserId(Long id, Boolean borrada) {
        if (borrada){
            return reservaRepository.findCancelBookingsForUser(id);
        } else return reservaRepository.findBookingsForUser(id);
    }

    public Reserva find(Long id){
        return reservaRepository.findById(id).orElse(null);
    }

    public String cancel(Long id) {
        logger.debug("Cancelando reserva con id: {}"+ id);

        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reserva con id {} no encontrada"+ id);
                    return new EntityNotFoundException("Reserva con id " + id + " no encontrada");
                });

        if (r.getBorrado() != null) {
            logger.warn("La reserva con id {} ya fue cancelada previamente"+ id);
            throw new IllegalStateException("La reserva ya fue cancelada anteriormente.");
        }

        r.setBorrado(LocalDate.now());
        reservaRepository.save(r);

        logger.info("Reserva cancelada con id: {}"+ id);
        return "Reserva cancelada con id: " + id;
    }


}