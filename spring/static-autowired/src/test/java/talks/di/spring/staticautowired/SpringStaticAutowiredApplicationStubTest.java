package talks.di.spring.staticautowired;

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
class SpringStaticAutowiredApplicationStubTest {

    private StaticApplicationContext context;

    @BeforeEach
    void setUp() {
        context = SpringStaticAutowiredApplication.buildContext();
        context.registerBean(
                AccountRepository.class,
                () -> Mockito.mock(AccountRepository.class),
                bd -> bd.setPrimary(true));
        context.registerBean(
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
                context.getBean(AccountRepository.class),
                context.getBean(MoneyTransactionRepository.class),
                context.getBean(PurchasesOperation.class)
        );
    }
}