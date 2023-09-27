package talks.di.spring.springboot.requestscope.track;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@AllArgsConstructor
public class TrackController {
    private final ObjectFactory<StartTracker> startTrackerObjectFactory;
    private final TrackService accountService;

    @GetMapping("/data")
    public String getData() {
        startTrackerObjectFactory.getObject().start = Instant.now();
        var data = accountService.getData();
        log.info(data);
        return data;
    }
}
