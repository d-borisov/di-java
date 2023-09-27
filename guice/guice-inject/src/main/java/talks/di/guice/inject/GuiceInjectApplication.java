package talks.di.guice.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Names;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;
import talks.di.guice.inject.configurations.Conn;
import talks.di.utils.Json;

import java.sql.Connection;

@Slf4j
@AllArgsConstructor(onConstructor_ = {@Inject})
public class GuiceInjectApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) throws Exception {
        var injector = Guice.createInjector(new GuiceModule());
        injector
                .getInstance(GuiceInjectApplication.class)
                .performBusinessLogic();
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", Json.toJson(purchasesOperation.perform()));
    }

    public static class GuiceModule extends AbstractModule {

        @Override
        protected void configure() {
            // params
            bindConstant().annotatedWith(Names.named("dbUrl")).to("jdbc:postgresql://localhost:5432/postgres");
            bindConstant().annotatedWith(Names.named("dbUsername")).to("postgres");
            bindConstant().annotatedWith(Names.named("dbPassword")).to("postgres");
            // connection
            bind(Connection.class).to(Conn.class);
            // repositories
            bind(AccountRepository.class).to(AccountRepositoryPersistent.class);
            bind(MoneyTransactionRepository.class).to(MoneyTransactionRepositoryPersistent.class);
            // services
            bind(MoneyTransactionFactory.class);
            bind(MakePurchaseService.class);
            bind(ReturnPurchaseService.class);
            // business logic
            bind(PurchasesOperation.class);
            // application
            bind(GuiceInjectApplication.class);
        }
    }
}
