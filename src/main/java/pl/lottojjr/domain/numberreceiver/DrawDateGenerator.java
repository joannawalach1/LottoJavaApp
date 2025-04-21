package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@RequiredArgsConstructor
public class DrawDateGenerator {

    private final Clock clock;
    public LocalDateTime generateDrawDate() {
        return LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    }
}
