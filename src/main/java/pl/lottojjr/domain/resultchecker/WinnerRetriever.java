package pl.lottojjr.domain.resultchecker;

import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.util.Set;
import java.util.stream.Collectors;

public class WinnerRetriever {
    public Set<TicketDto> countMatchingNumbers(Set<TicketDto> ticketNumbers, Set<Integer> winningNumbers) {
        return ticketNumbers.stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
    }

    public String mapResultText(int matches) {
        return switch (matches) {
            case 6 -> "Szóstka! Wielka wygrana!";
            case 5 -> "Piątka!";
            case 4 -> "Czwórka!";
            case 3 -> "Trójka!";
            default -> "Brak wygranej";
        };
    }
}
