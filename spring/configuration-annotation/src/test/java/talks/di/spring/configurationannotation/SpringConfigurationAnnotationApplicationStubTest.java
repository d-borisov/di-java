package talks.di.spring.configurationannotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.testslogic.TestWithStub;

@ContextConfiguration(classes = {
        SpringConfigurationAnnotationApplication.BeanDefinitions.class,
        SpringConfigurationAnnotationApplicationStubTest.BeanDefinitions.class
})
@ExtendWith({
        SpringExtension.class,
})
class SpringConfigurationAnnotationApplicationStubTest {

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

    @Configuration
    public static class BeanDefinitions {
        @Primary
        @Bean
        public AccountRepository accountRepository() {
            return Mockito.mock(AccountRepository.class);
        }

        @Primary
        @Bean
        public MoneyTransactionRepository moneyTransactionRepository() {
            return Mockito.mock(MoneyTransactionRepository.class);
        }
    }
}