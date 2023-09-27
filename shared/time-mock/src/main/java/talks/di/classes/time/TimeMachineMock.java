package talks.di.classes.time;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeMachineMock {

    public static final LocalDateTime NOW = LocalDateTime.of(2023, 9, 1, 12, 45);
    public static final ZoneId ZONE = ZoneId.of("UTC");
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

    public static void setTime(Clock clock) {
        TimeMachine.setClock(clock);
    }

    public static void reset() {
        TimeMachine.setClock(Clock.systemDefaultZone());
    }
}
