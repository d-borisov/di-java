package talks.di.spring.xmlstraightforward;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.utils.Json;

@Slf4j
@AllArgsConstructor
public class SpringXmlStraightforwardApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) {
        // build Spring context
        try (var context = new ClassPathXmlApplicationContext("spring-context.xml")) {
            // application
            context.getBean(SpringXmlStraightforwardApplication.class)
                    .performBusinessLogic();
        }
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", Json.toJson(purchasesOperation.perform()));
    }
}
