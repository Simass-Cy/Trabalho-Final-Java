package Entities;


public class Receita {

    private long id; // Renomeado para seguir o padrão
    private String descricao;

    // no args constructor
    public Receita() {
    }

    // all args constructor
    public Receita(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // --- Getters e Setters ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}