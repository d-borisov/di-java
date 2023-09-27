package talks.di.classes.time;

import java.time.Clock;
import java.time.LocalDateTime;

public class TimeMachine {

    private static Clock CLOCK = Clock.systemDefaultZone();

    /**
     * package-private for mocking
     */
    static void setClock(Clock clock) {
        CLOCK = clock;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(CLOCK);
    }
}
