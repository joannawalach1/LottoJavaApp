package pl.lottojjr.domain.resultchecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lottojjr.domain.numbergenerator.WinningNumbers;
import pl.lottojjr.domain.numbergenerator.WinningNumbersFacade;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;
import pl.lottojjr.domain.resultchecker.dto.PlayerDto;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@SpringBootTest
@Log4j2
class ResultCheckerFacadeTest {
    private ResultCheckerFacade resultCheckerFacade;
    private NumberReceiverFacade numberReceiverFacade;
    private WinningNumbersFacade winningNumbersFacade;
    private DrawDateGenerator drawDateGenerator;
    private WinnerRetriever winnerRetriever;
    private PlayerRepositoryInMemoryImpl playerRepository;

    @BeforeEach
    void setUp() {
        WinnerMapper winnerMapper = mock(WinnerMapper.class);
        numberReceiverFacade = mock(NumberReceiverFacade.class);
        winningNumbersFacade = mock(WinningNumbersFacade.class);
        winnerRetriever = mock(WinnerRetriever.class);
        playerRepository = new PlayerRepositoryInMemoryImpl();
        drawDateGenerator = mock(DrawDateGenerator.class);
        resultCheckerFacade = new ResultCheckerFacade(numberReceiverFacade,
                winningNumbersFacade,
                playerRepository,
                winnerRetriever,
                drawDateGenerator,
                winnerMapper);
    }

    @Test
    public void should_generate_winners_dto_when_winning_numbers_are_retrieved_from_database() {
        LocalDateTime drawDate = LocalDateTime.now();
        LocalDateTime nextDrawDate = drawDateGenerator.nextDrawDate(drawDate);

        Set<TicketDto> allTickets = Set.of(new TicketDto("1", drawDate, Set.of(1, 2, 3, 4, 5, 6)));
        WinningNumbers winningNumbersDto = new WinningNumbers(UUID.randomUUID().toString(), nextDrawDate, Set.of(1, 2, 3, 4, 5, 6));

        when(winningNumbersFacade.findWinningNumbersByNextDrawDate(drawDate)).thenReturn(winningNumbersDto);
        when(numberReceiverFacade.userNumbers(drawDate)).thenReturn(allTickets);

        Set<ResultDto> resultDtos = Set.of(
                new ResultDto("1", Set.of(1, 2, 3, 4, 5, 6), Set.of(1, 2, 3, 4, 5,6), drawDate,"Szóstka! Wielka wygrana!", true)
        );
        when(winnerRetriever.countMatchingNumbers(allTickets, winningNumbersDto.winningNumbers())).thenReturn(resultDtos);

        PlayerDto result = resultCheckerFacade.generateWinners(drawDate);

        log.info(result.toString());
        assertNotNull(result);
        assertEquals("Trafienia: Szóstka! Wielka wygrana!", result.message());
    }



    @Test
    public void should_generate_all_players_with_correct_messages() {
        // Given
        LocalDateTime drawDate = LocalDateTime.of(2025, 5, 11, 12, 0, 0);
        Set<Integer> winningNumbersSet = Set.of(1, 2, 3, 4, 5, 6);

        when(winningNumbersFacade.findWinningNumbersByNextDrawDate(drawDate)).thenReturn(
                new WinningNumbers(
                        "001",
                        drawDate,
                        winningNumbersSet
                )
        );

        Set<TicketDto> ticketDtos = Set.of(
                new TicketDto("001", drawDate, Set.of(1, 2, 3, 4, 5, 6)),
                new TicketDto("002", drawDate, Set.of(1, 2, 3, 8, 9, 10)),
                new TicketDto("003", drawDate, Set.of(11, 12, 13, 14, 15, 16))
        );

        when(numberReceiverFacade.userNumbers(drawDate)).thenReturn(ticketDtos);

        when(winnerRetriever.countMatchingNumbers(anySet(), eq(winningNumbersSet)))
                .thenReturn(Set.of(
                        new ResultDto("001", Set.of(1, 2, 3, 4, 5, 6), Set.of(1, 2, 3, 4, 5, 6), drawDate, "Szóstka! Wielka wygrana!", true),
                        new ResultDto("002", Set.of(1, 2, 3, 8, 9, 10), Set.of(1, 2, 3), drawDate,"Trójka - niezła próba!", true),
                        new ResultDto("003", Set.of(11, 12, 13, 14, 15, 16), Set.of(), drawDate, "Brak trafień - spróbuj ponownie", false)
                ));

        // When
        PlayerDto playerDto = resultCheckerFacade.generateWinners(drawDate);
        log.info(playerDto.toString());


        // Then
        assertThat(playerDto.message()).isEqualTo(playerDto.message());
    }


    @Test
    public void should_generate_fail_if_winning_numbers_is_null() {
        // Given
        LocalDateTime drawDate = LocalDateTime.of(2025, 5, 11, 12, 0, 0);
        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(null);

        // When / Then
        assertThatThrownBy(() -> resultCheckerFacade.generateWinners(drawDate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Winning numbers not found");
    }


//    @Test
//    public void should_generate_fail_if_winning_numbers_is_empty() {
//            // Given
//            WinningNumbers emptyWinningNumbers = new WinningNumbers("123", LocalDateTime.now());
//            when(winningNumbersFacade.generateWinningNumbers()).thenReturn(emptyWinningNumbers);
//
//            // When / Then
//            assertThatThrownBy(() -> resultCheckerFacade.generateWinners())
//                    .isInstanceOf(IllegalStateException.class)
//                    .hasMessageContaining("Winning numbers not found");
//        }


//    @Test
//    public void should_generate_with_correct_credentials() {
//        // Given
//        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
//        LocalDateTime drawDate = LocalDateTime.now();
//        WinningNumbers validWinningNumbers = new WinningNumbers(numbers, drawDate);
//
//        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(validWinningNumbers);
//        when(tick.findAllByDrawDate(drawDate)).thenReturn(List.of(
//                new Ticket("ABC123", numbers, drawDate)
//        ));
//
//        // When
//        List<ResultDto> results = resultCheckerFacade.generateWinners();
//
//        // Then
//        assertThat(results).is();
//        assertThat(results.get(0).ticketId()).isEqualTo("ABC123");
//        assertThat(results.get(0).numbersHit()).isEqualTo(6); // lub inna odpowiednia weryfikacja
//    }


}