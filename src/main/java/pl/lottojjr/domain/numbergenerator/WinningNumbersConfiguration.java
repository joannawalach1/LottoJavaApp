package pl.lottojjr.domain.numbergenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.lottojjr.domain.numberreceiver.DrawDateGenerator;
import pl.lottojjr.infrastructure.numbergenerator.WebClientFetcher;

import java.time.Clock;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class WinningNumbersConfiguration {

    private final WebClientFetcher WebClientFetcher;

    @Bean
    public NumberGenerator numberGenerator() {
        return new NumberGenerator(WebClientFetcher);
    }
    @Bean
    public WinningNumbersFacade winningNumbersFacade(NumberGenerator numberGenerator,
                                                     DrawDateGenerator drawDateGenerator,
                                                     Clock clock,
                                                     WinningNumbersRepository winningNumbersRepository
                                                     ) {
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator();
        return new WinningNumbersFacade(
                numberGenerator,
                drawDateGenerator,
                clock,
                winningNumbersRepository,
                winningNumbersValidator
        );
    }
}

