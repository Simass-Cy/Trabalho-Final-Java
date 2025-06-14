package Entities;

import java.time.LocalDateTime;

public class Consulta {

    private long id;
    private LocalDateTime dataHora;
    private String descricao;
    private Funcionario veterinario;
    private Animais animal;
    private Receita receita;

    /**
     * Construtor padrão.
     */
    public Consulta() {
    }

    public Consulta(long id, LocalDateTime dataHora, String descricao, Funcionario veterinario, Animais animal) {
        // Validação para garantir que o funcionário é um veterinário
        if (veterinario.getCargo() != Application.Cargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário responsável pela consulta deve ser um VETERINARIO.");
        }
        this.id = id;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.veterinario = veterinario;
        this.animal = animal;
    }

    // --- Getters e Setters ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Funcionario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Funcionario veterinario) {
        // Validação para garantir que o funcionário é um veterinário
        if (veterinario.getCargo() != Application.Cargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário responsável pela consulta deve ser um VETERINARIO.");
        }
        this.veterinario = veterinario;
    }

    public Animais getAnimal() {
        return animal;
    }

    public void setAnimal(Animais animal) {
        this.animal = animal;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }
}