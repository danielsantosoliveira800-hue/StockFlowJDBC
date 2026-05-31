package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/stockflow";

    private  static final String USER = "root";

    private static final String PASSWORD = "24072004";

    public static Connection getConnection() {
        try {
             return DriverManager.getConnection(
                     URL,
                     USER,
                     PASSWORD
             );
        }catch (SQLException e){
            throw  new RuntimeException(
                    "Erro ao conectar com o banco."
            );
        }
    }
}
