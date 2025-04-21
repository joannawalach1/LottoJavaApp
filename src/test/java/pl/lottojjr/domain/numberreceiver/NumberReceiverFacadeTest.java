package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
class NumberReceiverFacadeTest {
    private final TicketRepositoryInMemoryImpl ticketRepository = new TicketRepositoryInMemoryImpl();
    private final NumberValidator numberValidator = new NumberValidator();
    private final NumberReceiverMapper numberReceiverMapper = Mappers.getMapper(NumberReceiverMapper.class);

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(ticketRepository, numberValidator, numberReceiverMapper);

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
    public void should_return_nex_draw_date_if_user_gave_six_correct_numbers() {
        //given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 15, 12, 0, 0);
        //when
        TicketDto ticket = numberReceiverFacade.inputNumbers(numbers);
        //then
        assertThat(ticket.drawDate()).isNotNull();
        assertThat(ticket.userNumbers()).containsExactlyInAnyOrderElementsOf(numbers);

    }
}