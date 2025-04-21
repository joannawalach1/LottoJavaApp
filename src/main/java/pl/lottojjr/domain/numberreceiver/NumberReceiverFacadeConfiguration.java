package pl.lottojjr.domain.numberreceiver;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class NumberReceiverFacadeConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public NumberReceiverMapper numberReceiverMapper() {
        return Mappers.getMapper(NumberReceiverMapper.class);
    }

    @Bean
    public DrawDateGenerator drawDateGenerator(Clock clock) {
        return new DrawDateGenerator(clock);
    }

    @Bean
    public NumberReceiverFacade numberReceiverFacade(TicketRepository repository, NumberReceiverMapper numberReceiverMapper, Clock clock) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        HashGenerable hashGenerable = new HashGenerator();
        return new NumberReceiverFacade(
                repository,
                numberValidator,
                numberReceiverMapper,
                clock,
                drawDateGenerator,
                hashGenerable
        );
    }
}
