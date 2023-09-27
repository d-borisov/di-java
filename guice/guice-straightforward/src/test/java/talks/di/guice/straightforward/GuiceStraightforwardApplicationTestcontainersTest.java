package talks.di.guice.straightforward;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithTestcontainers;

@SuppressWarnings({"resource", "rawtypes"})
@Testcontainers
@ExtendWith(TimeMachineJUnitExtension.class)
class GuiceStraightforwardApplicationTestcontainersTest {

    @Container
    private final PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:15.3").withInitScript("db-init.sql");

    private Injector injector;

    @BeforeEach
    void setUp() {
        class TestModule extends AbstractModule {
            @Provides
            @Named("dbUrl")
            @Singleton
            public String dbUrl() {
                return postgresqlContainer.getJdbcUrl();
            }

            @Provides
            @Named("dbUsername")
            @Singleton
            public String dbUsername() {
                return postgresqlContainer.getUsername();
            }

            @Provides
            @Named("dbPassword")
            @Singleton
            public String dbPassword() {
                return postgresqlContainer.getPassword();
            }
        }
        injector = Guice.createInjector(Modules.override(new GuiceStraightforwardApplication.GuiceModule()).with(new TestModule()));
    }

    @Test
    void test() throws Exception {
        TestWithTestcontainers.doTest(postgresqlContainer, injector.getInstance(PurchasesOperation.class));
    }
}