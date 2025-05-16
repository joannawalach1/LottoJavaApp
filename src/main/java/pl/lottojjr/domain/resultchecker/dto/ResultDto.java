package pl.lottojjr.domain.resultchecker.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record ResultDto(String hash,
                        Set<Integer> numbers,
                        Set<Integer> hitNumbers,
                        LocalDateTime drawDate,
                        String message
) {
}
