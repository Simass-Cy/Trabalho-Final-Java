package Entities;

import Application.StatusAgendamento;
import java.time.LocalDateTime;

public class Agendamento {

    private long id;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private Cliente cliente;
    private Animais animal;
    private Funcionario funcionarioQueAgendou;

    // no args constructor
    public Agendamento() {
        // novo agendamento começa como PENDENTE
        this.status = StatusAgendamento.PENDENTE;
    }

    // all args constructor
    public Agendamento(long id, LocalDateTime dataHora, Cliente cliente, Animais animal, Funcionario funcionarioQueAgendou) {
        this(); // Chama o construtor para setar o status como PENDENTE
        this.id = id;
        this.dataHora = dataHora;
        this.cliente = cliente;
        this.animal = animal;
        this.funcionarioQueAgendou = funcionarioQueAgendou;
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

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Animais getAnimal() {
        return animal;
    }

    public void setAnimal(Animais animal) {
        this.animal = animal;
    }

    public Funcionario getFuncionarioQueAgendou() {
        return funcionarioQueAgendou;
    }

    public void setFuncionarioQueAgendou(Funcionario funcionarioQueAgendou) {
        this.funcionarioQueAgendou = funcionarioQueAgendou;
    }
}