package pl.lottojjr.domain.resultchecker;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface WinnerMapper {
    //@Mapping(source = "id", target = "id")
    @Mapping(source = "drawDate", target = "drawDate")
    @Mapping(source = "hitNumbers", target = "userNumbers")
    TicketDto toDto(Player ticket);

    List<TicketDto> toDtoList(List<Ticket> tickets);

    public Set<Ticket> toEntity(List<TicketDto> ticketDto);

    public default Player toPlayer(TicketDto ticketDto, Set<Integer> winningNumbers) {
        Set<Integer> matchedNumbers = ticketDto.userNumbers().stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());

        int matchCount = matchedNumbers.size();

        String resultText = switch (matchCount) {
            case 6 -> "Szóstka! Wielka wygrana!";
            case 5 -> "Piątka!";
            case 4 -> "Czwórka!";
            case 3 -> "Trójka!";
            default -> "Brak wygranej";
        };

        String id = UUID.randomUUID().toString();

        Set<Integer> numbers = ticketDto.userNumbers();
        Set<Integer> hitNumbers = numbers.stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
        LocalDateTime drawDate = ticketDto.drawDate();
        boolean isWinner = hitNumbers.size() >= 3;

        Player player = new Player(id, numbers, hitNumbers, drawDate, isWinner);
        return player;

    }
}
