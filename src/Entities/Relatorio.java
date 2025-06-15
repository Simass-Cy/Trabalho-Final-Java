package Entities;

import java.time.LocalDateTime;

/**
 * Classe base abstrata para todos os tipos de relatórios no sistema.
 * Não pode ser instanciada diretamente.
 */
public abstract class Relatorio {

    private long id;
    private LocalDateTime dataGeracao;
    private Funcionario autor; // Quem gerou o relatório

    /**
     * Construtor padrão que já define a data e hora de geração do relatório.
     */
    public Relatorio() {
        this.dataGeracao = LocalDateTime.now();
    }

    public Relatorio(long id, Funcionario autor) {
        this(); // Chama o construtor padrão para setar a data de geração
        this.id = id;
        this.autor = autor;
    }

    // --- Getters e Setters ---

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