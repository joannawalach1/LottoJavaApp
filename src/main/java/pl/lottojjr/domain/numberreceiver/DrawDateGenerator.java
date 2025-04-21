package pl.lottojjr.domain.numberreceiver;

import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class DrawDateGenerator {

    private final Clock clock;

    public LocalDateTime generateDrawDate() {
        return LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    }

    public LocalDateTime generateNextDrawDate(LocalDateTime currentDate) {
        int currentDay = currentDate.getDayOfWeek().getValue();
        int daysUntilSaturday = DayOfWeek.SATURDAY.getValue() - currentDay;

        if (daysUntilSaturday <= 0) {
            daysUntilSaturday += 7;
        }

        return currentDate.plusDays(daysUntilSaturday)
                .withHour(12)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
