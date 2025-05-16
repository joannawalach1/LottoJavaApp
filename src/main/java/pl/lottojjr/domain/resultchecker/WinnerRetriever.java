package pl.lottojjr.domain.resultchecker;

import pl.lottojjr.domain.numberreceiver.dto.TicketDto;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

import java.util.Set;
import java.util.stream.Collectors;

public class WinnerRetriever {
    public Set<ResultDto> countMatchingNumbers(Set<TicketDto> tickets, Set<Integer> winningNumbers) {
        return tickets.stream()
                .map(ticket -> {
                    Set<Integer> hitNumbers = ticket.userNumbers().stream()
                            .filter(winningNumbers::contains)
                            .collect(Collectors.toSet());

                    String message = mapResultText(hitNumbers.size());

                    return new ResultDto(
                            ticket.id(),
                            ticket.userNumbers(),
                            hitNumbers,
                            ticket.drawDate(),
                            message
                    );
                })
                .collect(Collectors.toSet());
    }



    public String mapResultText(int matches) {
        System.out.println("Mapping matches: " + matches);
        return switch (matches) {
            case 6 -> "Szóstka! Wielka wygrana!";
            case 5 -> "Piątka!";
            case 4 -> "Czwórka!";
            case 3 -> "Trójka!";
            default -> "Brak wygranej";
        };
    }
}
