package Application;

public enum TipoDeProduto {

    MEDICAMENTO("Medicamento"),
    ALIMENTO("Alimento"),
    HIGIENE("Higiene e Limpeza"),
    BRINQUEDO("Brinquedo"),
    ACESSORIO("Acessório"); // Ex: coleiras, camas, etc.

    // Atributo para guardar um nome mais "amigável" para exibição
    private final String nomeAmigavel;

    /**
     * Construtor do enum.
     * @param nomeAmigavel O nome que pode ser exibido para o usuário.
     */
    TipoDeProduto(String nomeAmigavel) {
        this.nomeAmigavel = nomeAmigavel;
    }

    /**
     * Retorna o nome amigável para exibição.
     * @return O nome formatado do tipo de produto.
     */
    public String getNomeAmigavel() {
        return nomeAmigavel;
    }
}