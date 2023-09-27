package talks.di.nocontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.ConnectionFactory;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.testslogic.TestWithTestcontainers;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings({"resource", "rawtypes"})
@Testcontainers
@ExtendWith(TimeMachineJUnitExtension.class)
class NoContainerApplicationTestcontainersTest {

    @Container
    private final PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:15.3")
            .withInitScript("db-init.sql");

    private Connection connection;
    private PurchasesOperation purchasesOperation;

    @BeforeEach
    void setUp() {
        connection = ConnectionFactory.build(
                postgresqlContainer.getJdbcUrl(),
                postgresqlContainer.getUsername(),
                postgresqlContainer.getPassword());

        var accountRepository = new AccountRepositoryPersistent(connection);
        var moneyTransactionRepository = new MoneyTransactionRepositoryPersistent(connection);
        var moneyTransactionFactory = new MoneyTransactionFactory();
        var makePurchaseService = new MakePurchaseService(
                accountRepository, moneyTransactionRepository, moneyTransactionFactory);
        var returnPurchaseService = new ReturnPurchaseService(
                accountRepository, moneyTransactionRepository, moneyTransactionFactory);

        purchasesOperation = new PurchasesOperation(
                accountRepository, moneyTransactionRepository, makePurchaseService, returnPurchaseService);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void test() throws Exception {
        TestWithTestcontainers.doTest(
                postgresqlContainer,
                purchasesOperation
        );
    }
}