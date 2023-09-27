package talks.di.classes.persistence;

import talks.di.classes.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> findAll();

    void save(Account account);

    Optional<Account> findByName(String name);
}
