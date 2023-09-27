package talks.di.spring.features.circulardependencies.container;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesCircularDependenciesContainer {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.container")) {
            var a = context.getBean(A.class);
            log.info("got: {}", a);
            a.some();
        }
    }

    @Component
    static class A {
        private final ApplicationContext applicationContext;

        public A(ApplicationContext applicationContext) {
            log.info("Constructor of A");
            this.applicationContext = applicationContext;
        }

        void some() {
            log.info("Some method of A");
            applicationContext.getBean(B.class);
        }
    }

    @Component
    static class B {
        private final A a;

        public B(A a) {
            log.info("Constructor of B");
            this.a = a;
        }
    }
}
