package pl.lottojjr.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lottojjr.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class NumberReceiverFacadeConfiguration {

    @Bean
    public NumberValidator numberValidator() {
        return new NumberValidator();
    }

    @Bean
    public NumberReceiverMapper numberReceiverMapper() {
        return new NumberReceiverMapper() {
            @Override
            public TicketDto toDto(Ticket ticket) {
                return null;
            }
        };
    }
    
    @Bean
    public Clock clock() {
        Clock clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };
        return clock;
    }


    @Bean
    public NumberReceiverFacade numberReceiverFacade(TicketRepository repository, NumberValidator numberValidator, NumberReceiverMapper numberReceiverMapper, Clock clock) {
        return new NumberReceiverFacade(
                repository,
                numberValidator,
                numberReceiverMapper,
                clock
        );
    }
}
