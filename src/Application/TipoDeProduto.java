package Application;

public enum TipoDeProduto {

    MEDICAMENTO("Medicamento"),
    ALIMENTO("Alimento"),
    HIGIENE("Higiene e Limpeza"),
    BRINQUEDO("Brinquedo"),
    ACESSORIO("Acessório");

    private final String nomeAmigavel;

    TipoDeProduto(String nomeAmigavel) {
        this.nomeAmigavel = nomeAmigavel;
    }

    //mostra o nome mais facil de entender
    public String getNomeAmigavel() {
        return nomeAmigavel;
    }
}