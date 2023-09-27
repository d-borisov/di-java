package talks.di.spring.xmlstraightforward;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@ContextConfiguration({
        "classpath:spring-context.xml",
        "classpath:spring-context-db-mock.xml"
})
@ExtendWith({
        SpringExtension.class,
        TimeMachineJUnitExtension.class
})
class SpringXmlStraightforwardApplicationMockTest {

    @Autowired
    private AccountRepositoryInMemory accountRepository;
    @Autowired
    private MoneyTransactionRepositoryInMemory moneyTransactionRepository;
    @Autowired
    private PurchasesOperation purchasesOperation;

    @Test
    void test() {
        TestWithMock.doTest(
                accountRepository,
                moneyTransactionRepository,
                purchasesOperation
        );
    }
}