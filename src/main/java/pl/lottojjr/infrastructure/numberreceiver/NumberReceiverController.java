package pl.lottojjr.infrastructure.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

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
}
