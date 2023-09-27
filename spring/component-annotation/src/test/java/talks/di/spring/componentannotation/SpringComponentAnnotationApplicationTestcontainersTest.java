package talks.di.spring.componentannotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithTestcontainers;

@SuppressWarnings({"rawtypes", "resource"})
@ContextConfiguration(classes = {
        SpringComponentAnnotationApplicationTestcontainersTest.BeanDefinitions.class
})
@Testcontainers
@ExtendWith({
        SpringExtension.class,
        TimeMachineJUnitExtension.class
})
class SpringComponentAnnotationApplicationTestcontainersTest {

    @Container
    private static final PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:15.3")
            .withInitScript("db-init.sql");

    @Autowired
    private PurchasesOperation purchasesOperation;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("testcontainers.postgres.jdbcUrl", postgresqlContainer::getJdbcUrl);
        registry.add("testcontainers.postgres.username", postgresqlContainer::getUsername);
        registry.add("testcontainers.postgres.password", postgresqlContainer::getPassword);
    }

    @Test
    void test() throws Exception {
        TestWithTestcontainers.doTest(
                postgresqlContainer,
                purchasesOperation
        );
    }

    @ComponentScan(
            lazyInit = true,
            value = {
                    "talks.di.classes",
                    "talks.di.spring.componentannotation.configurations"
            })
    @Configuration
    public static class BeanDefinitions {
        @Primary
        @Bean("dbUrl")
        public String dbUrl(@Value("${testcontainers.postgres.jdbcUrl}") String jdbcUrl) {
            return jdbcUrl;
        }

        @Primary
        @Bean("dbUsername")
        public String dbUsername(@Value("${testcontainers.postgres.username}") String username) {
            return username;
        }

        @Primary
        @Bean("dbPassword")
        public String dbPassword(@Value("${testcontainers.postgres.password}") String password) {
            return password;
        }
    }
}