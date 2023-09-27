package talks.di.classes.logic.services.purchases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.persistence.AccountRepository;
import talks.di.classes.persistence.MoneyTransactionRepository;
import talks.di.classes.time.TimeMachine;

@Service
@AllArgsConstructor
public class ReturnPurchaseService {

    private final AccountRepository accountRepository;
    private final MoneyTransactionRepository moneyTransactionRepository;
    private final MoneyTransactionFactory moneyTransactionFactory;

    public void returnPurchase(ReturnPurchaseRequest request) {
        var buyer = accountRepository.findByName(request.getBuyer())
                .orElseThrow(() -> new IllegalArgumentException("No buyer found: " + request.getBuyer()));
        var salesman = accountRepository.findByName(request.getSalesman())
                .orElseThrow(() -> new IllegalArgumentException("No salesman found: " + request.getSalesman()));

        var price = request.getPrice();
        var updatedBuyer = buyer.increment(price);
        var updatedSalesman = salesman.decrement(price);
        var moneyTransaction = moneyTransactionFactory.create(
                salesman, buyer, price, TimeMachine.now());

        accountRepository.save(updatedBuyer);
        accountRepository.save(updatedSalesman);
        moneyTransactionRepository.save(moneyTransaction);
    }
}