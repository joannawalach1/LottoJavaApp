package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.lottojjr.AdjustableClock;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Log4j2
@RequiredArgsConstructor
class NumberReceiverFacadeTest {
    Clock clock = new AdjustableClock(
            LocalDateTime.of(2025, 5, 21, 12, 0, 0).toInstant(ZoneOffset.UTC),
            ZoneId.systemDefault()
    );

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new TicketRepositoryInMemoryImpl(),
            new NumberValidator(),
            Mappers.getMapper(NumberReceiverMapper.class),
            clock,
            new DrawDateGenerator(clock),
            new HashGenerator()
    );
    private final DrawDateGenerator drawDateGenerator= new DrawDateGenerator(clock);


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
        Set<TicketDto> tickets = numberReceiverFacade.userNumbers(ticket.drawDate());
        //then
        assertTrue(tickets.size() > 0);
    }
    @Test
    public void testGenerateNextDrawDate() {
        // Test dla daty 2025-04-21 (poniedziałek)
        LocalDateTime currentDate = LocalDateTime.of(2025, 4, 21, 10, 0, 0, 0);
        LocalDateTime localDateTime = drawDateGenerator.nextDrawDate(currentDate);

        // Sprawdzamy, czy obliczona data to najbliższa sobota (2025-04-26) o godzinie 12:00
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 4, 26, 12, 0, 0, 0);
        AssertionsForClassTypes.assertThat(localDateTime).isEqualTo(expectedNextDrawDate);
    }
    
}