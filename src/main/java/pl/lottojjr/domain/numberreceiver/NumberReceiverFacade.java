package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@RequiredArgsConstructor
@Repository
public class NumberReceiverFacade {
    private final TicketRepository ticketRepository;

    public Ticket inputNumbers(Set<Integer> userNumbers) {

        String id = UUID.randomUUID().toString();
        LocalDateTime drawDate = LocalDateTime.now();
        Ticket newTicket = new Ticket(id, drawDate, userNumbers);
        Ticket savedTicket = ticketRepository.save(newTicket);
        return savedTicket;


    }
}
