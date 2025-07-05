package Repositories;

import Application.TipoDeProduto;
import Entities.Produto;
import java.util.List;

public interface IProdutoRepository {

    void salvarProduto(Produto produto);

    Produto buscarPorIdDoProduto(long id);

    List<Produto> buscarPorNomeDoProduto(String nome);

    List<Produto> buscarPorTipoDeProduto(TipoDeProduto tipo);

    List<Produto> buscarTodosOsProduto();

    void deletarProduto(long id);

}