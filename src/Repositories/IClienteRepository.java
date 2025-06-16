package Repositories;
import Entities.Animais;
import Entities.Cliente;
import java.util.List;

    //interface dos metodos do cliente (talvez sejam adicionados novos metodos)
public interface IClienteRepository {

    void salvar(Cliente cliente);

    Cliente buscarPorId(long id);
    //listas para retornar informacoes que tem mais de uma correspondecia
    List<Cliente> buscarPorNome(String nome);

    List<Animais> buscarPeloNomeDoAnimal(String nomeAnimal);

    List<Cliente> buscarTodos();

    void deletar(long id);

    void adicionarAnimalAoCliente(Cliente cliente, Animais animal);
}

