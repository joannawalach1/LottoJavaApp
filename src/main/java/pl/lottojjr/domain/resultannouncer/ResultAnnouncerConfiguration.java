package pl.lottojjr.domain.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lottojjr.domain.resultchecker.ResultCheckerFacade;

import java.time.Clock;

@Configuration
public class ResultAnnouncerConfiguration {
    @Bean
    ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, Clock clock, ResponseRepository responseRepository ) {
        return new ResultAnnouncerFacade(
                resultCheckerFacade,
                clock,
                responseRepository
        );
    }
}
