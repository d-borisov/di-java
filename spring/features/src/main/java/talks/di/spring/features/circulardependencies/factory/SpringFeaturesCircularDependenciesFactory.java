package talks.di.spring.features.circulardependencies.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Slf4j
public class SpringFeaturesCircularDependenciesFactory {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.factory")) {
            var a = context.getBean(A.class);
            log.info("got: {}", a);
            a.logic();
        }
    }

    static class A {
        private final BeanDefinitions bd;

        public A(BeanDefinitions bd) {
            log.info("Constructor of A. {}", bd);
            this.bd = bd;
        }

        void logic() {
            bd.b(this);
            log.info("logic of A");
        }
    }

    static class B {
        private final A a;

        public B(A a) {
            log.info("Constructor of B");
            this.a = a;
        }
    }

    @Lazy
    @Configuration
    public static class BeanDefinitions {

        @Bean
        public A a() {
            return new A(this);
        }

        @Bean
        public B b(A a) {
            return new B(a);
        }
    }
}
