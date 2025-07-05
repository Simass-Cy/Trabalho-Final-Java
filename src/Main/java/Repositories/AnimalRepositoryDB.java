package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalRepositoryDB implements IAnimalRepository {

    // Dependência para buscar o 'dono' do animal.
    // Em uma arquitetura mais avançada, injetaríamos isso via construtor.
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();

    @Override
    public void salvar(Animais animal) {
        String sql;
        if (animal.getIdAnimal() == null || animal.getIdAnimal() == 0) {
            sql = "INSERT INTO animal (nome, data_nascimento, id_cliente) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE animal SET nome = ?, data_nascimento = ?, id_cliente = ? WHERE id = ?";
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, animal.getNomeAnimal());
            // Converte java.time.LocalDate para java.sql.Date
            stmt.setDate(2, Date.valueOf(animal.getDataNascimentoAnimal()));
            // Salva apenas o ID do dono
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
            e.printStackTrace(); // Tratamento de erro
        }
    }

    @Override
    public Animais buscarPorId(long id) {
        String sql = "SELECT * FROM animal WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para mapear uma linha do ResultSet para um objeto Animais.
     */
    private Animais mapearParaAnimal(ResultSet rs) throws SQLException {
        // Pega o ID do cliente da tabela animal
        long idCliente = rs.getLong("id_cliente");
        // Usa o repositório de clientes para buscar o objeto Cliente completo
        Cliente dono = clienteRepository.buscarPorId(idCliente);

        // Cria o objeto Animais e já associa o dono
        return new Animais(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDate("data_nascimento").toLocalDate(), // Converte java.sql.Date para java.time.LocalDate
                dono
        );
    }

}
