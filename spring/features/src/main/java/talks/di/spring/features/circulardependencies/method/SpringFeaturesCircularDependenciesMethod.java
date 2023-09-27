package talks.di.spring.features.circulardependencies.method;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesCircularDependenciesMethod {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.method")) {
            var a = context.getBean(A.class);
            log.info("a: {}, a.b: {}", a, a.b);
        }
    }

    @Component
    static class A {
        private B b;

        public A() {
            log.info("Constructor of A");
        }

        @Autowired
        public void setB(B b) {
            log.info("Setter of A");
            this.b = b;
        }
    }

    @Lazy
    @Component
    static class B {
        private final A a;

        public B(A a) {
            log.info("Constructor of B");
            this.a = a;
        }
    }
}
