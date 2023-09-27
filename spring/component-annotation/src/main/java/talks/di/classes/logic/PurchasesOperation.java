package talks.di.classes.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import talks.di.classes.logic.services.purchases.MakePurchaseRequest;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseRequest;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.models.Account;
import talks.di.classes.models.MoneyTransaction;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;

import java.util.ArrayList;
import java.util.List;

import static talks.di.utils.Json.toJson;

@Component
@Slf4j
@AllArgsConstructor
public class PurchasesOperation {

    private final AccountRepository accountRepository;
    private final MoneyTransactionRepository moneyTransactionRepository;
    private final MakePurchaseService makePurchaseService;
    private final ReturnPurchaseService returnPurchaseService;

    @Data
    @AllArgsConstructor
    public static class ResultOfOperation {
        private List<Account> accountsBefore;
        private List<Account> accountsAfter;
        private List<MoneyTransaction> moneyTransactions;
    }

    public ResultOfOperation perform() {
        var accountsBefore = accountRepository.findAll();
        var moneyTransactionsBefore = moneyTransactionRepository.findAll();
        log.info("Accounts before: {}", toJson(accountsBefore));
        log.info("MoneyTransactions before: {}", toJson(moneyTransactionsBefore));

        var makePurchase1000 = new MakePurchaseRequest("salesman", "buyer", 1000);
        var makePurchase200 = new MakePurchaseRequest("salesman", "buyer", 200);
        makePurchaseService.makePurchase(makePurchase1000);
        makePurchaseService.makePurchase(makePurchase200);

        var returnPurchase1000 = new ReturnPurchaseRequest("salesman", "buyer", 1000);
        returnPurchaseService.returnPurchase(returnPurchase1000);

        log.info("Purchases were processed");

        var accountsAfter = accountRepository.findAll();
        var moneyTransactionsAfter = moneyTransactionRepository.findAll();
        log.info("Accounts after: {}", toJson(accountsAfter));
        log.info("MoneyTransactions after: {}", toJson(moneyTransactionsAfter));

        var newTransactions = new ArrayList<>(moneyTransactionsAfter);
        newTransactions.removeAll(moneyTransactionsBefore);
        return new ResultOfOperation(accountsBefore, accountsAfter, newTransactions);
    }
}
