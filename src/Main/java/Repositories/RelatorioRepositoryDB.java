package Repositories;

import Entities.Consulta;
import Entities.RelatorioConsulta;
import Exceptions.RepositoryException;
import java.sql.*;

public class RelatorioRepositoryDB implements IRelatorioRepository {

    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvarRelatorio(RelatorioConsulta relatorio) throws RepositoryException {
        String sqlRelatorio = "INSERT INTO relatorio (data_geracao, id_autor, tipo_relatorio) VALUES (?, ?, ?)";
        String sqlRelatorioConsulta = "INSERT INTO relatorio_consulta (id_relatorio, data_inicio, data_fim) VALUES (?, ?, ?)";
        String sqlRelatorioXConsulta = "INSERT INTO relatorio_x_consulta (id_relatorio, id_consulta) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false); // Inicia a transação

            // Salva o relatório principal
            try (PreparedStatement stmt1 = conn.prepareStatement(sqlRelatorio, Statement.RETURN_GENERATED_KEYS)) {
                stmt1.setTimestamp(1, Timestamp.valueOf(relatorio.getDataGeracao()));
                stmt1.setLong(2, relatorio.getAutor().getIdFuncionario());
                stmt1.setString(3, "CONSULTA");
                stmt1.executeUpdate();
                try (ResultSet rs = stmt1.getGeneratedKeys()) {
                    if (rs.next()) {
                        relatorio.setId(rs.getLong(1));
                    }
                }
            }

            // Salva os detalhes do relatório de consulta
            try (PreparedStatement stmt2 = conn.prepareStatement(sqlRelatorioConsulta)) {
                stmt2.setLong(1, relatorio.getId());
                stmt2.setDate(2, Date.valueOf(relatorio.getDataInicio()));
                stmt2.setDate(3, Date.valueOf(relatorio.getDataFim()));
                stmt2.executeUpdate();
            }

            // Salva a associação entre o relatório e as consultas
            try (PreparedStatement stmt3 = conn.prepareStatement(sqlRelatorioXConsulta)) {
                for (Consulta consulta : relatorio.getConsultas()) {
                    stmt3.setLong(1, relatorio.getId());
                    stmt3.setLong(2, consulta.getId());
                    stmt3.addBatch();
                }
                stmt3.executeBatch();
            }

            conn.commit(); // Confirma a transação se tudo deu certo

        } catch (SQLException e) {
            try {
                conn.rollback(); // Desfaz tudo se algum erro ocorreu
            } catch (SQLException ex) {
                // Em um sistema real, logaríamos este erro crítico
                ex.printStackTrace();
            }
            // Lança nossa exceção customizada para a camada de serviço
            throw new RepositoryException("Erro ao salvar o relatório. A transação foi revertida.", e);
        } finally {
            try {
                conn.setAutoCommit(true); // Restaura o comportamento padrão da conexão
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}