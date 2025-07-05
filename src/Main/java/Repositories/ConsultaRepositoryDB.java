package Repositories;

import Entities.Animais;
import Entities.Consulta;
import Entities.Funcionario;
import Entities.Receita; // Import necessário
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepositoryDB implements IConsultaRepository {

    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();

    private Consulta mapearParaConsulta(ResultSet rs) throws SQLException {
        // Busca os IDs das chaves estrangeiras
        long idVeterinario = rs.getLong("id_veterinario");
        long idAnimal = rs.getLong("id_animal");
        long idReceita = rs.getLong("id_receita"); // Pega o id da receita

        // Usa os outros repositórios para buscar os objetos completos
        Funcionario veterinario = funcionarioRepository.buscarPorId(idVeterinario);
        Animais animal = animalRepository.buscarPorId(idAnimal);

        // Lógica para buscar a receita (precisaremos criar IReceitaRepository)
        Receita receita = null;
        if (idReceita != 0) {
            // receita = receitaRepository.buscarPorId(idReceita); // Linha futura
        }

        // Cria o objeto Consulta
        Consulta consulta = new Consulta();
        consulta.setId(rs.getLong("id"));
        consulta.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        consulta.setDescricao(rs.getString("descricao"));
        consulta.setVeterinario(veterinario);
        consulta.setAnimal(animal);
        consulta.setReceita(receita); // Associa a receita encontrada

        // Lógica para buscar o agendamento de origem pode ser adicionada aqui também

        return consulta;
    }

    @Override
    public void salvarConsulta(Consulta consulta) {
        String sql;
        // Assumimos que a consulta, uma vez criada, não é atualizada, apenas inserida.
        // A lógica de UPDATE poderia ser mais complexa (ex: alterar descrição).
        sql = "INSERT INTO consulta (data_hora, descricao, id_veterinario, id_animal, id_agendamento, id_receita) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setString(2, consulta.getDescricao());
            stmt.setLong(3, consulta.getVeterinario().getIdFuncionario());
            stmt.setLong(4, consulta.getAnimal().getIdAnimal());

            // Trata campos que podem ser nulos
            if (consulta.getAgendamentoOrigem() != null) {
                stmt.setLong(5, consulta.getAgendamentoOrigem().getId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }

            if (consulta.getReceita() != null) {
                // Aqui, assumimos que a receita já foi salva e tem um ID.
                // A lógica de salvar a receita primeiro ficaria em uma classe de Serviço.
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
            e.printStackTrace(); // Tratamento de erro
        }
    }

    @Override
    public Consulta buscarPorIdConsulta(long id) {
        String sql = "SELECT * FROM consulta WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaConsulta(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Consulta> buscarPorAnimalConsulta(Animais animal) {
        String sql = "SELECT * FROM consulta WHERE id_animal = ?";
        List<Consulta> consultas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, animal.getIdAnimal());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    @Override
    public List<Consulta> buscarPorVeterinario(Funcionario veterinario) {
        String sql = "SELECT * FROM consulta WHERE id_veterinario = ?";
        List<Consulta> consultas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, veterinario.getIdFuncionario());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    @Override
    public List<Consulta> buscarPorPeriodoConsulta(LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT * FROM consulta WHERE DATE(data_hora) BETWEEN ? AND ?";
        List<Consulta> consultas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }




}
