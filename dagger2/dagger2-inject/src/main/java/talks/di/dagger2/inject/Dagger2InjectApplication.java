package talks.di.dagger2.inject;

import dagger.Component;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class Dagger2InjectApplication {

    public static void main(String[] args) throws Exception {
        var component = DaggerDagger2InjectApplication_ApplicationComponent.create();
        log.info("Got a: {}", component.getA());
    }

    static class A {
        private final B b;

        @Inject
        public A(B b) {
            this.b = b;
            log.info("Constructor of A. b: {}. this: {}", b, this);
        }
    }

    static class B {
        @Inject
        public B() {
            log.info("Constructor of B. this: {}", this);
        }
    }

    @Component
    public interface ApplicationComponent {
        A getA();
    }
}
