package pl.lottojjr.domain.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
@Builder
public record TicketDto(String id, LocalDateTime drawDate, Set<Integer> userNumbers) {
}