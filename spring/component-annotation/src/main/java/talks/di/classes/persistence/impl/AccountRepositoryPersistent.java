package talks.di.classes.persistence.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import talks.di.classes.models.Account;
import talks.di.classes.persistence.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccountRepositoryPersistent implements AccountRepository {

    private final Connection connection;

    @Override
    public List<Account> findAll() {
        var result = new ArrayList<Account>();
        try (
                var ps = this.connection.prepareStatement(
                        "SELECT name, money FROM accounts ORDER BY name");
                var rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                result.add(Account.builder()
                        .name(rs.getString("name"))
                        .money(rs.getInt("money"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void save(Account account) {
        try (var ps = this.connection.prepareStatement(
                "UPDATE accounts SET money=? WHERE name=?")
        ) {
            ps.setInt(1, account.getMoney());
            ps.setString(2, account.getName());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException(
                        "UPDATE was not applied for: " + account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> findByName(String name) {
        try (var ps = this.connection.prepareStatement(
                "SELECT name, money FROM accounts WHERE name=? LIMIT 1")
        ) {
            ps.setString(1, name);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(Account.builder()
                        .name(rs.getString("name"))
                        .money(rs.getInt("money"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
