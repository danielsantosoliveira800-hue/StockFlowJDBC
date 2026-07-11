package integration;

import db.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.Statement;

public abstract class IntegrationTestBase {

    static {
        System.setProperty("config.file", "config-test.properties");
    }

    @BeforeEach
    void limparTabelas() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");
            statement.execute("TRUNCATE TABLE movimentacoes");
            statement.execute("TRUNCATE TABLE auditoria_produtos");
            statement.execute("TRUNCATE TABLE historico_dashboard");
            statement.execute("TRUNCATE TABLE produtos");
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }
}