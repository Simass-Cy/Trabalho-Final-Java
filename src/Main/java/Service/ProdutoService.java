package Service;

import Entities.Produto;
import Repositories.IProdutoRepository;
import Repositories.ProdutoRepositoryDB;
import Application.TipoDeProduto;
import Exceptions.RepositoryException;
import Exceptions.ServiceException;
import java.util.List;

public class ProdutoService {

    private final IProdutoRepository produtoRepository = new ProdutoRepositoryDB();

    public Produto cadastrarNovoProduto(String nome, String descricao, float preco, TipoDeProduto categoria) throws ServiceException {
        try {
            // Validações de Negócio
            if (nome == null || nome.trim().isEmpty()) {
                throw new ServiceException("O nome do produto não pode ser vazio.");
            }
            if (preco <= 0) {
                throw new ServiceException("O preço do produto deve ser maior que zero.");
            }
            if (categoria == null) {
                throw new ServiceException("A categoria do produto deve ser definida.");
            }

            Produto novoProduto = new Produto(null, nome, descricao, preco, categoria);
            produtoRepository.salvarProduto(novoProduto);

            return novoProduto;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro na camada de dados ao tentar cadastrar o produto.", e);
        }
    }


    public Produto encontrarProdutoPorId(long id) throws ServiceException {
        try {
            return produtoRepository.buscarPorIdDoProduto(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar produto por ID.", e);
        }
    }


    public List<Produto> listarProdutosPorCategoria(TipoDeProduto categoria) throws ServiceException {
        try {
            return produtoRepository.buscarPorTipoDeProduto(categoria);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar produtos por categoria.", e);
        }
    }


    public List<Produto> listarTodosOsProdutos() throws ServiceException {
        try {
            return produtoRepository.buscarTodosOsProduto();
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar todos os produtos.", e);
        }
    }

    public void deletarProduto(long id) throws ServiceException {
        try {
            Produto produtoExistente = produtoRepository.buscarPorIdDoProduto(id);
            if (produtoExistente == null) {
                throw new ServiceException("Produto com ID " + id + " não encontrado. Nada a ser deletado.");
            }

            produtoRepository.deletarProduto(id);
            System.out.println("SERVICE: Produto '" + produtoExistente.getNomeProduto() + "' deletado com sucesso.");

        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao deletar o produto. Verifique se ele não está associado a outras partes do sistema.", e);
        }
    }


}
