package talks.di.spring.staticstraightforward;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.support.StaticApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithStub;

@ExtendWith(TimeMachineJUnitExtension.class)
class SpringStaticStraightforwardApplicationStubTest {

    private StaticApplicationContext context;

    @BeforeEach
    void setUp() {
        context = SpringStaticStraightforwardApplication.buildContext();
        context.registerBean(
                "accountRepository",
                AccountRepository.class,
                () -> Mockito.mock(AccountRepository.class),
                bd -> bd.setPrimary(true));
        context.registerBean(
                "moneyTransactionRepository",
                MoneyTransactionRepository.class,
                () -> Mockito.mock(MoneyTransactionRepository.class),
                bd -> bd.setPrimary(true));
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void test() {
        TestWithStub.doTest(
                context.getBean("accountRepository", AccountRepository.class),
                context.getBean("moneyTransactionRepository", MoneyTransactionRepository.class),
                context.getBean("purchasesOperation", PurchasesOperation.class)
        );
    }
}