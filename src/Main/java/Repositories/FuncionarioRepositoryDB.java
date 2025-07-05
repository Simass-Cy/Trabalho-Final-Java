package Repositories;

import Application.Cargo;
import Entities.Funcionario;
import Entities.Veterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface IFuncionarioRepository que utiliza JDBC.
 * Esta versão foi corrigida para gerenciar corretamente a conexão Singleton.
 */
public class FuncionarioRepositoryDB implements IFuncionarioRepository {

    // 1. A conexão agora é um atributo da classe, obtida uma única vez.
    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvar(Funcionario funcionario) {
        String sql;
        if (funcionario.getIdFuncionario() == null || funcionario.getIdFuncionario() == 0) {
            sql = "INSERT INTO funcionario (nome, email, telefone, senha, cargo, descricao_funcao, crmv) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE funcionario SET nome = ?, email = ?, telefone = ?, senha = ?, cargo = ?, descricao_funcao = ?, crmv = ? WHERE id = ?";
        }

        // 2. O try-with-resources agora gerencia APENAS o PreparedStatement.
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNomeFuncionario());
            stmt.setString(2, funcionario.getEmailFuncionario());
            stmt.setString(3, funcionario.getTelefoneFuncionario());
            stmt.setString(4, funcionario.getSenhaFuncionario());
            stmt.setString(5, funcionario.getCargo().toString());
            stmt.setString(6, funcionario.getDescricaoFuncaoFuncionario());

            if (funcionario instanceof Veterinario) {
                stmt.setString(7, ((Veterinario) funcionario).getCrmv());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            if (funcionario.getIdFuncionario() != null && funcionario.getIdFuncionario() != 0) {
                stmt.setLong(8, funcionario.getIdFuncionario());
            }

            stmt.executeUpdate();

            if (funcionario.getIdFuncionario() == null || funcionario.getIdFuncionario() == 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        funcionario.setIdFuncionario(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Funcionario buscarPorId(long id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaFuncionario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Funcionario> buscarTodos() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                funcionarios.add(mapearParaFuncionario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

    @Override
    public List<Funcionario> buscarPorCargo(Cargo cargo) {
        String sql = "SELECT * FROM funcionario WHERE cargo = ?";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cargo.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    funcionarios.add(mapearParaFuncionario(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

    @Override
    public List<Funcionario> buscarPorNome(String nomeFuncionario) {
        String sql = "SELECT * FROM funcionario WHERE nome LIKE ?";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeFuncionario + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    funcionarios.add(mapearParaFuncionario(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

    @Override
    public void deletar(long id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Funcionario mapearParaFuncionario(ResultSet rs) throws SQLException {
        Cargo cargo = Cargo.valueOf(rs.getString("cargo"));
        Funcionario funcionario;

        if (cargo == Cargo.VETERINARIO) {
            Veterinario vet = new Veterinario();
            vet.setCrmv(rs.getString("crmv"));
            funcionario = vet;
        } else {
            // Futuramente, esta lógica será expandida para outros cargos
            // Ex: if (cargo == Cargo.RECEPCIONISTA) { funcionario = new Recepcionista(); }
            // Por enquanto, é um placeholder.
            throw new IllegalStateException("Cargo não suportado para mapeamento: " + cargo);
        }

        funcionario.setIdFuncionario(rs.getLong("id"));
        funcionario.setNomeFuncionario(rs.getString("nome"));
        funcionario.setEmailFuncionario(rs.getString("email"));
        funcionario.setTelefoneFuncionario(rs.getString("telefone"));
        funcionario.setSenhaFuncionario(rs.getString("senha"));
        funcionario.setCargo(cargo);
        funcionario.setDescricaoFuncaoFuncionario(rs.getString("descricao_funcao"));

        return funcionario;
    }
}