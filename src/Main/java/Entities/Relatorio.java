package Entities;

import java.time.LocalDateTime;

//classe molde para relatorios
public abstract class Relatorio {

    private long id;
    private LocalDateTime dataGeracao;
    private Funcionario autor; // Quem gerou o relatório

    //construtor seta data e hora padrao
    public Relatorio() {
        this.dataGeracao = LocalDateTime.now();
    }

    public Relatorio(long id, Funcionario autor) {
        this(); //chama o construtor que setou data e hora
        this.id = id;
        this.autor = autor;
    }

    // gets e sets

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public Funcionario getAutor() {
        return autor;
    }

    public void setAutor(Funcionario autor) {
        this.autor = autor;
    }
}