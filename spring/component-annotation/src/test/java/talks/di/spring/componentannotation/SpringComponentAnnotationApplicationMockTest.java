package talks.di.spring.componentannotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@ContextConfiguration(classes = {
        SpringComponentAnnotationApplicationMockTest.BeanDefinitions.class
})
@ExtendWith({
        SpringExtension.class,
        TimeMachineJUnitExtension.class
})
class SpringComponentAnnotationApplicationMockTest {

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

    @ComponentScan(
            lazyInit = true,
            value = {
                    "talks.di.classes.logic",
                    "talks.di.spring.componentannotation.configurations"
            })
    @Configuration
    public static class BeanDefinitions {
        @Bean
        public AccountRepositoryInMemory accountRepository() {
            return new AccountRepositoryInMemory();
        }

        @Bean
        public MoneyTransactionRepositoryInMemory moneyTransactionRepository() {
            return new MoneyTransactionRepositoryInMemory();
        }
    }
}