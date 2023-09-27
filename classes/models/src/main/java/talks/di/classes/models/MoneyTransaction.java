package talks.di.classes.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MoneyTransaction {

    private final String from;
    private final String to;
    private final Integer amount;
    private final LocalDateTime dateTime;
}
