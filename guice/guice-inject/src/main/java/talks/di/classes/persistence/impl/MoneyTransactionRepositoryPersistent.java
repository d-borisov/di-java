package talks.di.classes.persistence.impl;

import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import talks.di.classes.models.MoneyTransaction;
import talks.di.classes.persistence.MoneyTransactionRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(onConstructor_ = {@Inject})
public class MoneyTransactionRepositoryPersistent implements MoneyTransactionRepository {

    private final Connection connection;

    @Override
    public List<MoneyTransaction> findAll() {
        var result = new ArrayList<MoneyTransaction>();
        try (
                var ps = this.connection.prepareStatement(
                        "SELECT from_account, to_account, amount, datetime FROM money_transactions ORDER BY datetime");
                var rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                result.add(MoneyTransaction.builder()
                        .from(rs.getString("from_account"))
                        .to(rs.getString("to_account"))
                        .amount(rs.getInt("amount"))
                        .dateTime(rs.getObject("datetime", LocalDateTime.class))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    @Override
    public void save(MoneyTransaction moneyTransaction) {
        try (var ps = this.connection.prepareStatement(
                "INSERT INTO money_transactions(from_account, to_account, amount, datetime) " +
                        "VALUES (?, ?, ?, ?)")
        ) {
            ps.setString(1, moneyTransaction.getFrom());
            ps.setString(2, moneyTransaction.getTo());
            ps.setInt(3, moneyTransaction.getAmount());
            ps.setObject(4, moneyTransaction.getDateTime());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException(
                        "UPDATE was not applied for: " + moneyTransaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
