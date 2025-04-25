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

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach
    void setUp() {
        WinnerMapper winnerMapper = mock(WinnerMapper.class);
        numberReceiverFacade = mock(NumberReceiverFacade.class);
        winningNumbersFacade = mock(WinningNumbersFacade.class);
        WinnerRetriever winnerRetriever = mock(WinnerRetriever.class);
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        drawDateGenerator =mock(DrawDateGenerator.class);
        resultCheckerFacade = new ResultCheckerFacade(numberReceiverFacade,
                winningNumbersFacade,
                playerRepository,
                winnerRetriever,
                drawDateGenerator,
                winnerMapper);
    }

    @Test
    void generateWinners_shouldReturnWinnersDto_whenWinnersAreRetrieved() {
        LocalDateTime drawDate = LocalDateTime.now();
        Set<TicketDto> allTickets = Set.of(new TicketDto(1, drawDate, Set.of(1, 2, 3, 4, 5, 6)));
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbersDto = new WinningNumbers(id, drawDate, winningNumbers);

        when(numberReceiverFacade.userNumbers(drawDate)).thenReturn(allTickets);
        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(winningNumbersDto);

        PlayerDto result = resultCheckerFacade.generateWinners();
        log.info(result);

        assertNotNull(result);
        assertEquals("Winners succeeded to retrieve", result.message());
    }

    @Test
    public void should_generate_all_players_with_correct_messages() {

    }

    @Test
    public void should_generate_fail_if_winning_numbers_is_null() {

    }

    @Test
    public void should_generate_fail_if_winning_numbers_is_empty() {

    }

    @Test
    public void should_generate_with_correct_credentials() {

    }

}