package Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa um relatório específico sobre as consultas realizadas em um período.
 * Herda os campos comuns da classe Relatorio.
 */
public class RelatorioConsulta extends Relatorio {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<Consulta> consultas;

    /**
     * Construtor padrão.
     */
    public RelatorioConsulta() {
        super(); // Chama o construtor da classe mãe
    }

    /**
     * Construtor completo para criar um relatório de consultas.
     * @param id O identificador do relatório.
     * @param autor O funcionário que gerou o relatório.
     * @param dataInicio A data de início do período do relatório.
     * @param dataFim A data de fim do período do relatório.
     * @param consultas A lista de consultas realizadas no período.
     */
    public RelatorioConsulta(long id, Funcionario autor, LocalDate dataInicio, LocalDate dataFim, List<Consulta> consultas) {
        super(id, autor); // Chama o construtor da classe mãe para inicializar os dados comuns
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.consultas = consultas;
    }

    // --- Getters e Setters específicos ---

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}