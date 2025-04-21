package pl.lottojjr.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
@Builder
public record Ticket(String id, LocalDateTime drawDate, Set<Integer> userNumbers) {
}
