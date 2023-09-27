package talks.di.spring.features.circulardependencies.getteroverride;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class SpringFeaturesCircularDependenciesGetterOverride {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.circulardependencies.getteroverride")) {
            var a = context.getBean(A.class);
            log.info("got: {}", a);
            a.logic();
        }
    }

    @Component
    static class A {
        @Lookup
        B getB() {
            return null;
        }

        public void logic() {
            log.info("A logic. b: {}", getB());
        }
    }

    @Component
    static class B {
        private final A a;

        public B(A a) {
            log.info("Constructor of B. a: {}", a);
            this.a = a;
        }
    }

    static class asd {
        @Getter
        @AllArgsConstructor
        static class A {
            private final B b;

            public void logic() {
                getB();
            }
        }

        @AllArgsConstructor
        static class B {
            private final A a;
        }

        public static void viaGetterOverride() {
            var b = new AtomicReference<B>(); // effectively final. any container: Set/Map/etc.
            class AA extends A {
                public AA() {
                    super(null);
                }

                @Override
                public B getB() {
                    return b.get();
                }
            }
            var a = new AA();
            b.set(new B(a));
        }

    }
}
