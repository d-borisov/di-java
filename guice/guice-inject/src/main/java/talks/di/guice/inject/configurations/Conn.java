package talks.di.guice.inject.configurations;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.experimental.Delegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    @Inject
    public Conn(
            @Named("dbUrl") String dbUrl,
            @Named("dbUsername") String dbUsername,
            @Named("dbPassword") String dbPassword
    ) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
