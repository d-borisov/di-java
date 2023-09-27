package talks.di.testslogic;

import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.models.Account;
import talks.di.classes.models.MoneyTransaction;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;

import static org.assertj.core.api.Assertions.assertThat;
import static talks.di.classes.time.TimeMachine.now;

public class TestWithMock {

    public static void doTest(
            AccountRepositoryInMemory accountRepository,
            MoneyTransactionRepositoryInMemory moneyTransactionRepository,
            PurchasesOperation purchasesOperation
    ) {
        // Arrange
        var internalStorage = accountRepository.getInternalStorage();
        internalStorage.put("salesman", new Account("salesman", 2000));
        internalStorage.put("buyer", new Account("buyer", 2000));

        // Act
        var result = purchasesOperation.perform();

        // Assert
        assertThat(result.getAccountsBefore()).containsExactly(
                new Account("buyer", 2000),
                new Account("salesman", 2000));
        assertThat(result.getAccountsAfter()).containsExactly(
                new Account("buyer", 1800),
                new Account("salesman", 2200));
        assertThat(result.getMoneyTransactions()).containsExactly(
                new MoneyTransaction("buyer", "salesman", 1000, now()),
                new MoneyTransaction("buyer", "salesman", 200, now()),
                new MoneyTransaction("salesman", "buyer", 1000, now())
        );
        assertThat(accountRepository.getInternalStorage())
                .containsOnlyKeys(
                        "buyer",
                        "salesman")
                .containsValues(
                        new Account("buyer", 1800),
                        new Account("salesman", 2200)
                );
        assertThat(moneyTransactionRepository.getInternalStorage())
                .containsExactly(
                        new MoneyTransaction("buyer", "salesman", 1000, now()),
                        new MoneyTransaction("buyer", "salesman", 200, now()),
                        new MoneyTransaction("salesman", "buyer", 1000, now())
                );
    }
}