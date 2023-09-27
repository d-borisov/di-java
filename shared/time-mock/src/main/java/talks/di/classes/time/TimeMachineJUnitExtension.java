package talks.di.classes.time;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Clock;

public class TimeMachineJUnitExtension implements AfterEachCallback, BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        TimeMachineMock.setTime(Clock.fixed(
                TimeMachineMock.NOW.toInstant(TimeMachineMock.ZONE_OFFSET),
                TimeMachineMock.ZONE));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        TimeMachineMock.reset();
    }
}