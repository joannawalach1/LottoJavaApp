package pl.lottojjr.domain.numberreceiver;

import org.mapstruct.Mapper;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

@Mapper
public interface NumberReceiverMapper {
    TicketDto toDto(Ticket ticket);
}
