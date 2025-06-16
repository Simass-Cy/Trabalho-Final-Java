package Entities;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private long id;
    private String nome;
    private int senha;
    private String email;
    private String telefone;
    //animais e agendamentos pertencem ao cliente
    private List<Animais> animais = new ArrayList<>();
    private List<Agendamento> agendamentos = new ArrayList<>();

   //no args constructor
    public Cliente() {
    }

    //all args constructor
    public Cliente(long id, String nome, int senha, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
    }
    //construtor sobrecarregado
    public Cliente(long id, String nome, int senha, String email, String telefone, List<Animais> animais, List<Agendamento> agendamentos) {
        this(id, nome, senha, email, telefone);
        this.animais = animais;
        this.agendamentos = agendamentos;
    }

    //getters e setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Animais> getAnimais() {
        return animais;
    }

    public void setAnimais(List<Animais> animais) {
        this.animais = animais;
    }
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

}