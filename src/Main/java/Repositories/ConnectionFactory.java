package Repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection connection;

    // --- Dados de conexão (substitua com os seus quando criar o banco) ---
    private static final String URL = "jdbc:mysql://localhost:3306/clinica_vet_db";
    private static final String USER = "root";
    private static final String PASSWORD = "A6gsa598fejw82";

   //impedir outras conexoes
    private ConnectionFactory() {
    }


    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("INFO: Criando uma nova conexão com o banco de dados...");
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("ERRO DE SQL: Falha ao obter a conexão com o banco de dados.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO: Driver JDBC do MySQL não encontrado!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return connection;
    }
}
