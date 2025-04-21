package pl.lottojjr.domain.numberreceiver.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record TicketDto(String id, LocalDateTime drawDate, Set<Integer> userNumbers) {
}