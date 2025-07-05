package Repositories;

import Entities.Animais;
import Entities.Cliente;
import Exceptions.RepositoryException; // 1. Importa a nossa nova exceção
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryDB implements IClienteRepository {

    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvarCliente(Cliente cliente) throws RepositoryException {
        String sql;
        if (cliente.getId() == 0) {
            sql = "INSERT INTO cliente (nome, senha, email, telefone) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE cliente SET nome = ?, senha = ?, email = ?, telefone = ? WHERE id = ?";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSenha());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            if (cliente.getId() != 0) {
                stmt.setLong(5, cliente.getId());
            }
            stmt.executeUpdate();
            if (cliente.getId() == 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar o cliente no banco de dados.", e);
        }
    }

    @Override
    public Cliente buscarPorIdCliente(long id) throws RepositoryException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
            throw new RepositoryException("Erro ao buscar cliente por ID.", e);
        }
        return null;
    }

    @Override
    public List<Cliente> buscarPorNomeCliente(String nome) throws RepositoryException {
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
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
            throw new RepositoryException("Erro ao buscar cliente por nome.", e);
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarPorEmailCliente(String email) throws RepositoryException {
        String sql = "SELECT * FROM cliente WHERE email = ?";
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
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
            throw new RepositoryException("Erro ao buscar cliente por email.", e);
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarTodosCliente() throws RepositoryException {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
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
            throw new RepositoryException("Erro ao buscar todos os clientes.", e);
        }
        return clientes;
    }

    @Override
    public void deletarCliente(long id) throws RepositoryException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao deletar cliente com ID: " + id, e);
        }
    }

    @Override
    public void adicionarAnimalAoCliente(Cliente cliente, Animais animal) throws RepositoryException {
        // Esta lógica apenas cria a associação. O animal deve ser salvo separadamente.
        String sql = "UPDATE animal SET id_cliente = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            stmt.setLong(2, animal.getIdAnimal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao associar animal ao cliente.", e);
        }
    }
}