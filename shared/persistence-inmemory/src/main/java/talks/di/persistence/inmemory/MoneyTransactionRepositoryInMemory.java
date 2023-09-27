package talks.di.persistence.inmemory;

import talks.di.classes.models.MoneyTransaction;
import talks.di.classes.persistence.MoneyTransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class MoneyTransactionRepositoryInMemory implements MoneyTransactionRepository {

    private final List<MoneyTransaction> list = new ArrayList<>();

    public List<MoneyTransaction> getInternalStorage() {
        return list;
    }

    @Override
    public List<MoneyTransaction> findAll() {
        return new ArrayList<>(list);
    }

    @Override
    public void save(MoneyTransaction moneyTransaction) {
        list.add(moneyTransaction);
    }
}
