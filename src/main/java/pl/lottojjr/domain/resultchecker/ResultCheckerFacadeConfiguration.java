package pl.lottojjr.domain.resultchecker;

import pl.lottojjr.domain.numbergenerator.WinningNumbersFacade;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;

public class ResultCheckerFacadeConfiguration {
    public ResultCheckerFacade resultCheckerFacade(WinningNumbersFacade winningNumbersFacade, NumberReceiverFacade numberReceiverFacade, PlayerRepository playerRepository) {
        WinnerRetriever winnerGenerator = new WinnerRetriever();
        return new ResultCheckerFacade(numberReceiverFacade, winningNumbersFacade, playerRepository, winnerGenerator);
    }
}
