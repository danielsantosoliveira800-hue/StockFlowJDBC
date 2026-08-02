package infrasctructure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final HikariDataSource dataSource;
    private static final String CONFIG_FILE =
            System.getProperty("config.file","config.properties");

    static {
        try (var input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado.");
            }

            Properties props = new Properties();
            props.load(input);

            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));

            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações do banco.");
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco.");
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}