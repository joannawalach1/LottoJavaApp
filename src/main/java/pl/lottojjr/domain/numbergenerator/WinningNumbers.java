package pl.lottojjr.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Set;

public record WinningNumbers(String id, LocalDateTime nextDrawDate, Set<Integer> winningNumbers) {
}
