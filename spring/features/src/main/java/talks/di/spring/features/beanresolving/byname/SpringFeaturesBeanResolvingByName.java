package talks.di.spring.features.beanresolving.byname;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesBeanResolvingByName {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.beanresolving.byname")) {
            log.info("autoName: {}, name1: {}, nameN: {}, nameM: {}, customName: {}",
                    context.getBean("springFeaturesBeanResolvingByName.A"),
                    context.getBean("name1", A.class),
                    context.getBean("nameN"),
                    context.getBean("nameM"),
                    context.getBean("customName")
            );
        }
    }

    @Component
    static class A {
    }

    @Component("customName")
    static class B {
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
