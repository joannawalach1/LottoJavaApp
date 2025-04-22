package pl.lottojjr.domain.numbergenerator;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
@Builder
public record WinningNumbers(String id, LocalDateTime nextDrawDate, Set<Integer> winningNumbers) {
}
