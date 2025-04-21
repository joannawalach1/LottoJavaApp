package pl.lottojjr.domain;

import org.mapstruct.Mapper;
import pl.lottojjr.domain.dto.TicketDto;

@Mapper
public interface NumberReceiverMapper {
    TicketDto toDto(Ticket ticket);
}
