package Repositories;

import Application.Cargo;
import Entities.Funcionario;
import java.util.List;

public interface IFuncionarioRepository {

    void salvar(Funcionario funcionario);

    Funcionario buscarPorId(long id);

    List<Funcionario> buscarTodos();

    List<Funcionario> buscarPorCargo(Cargo cargo);

    List<Funcionario> buscarPorNome(String nomeFuncionario);

    void deletar(long id);
}