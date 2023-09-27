package talks.di.spring.componentannotation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.spring.componentannotation.annotations.App;
import talks.di.utils.Json;

@App
@Slf4j
@AllArgsConstructor
public class SpringComponentAnnotationApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) {
        // build Spring context
        try (var context = new AnnotationConfigApplicationContext(
                "talks.di.classes",
                "talks.di.spring.componentannotation"
        )) {
            // application
            context.getBean(SpringComponentAnnotationApplication.class)
                    .performBusinessLogic();
        }
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", Json.toJson(purchasesOperation.perform()));
    }

}
