package talks.di.spring.features.circulardependencies.deferredaccess;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
public class SpringFeaturesCircularDependenciesDeferredAccess {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.deferredaccess")) {
            var a = context.getBean(A.class);
            log.info("got: {}", a);
            a.some();
        }
    }

    @Component
    static class A {
        private final ObjectFactory<B> b;

        public A(ObjectFactory<B> b) {
            log.info("Constructor of A");
            this.b = b;
        }

        void some() {
            log.info("Some method of A");
            b.getObject();
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
