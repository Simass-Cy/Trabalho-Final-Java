package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryDB implements IClienteRepository{
    @Override
    public void salvar(Cliente cliente) {
        String sql;
        // Se o id é 0, é um novo cliente (INSERT). Caso contrário, é atualização (UPDATE).
        if (cliente.getId() == 0) {
            sql = "INSERT INTO cliente (nome, senha, email, telefone) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE cliente SET nome = ?, senha = ?, email = ?, telefone = ? WHERE id = ?";
        }

        try (Connection conn = ConnectionFactory.getConnection();
             // Pedimos ao JDBC para nos retornar as chaves geradas (o ID)
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSenha()); // Assumindo que a senha na classe Cliente foi corrigida para String
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());

            if (cliente.getId() != 0) {
                stmt.setLong(5, cliente.getId()); // Define o ID para a cláusula WHERE no UPDATE
            }

            stmt.executeUpdate();

            // Se foi uma inserção, precisamos pegar o ID gerado pelo banco
            if (cliente.getId() == 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Atualiza o objeto cliente com o ID que o banco acabou de criar
                        cliente.setId(rs.getLong(1));
                    }
                }
            }
            System.out.println("INFO: Cliente '" + cliente.getNome() + "' salvo no banco de dados.");

        } catch (SQLException e) {
            System.err.println("ERRO AO SALVAR CLIENTE: " + e.getMessage());
            // Futuramente, lançaremos uma exceção customizada aqui
        }
    }

    @Override
    public Cliente buscarPorId(long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapeia o resultado do banco para um objeto Cliente
                    return new Cliente(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("senha"),
                            rs.getString("email"),
                            rs.getString("telefone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRO AO BUSCAR CLIENTE POR ID: " + e.getMessage());
        }
        return null; // Retorna null se não encontrar ou se ocorrer um erro
    }

    @Override
    public List<Cliente> buscarPorNome(String nome) {
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%"); // Usamos LIKE com '%' para buscas parciais

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new Cliente(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("senha"),
                            rs.getString("email"),
                            rs.getString("telefone")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRO AO BUSCAR CLIENTE POR NOME: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarTodos() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("email"),
                        rs.getString("telefone")
                ));
            }
        } catch (SQLException e) {
            System.err.println("ERRO AO BUSCAR TODOS OS CLIENTES: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public void deletar(long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("INFO: Cliente com ID " + id + " deletado do banco de dados.");

        } catch (SQLException e) {
            System.err.println("ERRO AO DELETAR CLIENTE: " + e.getMessage());
        }
    }

    @Override
    public void adicionarAnimalAoCliente(Cliente cliente, Animais animal) {
        // A responsabilidade de salvar o animal em si deve ser do AnimalRepository.
        // Este método aqui deve apenas estabelecer a ligação no banco de dados.
        // Assumindo que o animal já foi salvo e tem um ID.
        String sql = "UPDATE animal SET id_cliente = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cliente.getId());
            stmt.setLong(2, animal.getIdAnimal());
            stmt.executeUpdate();

            System.out.println("INFO: Animal '" + animal.getNomeAnimal() + "' associado ao cliente '" + cliente.getNome() + "'.");

        } catch (SQLException e) {
            System.err.println("ERRO AO ADICIONAR ANIMAL AO CLIENTE: " + e.getMessage());
        }
    }

}
