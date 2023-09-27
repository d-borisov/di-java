package talks.di.classes.logic.services.purchases;

import lombok.Value;

@Value
public class ReturnPurchaseRequest {
    String salesman;
    String buyer;
    Integer price;
}
