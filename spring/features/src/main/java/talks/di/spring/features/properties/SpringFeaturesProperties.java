package talks.di.spring.features.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j
public class SpringFeaturesProperties {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext("talks.di.spring.features.properties")) {
            log.info("properties: {}", context.getBean(A.class).getProps());
        }
    }

    static class A {

        @Value("${some.property}")
        private String someProperty;

        @Autowired
        private Environment environment;

        public String getProps() {
            return someProperty + " :: " + environment.getProperty("another.property");
        }
    }

    @PropertySource("classpath:someFile.properties")
    @Configuration
    public static class BeanDefinitions {
        @Bean
        public A a() {
            return new A();
        }
    }
}
