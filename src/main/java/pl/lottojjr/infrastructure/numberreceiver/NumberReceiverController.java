package pl.lottojjr.infrastructure.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.domain.numberreceiver.Ticket;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class NumberReceiverController {

    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping
    public ResponseEntity<TicketDto> addTicket(@RequestBody Set<Integer> numbers) {
        TicketDto ticketDto = numberReceiverFacade.inputNumbers(numbers);
        return ResponseEntity.status(HttpStatus.OK).body(ticketDto);
    }

    @GetMapping("/drawDate")
    public ResponseEntity<List<Ticket>> getTicket(@RequestParam("drawDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drawDate) {
        List<Ticket> ticketDto = numberReceiverFacade.userNumbers(drawDate.truncatedTo(ChronoUnit.SECONDS));
        return ResponseEntity.status(HttpStatus.OK).body(ticketDto);
    }
}
