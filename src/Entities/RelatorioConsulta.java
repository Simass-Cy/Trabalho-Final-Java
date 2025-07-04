package Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/* herda caracteristicas gerais da classe relatorio, pois podem (futuramente) ser adicionados
*outros tipos de relatorio, por exemplo o relatorio financeiro.
* */

public class RelatorioConsulta extends Relatorio {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<Consulta> consultas;

   //no args constructor
    public RelatorioConsulta() {
        super();
    }
    // no args constructor
    public RelatorioConsulta(long id, Funcionario autor, LocalDate dataInicio, LocalDate dataFim, List<Consulta> consultas) {
        super(id, autor); //construtor da classe mae para inicializar os dados basicos
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.consultas = consultas;
    }

    // gets e sets

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