package talks.di.guice.straightforward;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@ExtendWith(TimeMachineJUnitExtension.class)
class GuiceStraightforwardApplicationMockTest {

    private Injector injector;

    @BeforeEach
    void setUp() {
        injector = Guice.createInjector(Modules
                .override(new GuiceStraightforwardApplication.GuiceModule())
                .with(new TestModule())
        );
    }

    static class TestModule extends AbstractModule {
        @Provides
        @Singleton
        public AccountRepository accountRepository() {
            return new AccountRepositoryInMemory();
        }

        @Provides
        @Singleton
        public MoneyTransactionRepository moneyTransactionRepository() {
            return new MoneyTransactionRepositoryInMemory();
        }
    }

    @Test
    void test() {
        TestWithMock.doTest(
                (AccountRepositoryInMemory) injector.getInstance(AccountRepository.class),
                (MoneyTransactionRepositoryInMemory) injector.getInstance(MoneyTransactionRepository.class),
                injector.getInstance(PurchasesOperation.class)
        );
    }
}