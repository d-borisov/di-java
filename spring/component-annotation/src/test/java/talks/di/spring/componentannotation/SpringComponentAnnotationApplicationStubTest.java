package talks.di.spring.componentannotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.testslogic.TestWithStub;

@ContextConfiguration(classes = {
        SpringComponentAnnotationApplicationStubTest.BeanDefinitions.class
})
@ExtendWith({SpringExtension.class,})
class SpringComponentAnnotationApplicationStubTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;
    @Autowired
    private PurchasesOperation purchasesOperation;

    @Test
    void test() {
        TestWithStub.doTest(accountRepository, moneyTransactionRepository, purchasesOperation);
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
        public AccountRepository accountRepository() {
            return Mockito.mock(AccountRepository.class);
        }

        @Bean
        public MoneyTransactionRepository moneyTransactionRepository() {
            return Mockito.mock(MoneyTransactionRepository.class);
        }
    }
}