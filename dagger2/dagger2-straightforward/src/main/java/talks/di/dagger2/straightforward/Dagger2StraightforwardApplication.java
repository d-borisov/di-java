package talks.di.dagger2.straightforward;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
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

import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;

import static talks.di.utils.Json.toJson;

@Slf4j
@AllArgsConstructor
public class Dagger2StraightforwardApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) throws Exception {
        var component = DaggerDagger2StraightforwardApplication_ApplicationComponent.create();
        component.getApplication().performBusinessLogic();
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", toJson(purchasesOperation.perform()));
    }

    @Module
    public static class ApplicationModule {

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
        @Singleton
        public AccountRepository accountRepository(Connection connection) {
            return new AccountRepositoryPersistent(connection);
        }

        @Provides
        @Singleton
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
        public Dagger2StraightforwardApplication dagger2StraightforwardApplication(
                PurchasesOperation purchasesOperation
        ) {
            return new Dagger2StraightforwardApplication(purchasesOperation);
        }
    }

    @Singleton
    @Component(modules = {ApplicationModule.class})
    public interface ApplicationComponent {
        Dagger2StraightforwardApplication getApplication();
    }
}
