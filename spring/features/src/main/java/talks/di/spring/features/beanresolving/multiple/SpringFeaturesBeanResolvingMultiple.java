package talks.di.spring.features.beanresolving.multiple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesBeanResolvingMultiple {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.beanresolving.multiple")) {
            log.info("beans: {}", context.getBeansOfType(A.class));
        }
    }

    @Component
    static class A {
    }

    @Configuration
    static class BeanDefinitions {

        @Bean
        A name1() {
            return new A();
        }

        @Bean(name = {"nameN", "nameM"})
        A name2() {
            return new A();
        }
    }
}
