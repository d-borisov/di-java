package talks.di.persistence.inmemory;

import talks.di.classes.models.Account;
import talks.di.classes.persistence.AccountRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public class AccountRepositoryInMemory implements AccountRepository {

    private final Map<String, Account> map = new HashMap<>();

    public Map<String, Account> getInternalStorage() {
        return map;
    }

    @Override
    public List<Account> findAll() {
        var order = new TreeSet<>(Comparator.comparing(Account::getName));
        order.addAll(map.values());
        return new ArrayList<>(order);
    }

    @Override
    public void save(Account account) {
        map.put(account.getName(), account);
    }

    @Override
    public Optional<Account> findByName(String name) {
        return Optional.of(map.get(name));
    }
}
