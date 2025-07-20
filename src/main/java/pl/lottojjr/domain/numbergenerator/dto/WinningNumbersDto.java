package pl.lottojjr.domain.numbergenerator.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record WinningNumbersDto(String id, LocalDateTime nextDrawDate, Set<Integer> winningNumbers) {
}
