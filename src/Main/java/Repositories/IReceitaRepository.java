package Repositories;

import Entities.Receita;
import Exceptions.RepositoryException; // Importa a exceção
import java.util.List;

public interface IReceitaRepository {

    void salvarReceita(Receita receita) throws RepositoryException;

    Receita buscarPorIdReceita(long id) throws RepositoryException;

    void deletarReceita(long id) throws RepositoryException;
}