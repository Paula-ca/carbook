package CarbookApp.Interface;

import CarbookApp.dto.ReservaDTO;
import CarbookApp.entity.Reserva;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservaMapper {
    ReservaDTO toDto(Reserva reserva);
    Reserva toEntity(ReservaDTO dto);
}

