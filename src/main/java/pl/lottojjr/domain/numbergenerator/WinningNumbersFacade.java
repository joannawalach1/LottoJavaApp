package pl.lottojjr.domain.numbergenerator;

import lombok.RequiredArgsConstructor;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import sun.security.krb5.internal.ccache.MemoryCredentialsCache;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class WinningNumbersFacade {
    private final NumberGenerator numberGenerator;
    private final DrawDateGenerator drawDateGenerator;
    private final Clock clock;
    private final WinningNumbersRepository winningNumbersRepository;

    public WinningNumbers generateWinningNumbers() {
        String id = UUID.randomUUID().toString();
        LocalDateTime currentDay = LocalDateTime.now(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.generateNextDrawDate(currentDay);
        Set<Integer> winningNumbers = numberGenerator.generateRandomNumbers();
        WinningNumbers winningNumbersTicket = new WinningNumbers(id, nextDrawDate, winningNumbers);
        WinningNumbers savedWinningNumbers = winningNumbersRepository.save(winningNumbersTicket);
        return savedWinningNumbers;

    }
}
