package talks.di.classes.logic.services.purchases;

import lombok.Value;

@Value
public class MakePurchaseRequest {
    String salesman;
    String buyer;
    Integer price;
}
