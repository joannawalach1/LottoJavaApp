package pl.lottojjr.domain.numbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.infrastructure.numbergenerator.WebClientFetcher;

import java.time.Clock;

public class WinningNumbersConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebClientFetcher webClientFetcher(WebClient webClient) {
        return new WebClientFetcher(webClient);
    }

    @Bean
    public WinningNumbersFacade winningNumbersFacade(NumberGenerator numberGenerator,
                                                     DrawDateGenerator drawDateGenerator,
                                                     Clock clock,
                                                     WinningNumbersRepository winningNumbersRepository,
                                                     WinningNumbersValidator winningNumbersValidator) {
        return new WinningNumbersFacade(
                numberGenerator,
                drawDateGenerator,
                clock,
                winningNumbersRepository,
                winningNumbersValidator
        );
    }
}

