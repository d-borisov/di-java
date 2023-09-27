package talks.di.spring.features.beanresolving.bytype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesBeanResolvingByType {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.beanresolving.bytype")) {
            log.info("got a: {}, b: {}, ab: {}", context.getBean(A.class), context.getBean(B.class), context.getBean(AB.class));
        }
    }

    interface A {
    }

    interface B {
    }

    @Component
    static class AB implements A, B {
        public AB() {
            log.info("Constructor of AB class");
        }
    }
}

