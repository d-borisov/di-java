package talks.di.dagger2.straightforward;

import dagger.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.testslogic.TestWithStub;

import javax.inject.Singleton;
import java.sql.Connection;

@ExtendWith(MockitoExtension.class)
class Dagger2StraightforwardApplicationStubTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MoneyTransactionRepository moneyTransactionRepository;

    private Dagger2StraightforwardApplicationStubTest.ApplicationTestComponent applicationTestComponent;

    @BeforeEach
    void setUp() {
        applicationTestComponent = DaggerDagger2StraightforwardApplicationStubTest_ApplicationTestComponent.builder()
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
        TestWithStub.doTest(
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