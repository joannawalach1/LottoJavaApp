package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@RequiredArgsConstructor
public class NumberReceiverFacade {
    private final TicketRepository ticketRepository;
    private final NumberValidator numberValidator;

    public Ticket inputNumbers(Set<Integer> userNumbers) {
        String id = UUID.randomUUID().toString();
        LocalDateTime drawDate = LocalDateTime.now();
        numberValidator.validateNumbers(userNumbers);
        Ticket newTicket = new Ticket(id, drawDate, userNumbers);
        Ticket savedTicket = ticketRepository.save(newTicket);
        return savedTicket;
      }
}
