package talks.di.nocontainer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.ConnectionFactory;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;
import talks.di.utils.Json;

@Slf4j
@AllArgsConstructor
public class NoContainerApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) throws Exception {
        var dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        var dbUser = "postgres";
        var dbPassword = "postgres";
        try (var connection = ConnectionFactory.build(dbUrl, dbUser, dbPassword)) {
            // repos
            var accountRepository = new AccountRepositoryPersistent(connection);
            var moneyTransactionRepository = new MoneyTransactionRepositoryPersistent(connection);
            // services
            var moneyTransactionFactory = new MoneyTransactionFactory();
            var makePurchaseService = new MakePurchaseService(
                    accountRepository, moneyTransactionRepository, moneyTransactionFactory);
            var returnPurchaseService = new ReturnPurchaseService(
                    accountRepository, moneyTransactionRepository, moneyTransactionFactory);
            // business logic
            var purchasesOperation = new PurchasesOperation(
                    accountRepository, moneyTransactionRepository, makePurchaseService, returnPurchaseService);
            // application
            new NoContainerApplication(purchasesOperation)
                    .performBusinessLogic();
        }
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", Json.toJson(purchasesOperation.perform()));
    }
}
