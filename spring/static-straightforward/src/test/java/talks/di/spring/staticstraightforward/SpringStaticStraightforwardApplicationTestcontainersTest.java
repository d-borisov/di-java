package talks.di.spring.staticstraightforward;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.support.StaticApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithTestcontainers;

@SuppressWarnings({"rawtypes", "resource"})
@Testcontainers
@ExtendWith(TimeMachineJUnitExtension.class)
class SpringStaticStraightforwardApplicationTestcontainersTest {

    @Container
    private static final PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:15.3")
            .withInitScript("db-init.sql");

    private StaticApplicationContext context;

    @BeforeEach
    void setUp() {
        context = SpringStaticStraightforwardApplication.buildContext();
        context.registerBean(
                "dbUrl",
                String.class,
                postgresqlContainer::getJdbcUrl,
                bd -> bd.setPrimary(true));
        context.registerBean(
                "dbUsername",
                String.class,
                postgresqlContainer::getUsername,
                bd -> bd.setPrimary(true));
        context.registerBean(
                "dbPassword",
                String.class,
                postgresqlContainer::getPassword,
                bd -> bd.setPrimary(true));
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void test() throws Exception {
        TestWithTestcontainers.doTest(
                postgresqlContainer,
                context.getBean("purchasesOperation", PurchasesOperation.class)
        );
    }
}