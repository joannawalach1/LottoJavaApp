package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Log4j2
public class NumberReceiverFacade {
    private final TicketRepository ticketRepository;
    private final NumberValidator numberValidator;
    private final NumberReceiverMapper numberReceiverMapper;
    private final Clock clock;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerable;

    public TicketDto inputNumbers(Set<Integer> userNumbers) {
        String id = hashGenerable.generateHash();
        LocalDateTime drawDate = drawDateGenerator.generateDrawDate();
        numberValidator.validateNumbers(userNumbers);
        Ticket newTicket = new Ticket(id, drawDate, userNumbers);
        Ticket savedTicket = ticketRepository.save(newTicket);
        TicketDto dto = numberReceiverMapper.toDto(newTicket);
        log.info(savedTicket);
        return dto;
    }

    public List<Ticket> userNumbers(LocalDateTime drawDate) {
        return Optional.ofNullable(ticketRepository.findByDrawDate(drawDate))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new TicketNotFoundException("No tickets found for draw date: " + drawDate));
    }
}
