package pl.lottojjr.domain.numbergenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
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
        LocalDateTime currentDay = LocalDateTime.now(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.generateNextDrawDate(currentDay);
        Set<Integer> winningNumbers = numberGenerator.generateRandomNumbers();
        WinningNumbers winningNumbersTicket = new WinningNumbers(id, nextDrawDate, winningNumbers);
        WinningNumbers savedWinningNumbers = winningNumbersRepository.save(winningNumbersTicket);
        log.info(savedWinningNumbers);
        return savedWinningNumbers;
    }

    public List<WinningNumbers> findWinningNumbersByDate(LocalDateTime drawDate) {
        List<WinningNumbers> winningNumbersByDrawDate = winningNumbersRepository.findWinningNumbersByDrawDate(drawDate);
        if (winningNumbersByDrawDate.isEmpty()) {
            throw new WinningNumbersNotFoundException("No winning numbers found for: " + drawDate);
        }
        return winningNumbersByDrawDate;
    }
}
