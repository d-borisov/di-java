package talks.di.guice.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.testslogic.TestWithStub;

@ExtendWith(MockitoExtension.class)
class GuiceInjectApplicationStubTest {

    private Injector injector;

    @BeforeEach
    void setUp() {
        injector = Guice.createInjector(Modules
                .override(new GuiceInjectApplication.GuiceModule())
                .with(new TestModule())
        );
    }

    static class TestModule extends AbstractModule {
        @Provides
        @Singleton
        public AccountRepository accountRepository() {
            return Mockito.mock(AccountRepository.class);
        }

        @Provides
        @Singleton
        public MoneyTransactionRepository moneyTransactionRepository() {
            return Mockito.mock(MoneyTransactionRepository.class);
        }
    }

    @Test
    void test() {
        TestWithStub.doTest(
                injector.getInstance(AccountRepository.class),
                injector.getInstance(MoneyTransactionRepository.class),
                injector.getInstance(PurchasesOperation.class)
        );
    }
}