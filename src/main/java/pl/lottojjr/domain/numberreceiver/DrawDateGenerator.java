package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
@Component
@RequiredArgsConstructor
public class DrawDateGenerator {

    private final Clock clock;

    public LocalDateTime generateDrawDate() {
        return LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    }


    public LocalDateTime nextDrawDate(LocalDateTime currentDate) {
        currentDate = LocalDateTime.now();
        LocalDateTime nextSaturday = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        LocalDateTime nextDrawDate = nextSaturday.withHour(12).withMinute(0).withSecond(0).withNano(0);
        if (nextDrawDate.isBefore(currentDate)) {
            nextDrawDate = nextDrawDate.plusYears(1);
        }
        return nextDrawDate;
    }

}
