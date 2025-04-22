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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    private final WebClient webClient = WebClient.builder().build();
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
    public void shouldGenerateExactlySixWinningNumbersBasedOnDrawDate() throws JsonProcessingException {
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 0, 0, 0);
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 4, 26, 12, 0, 0);

        when(drawDateGenerator.generateNextDrawDate(drawDate)).thenReturn(expectedNextDrawDate);

        WinningNumbers winningNumbers = numberGeneratorFacade.generateWinningNumbers();
        assertThat(winningNumbers.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void shouldGenerateSexWinningNumbersWithinCorrectRange() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 0, 0, 0);
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 4, 26, 12, 0, 0);

        when(drawDateGenerator.generateNextDrawDate(drawDate)).thenReturn(expectedNextDrawDate);

        WinningNumbers winningNumbers = numberGeneratorFacade.generateWinningNumbers();
        assertThat(winningNumbers.winningNumbers().size()).isEqualTo(6);
        assertThat(winningNumbers.winningNumbers()
                .stream()
                .allMatch(n -> n >= 1 && n <= 99))
                .isTrue();
    }
//TODO
//    @Test
//    public void shouldThrowAnExceptionIfAnyNumberIsOutOfRange() {
//        WinningNumbers winningNumbers = new WinningNumbers(
//                UUID.randomUUID().toString(),
//                LocalDateTime.now(),
//                Set.of(1, 10, 20, 30, 40, 101));
//        when(numberGeneratorFacade.generateWinningNumbers())
//                .thenReturn(winningNumbers);
//        assertThrows(IllegalArgumentException.class, () -> numberGeneratorFacade.generateWinningNumbers());
//    }

    @Test
    public void shouldCreateCollectionOfUniqueValues() {
        Set<Integer> numbers = winningNumbersGenerator.generateRandomNumbers();
        assertThat(numbers.size()).isEqualTo(numbers.stream().distinct().count());
    }

    @Test
    public void shouldTThrowAnExceptionIfArentNumbersForGivenDate() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 12, 0);
        when(winningNumbersRepository.findWinningNumbersByDrawDate(drawDate)).thenReturn(null);
        assertThrows(WinningNumbersNotFoundException.class, () -> numberGeneratorFacade.findWinningNumbersByDate(drawDate));
    }
}

