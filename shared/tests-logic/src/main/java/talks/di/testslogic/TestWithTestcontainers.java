package talks.di.testslogic;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.models.Account;
import talks.di.classes.models.MoneyTransaction;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static talks.di.classes.time.TimeMachine.now;

@Slf4j
public class TestWithTestcontainers {

    public static void doTest(
            PostgreSQLContainer<?> postgresqlContainer,
            PurchasesOperation purchasesOperation

    ) throws Exception {
        // Arrange
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
        assertThat(runSql(postgresqlContainer, "SELECT name, money FROM accounts ORDER BY name")).containsExactly(
                "   name   | money ",
                "----------+-------",
                " buyer    |  1800",
                " salesman |  2200",
                "(2 rows)"
        );
        assertThat(runSql(postgresqlContainer, "SELECT from_account, to_account, amount, datetime FROM money_transactions ORDER BY datetime")).containsExactly(
                " from_account | to_account | amount |      datetime       ",
                "--------------+------------+--------+---------------------",
                " buyer        | salesman   |   1000 | 2023-09-01 12:45:00",
                " buyer        | salesman   |    200 | 2023-09-01 12:45:00",
                " salesman     | buyer      |   1000 | 2023-09-01 12:45:00",
                "(3 rows)"
        );
    }

    private static List<String> runSql(PostgreSQLContainer<?> postgresqlContainer, String sql) throws Exception {
        var res = postgresqlContainer.execInContainer("psql", "--username", "test", "--command", sql);
        if (res.getExitCode() != 0) {
            log.error(res.getStderr());
            throw new RuntimeException("Error while executing: " + sql);
        }
        return List.of(res.getStdout().split("\n"));
    }
}