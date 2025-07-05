package Service;

import Entities.Produto;
import Repositories.IProdutoRepository;
import Repositories.ProdutoRepositoryDB;
import Application.TipoDeProduto; // Import necessário

import java.util.List;

public class ProdutoService {

    private final IProdutoRepository produtoRepository = new ProdutoRepositoryDB();


    public Produto cadastrarNovoProduto(String nome, String descricao, float preco, TipoDeProduto categoria) throws Exception {
        System.out.println("SERVICE: Iniciando cadastro de novo produto...");

        // Validações de Negócio
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome do produto não pode ser vazio.");
        }
        if (preco <= 0) {
            throw new Exception("O preço do produto deve ser maior que zero.");
        }
        if (categoria == null) {
            throw new Exception("A categoria do produto deve ser definida.");
        }

        // Se tudo estiver certo, cria o objeto e chama o repositório
        Produto novoProduto = new Produto(null, nome, descricao, preco, categoria);
        produtoRepository.salvarProduto(novoProduto);

        System.out.println("SERVICE: Produto '" + nome + "' cadastrado com sucesso!");
        return novoProduto;
    }

    /**
     * Busca um produto pelo seu ID.
     * @param id O ID do produto.
     * @return O produto encontrado.
     */
    public Produto encontrarProdutoPorId(long id) {
        return produtoRepository.buscarPorIdDoProduto(id);
    }

    /**
     * Lista todos os produtos de uma determinada categoria.
     * @param categoria O tipo do produto a ser filtrado.
     * @return Uma lista de produtos.
     */
    public List<Produto> listarProdutosPorCategoria(TipoDeProduto categoria) {
        return produtoRepository.buscarPorTipoDeProduto(categoria);
    }

    /**
     * Retorna a lista de todos os produtos cadastrados.
     * @return Uma lista de todos os produtos.
     */
    public List<Produto> listarTodosOsProdutos() {
        return produtoRepository.buscarTodosOsProduto();
    }

}
