package Entities;

public class Receita {

    private long idReceita;
    private String descricaoReceita;
    private Cliente ClienteReceita;

    //no args constructor
    public Receita() {
    }

    //all args constructor
    public Receita(long idReceita, String descricaoReceita, Cliente clienteReceita) {
        this.idReceita = idReceita;
        this.descricaoReceita = descricaoReceita;
        ClienteReceita = clienteReceita;
    }

    public long getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(long idReceita) {
        this.idReceita = idReceita;
    }

    public String getDescricaoReceita() {
        return descricaoReceita;
    }

    public void setDescricaoReceita(String descricaoReceita) {
        this.descricaoReceita = descricaoReceita;
    }

    public Cliente getClienteReceita() {
        return ClienteReceita;
    }

    public void setClienteReceita(Cliente clienteReceita) {
        ClienteReceita = clienteReceita;
    }
}
