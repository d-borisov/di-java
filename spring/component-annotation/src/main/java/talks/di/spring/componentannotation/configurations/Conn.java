package talks.di.spring.componentannotation.configurations;

import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class Conn implements Connection {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Delegate
    private final Connection connection;

    public Conn(
            @Qualifier("dbUrl") String dbUrl,
            @Qualifier("dbUsername") String dbUsername,
            @Qualifier("dbPassword") String dbPassword
    ) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
