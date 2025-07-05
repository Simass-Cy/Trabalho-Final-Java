package Repositories;

import Entities.Receita;
import java.sql.*;

public class ReceitaRepositoryDB implements IReceitaRepository {
    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvarReceita(Receita receita) {
        // Esta implementação está correta, mas vamos renomear os métodos para o padrão
        String sql = "INSERT INTO receita (descricao) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, receita.getDescricao());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    receita.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Receita buscarPorIdReceita(long id) {
        String sql = "SELECT * FROM receita WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Receita(rs.getLong("id"), rs.getString("descricao"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deletarReceita(long id) {
        String sql = "DELETE FROM receita WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}