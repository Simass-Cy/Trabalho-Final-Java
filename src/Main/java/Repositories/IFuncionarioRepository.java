package Repositories;

import Application.Cargo;
import Entities.Funcionario;
import java.util.List;
import Exceptions.RepositoryException;

public interface IFuncionarioRepository {

    void salvarFuncionario(Funcionario funcionario) throws RepositoryException;

    Funcionario buscarPorIdFuncionario(long id) throws RepositoryException;

    List<Funcionario> buscarTodosFuncionario() throws RepositoryException;

    List<Funcionario> buscarPorCargo(Cargo cargo) throws RepositoryException;

    List<Funcionario> buscarPorNomeFuncionario(String nomeFuncionario) throws RepositoryException;

    void deletarFuncionario(long id) throws RepositoryException;

    List<Funcionario> buscarPorEmailFuncionario(String email) throws RepositoryException;

}