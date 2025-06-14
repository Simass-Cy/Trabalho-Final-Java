package Entities;

import Application.Cargo;

/*classe abstrata
* nao quero que ngm crie nada com ela só vai servir pra ser herdada
* */
public abstract class Funcionario {
    private Long idFuncionario;
    private Cargo cargo;
    private String descricaoFuncaoFuncionario;
    private String emailFuncionario;
    private String nomeFuncionario;
    private String telefoneFuncionario;
    private String senhaFuncionario;

    //no args constructor
    public Funcionario() {
    }

    //all args constructor
    public Funcionario(Long idFuncionario, Cargo cargo, String descricaoFuncaoFuncionario, String emailFuncionario, String nomeFuncionario, String telefoneFuncionario) {
        this.idFuncionario = idFuncionario;
        this.cargo = cargo;
        this.descricaoFuncaoFuncionario = descricaoFuncaoFuncionario;
        this.emailFuncionario = emailFuncionario;
        this.nomeFuncionario = nomeFuncionario;
        this.telefoneFuncionario = telefoneFuncionario;
    }

    //construtor sobrecarregado
    public Funcionario(Long idFuncionario, Cargo cargo, String descricaoFuncaoFuncionario, String emailFuncionario, String nomeFuncionario, String telefoneFuncionario, String senhaFuncionario) {
        this(idFuncionario,cargo,descricaoFuncaoFuncionario,emailFuncionario,nomeFuncionario,telefoneFuncionario);
        this.senhaFuncionario = senhaFuncionario;
    }

    //get e set
    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getDescricaoFuncaoFuncionario() {
        return descricaoFuncaoFuncionario;
    }

    public void setDescricaoFuncaoFuncionario(String descricaoFuncaoFuncionario) {
        this.descricaoFuncaoFuncionario = descricaoFuncaoFuncionario;
    }

    public String getEmailFuncionario() {
        return emailFuncionario;
    }

    public void setEmailFuncionario(String emailFuncionario) {
        this.emailFuncionario = emailFuncionario;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getTelefoneFuncionario() {
        return telefoneFuncionario;
    }

    public void setTelefoneFuncionario(String telefoneFuncionario) {
        this.telefoneFuncionario = telefoneFuncionario;
    }
}
