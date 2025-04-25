package pl.lottojjr.domain.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;

public record Ticket(String id, LocalDateTime drawDate, Set<Integer> userNumbers) {
}
