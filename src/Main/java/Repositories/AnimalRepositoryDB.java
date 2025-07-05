package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Exceptions.RepositoryException;

/**
 * Implementação da interface IAnimalRepository que utiliza JDBC para
 * interagir com o banco de dados.
 * Esta versão foi corrigida para gerenciar corretamente a conexão Singleton.
 */
public class AnimalRepositoryDB implements IAnimalRepository {

    private final Connection conn = ConnectionFactory.getConnection();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();

    @Override
    public void salvarAnimal(Animais animal) throws RepositoryException {
        String sql;
        if (animal.getIdAnimal() == null || animal.getIdAnimal() == 0) {
            sql = "INSERT INTO animal (nome, data_nascimento, id_cliente) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE animal SET nome = ?, data_nascimento = ?, id_cliente = ? WHERE id = ?";
        }

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
            throw new RepositoryException("Erro ao salvar o animal no banco de dados.", e);
        }
    }

    @Override
    public Animais buscarPorIdAnimal(long id) throws RepositoryException {
        String sql = "SELECT * FROM animal WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaAnimal(rs);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar animal por ID.", e);
        }
        return null;
    }

    @Override
    public List<Animais> buscarPorNomeAnimal(String nome) throws RepositoryException {
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
            throw new RepositoryException("Erro ao buscar animais por nome.", e);
        }
        return animais;
    }

    @Override
    public List<Animais> buscarPorDono(Cliente dono) throws RepositoryException {
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
            throw new RepositoryException("Erro ao buscar animais pelo dono.", e);
        }
        return animais;
    }

    @Override
    public List<Animais> buscarTodosAnimal() throws RepositoryException {
        String sql = "SELECT * FROM animal";
        List<Animais> animais = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                animais.add(mapearParaAnimal(rs));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar todos os animais.", e);
        }
        return animais;
    }

    @Override
    public void deletarAnimal(long id) throws RepositoryException {
        String sql = "DELETE FROM animal WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao deletar o animal com ID: " + id, e);
        }
    }

    private Animais mapearParaAnimal(ResultSet rs) throws SQLException, RepositoryException {
        long idCliente = rs.getLong("id_cliente");
        // AQUI ESTÁ A CORREÇÃO, usando o nome correto do método da interface
        Cliente dono = clienteRepository.buscarPorIdCliente(idCliente);

        if (dono == null) {
            throw new RepositoryException("Inconsistência de dados: Animal com ID " + rs.getLong("id") + " refere-se a um cliente inexistente (ID: " + idCliente + ").");
        }

        return new Animais(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDate("data_nascimento").toLocalDate(),
                dono
        );
    }
}