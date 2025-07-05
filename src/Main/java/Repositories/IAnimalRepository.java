package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.util.List;
import Exceptions.RepositoryException;

public interface IAnimalRepository {

    void salvarAnimal(Animais animal) throws RepositoryException;

    Animais buscarPorIdAnimal(long id) throws RepositoryException;

    List<Animais> buscarPorNomeAnimal(String nome) throws RepositoryException;

    List<Animais> buscarPorDono(Cliente dono) throws RepositoryException;

    List<Animais> buscarTodosAnimal() throws RepositoryException;

    void deletarAnimal(long id) throws RepositoryException;
}