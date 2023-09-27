package talks.di.spring.xmlstraightforward;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration({
        "classpath:spring-context.xml",
        "classpath:spring-context-db-testcontainers.xml"
})
@Testcontainers
@ExtendWith({
        SpringExtension.class,
        TimeMachineJUnitExtension.class
})
class SpringXmlStraightforwardApplicationTestcontainersTest {

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
}