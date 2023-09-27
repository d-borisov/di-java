package talks.di.nocontainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.testslogic.TestWithStub;

@ExtendWith(MockitoExtension.class)
class NoContainerApplicationStubTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MoneyTransactionRepository moneyTransactionRepository;

    private PurchasesOperation purchasesOperation;

    @BeforeEach
    void setUp() {
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
        TestWithStub.doTest(
                accountRepository,
                moneyTransactionRepository,
                purchasesOperation
        );
    }
}