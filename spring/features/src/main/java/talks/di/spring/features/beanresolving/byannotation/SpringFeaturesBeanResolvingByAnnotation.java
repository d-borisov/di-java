package talks.di.spring.features.beanresolving.byannotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesBeanResolvingByAnnotation {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.beanresolving.byannotation")) {
            log.info("beans: {}", context.getBeansWithAnnotation(Component.class));
        }
    }

    @Component
    static class A {
    }

    @Component
    static class B {
    }
}
