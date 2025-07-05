package Service;

import Entities.Animais;
import Entities.Cliente;
import Repositories.AnimalRepositoryDB;
import Repositories.ClienteRepositoryDB;
import Repositories.IAnimalRepository;
import Repositories.IClienteRepository;
import java.time.LocalDate;
import java.util.List;

public class AnimalService {

    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();


    public Animais cadastrarNovoAnimal(long idCliente, String nomeAnimal, LocalDate dataNascimento) throws Exception {
        System.out.println("SERVICE: Iniciando processo de cadastro de novo animal...");

        // Regra de Negócio: O animal precisa de um dono que exista.
        Cliente dono = clienteRepository.buscarPorId(idCliente);
        if (dono == null) {
            throw new Exception("Cliente com ID " + idCliente + " não encontrado. Não é possível cadastrar o animal.");
        }

        if (nomeAnimal == null || nomeAnimal.trim().isEmpty()) {
            throw new Exception("O nome do animal não pode ser vazio.");
        }

        // Cria o objeto e chama o repositório para salvar
        Animais novoAnimal = new Animais(null, nomeAnimal, dataNascimento, dono);
        animalRepository.salvar(novoAnimal);

        System.out.println("SERVICE: Animal '" + nomeAnimal + "' cadastrado com sucesso para o cliente '" + dono.getNome() + "'.");
        return novoAnimal;
    }

    /**
     * Busca todos os animais pertencentes a um cliente.
     * @param idCliente O ID do cliente.
     * @return Uma lista com os animais do cliente.
     */
    public List<Animais> listarAnimaisPorDono(long idCliente) {
        Cliente dono = new Cliente();
        dono.setId(idCliente); // Só precisamos do ID para a busca no repositório
        return animalRepository.buscarPorDono(dono);
    }

    /**
     * Busca um animal específico pelo seu ID.
     * @param idAnimal O ID do animal.
     * @return O animal encontrado.
     */
    public Animais encontrarAnimalPorId(long idAnimal) {
        return animalRepository.buscarPorId(idAnimal);
    }

}
