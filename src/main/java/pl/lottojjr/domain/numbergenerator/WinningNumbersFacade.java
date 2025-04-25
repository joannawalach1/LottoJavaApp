package pl.lottojjr.domain.numbergenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
public class WinningNumbersFacade {
    private final NumberGenerator numberGenerator;
    private final DrawDateGenerator drawDateGenerator;
    private final Clock clock;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumbersValidator winningNumbersValidator;

    public WinningNumbers generateWinningNumbers() {
        String id = UUID.randomUUID().toString();
        LocalDateTime currentDay = LocalDateTime.of(2024,4,21,12,0,0);
        LocalDateTime nextDrawDate = drawDateGenerator.nextDrawDate(currentDay);
        log.info("Next Draw Date: {}", nextDrawDate);
        Set<Integer> winningNumbers = numberGenerator.generateRandomNumbers();
        WinningNumbers winningNumbersTicket = new WinningNumbers(id, nextDrawDate, winningNumbers);
        WinningNumbers savedWinningNumbers = winningNumbersRepository.save(winningNumbersTicket);
        log.info(savedWinningNumbers);
        return savedWinningNumbers;
    }

    public WinningNumbers findWinningNumbersByNextDrawDate(LocalDateTime nextDrawDate) {
        return winningNumbersRepository.findWinningNumbersByNextDrawDate(nextDrawDate)
                .filter(list -> !list.winningNumbers().isEmpty())
                .orElseThrow(() -> new WinningNumbersNotFoundException("No winning numbers found for: " + nextDrawDate));
    }
}
