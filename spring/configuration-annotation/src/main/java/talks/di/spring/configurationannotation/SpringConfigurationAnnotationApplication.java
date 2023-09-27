package talks.di.spring.configurationannotation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.ConnectionFactory;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;

import java.sql.Connection;

import static talks.di.utils.Json.toJson;

@Slf4j
@AllArgsConstructor
public class SpringConfigurationAnnotationApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) {
        // build Spring context
        try (var context = new AnnotationConfigApplicationContext(BeanDefinitions.class)) {
            // application
            context.getBean(SpringConfigurationAnnotationApplication.class)
                    .performBusinessLogic();
        }
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", toJson(purchasesOperation.perform()));
    }

    @Lazy
    @Configuration
    public static class BeanDefinitions {

        // params
        @Bean
        public String dbUrl() {
            return "jdbc:postgresql://localhost:5432/postgres";
        }

        @Bean
        public String dbUsername() {
            return "postgres";
        }

        @Bean
        public String dbPassword() {
            return "postgres";
        }

        // connection
        @Bean
        public Connection connection(
                @Qualifier("dbUrl") String dbUrl,
                @Qualifier("dbUsername") String dbUsername,
                @Qualifier("dbPassword") String dbPassword) {
            return ConnectionFactory.build(dbUrl, dbUsername, dbPassword);
        }

        // repositories
        @Bean
        public AccountRepository accountRepository(Connection connection) {
            return new AccountRepositoryPersistent(connection);
        }

        @Bean
        public MoneyTransactionRepository moneyTransactionRepository(Connection connection) {
            return new MoneyTransactionRepositoryPersistent(connection);
        }

        // services
        @Bean
        public MoneyTransactionFactory moneyTransactionFactory() {
            return new MoneyTransactionFactory();
        }

        @Bean
        public MakePurchaseService makePurchaseService(
                AccountRepository accountRepository,
                MoneyTransactionRepository moneyTransactionRepository,
                MoneyTransactionFactory moneyTransactionFactory
        ) {
            return new MakePurchaseService(accountRepository, moneyTransactionRepository, moneyTransactionFactory);
        }

        @Bean
        public ReturnPurchaseService returnPurchaseService(
                AccountRepository accountRepository,
                MoneyTransactionRepository moneyTransactionRepository,
                MoneyTransactionFactory moneyTransactionFactory
        ) {
            return new ReturnPurchaseService(accountRepository, moneyTransactionRepository, moneyTransactionFactory);
        }

        // business logic
        @Bean
        public PurchasesOperation purchasesOperation(
                AccountRepository accountRepository,
                MoneyTransactionRepository moneyTransactionRepository,
                MakePurchaseService makePurchaseService,
                ReturnPurchaseService returnPurchaseService
        ) {
            return new PurchasesOperation(
                    accountRepository, moneyTransactionRepository, makePurchaseService, returnPurchaseService);
        }

        // application
        @Bean
        public SpringConfigurationAnnotationApplication springConfigurationAnnotationApplication(
                PurchasesOperation purchasesOperation
        ) {
            return new SpringConfigurationAnnotationApplication(purchasesOperation);
        }
    }
}
