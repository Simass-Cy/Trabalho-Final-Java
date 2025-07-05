package Repositories;
import Entities.Animais;
import Entities.Cliente;
import java.util.List;
import Exceptions.RepositoryException;

    //interface dos metodos do cliente (talvez sejam adicionados novos metodos)
public interface IClienteRepository {

        void salvarCliente(Cliente cliente) throws RepositoryException;

        Cliente buscarPorIdCliente(long id) throws RepositoryException;

        List<Cliente> buscarPorNomeCliente(String nome) throws RepositoryException;

        List<Cliente> buscarTodosCliente() throws RepositoryException;

        void deletarCliente(long id) throws RepositoryException;

        void adicionarAnimalAoCliente(Cliente cliente, Animais animal) throws RepositoryException;

        List<Cliente> buscarPorEmailCliente(String email) throws RepositoryException;
}

