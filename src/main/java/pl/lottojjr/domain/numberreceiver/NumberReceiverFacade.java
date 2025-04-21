package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@RequiredArgsConstructor
@Log4j2
public class NumberReceiverFacade {
    private final TicketRepository ticketRepository;
    private final NumberValidator numberValidator;
    private final NumberReceiverMapper numberReceiverMapper;

    public TicketDto inputNumbers(Set<Integer> userNumbers) {
        String id = UUID.randomUUID().toString();
        LocalDateTime drawDate = LocalDateTime.now();
        numberValidator.validateNumbers(userNumbers);
        Ticket newTicket = new Ticket(id, drawDate, userNumbers);
        Ticket savedTicket = ticketRepository.save(newTicket);
        TicketDto dto = numberReceiverMapper.toDto(newTicket);
        log.info(savedTicket);
        return dto;
      }


      public List<Ticket> userNumbers(LocalDateTime drawDate) {
        return ticketRepository.findByDrawDate(drawDate);
      }
}
