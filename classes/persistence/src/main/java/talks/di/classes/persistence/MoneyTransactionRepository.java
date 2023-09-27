package talks.di.classes.persistence;

import talks.di.classes.models.MoneyTransaction;

import java.util.List;

public interface MoneyTransactionRepository {

    List<MoneyTransaction> findAll();

    void save(MoneyTransaction moneyTransaction);
}
