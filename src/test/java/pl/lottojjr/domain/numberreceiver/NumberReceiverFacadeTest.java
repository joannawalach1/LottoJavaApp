package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.lottojjr.AdjustableClock;
import pl.lottojjr.domain.NumberReceiverFacade;
import pl.lottojjr.domain.NumberReceiverMapper;
import pl.lottojjr.domain.NumberValidator;
import pl.lottojjr.domain.Ticket;
import pl.lottojjr.domain.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NumberReceiverFacadeTest {
    private final TicketRepositoryInMemoryImpl ticketRepository = new TicketRepositoryInMemoryImpl();
    private final NumberValidator numberValidator = new NumberValidator();
    private final NumberReceiverMapper numberReceiverMapper = Mappers.getMapper(NumberReceiverMapper.class);
    private final AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2025, 5, 21, 12, 0, 0)
            .toInstant(ZoneOffset.UTC),
            ZoneId.systemDefault()
    );

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            ticketRepository,
            numberValidator,
            numberReceiverMapper,
            clock
    );

    @Test
    public void should_return_success_if_user_gave_six_numbers() {
        //when
        TicketDto ticket = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        //then
        assertThat(ticket.userNumbers()).isEqualTo(Set.of(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void should_return_fail_if_user_gave_less_than_six_numbers() {
        //when
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        //then
        assertThrows(IllegalArgumentException.class, () -> numberReceiverFacade.inputNumbers(numbers));
    }

    @Test
    public void should_return_fail_if_user_gave_more_than_six_numbers() {
        //when
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        //then
        assertThrows(IllegalArgumentException.class, () -> numberReceiverFacade.inputNumbers(numbers));
    }

    @Test
    public void should_return_fail_if_any_of_given_numbers_is_out_of_range() {
        //when
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 3000);
        //then
        assertThrows(IllegalArgumentException.class, () -> numberReceiverFacade.inputNumbers(numbers));
    }

    @Test
    public void should_return_ticket_id_if_user_gave_six_correct_numbers() {
        //given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        //when
        TicketDto ticket = numberReceiverFacade.inputNumbers(numbers);
        //then
        assertThat(ticket.id()).isNotNull();
        assertThat(ticket.userNumbers()).containsExactlyInAnyOrderElementsOf(numbers);
    }

    @Test
    public void should_return_next_draw_date_if_user_gave_six_correct_numbers() {
        //given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 12, 0, 0);
        //when
        TicketDto ticket = numberReceiverFacade.inputNumbers(numbers);
        //then
        assertThat(ticket.drawDate()).isNotNull();
        assertThat(ticket.userNumbers()).containsExactlyInAnyOrderElementsOf(numbers);
    }

    @Test
    public void should_save_to_database_if_user_gave_six_numbers() {
        //when
        TicketDto ticket = numberReceiverFacade.inputNumbers(Set.of(11,12,13,14,15,16));
        List<Ticket> tickets = numberReceiverFacade.userNumbers(ticket.drawDate());
        //then
        assertTrue(tickets.size() > 0);
    }
}