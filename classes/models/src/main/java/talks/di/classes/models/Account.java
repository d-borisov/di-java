package talks.di.classes.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Account {

    private final String name;
    private final Integer money;

    public Account increment(Integer amount) {
        return this.toBuilder()
                .money(money + amount)
                .build();
    }

    public Account decrement(Integer amount) {
        return this.toBuilder()
                .money(money - amount)
                .build();
    }
}
