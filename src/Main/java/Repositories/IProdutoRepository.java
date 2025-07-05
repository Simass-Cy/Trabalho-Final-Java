package Repositories;

import Application.TipoDeProduto;
import Entities.Produto;
import java.util.List;
import Exceptions.RepositoryException;

public interface IProdutoRepository {

    void salvarProduto(Produto produto) throws RepositoryException;

    Produto buscarPorIdDoProduto(long id) throws RepositoryException;

    List<Produto> buscarPorNomeDoProduto(String nome) throws RepositoryException;

    List<Produto> buscarPorTipoDeProduto(TipoDeProduto tipo) throws RepositoryException;

    List<Produto> buscarTodosOsProduto() throws RepositoryException;

    void deletarProduto(long id) throws RepositoryException;

}