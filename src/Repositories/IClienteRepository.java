package Repositories;
import Entities.Animais;
import Entities.Cliente;

import java.util.List;
public interface IClienteRepository {

    void salvar(Cliente cliente);

    Cliente buscarPorId(long id);

    List<Cliente> buscarPorNome(String nome);

    List<Animais> buscarPeloNomeDoAnimal(String nomeAnimal);

    List<Cliente> buscarTodos();

    void deletar(long id);

    void adicionarAnimalAoCliente(Cliente cliente, Animais animal);
}

