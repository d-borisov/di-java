package talks.di.spring.springboot.requestscope.track;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;

@Slf4j
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Component
public class StartTracker {
    public Instant start;

    public StartTracker() {
        log.info("created");
    }

    @PostConstruct
    private void postConstruct() {
        log.info("constructed");
    }

    @PreDestroy
    private void preDestroy() {
        log.info("to be destroyed");
    }
}
