package Repositories;

import Application.TipoDeProduto;
import Entities.Produto;
import Exceptions.RepositoryException; // Import necessário
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProdutoRepositoryDB implements IProdutoRepository {

    // 1. A conexão agora é um atributo da classe, obtida uma única vez.
    private final Connection conn = ConnectionFactory.getConnection();

    @Override
    public void salvarProduto(Produto produto) throws RepositoryException {
        String sql;
        if (produto.getIdProduto() == null || produto.getIdProduto() == 0) {
            sql = "INSERT INTO produto (nome, descricao, preco, categoria) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, categoria = ? WHERE id = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNomeProduto());
            stmt.setString(2, produto.getDescricaoProduto());
            stmt.setFloat(3, produto.getPrecoProduto());
            stmt.setString(4, produto.getCategoria().toString());

            if (produto.getIdProduto() != null && produto.getIdProduto() != 0) {
                stmt.setLong(5, produto.getIdProduto());
            }
            stmt.executeUpdate();

            if (produto.getIdProduto() == null || produto.getIdProduto() == 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setIdProduto(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao salvar o produto no banco de dados.", e);
        }
    }

    @Override
    public Produto buscarPorIdDoProduto(long id) throws RepositoryException {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParaProduto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar produto por ID.", e);
        }
        return null;
    }

    @Override
    public List<Produto> buscarPorNomeDoProduto(String nome) throws RepositoryException {
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearParaProduto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar produtos por nome.", e);
        }
        return produtos;
    }

    @Override
    public List<Produto> buscarPorTipoDeProduto(TipoDeProduto tipo) throws RepositoryException {
        String sql = "SELECT * FROM produto WHERE categoria = ?";
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearParaProduto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar produtos por tipo.", e);
        }
        return produtos;
    }

    @Override
    public List<Produto> buscarTodosOsProduto() throws RepositoryException {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapearParaProduto(rs));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao buscar todos os produtos.", e);
        }
        return produtos;
    }

    @Override
    public void deletarProduto(long id) throws RepositoryException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao deletar o produto com ID: " + id, e);
        }
    }

    private Produto mapearParaProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getFloat("preco"),
                TipoDeProduto.valueOf(rs.getString("categoria"))
        );
    }
}