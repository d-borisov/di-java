package talks.di.spring.configurationannotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@ContextConfiguration(classes = {
        SpringConfigurationAnnotationApplication.BeanDefinitions.class,
        SpringConfigurationAnnotationApplicationMockTest.BeanDefinitions.class
})
@ExtendWith({
        SpringExtension.class,
        TimeMachineJUnitExtension.class
})
class SpringConfigurationAnnotationApplicationMockTest {

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

    @Configuration
    public static class BeanDefinitions {
        @Primary
        @Bean
        public AccountRepositoryInMemory accountRepository() {
            return new AccountRepositoryInMemory();
        }

        @Primary
        @Bean
        public MoneyTransactionRepositoryInMemory moneyTransactionRepository() {
            return new MoneyTransactionRepositoryInMemory();
        }
    }
}