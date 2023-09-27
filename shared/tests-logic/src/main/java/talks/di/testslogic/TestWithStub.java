package talks.di.testslogic;

import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.models.Account;
import talks.di.classes.models.MoneyTransaction;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static talks.di.classes.time.TimeMachine.now;

@SuppressWarnings({"unchecked", "ArraysAsListWithZeroOrOneArgument"})
public class TestWithStub {

    public static void doTest(
            AccountRepository accountRepository,
            MoneyTransactionRepository moneyTransactionRepository,
            PurchasesOperation purchasesOperation
    ) {
        // Arrange
        var account1Before = new Account("account1", 1000);
        var account2Before = new Account("account2", 2000);
        var account1After = new Account("account1", 3000);
        var account2After = new Account("account2", 4000);
        when(accountRepository.findByName(eq("salesman"))).thenReturn(of(account1Before));
        when(accountRepository.findByName(eq("buyer"))).thenReturn(of(account2Before));
        when(accountRepository.findAll()).thenReturn(
                asList(account1Before, account2Before),
                asList(account1After, account2After));

        var oldTransaction = new MoneyTransaction("account1", "account2", 500, now());
        var newTransaction = new MoneyTransaction("account1", "account2", 1000, now());
        when(moneyTransactionRepository.findAll()).thenReturn(
                asList(oldTransaction),
                asList(oldTransaction, newTransaction));

        // Act
        var result = purchasesOperation.perform();

        // Assert
        assertThat(result.getAccountsBefore()).containsExactly(account1Before, account2Before);
        assertThat(result.getAccountsAfter()).containsExactly(account1After, account2After);
        assertThat(result.getMoneyTransactions()).containsExactly(newTransaction);
    }
}