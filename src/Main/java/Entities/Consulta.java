package Entities;

import java.time.LocalDateTime;
import Application.Cargo;

public class Consulta {

    private long id;
    private LocalDateTime dataHora;
    private String descricao;
    private Funcionario veterinario;
    private Animais animal;
    private Receita receita;
    private Agendamento agendamentoOrigem;

    //no args constructor
    public Consulta(String rex, String cachorro, String joão, String time, String date, String s, String consultaDeRotina) {
    }

    // all args constructor (sem sobrecarregar)
    public Consulta(long id, LocalDateTime dataHora, String descricao, Funcionario veterinario, Animais animal) {
        // verifica se o funcionario eh veterinario
        if (veterinario.getCargo() != Cargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário responsável pela consulta deve ser um VETERINARIO.");
        }
        this.id = id;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.veterinario = veterinario;
        this.animal = animal;
    }

    //construtor sobrecarregado
    public Consulta(long id, LocalDateTime dataHora, String descricao, Funcionario veterinario, Animais animal, Agendamento agendamentoOrigem) {
        this(id, dataHora, descricao, veterinario, animal);
        this.agendamentoOrigem = agendamentoOrigem;
    }


    //gets e sets

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
        if (veterinario.getCargo() != Cargo.VETERINARIO) {
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
    public Agendamento getAgendamentoOrigem() {
        return agendamentoOrigem;
    }

    public void setAgendamentoOrigem(Agendamento agendamentoOrigem) {
        this.agendamentoOrigem = agendamentoOrigem;
    }
}