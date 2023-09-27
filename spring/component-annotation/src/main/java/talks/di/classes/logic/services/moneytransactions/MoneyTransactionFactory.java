package talks.di.classes.logic.services.moneytransactions;

import talks.di.classes.models.Account;
import talks.di.classes.models.MoneyTransaction;
import talks.di.spring.componentannotation.annotations.Creator;

import java.time.LocalDateTime;

@Creator
public class MoneyTransactionFactory {

    public MoneyTransaction create(Account from, Account to, Integer amount, LocalDateTime dateTime) {
        return MoneyTransaction.builder()
                .from(from.getName())
                .to(to.getName())
                .amount(amount)
                .dateTime(dateTime)
                .build();
    }
}
