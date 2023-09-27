package talks.di.spring.xmlstraightforward;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithStub;

@SuppressWarnings({"unchecked", "ArraysAsListWithZeroOrOneArgument"})
@ContextConfiguration({
        "classpath:spring-context.xml",
        "classpath:spring-context-db-stub.xml"
})
@ExtendWith({
        SpringExtension.class,
        TimeMachineJUnitExtension.class
})
class SpringXmlStraightforwardApplicationStubTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;
    @Autowired
    private PurchasesOperation purchasesOperation;

    @Test
    void test() {
        TestWithStub.doTest(
                accountRepository,
                moneyTransactionRepository,
                purchasesOperation
        );
    }
}