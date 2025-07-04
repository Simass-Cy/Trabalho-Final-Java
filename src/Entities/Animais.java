package Entities;

import java.time.LocalDate;
import java.time.Period;


public class Animais {

    private Long idAnimal;
    private String nomeAnimal;
    private LocalDate dataNascimentoAnimal;
    private Cliente dono;

    // no args constructor
    public Animais() {
    }

    // all args constructor
    public Animais(Long idAnimal, String nomeAnimal, LocalDate dataNascimentoAnimal, Cliente dono) {
        this.idAnimal = idAnimal;
        this.nomeAnimal = nomeAnimal;
        this.dataNascimentoAnimal = dataNascimentoAnimal;
        this.dono = dono;
    }

    // gets e sets

    public Long getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Long idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public LocalDate getDataNascimentoAnimal() {
        return dataNascimentoAnimal;
    }

    public void setDataNascimentoAnimal(LocalDate dataNascimentoAnimal) {
        this.dataNascimentoAnimal = dataNascimentoAnimal;
    }

    public Cliente getDono() {
        return dono;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    /*mudei a forma de armzenar a idade do animail, agora ela usa os parametros de data do
       proprio java*/

    public int getIdade() {
        if (this.dataNascimentoAnimal == null) {
            return 0;
        }
        return Period.between(this.dataNascimentoAnimal, LocalDate.now()).getYears();
    }
}