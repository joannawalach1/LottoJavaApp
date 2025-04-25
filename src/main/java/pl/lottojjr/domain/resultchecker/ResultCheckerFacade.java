package pl.lottojjr.domain.resultchecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.numbergenerator.WinningNumbers;
import pl.lottojjr.domain.numbergenerator.WinningNumbersFacade;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;
import pl.lottojjr.domain.resultchecker.dto.PlayerDto;

import java.time.LocalDateTime;
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

    public PlayerDto generateWinners() {
        LocalDateTime drawDate = drawDateGenerator.generateDrawDate();
        Set<TicketDto> allTicketsByDate = numberReceiverFacade.userNumbers(drawDate);

        WinningNumbers winningNumbers = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> winningNumbersSet = winningNumbers.winningNumbers();

        if (winningNumbersSet == null || winningNumbersSet.isEmpty()) {
            return new PlayerDto("No winners found", "Winners failed to retrieve");
        }

        Set<TicketDto> players = winnerRetriever.countMatchingNumbers(allTicketsByDate, winningNumbersSet);
        String resultText = "Winners: " + players.stream()
                .map(ticketDto -> ticketDto.toString())
                .collect(Collectors.joining(", "));

        return new PlayerDto(resultText, "Winners succeeded to retrieve");
    }


}

