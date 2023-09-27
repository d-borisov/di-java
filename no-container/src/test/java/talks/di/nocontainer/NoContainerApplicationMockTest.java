package talks.di.nocontainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.time.TimeMachineJUnitExtension;
import talks.di.persistence.inmemory.AccountRepositoryInMemory;
import talks.di.persistence.inmemory.MoneyTransactionRepositoryInMemory;
import talks.di.testslogic.TestWithMock;

@SuppressWarnings("FieldCanBeLocal")
@ExtendWith(TimeMachineJUnitExtension.class)
class NoContainerApplicationMockTest {

    private AccountRepositoryInMemory accountRepository;
    private MoneyTransactionRepositoryInMemory moneyTransactionRepository;

    private PurchasesOperation purchasesOperation;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepositoryInMemory();
        moneyTransactionRepository = new MoneyTransactionRepositoryInMemory();
        var moneyTransactionFactory = new MoneyTransactionFactory();
        var makePurchaseService = new MakePurchaseService(
                accountRepository, moneyTransactionRepository, moneyTransactionFactory);
        var returnPurchaseService = new ReturnPurchaseService(
                accountRepository, moneyTransactionRepository, moneyTransactionFactory);

        purchasesOperation = new PurchasesOperation(
                accountRepository, moneyTransactionRepository, makePurchaseService, returnPurchaseService);
    }

    @Test
    void test() {
        TestWithMock.doTest(
                accountRepository,
                moneyTransactionRepository,
                purchasesOperation
        );
    }
}