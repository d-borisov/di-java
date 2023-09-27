package talks.di.dagger2.straightforward;

import dagger.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

import javax.inject.Singleton;
import java.sql.Connection;

@SuppressWarnings("FieldCanBeLocal")
@ExtendWith(TimeMachineJUnitExtension.class)
class Dagger2StraightforwardApplicationMockTest {

    private AccountRepositoryInMemory accountRepository;
    private MoneyTransactionRepositoryInMemory moneyTransactionRepository;

    private ApplicationTestComponent applicationTestComponent;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepositoryInMemory();
        moneyTransactionRepository = new MoneyTransactionRepositoryInMemory();
        applicationTestComponent = DaggerDagger2StraightforwardApplicationMockTest_ApplicationTestComponent.builder()
                .applicationModule(new Dagger2StraightforwardApplication.ApplicationModule() {
                    @Override
                    public Connection connection(String dbUrl, String dbUsername, String dbPassword) {
                        return Mockito.mock(Connection.class);
                    }

                    @Override
                    public AccountRepository accountRepository(Connection connection) {
                        return accountRepository;
                    }

                    @Override
                    public MoneyTransactionRepository moneyTransactionRepository(Connection connection) {
                        return moneyTransactionRepository;
                    }
                })
                .build();
    }

    @Test
    void test() {
        TestWithMock.doTest(
                accountRepository,
                moneyTransactionRepository,
                applicationTestComponent.getPurchasesOperation()
        );
    }

    @Singleton
    @Component(modules = {Dagger2StraightforwardApplication.ApplicationModule.class})
    public interface ApplicationTestComponent {
        PurchasesOperation getPurchasesOperation();
    }
}