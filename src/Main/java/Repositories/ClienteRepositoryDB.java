package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryDB implements IClienteRepository {

    // 1. A conexão agora é um atributo da classe, obtida uma única vez.
    private Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvar(Cliente cliente) {
        String sql;
        if (cliente.getId() == 0) {
            sql = "INSERT INTO cliente (nome, senha, email, telefone) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE cliente SET nome = ?, senha = ?, email = ?, telefone = ? WHERE id = ?";
        }

        // 2. O try-with-resources agora gerencia APENAS o PreparedStatement.
        // A conexão 'conn' não é mais fechada ao final do try.
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
            e.printStackTrace();
        }
    }

    @Override
    public Cliente buscarPorId(long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        // A conexão 'conn' não é fechada aqui.
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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cliente> buscarPorNome(String nome) {
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
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarTodos() {
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
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public void deletar(long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adicionarAnimalAoCliente(Cliente cliente, Animais animal) {
        String sql = "UPDATE animal SET id_cliente = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            stmt.setLong(2, animal.getIdAnimal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}