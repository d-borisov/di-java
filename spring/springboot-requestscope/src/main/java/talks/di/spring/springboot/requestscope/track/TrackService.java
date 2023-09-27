package talks.di.spring.springboot.requestscope.track;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TrackService {
    private final ObjectFactory<StartTracker> startTrackerObjectFactory;

    public String getData() {
        return "" + startTrackerObjectFactory.getObject().start;
    }
}
