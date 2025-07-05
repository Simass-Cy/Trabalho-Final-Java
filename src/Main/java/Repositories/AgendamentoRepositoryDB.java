package Repositories;

import Application.StatusAgendamento;
import Entities.Agendamento;
import Entities.Animais;
import Entities.Cliente;
import Entities.Funcionario;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoRepositoryDB implements IAgendamentoRepository {

    // 1. A conexão e os outros repositórios agora são atributos da classe.
    private final Connection conn = ConnectionFactory.getConnection();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();
    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();

    @Override
    public void salvarAgendamento(Agendamento agendamento) {
        String sql;
        if (agendamento.getId() == 0) {
            sql = "INSERT INTO agendamento (data_hora, status, id_cliente, id_animal, id_funcionario) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE agendamento SET data_hora = ?, status = ?, id_cliente = ?, id_animal = ?, id_funcionario = ? WHERE id = ?";
        }

        // 2. O try-with-resources agora gerencia APENAS o PreparedStatement.
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(2, agendamento.getStatus().toString());
            stmt.setLong(3, agendamento.getCliente().getId());
            stmt.setLong(4, agendamento.getAnimal().getIdAnimal());
            stmt.setLong(5, agendamento.getFuncionarioQueAgendou().getIdFuncionario());

            if (agendamento.getId() != 0) {
                stmt.setLong(6, agendamento.getId());
            }
            stmt.executeUpdate();

            if (agendamento.getId() == 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        agendamento.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Agendamento buscarPorIdAgendamento(long id) {
        String sql = "SELECT * FROM agendamento WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaAgendamento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Agendamento> buscarPorCliente(Cliente cliente) {
        String sql = "SELECT * FROM agendamento WHERE id_cliente = ?";
        List<Agendamento> agendamentos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    agendamentos.add(mapearParaAgendamento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agendamentos;
    }

    @Override
    public List<Agendamento> buscarPorData(LocalDate data) {
        String sql = "SELECT * FROM agendamento WHERE DATE(data_hora) = ?";
        List<Agendamento> agendamentos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(data));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    agendamentos.add(mapearParaAgendamento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agendamentos;
    }

    @Override
    public List<Agendamento> buscarPorVeterinario(Funcionario veterinario) {
        String sql = "SELECT * FROM agendamento WHERE id_funcionario = ?";
        List<Agendamento> agendamentos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, veterinario.getIdFuncionario());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    agendamentos.add(mapearParaAgendamento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agendamentos;
    }

    @Override
    public void alterarStatusAgendamento(long id, StatusAgendamento novoStatus) {
        String sql = "UPDATE agendamento SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus.toString());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletarAgendamento(long id) {
        String sql = "DELETE FROM agendamento WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Agendamento mapearParaAgendamento(ResultSet rs) throws SQLException {
        long idCliente = rs.getLong("id_cliente");
        long idAnimal = rs.getLong("id_animal");
        long idFuncionario = rs.getLong("id_funcionario");

        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        Animais animal = animalRepository.buscarPorId(idAnimal);
        Funcionario funcionario = funcionarioRepository.buscarPorId(idFuncionario);

        Agendamento agendamento = new Agendamento(
                rs.getLong("id"),
                rs.getTimestamp("data_hora").toLocalDateTime(),
                cliente,
                animal,
                funcionario
        );
        agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));
        return agendamento;
    }
}