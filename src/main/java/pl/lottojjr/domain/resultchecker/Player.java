package pl.lottojjr.domain.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;

public record Player(String Hash, Set<Integer> numbers, Set<Integer> hitNumbers, LocalDateTime drawDate, boolean isWinner) {
}
