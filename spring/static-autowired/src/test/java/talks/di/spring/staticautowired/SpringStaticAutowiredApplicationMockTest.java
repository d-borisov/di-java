package talks.di.spring.staticautowired;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.support.StaticApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@ExtendWith(TimeMachineJUnitExtension.class)
class SpringStaticAutowiredApplicationMockTest {

    private StaticApplicationContext context;

    @BeforeEach
    void setUp() {
        context = SpringStaticAutowiredApplication.buildContext();
        context.registerBean(
                AccountRepositoryInMemory.class,
                bd -> bd.setPrimary(true));
        context.registerBean(
                MoneyTransactionRepositoryInMemory.class,
                bd -> bd.setPrimary(true));
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void test() {
        TestWithMock.doTest(
                context.getBean(AccountRepositoryInMemory.class),
                context.getBean(MoneyTransactionRepositoryInMemory.class),
                context.getBean(PurchasesOperation.class)
        );
    }
}