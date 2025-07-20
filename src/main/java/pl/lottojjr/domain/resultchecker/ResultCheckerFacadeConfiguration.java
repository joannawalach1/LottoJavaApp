package pl.lottojjr.domain.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lottojjr.domain.numbergenerator.WinningNumbersFacade;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.domain.numberreceiver.NumberReceiverFacade;
@Configuration
public class ResultCheckerFacadeConfiguration {

    @Bean
    public WinnerRetriever winnerRetriever() {
        return new WinnerRetriever();
    }

    @Bean
    public WinnerMapper winnerMapper() {
        return new WinnerMapperImpl();
    }
    @Bean
    public ResultCheckerFacade resultCheckerFacade(
            NumberReceiverFacade numberReceiverFacade,
            WinningNumbersFacade winningNumbersFacade,
            PlayerRepository playerRepository,
            WinnerRetriever winnerRetriever,
            DrawDateGenerator drawDateGenerator,
            WinnerMapper winnerMapper
    ) {
        return new ResultCheckerFacade(
                numberReceiverFacade,
                winningNumbersFacade,
                playerRepository,
                winnerRetriever,
                drawDateGenerator,
                winnerMapper
        );
    }
}
