package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface IAnimalRepository que utiliza JDBC para
 * interagir com o banco de dados.
 * Esta versão foi corrigida para gerenciar corretamente a conexão Singleton.
 */
public class AnimalRepositoryDB implements IAnimalRepository {

    // 1. A conexão e o repositório de cliente agora são atributos da classe.
    private final Connection conn = ConnectionFactory.getConnection();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();

    @Override
    public void salvar(Animais animal) {
        String sql;
        if (animal.getIdAnimal() == null || animal.getIdAnimal() == 0) {
            sql = "INSERT INTO animal (nome, data_nascimento, id_cliente) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE animal SET nome = ?, data_nascimento = ?, id_cliente = ? WHERE id = ?";
        }

        // 2. O try-with-resources agora gerencia APENAS o PreparedStatement.
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, animal.getNomeAnimal());
            stmt.setDate(2, Date.valueOf(animal.getDataNascimentoAnimal()));
            stmt.setLong(3, animal.getDono().getId());

            if (animal.getIdAnimal() != null && animal.getIdAnimal() != 0) {
                stmt.setLong(4, animal.getIdAnimal());
            }

            stmt.executeUpdate();

            if (animal.getIdAnimal() == null || animal.getIdAnimal() == 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        animal.setIdAnimal(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Animais buscarPorId(long id) {
        String sql = "SELECT * FROM animal WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaAnimal(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Animais> buscarPorNome(String nome) {
        String sql = "SELECT * FROM animal WHERE nome LIKE ?";
        List<Animais> animais = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    animais.add(mapearParaAnimal(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animais;
    }

    @Override
    public List<Animais> buscarPorDono(Cliente dono) {
        String sql = "SELECT * FROM animal WHERE id_cliente = ?";
        List<Animais> animais = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, dono.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    animais.add(mapearParaAnimal(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animais;
    }

    @Override
    public List<Animais> buscarTodos() {
        String sql = "SELECT * FROM animal";
        List<Animais> animais = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                animais.add(mapearParaAnimal(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animais;
    }

    @Override
    public void deletar(long id) {
        String sql = "DELETE FROM animal WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Animais mapearParaAnimal(ResultSet rs) throws SQLException {
        long idCliente = rs.getLong("id_cliente");
        Cliente dono = clienteRepository.buscarPorId(idCliente);

        return new Animais(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDate("data_nascimento").toLocalDate(),
                dono
        );
    }
}