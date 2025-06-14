package Entities;

public class Animais {

    private String nomeAnimal;
    private Long idAnimal;
    private String dataNascimentoAnimal;
    private int idadeAnimal;
    private Cliente dono;

    //all args constructor

    public Animais(String nomeAnimal, Long idAnimal, String dataNascimentoAnimal, int idadeAnimal, Cliente dono) {
        this.nomeAnimal = nomeAnimal;
        this.idAnimal = idAnimal;
        this.dataNascimentoAnimal = dataNascimentoAnimal;
        this.idadeAnimal = idadeAnimal;
        this.dono = dono;
    }

    //no args constructor

    public Animais() {
    }

    //gets e sets


    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public Long getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Long idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getDataNascimentoAnimal() {
        return dataNascimentoAnimal;
    }

    public void setDataNascimentoAnimal(String dataNascimentoAnimal) {
        this.dataNascimentoAnimal = dataNascimentoAnimal;
    }

    public int getIdadeAnimal() {
        return idadeAnimal;
    }

    public void setIdadeAnimal(int idadeAnimal) {
        this.idadeAnimal = idadeAnimal;
    }

    public Cliente getDono() {
        return dono;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }
}

