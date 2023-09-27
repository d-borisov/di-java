package talks.di.guice.straightforward;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.ConnectionFactory;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;
import talks.di.utils.Json;

import java.sql.Connection;

@Slf4j
@AllArgsConstructor
public class GuiceStraightforwardApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) {
        var injector = Guice.createInjector(new GuiceModule());
        injector
                .getInstance(GuiceStraightforwardApplication.class)
                .performBusinessLogic();
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", Json.toJson(purchasesOperation.perform()));
    }

    public static class GuiceModule extends AbstractModule {

        @Override
        protected void configure() {
//            bind(Connection.class).toProvider(() -> ConnectionFactory.build(
//                    "jdbc:postgresql://localhost:5432/postgres",
//                    "postgres",
//                    "postgres"
//            ));
        }

        // params
        @Provides
        @Named("dbUrl")
        public String dbUrl() {
            return "jdbc:postgresql://localhost:5432/postgres";
        }

        @Provides
        @Named("dbUsername")
        public String dbUsername() {
            return "postgres";
        }

        @Provides
        @Named("dbPassword")
        public String dbPassword() {
            return "postgres";
        }

        // connection
        @Provides
        public Connection connection(
                @Named("dbUrl") String dbUrl,
                @Named("dbUsername") String dbUsername,
                @Named("dbPassword") String dbPassword) {
            return ConnectionFactory.build(dbUrl, dbUsername, dbPassword);
        }

        // repositories
        @Provides
        public AccountRepository accountRepository(Connection connection) {
            return new AccountRepositoryPersistent(connection);
        }

        @Provides
        public MoneyTransactionRepository moneyTransactionRepository(Connection connection) {
            return new MoneyTransactionRepositoryPersistent(connection);
        }

        // services
        @Provides
        public MoneyTransactionFactory moneyTransactionFactory() {
            return new MoneyTransactionFactory();
        }

        @Provides
        public MakePurchaseService makePurchaseService(
                AccountRepository accountRepository,
                MoneyTransactionRepository moneyTransactionRepository,
                MoneyTransactionFactory moneyTransactionFactory
        ) {
            return new MakePurchaseService(accountRepository, moneyTransactionRepository, moneyTransactionFactory);
        }

        @Provides
        public ReturnPurchaseService returnPurchaseService(
                AccountRepository accountRepository,
                MoneyTransactionRepository moneyTransactionRepository,
                MoneyTransactionFactory moneyTransactionFactory
        ) {
            return new ReturnPurchaseService(accountRepository, moneyTransactionRepository, moneyTransactionFactory);
        }

        // business logic
        @Provides
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
        @Provides
        public GuiceStraightforwardApplication guiceApplication(
                PurchasesOperation purchasesOperation
        ) {
            return new GuiceStraightforwardApplication(purchasesOperation);
        }
    }
}
