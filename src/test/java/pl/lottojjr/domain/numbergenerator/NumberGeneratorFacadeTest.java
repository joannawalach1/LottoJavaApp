package pl.lottojjr.domain.numbergenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import pl.lottojjr.AdjustableClock;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.infrastructure.numbergenerator.WebClientFetcher;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
class NumberGeneratorFacadeTest {

    Clock clock = new AdjustableClock(
            LocalDateTime.of(2025, 5, 21, 12, 0, 0).toInstant(ZoneOffset.UTC),
            ZoneId.systemDefault()
    );

    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final DrawDateGenerator drawDateGenerator = mock(DrawDateGenerator.class);
    private final InMemoryNumberGeneratorRepository winningNumbersRepository = new InMemoryNumberGeneratorRepository();
    private WebClient webClient;
    private final WebClientFetcher fetcher = new WebClientFetcher(webClient);
    private final NumberGenerator winningNumbersGenerator = new NumberGenerator(fetcher);
    private final WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator();
    WinningNumbersFacade numberGeneratorFacade = new WinningNumbersFacade(
            winningNumbersGenerator,
            drawDateGenerator,
            clock,
            winningNumbersRepository,
            winningNumbersValidator

    );

    @Test
    public void shouldGenerateWinningNumbersBasedOnDrawDate() throws JsonProcessingException {
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 0, 0, 0);
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 4, 21, 23, 45, 0);

        when(drawDateGenerator.generateNextDrawDate(drawDate)).thenReturn(expectedNextDrawDate);

        List<WinningNumbers> winningTicketDto = numberGeneratorFacade.findWinningNumbersByDate(expectedNextDrawDate);

        assertNotNull(winningTicketDto);
    }
}

