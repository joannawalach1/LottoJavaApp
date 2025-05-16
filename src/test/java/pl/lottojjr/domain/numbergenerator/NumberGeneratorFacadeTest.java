package pl.lottojjr.domain.numbergenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@RequiredArgsConstructor
class NumberGeneratorFacadeTest {
    private WinningNumbersFacade numberGeneratorFacade;
    private NumberReceiverFacade numberReceiverFacade;
    private DrawDateGenerator drawDateGenerator;
    private InMemoryNumberGeneratorRepository winningNumbersRepository;
    private NumberGenerator winningNumbersGenerator;
    private WinningNumbersValidator winningNumbersValidator;

    @BeforeEach
    public void setUp() {
        Clock clock = new AdjustableClock(
                LocalDateTime.of(2025, 5, 10, 12, 0, 0).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );

        numberReceiverFacade = mock(NumberReceiverFacade.class);
        drawDateGenerator = new DrawDateGenerator(clock);
        winningNumbersRepository = new InMemoryNumberGeneratorRepository();
        WebClient webClient = WebClient.builder().build();
        WebClientFetcher fetcher = new WebClientFetcher(webClient);
        winningNumbersGenerator = new NumberGenerator(fetcher);
        winningNumbersValidator = new WinningNumbersValidator();

        numberGeneratorFacade = new WinningNumbersFacade(
                winningNumbersGenerator,
                drawDateGenerator,
                clock,
                winningNumbersRepository,
                winningNumbersValidator
        );
    }
    @Test
    public void shouldGenerateExactlySixWinningNumbersBasedOnDrawDate() throws JsonProcessingException {
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 0, 0, 0);
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 5, 17, 12, 0, 0);

        WinningNumbers winningNumbers = numberGeneratorFacade.generateWinningNumbers();

        assertThat(winningNumbers.nextDrawDate()).isEqualTo(expectedNextDrawDate);
        assertThat(winningNumbers.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void shouldGenerateSexWinningNumbersWithinCorrectRange() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 21, 0, 0, 0);
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 5, 17, 12, 0, 0);

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
        LocalDateTime drawDate = LocalDateTime.of(2025, 4, 26, 12, 0);
        assertThrows(WinningNumbersNotFoundException.class, () -> numberGeneratorFacade.findWinningNumbersByNextDrawDate(drawDate));
    }
    @Test
    void shouldGenerateWinningNumbersWithCorrectNextDrawDate() {
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2025, 4, 21, 12, 0, 0, 0);
        LocalDateTime expectedNextDrawDate1 = LocalDateTime.of(2025, 5, 17, 12, 0, 0, 0);
        LocalDateTime result = drawDateGenerator.nextDrawDate(expectedNextDrawDate1);
        assertEquals(expectedNextDrawDate1, result, "Next draw date should be correct");
    }



}

