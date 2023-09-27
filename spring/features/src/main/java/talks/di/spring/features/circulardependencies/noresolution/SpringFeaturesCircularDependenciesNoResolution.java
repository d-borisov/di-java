package talks.di.spring.features.circulardependencies.noresolution;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesCircularDependenciesNoResolution {

//    public static void main(String[] args) {
//        new A(new B(new A(new B(new A(...)))));
//    }

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.noresolution")) {
            var a = context.getBean(A.class);
            log.info("got: {}", a);
        }
    }

    @Component
    @AllArgsConstructor
    static class A {
        private final B b;
    }

    @Component
    @AllArgsConstructor
    static class B {
        private final A a;
    }
}
