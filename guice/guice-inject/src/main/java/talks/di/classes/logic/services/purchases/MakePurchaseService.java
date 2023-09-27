package talks.di.classes.logic.services.purchases;

import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachine;

@AllArgsConstructor(onConstructor_ = {@Inject})
public class MakePurchaseService {

    private final AccountRepository accountRepository;
    private final MoneyTransactionRepository moneyTransactionRepository;
    private final MoneyTransactionFactory moneyTransactionFactory;

    public void makePurchase(MakePurchaseRequest request) {
        var buyer = accountRepository.findByName(request.getBuyer())
                .orElseThrow(() -> new IllegalArgumentException("No buyer found: " + request.getBuyer()));
        var salesman = accountRepository.findByName(request.getSalesman())
                .orElseThrow(() -> new IllegalArgumentException("No salesman found: " + request.getSalesman()));

        var price = request.getPrice();
        var updatedBuyer = buyer.decrement(price);
        var updatedSalesman = salesman.increment(price);
        var moneyTransaction = moneyTransactionFactory.create(
                buyer, salesman, price, TimeMachine.now());

        accountRepository.save(updatedBuyer);
        accountRepository.save(updatedSalesman);
        moneyTransactionRepository.save(moneyTransaction);
    }
}
