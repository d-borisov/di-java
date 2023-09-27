package talks.di.spring.features.dependencyresolving;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class SpringFeaturesDependencyResolving {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.dependencyresolving")) {
            var a = context.getBean(A.class);
            log.info("a: {}, field: {}", a, a.dependency1);
        }
    }

    @Component
    static class A {
        @Autowired
        private Multiple dependency1; // not required to be public

        @Autowired
        public A( // not required to be public
                  @Autowired Single single,  // @Autowired here is outdated API
                  Multiple dependency1,
                  Optional<Multiple> dependency2,
                  @Qualifier("specific") Multiple specificDependency,
                  @Nullable Absent absentDependency,
                  Map<String, Multiple> dependenciesMap,
                  Set<Multiple> dependenciesSet,
                  List<Multiple> dependenciesList
        ) {
            log.info(
                    "constructor. single: {}, dependency1: {}, dependency2: {}, specific: {}, absence: {}, map: {}, set: {}, list: {}",
                    single,
                    dependency1,
                    dependency2,
                    specificDependency,
                    absentDependency,
                    dependenciesMap,
                    dependenciesSet,
                    dependenciesList
            );
        }

        @Autowired
        public void method(Multiple dependency1, Multiple dependency2) {  // not required to be public
            log.info("method. {}, {}", dependency1, dependency2);
        }
    }

    static class Multiple {
    }

    static class Single {
    }

    static class Absent {
    }

    @Configuration
    public static class BeanDefinitions {
        @Bean
        public Multiple dependency1() {
            return new Multiple();
        }

        @Bean
        public Multiple dependency2() {
            return new Multiple();
        }

        @Qualifier("specific")
        @Bean
        public Multiple withQualifier() {
            return new Multiple();
        }

        @Bean
        public Single anyName() {
            return new Single();
        }
    }
}
