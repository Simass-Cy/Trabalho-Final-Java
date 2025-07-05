package Repositories;

import Application.Cargo;
import Entities.Funcionario;
import Entities.Veterinario; // Importamos a classe específica
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepositoryDB implements IFuncionarioRepository {

    private Funcionario mapearParaFuncionario(ResultSet rs) throws SQLException {
        Cargo cargo = Cargo.valueOf(rs.getString("cargo"));

        Funcionario funcionario;

        // "Monta" o objeto correto baseado no cargo
        if (cargo == Cargo.VETERINARIO) {
            Veterinario vet = new Veterinario();
            vet.setCrmv(rs.getString("crmv"));
            funcionario = vet;
        } else {
            // Se tivéssemos outras classes como Recepcionista, elas seriam tratadas aqui
            // Por enquanto, usamos a implementação padrão (não podemos instanciar Funcionario diretamente)
            // Esta parte precisará de ajuste se tivermos mais classes concretas
            funcionario = new Veterinario(); // Placeholder
        }

        // Preenche os dados comuns a todos os funcionários
        funcionario.setIdFuncionario(rs.getLong("id"));
        funcionario.setNomeFuncionario(rs.getString("nome"));
        funcionario.setEmailFuncionario(rs.getString("email"));
        funcionario.setTelefoneFuncionario(rs.getString("telefone"));
        funcionario.setSenhaFuncionario(rs.getString("senha"));
        funcionario.setCargo(cargo);
        funcionario.setDescricaoFuncaoFuncionario(rs.getString("descricao_funcao"));

        return funcionario;
    }

    @Override
    public void salvar(Funcionario funcionario) {
        String sql;
        if (funcionario.getIdFuncionario() == null || funcionario.getIdFuncionario() == 0) {
            sql = "INSERT INTO funcionario (nome, email, telefone, senha, cargo, descricao_funcao, crmv) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE funcionario SET nome = ?, email = ?, telefone = ?, senha = ?, cargo = ?, descricao_funcao = ?, crmv = ? WHERE id = ?";
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNomeFuncionario());
            stmt.setString(2, funcionario.getEmailFuncionario());
            stmt.setString(3, funcionario.getTelefoneFuncionario());
            stmt.setString(4, funcionario.getSenhaFuncionario());
            stmt.setString(5, funcionario.getCargo().toString()); // Converte o Enum para String
            stmt.setString(6, funcionario.getDescricaoFuncaoFuncionario());

            // Verifica se o funcionário é um Veterinario para salvar o CRMV
            if (funcionario instanceof Veterinario) {
                stmt.setString(7, ((Veterinario) funcionario).getCrmv());
            } else {
                stmt.setNull(7, Types.VARCHAR); // Define como nulo para outros cargos
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
            System.out.println("INFO: Funcionário '" + funcionario.getNomeFuncionario() + "' salvo no banco de dados.");

        } catch (SQLException e) {
            System.err.println("ERRO AO SALVAR FUNCIONÁRIO: " + e.getMessage());
            // Lançar exceção customizada aqui
        }
    }

    @Override
    public Funcionario buscarPorId(long id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Chama um método auxiliar para mapear o resultado para um objeto
                    return mapearParaFuncionario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRO AO BUSCAR FUNCIONÁRIO POR ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Funcionario> buscarTodos() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
