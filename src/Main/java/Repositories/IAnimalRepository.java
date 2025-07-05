package Repositories;

import Entities.Animais;
import Entities.Cliente;
import java.util.List;

public interface IAnimalRepository {

    void salvar(Animais animal);

    Animais buscarPorId(long id);

    List<Animais> buscarPorNome(String nome);

    List<Animais> buscarPorDono(Cliente dono);

    List<Animais> buscarTodos();

    void deletar(long id);
}