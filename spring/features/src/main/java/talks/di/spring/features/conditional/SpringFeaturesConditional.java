package talks.di.spring.features.conditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Slf4j
public class SpringFeaturesConditional {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.conditional")) {
            log.info("beans: {}", context.getBeansOfType(A.class));
        }
    }

    static class A {
    }

    @Configuration
    public static class BeanDefinitions {
        @ConditionalFlag(flag = false)
        @Bean
        public A aForFalse() {
            return new A();
        }

        @ConditionalFlag(flag = false)
        @Bean
        public A aForTrue() {
            return new A();
        }
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Conditional({FlagCondition.class})
    public @interface ConditionalFlag {
        boolean flag() default true;
    }

    public static class FlagCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            var attrs = metadata.getAnnotationAttributes(ConditionalFlag.class.getName());
            return attrs != null && (boolean) attrs.get("flag");
        }
    }
}
