package Repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // 1. Atributo estático para guardar a ÚNICA instância da conexão.
    private static Connection connection;

    // --- Dados de conexão (substitua com os seus quando criar o banco) ---
    private static final String URL = "jdbc:mysql://localhost:3306/clinica_vet_db";
    private static final String USER = "root"; // ou o seu usuário do MySQL
    private static final String PASSWORD = "sua_senha"; // a senha do seu usuário do MySQL

    // 2. O construtor é PRIVADO.
    // Isso impede que qualquer outra classe crie uma instância com 'new ConnectionFactory()'.
    private ConnectionFactory() {
    }

    /**
     * Método público e estático para obter a instância da conexão.
     * É 'synchronized' para ser seguro em ambientes com múltiplas threads.
     * @return A instância única da conexão com o banco de dados.
     */
    public static synchronized Connection getConnection() {
        // 3. Verifica se a conexão ainda não foi criada ou se foi fechada.
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("INFO: Criando uma nova conexão com o banco de dados...");
                // Carrega o driver do MySQL antes de conectar
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            // Um tratamento de erro mais robusto será implementado depois
            System.err.println("ERRO DE SQL: Falha ao obter a conexão com o banco de dados.");
            e.printStackTrace();
            // Lançar uma exceção em tempo de execução para parar o programa se a conexão falhar
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO: Driver JDBC do MySQL não encontrado!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 4. Retorna a conexão, seja a que acabou de ser criada ou a que já existia.
        return connection;
    }
}
