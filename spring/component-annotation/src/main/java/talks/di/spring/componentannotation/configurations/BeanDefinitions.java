package talks.di.spring.componentannotation.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class BeanDefinitions {

    // params
    @Bean
    public String dbUrl() {
        return "jdbc:postgresql://localhost:5432/postgres";
    }

    @Bean
    public String dbUsername() {
        return "postgres";
    }

    @Bean
    public String dbPassword() {
        return "postgres";
    }

    // connection
//    @Bean
//    public Connection connection(
//            @Qualifier("dbUrl") String dbUrl,
//            @Qualifier("dbUsername") String dbUsername,
//            @Qualifier("dbPassword") String dbPassword) {
//        return ConnectionFactory.build(dbUrl, dbUsername, dbPassword);
//    }
//
//    public class ConnectionFactory {
//        static {
//            try {
//                Class.forName("org.postgresql.Driver");
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        public static Connection build(String url, String user, String password) {
//            try {
//                return DriverManager.getConnection(url, user, password);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
