package pl.lottojjr.domain.resultannouncer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
@Builder
public record ResultResponse(String hash, Set<Integer> userNumbers, Set<Integer> hitNumbers, LocalDateTime drawDate, boolean isWinner) {
}
