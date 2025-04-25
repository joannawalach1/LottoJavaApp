package pl.lottojjr.domain.resultchecker;

import lombok.RequiredArgsConstructor;
import pl.lottojjr.domain.numbergenerator.WinningNumbersFacade;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
import pl.lottojjr.domain.resultchecker.dto.PlayerDto;

import java.util.List;
@RequiredArgsConstructor
public class ResultCheckerFacade {
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersFacade winningNumbersFacade;
    private final PlayerRepository playerRepository;
    private final WinnerRetriever winnerRetriever;


    public List<PlayerDto> generatePlayers() {


        return null;
    }
}
