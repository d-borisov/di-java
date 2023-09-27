package talks.di.spring.features.circulardependencies.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesCircularDependenciesProxy {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.proxy")) {
            var a = context.getBean(A.class);
            log.info("got: {}", a);
        }
    }

    @Component // mechanism resolves
    static class A {
        private final B b;

        public A(@Lazy B b) {
            log.info("Constructor of A. b: {}", b.getClass());
            this.b = b;
        }
    }

    @Component
    static class B {
        private final A a;

        public B(A a) {
            log.info("Constructor of B. a: {}", a.getClass());
            this.a = a;
        }
    }
}
