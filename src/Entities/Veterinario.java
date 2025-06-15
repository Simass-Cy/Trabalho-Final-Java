package Entities;

import Application.Cargo;


public class Veterinario extends Funcionario {

    //Atributo para a classe veterinario
    private String crmv;

  //seta por padrao o cargo como veterinario
    public Veterinario() {
        super.setCargo(Cargo.VETERINARIO);
    }

  //all args constructor
    public Veterinario(Long idFuncionario, String nomeFuncionario, String emailFuncionario, String telefoneFuncionario, String senhaFuncionario, String crmv) {
        //construtor da superclasse
        super(idFuncionario, Cargo.VETERINARIO, "Atendimento clínico de animais", emailFuncionario, nomeFuncionario, telefoneFuncionario, senhaFuncionario);
        this.crmv = crmv;
    }

    //gets e sets
    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }
}