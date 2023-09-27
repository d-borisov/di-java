package talks.di.spring.staticautowired;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.StaticApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.ConnectionFactory;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;

import java.sql.Connection;

import static talks.di.utils.Json.toJson;

@Slf4j
@AllArgsConstructor
public class SpringStaticAutowiredApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) {
        // build Spring context
        try (var context = buildContext()) {
            // application
            context.getBean(SpringStaticAutowiredApplication.class)
                    .performBusinessLogic();
        }
    }

    /**
     * It is still unclear why {@link StaticApplicationContext} performs autowiring even without
     * {@link org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor} registered.
     */
    static StaticApplicationContext buildContext() {
        var context = new StaticApplicationContext();
        // params
        context.registerBean("dbUrl", String.class, "jdbc:postgresql://localhost:5432/postgres");
        context.registerBean("dbUsername", String.class, () -> "postgres");
        context.registerBean("dbPassword", String.class, () -> "postgres", bd -> bd.setLazyInit(true));
        // connection
        context.registerBean("connection", Connection.class, () -> ConnectionFactory.build(
                context.getBean("dbUrl", String.class),
                context.getBean("dbUsername", String.class),
                context.getBean("dbPassword", String.class)
        ));
        // repositories
        context.registerBean(AccountRepositoryPersistent.class);
        context.registerBean(MoneyTransactionRepositoryPersistent.class);
        // services
        context.registerBean(MoneyTransactionFactory.class);
        context.registerBean(MakePurchaseService.class);
        context.registerBean(ReturnPurchaseService.class);
        // business logic
        context.registerBean(PurchasesOperation.class);
        // application
        context.registerBean(SpringStaticAutowiredApplication.class);
        return context;
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", toJson(purchasesOperation.perform()));
    }
}
