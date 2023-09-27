package talks.di.spring.staticstraightforward;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.StaticApplicationContext;
import talks.di.classes.logic.PurchasesOperation;
import talks.di.classes.logic.services.moneytransactions.MoneyTransactionFactory;
import talks.di.classes.logic.services.purchases.MakePurchaseService;
import talks.di.classes.logic.services.purchases.ReturnPurchaseService;
import talks.di.classes.persistence.impl.AccountRepositoryPersistent;
import talks.di.classes.persistence.impl.ConnectionFactory;
import talks.di.classes.persistence.impl.MoneyTransactionRepositoryPersistent;
import talks.di.utils.Json;

import java.sql.Connection;

@Slf4j
@AllArgsConstructor
public class SpringStaticStraightforwardApplication {

    private final PurchasesOperation purchasesOperation;

    public static void main(String[] args) {
        // build Spring context
        try (var context = buildContext()) {
            // application
            context.getBean(SpringStaticStraightforwardApplication.class)
                    .performBusinessLogic();
        }
    }

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
        context.registerBeanDefinition("accountRepository", BeanDefinitionBuilder
                .genericBeanDefinition(AccountRepositoryPersistent.class)
                .addConstructorArgReference("connection")
                .getBeanDefinition()
        );
        context.registerBeanDefinition("moneyTransactionRepository", BeanDefinitionBuilder
                .genericBeanDefinition(MoneyTransactionRepositoryPersistent.class)
                .addConstructorArgReference("connection")
                .getBeanDefinition()
        );
        // services
        context.registerBean("moneyTransactionFactory", MoneyTransactionFactory.class);
        context.registerBeanDefinition("makePurchaseService", BeanDefinitionBuilder
                .genericBeanDefinition(MakePurchaseService.class)
                .addConstructorArgReference("accountRepository")
                .addConstructorArgReference("moneyTransactionRepository")
                .addConstructorArgReference("moneyTransactionFactory")
                .getBeanDefinition()
        );
        context.registerBeanDefinition("returnPurchaseService", BeanDefinitionBuilder
                .genericBeanDefinition(ReturnPurchaseService.class)
                .addConstructorArgReference("accountRepository")
                .addConstructorArgReference("moneyTransactionRepository")
                .addConstructorArgReference("moneyTransactionFactory")
                .getBeanDefinition()
        );
        // business logic
        context.registerBeanDefinition("purchasesOperation", BeanDefinitionBuilder
                .genericBeanDefinition(PurchasesOperation.class)
                .addConstructorArgReference("accountRepository")
                .addConstructorArgReference("moneyTransactionRepository")
                .addConstructorArgReference("makePurchaseService")
                .addConstructorArgReference("returnPurchaseService")
                .getBeanDefinition()
        );
        // application
        context.registerBeanDefinition("application", BeanDefinitionBuilder
                .genericBeanDefinition(SpringStaticStraightforwardApplication.class)
                .addConstructorArgReference("purchasesOperation")
                .getBeanDefinition()
        );
        return context;
    }

    void performBusinessLogic() {
        log.info("purchasesOperation result: {}", Json.toJson(purchasesOperation.perform()));
    }
}
