package talks.di.spring.staticstraightforward;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.support.StaticApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@ExtendWith(TimeMachineJUnitExtension.class)
class SpringStaticStraightforwardApplicationMockTest {

    private StaticApplicationContext context;

    @BeforeEach
    void setUp() {
        context = SpringStaticStraightforwardApplication.buildContext();
        context.registerBean(
                "accountRepository",
                AccountRepository.class,
                AccountRepositoryInMemory::new,
                bd -> bd.setPrimary(true));
        context.registerBean(
                "moneyTransactionRepository",
                MoneyTransactionRepository.class,
                MoneyTransactionRepositoryInMemory::new,
                bd -> bd.setPrimary(true));
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void test() {
        TestWithMock.doTest(
                context.getBean("accountRepository", AccountRepositoryInMemory.class),
                context.getBean("moneyTransactionRepository", MoneyTransactionRepositoryInMemory.class),
                context.getBean("purchasesOperation", PurchasesOperation.class)
        );
    }
}