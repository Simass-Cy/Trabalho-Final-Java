package Repositories;

import Entities.Receita;
import java.sql.*;
import Exceptions.RepositoryException;

public class ReceitaRepositoryDB implements IReceitaRepository {

    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvarReceita(Receita receita) throws RepositoryException {
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
            throw new RepositoryException("Erro ao salvar a receita no banco de dados.", e);
        }
    }

    @Override
    public Receita buscarPorIdReceita(long id) throws RepositoryException {
        String sql = "SELECT * FROM receita WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Receita(rs.getLong("id"), rs.getString("descricao"));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar receita por ID.", e);
        }
        return null;
    }

    @Override
    public void deletarReceita(long id) throws RepositoryException {
        String sql = "DELETE FROM receita WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao deletar a receita com ID: " + id, e);
        }
    }
}