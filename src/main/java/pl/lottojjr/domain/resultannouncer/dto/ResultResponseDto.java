package pl.lottojjr.domain.resultannouncer.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultResponseDto(String hash, Set<Integer> userNumbers, Set<Integer> hitNumbers, LocalDateTime drawDate, boolean isWinner) {
}
