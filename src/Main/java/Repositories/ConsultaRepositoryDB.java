package Repositories;

import Entities.Animais;
import Entities.Consulta;
import Entities.Funcionario;
import Entities.Receita;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Exceptions.RepositoryException;

public class ConsultaRepositoryDB implements IConsultaRepository {

    // 1. A conexão e os outros repositórios agora são atributos da classe.
    private final Connection conn = ConnectionFactory.getConnection();
    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();
    // private final IReceitaRepository receitaRepository = new ReceitaRepositoryDB(); // Futuro

    @Override
    public void salvarConsulta(Consulta consulta) throws RepositoryException {
        String sql = "INSERT INTO consulta (data_hora, descricao, id_veterinario, id_animal, id_agendamento, id_receita) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setString(2, consulta.getDescricao());
            stmt.setLong(3, consulta.getVeterinario().getIdFuncionario());
            stmt.setLong(4, consulta.getAnimal().getIdAnimal());

            if (consulta.getAgendamentoOrigem() != null) {
                stmt.setLong(5, consulta.getAgendamentoOrigem().getId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }

            if (consulta.getReceita() != null) {
                stmt.setLong(6, consulta.getReceita().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar a consulta no banco de dados.", e);
        }
    }

    @Override
    public Consulta buscarPorIdConsulta(long id) throws RepositoryException {
        String sql = "SELECT * FROM consulta WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaConsulta(rs);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar consulta por ID.", e);
        }
        return null;
    }

    @Override
    public List<Consulta> buscarPorAnimalConsulta(Animais animal) throws RepositoryException {
        String sql = "SELECT * FROM consulta WHERE id_animal = ?";
        List<Consulta> consultas = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, animal.getIdAnimal());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar consultas por animal.", e);
        }
        return consultas;
    }

    @Override
    public List<Consulta> buscarPorVeterinario(Funcionario veterinario) throws RepositoryException {
        String sql = "SELECT * FROM consulta WHERE id_veterinario = ?";
        List<Consulta> consultas = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, veterinario.getIdFuncionario());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar consultas por veterinário.", e);
        }
        return consultas;
    }

    @Override
    public List<Consulta> buscarPorPeriodoConsulta(LocalDate dataInicio, LocalDate dataFim) throws RepositoryException {
        String sql = "SELECT * FROM consulta WHERE DATE(data_hora) BETWEEN ? AND ?";
        List<Consulta> consultas = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar consultas por período.", e);
        }
        return consultas;
    }

    private Consulta mapearParaConsulta(ResultSet rs) throws SQLException, RepositoryException {
        long idVeterinario = rs.getLong("id_veterinario");
        long idAnimal = rs.getLong("id_animal");
        long idReceita = rs.getLong("id_receita");

        Funcionario veterinario = funcionarioRepository.buscarPorIdFuncionario(idVeterinario);
        Animais animal = animalRepository.buscarPorIdAnimal(idAnimal);
        Receita receita = null;
        if (idReceita != 0) {
            // Lógica para buscar receita viria aqui com IReceitaRepository
        }

        Consulta consulta = new Consulta();
        consulta.setId(rs.getLong("id"));
        consulta.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        consulta.setDescricao(rs.getString("descricao"));
        consulta.setVeterinario(veterinario);
        consulta.setAnimal(animal);
        consulta.setReceita(receita);

        return consulta;
    }
}