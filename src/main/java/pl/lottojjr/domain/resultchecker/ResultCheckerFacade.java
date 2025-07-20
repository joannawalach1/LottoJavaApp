package pl.lottojjr.domain.resultchecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.numbergenerator.WinningNumbers;
import pl.lottojjr.domain.numbergenerator.WinningNumbersFacade;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;
import pl.lottojjr.domain.resultchecker.dto.PlayerDto;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class ResultCheckerFacade {
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersFacade winningNumbersFacade;
    private final PlayerRepository playerRepository;
    private final WinnerRetriever winnerRetriever;
    private final DrawDateGenerator drawDateGenerator;
    private final WinnerMapper winnerMapper;

    public PlayerDto generateWinners(LocalDateTime drawDate) {
        Set<TicketDto> allTicketsByDate = numberReceiverFacade.userNumbers(drawDate);
        WinningNumbers winningNumbers = winningNumbersFacade.findWinningNumbersByNextDrawDate(drawDate);

        if (winningNumbers == null) {
            throw new IllegalStateException("Winning numbers not found");
        }

        Set<Integer> winningNumbersSet = winningNumbers.winningNumbers();

        if (winningNumbersSet == null || winningNumbersSet.isEmpty()) {
            return new PlayerDto("No winners found", "Winners failed to retrieve");
        }

        Set<ResultDto> players = winnerRetriever.countMatchingNumbers(allTicketsByDate, winningNumbersSet);

        if (players.isEmpty()) {
            return new PlayerDto("No winners found", "Brak wygranej");
        }

        Set<String> uniqueMessages = players.stream()
                .map(ResultDto::message)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        String resultText = players.stream()
                .map(player -> "Ticket ID: " + player.hash() + ", " + player.drawDate() + ", " + player.numbers() + ", Trafienie: " + player.hitNumbers().size() + " - " + player.message())
                .collect(Collectors.joining("; "));

        String message = "Trafienia: " + String.join(", ", uniqueMessages);

        return new PlayerDto(resultText, message);
    }

    public ResultDto findResultByHash(String hash) {
        return playerRepository.findByHash(hash);
    }
}

