package Repositories;

import Entities.Consulta;
import Entities.RelatorioConsulta;
import java.sql.*;

public class RelatorioRepositoryDB implements IRelatorioRepository {

    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvarRelatorio(RelatorioConsulta relatorio) {
        String sqlRelatorio = "INSERT INTO relatorio (data_geracao, id_autor, tipo_relatorio) VALUES (?, ?, ?)";
        String sqlRelatorioConsulta = "INSERT INTO relatorio_consulta (id_relatorio, data_inicio, data_fim) VALUES (?, ?, ?)";
        String sqlRelatorioXConsulta = "INSERT INTO relatorio_x_consulta (id_relatorio, id_consulta) VALUES (?, ?)";

        try {
            // Desativa o auto-commit para garantir que todas as operações aconteçam juntas (transação)
            conn.setAutoCommit(false);

            // 1. Salva na tabela 'relatorio' principal
            try (PreparedStatement stmt1 = conn.prepareStatement(sqlRelatorio, Statement.RETURN_GENERATED_KEYS)) {
                stmt1.setTimestamp(1, Timestamp.valueOf(relatorio.getDataGeracao()));
                stmt1.setLong(2, relatorio.getAutor().getIdFuncionario());
                stmt1.setString(3, "CONSULTA"); // Define o tipo do relatório
                stmt1.executeUpdate();

                // Pega o ID que acabou de ser gerado para o relatório
                try (ResultSet rs = stmt1.getGeneratedKeys()) {
                    if (rs.next()) {
                        relatorio.setId(rs.getLong(1));
                    }
                }
            }

            // 2. Salva na tabela específica 'relatorio_consulta'
            try (PreparedStatement stmt2 = conn.prepareStatement(sqlRelatorioConsulta)) {
                stmt2.setLong(1, relatorio.getId());
                stmt2.setDate(2, Date.valueOf(relatorio.getDataInicio()));
                stmt2.setDate(3, Date.valueOf(relatorio.getDataFim()));
                stmt2.executeUpdate();
            }

            // 3. Salva as associações na tabela 'relatorio_x_consulta'
            try (PreparedStatement stmt3 = conn.prepareStatement(sqlRelatorioXConsulta)) {
                // Para cada consulta na lista do relatório...
                for (Consulta consulta : relatorio.getConsultas()) {
                    stmt3.setLong(1, relatorio.getId());
                    stmt3.setLong(2, consulta.getId());
                    stmt3.addBatch(); // Adiciona o comando a um "pacote" para execução em lote
                }
                stmt3.executeBatch(); // Executa todos os comandos de uma vez
            }

            // Se tudo deu certo, confirma as operações no banco de dados
            conn.commit();
            System.out.println("INFO: Relatório ID " + relatorio.getId() + " salvo com sucesso no banco de dados.");

        } catch (SQLException e) {
            System.err.println("ERRO AO SALVAR RELATÓRIO: Transação será revertida.");
            try {
                // Se algo deu errado, desfaz todas as operações
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                // Reativa o auto-commit para as próximas operações
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}