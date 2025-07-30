package CarbookApp.service;

import CarbookApp.Interface.ReservaMapper;
import CarbookApp.dto.ReservaDTO;
import CarbookApp.entity.Producto;
import CarbookApp.entity.Reserva;
import CarbookApp.entity.Usuario;
import CarbookApp.repository.ReservaRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaMapper reservaMapper;

    @Autowired
    public ReservaService(ReservaMapper reservaMapper) {
        this.reservaMapper = reservaMapper;
    }

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    public UsuarioService usuarioService;

    public static final Logger logger = Logger.getLogger(ReservaService.class);

    public ReservaDTO add(Reserva r) {
        if (    r.getFecha_ingreso() == null ||
                r.getFecha_final() == null ||
                r.getPrecio() == 0 ||
                r.getHora_comienzo() == null ||
                r.getProducto().getId() == null ||
                r.getUsuario().getId() == null) {
                 return null;
        }
        Producto productoCompleto = productoService.find(r.getProducto().getId());
        Usuario usuarioCompleto = usuarioService.find(r.getUsuario().getId());
        r.setProducto(productoCompleto);
        r.setUsuario(usuarioCompleto);

        List<Reserva> reservas = reservaRepository.findAll();
        for (Reserva reserva : reservas) {
            if (r.getFecha_ingreso().equals(reserva.getFecha_ingreso()) &&
                    r.getFecha_final().equals(reserva.getFecha_final()) &&
                    r.getProducto().getId().equals(reserva.getProducto().getId())&&
                    r.getUsuario().getId().equals(reserva.getUsuario().getId())) {
                return null;
            }
        }

        return reservaMapper.toDto(reservaRepository.save(r));
    }

    public ReservaDTO update(Reserva r) {
        Reserva reserva = reservaRepository.findById(r.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + r.getId()));

        List<ReservaDTO> lista = reservaRepository.findBookingsForProduct(reserva.getProducto().getId());
        List<ReservaDTO> otrasReservas = lista.stream()
                .filter(res -> !res.getId().equals(reserva.getId())&&res.getProducto().getId().equals(reserva.getProducto().getId()))
                .collect(Collectors.toList());

        LocalDate inicio = (r.getFecha_ingreso() != null) ? r.getFecha_ingreso() : reserva.getFecha_ingreso();
        LocalDate fin = (r.getFecha_final() != null) ? r.getFecha_final() : reserva.getFecha_final();

        boolean hayConflicto = otrasReservas.stream().anyMatch(resExistente ->
                resExistente.getBorrado() == null &&
                        resExistente.getFechaIngreso().isBefore(fin) &&
                        resExistente.getFechaFinal().isAfter(inicio)
        );

        if (hayConflicto) {
            throw new IllegalArgumentException("Ya hay reservas entre " + inicio + " y " + fin);
        }

        if (r.getHora_comienzo() != null) reserva.setHora_comienzo(r.getHora_comienzo());
        reserva.setFecha_ingreso(inicio);
        reserva.setFecha_final(fin);

        return reservaMapper.toDto(reservaRepository.save(reserva));
    }


    public List<ReservaDTO> filterByProductId(Long id, Boolean borrada) {
        if (borrada){
            return reservaRepository.findCancelBookingsForProduct(id);
        } else return reservaRepository.findBookingsForProduct(id);
    }

    public List<ReservaDTO> filterByUserId(Long id, Boolean borrada) {
        if (borrada){
            return reservaRepository.findCancelBookingsForUser(id);
        } else return reservaRepository.findBookingsForUser(id);
    }

    public ReservaDTO findDtoById(Long id){
        return reservaRepository.findDtoById(id);
    }
    public Optional<Reserva> findById(Long id){
        return reservaRepository.findById(id);
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
        Reserva reserva = reservaRepository.save(r);
        if(reserva == null){
            logger.error("La reserva id "+id+" no pudo cancelarse exitosamente");
        }
        logger.info("Reserva cancelada con id: {}"+ id);
        return "Reserva cancelada con id: " + id;
    }
}