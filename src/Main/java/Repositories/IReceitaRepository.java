package Repositories;

import Entities.Receita;
import java.util.List;

public interface IReceitaRepository {
    void salvarReceita(Receita receita);
    Receita buscarPorIdReceita(long id);
    void deletarReceita(long id);
}