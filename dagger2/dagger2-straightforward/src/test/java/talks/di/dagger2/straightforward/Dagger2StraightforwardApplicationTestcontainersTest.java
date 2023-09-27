package talks.di.dagger2.straightforward;


import dagger.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithTestcontainers;

import javax.inject.Singleton;

@SuppressWarnings({"resource", "rawtypes"})
@Testcontainers
@ExtendWith(TimeMachineJUnitExtension.class)
class Dagger2StraightforwardApplicationTestcontainersTest {

    @Container
    private final PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:15.3")
            .withInitScript("db-init.sql");

    private Dagger2StraightforwardApplicationTestcontainersTest.ApplicationTestComponent applicationTestComponent;

    @BeforeEach
    void setUp() {
        applicationTestComponent = DaggerDagger2StraightforwardApplicationTestcontainersTest_ApplicationTestComponent.builder()
                .applicationModule(new Dagger2StraightforwardApplication.ApplicationModule() {
                    @Override
                    public String dbUrl() {
                        return postgresqlContainer.getJdbcUrl();
                    }

                    @Override
                    public String dbUsername() {
                        return postgresqlContainer.getUsername();
                    }

                    @Override
                    public String dbPassword() {
                        return postgresqlContainer.getPassword();
                    }
                })
                .build();
    }

    @Test
    void test() throws Exception {
        TestWithTestcontainers.doTest(
                postgresqlContainer,
                applicationTestComponent.getPurchasesOperation()
        );
    }

    @Singleton
    @Component(modules = {Dagger2StraightforwardApplication.ApplicationModule.class})
    public interface ApplicationTestComponent {
        PurchasesOperation getPurchasesOperation();
    }
}