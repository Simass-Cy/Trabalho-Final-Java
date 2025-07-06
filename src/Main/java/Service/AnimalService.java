package Service;

import Entities.Animais;
import Entities.Cliente;
import Exceptions.RepositoryException;
import Exceptions.ServiceException;
import Repositories.AnimalRepositoryDB;
import Repositories.ClienteRepositoryDB;
import Repositories.IAnimalRepository;
import Repositories.IClienteRepository;
import java.time.LocalDate;
import java.util.List;

public class AnimalService {

    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();


    public Animais cadastrarNovoAnimal(long idCliente, String nomeAnimal, LocalDate dataNascimento) throws ServiceException {
        try {
            //O animal precisa de um dono que exista.
            Cliente dono = clienteRepository.buscarPorIdCliente(idCliente);
            if (dono == null) {
                throw new ServiceException("Cliente com ID " + idCliente + " não encontrado. Não é possível cadastrar o animal.");
            }

            if (nomeAnimal == null || nomeAnimal.trim().isEmpty()) {
                throw new ServiceException("O nome do animal não pode ser vazio.");
            }

            Animais novoAnimal = new Animais(null, nomeAnimal, dataNascimento, dono);
            animalRepository.salvarAnimal(novoAnimal); // Usa o novo nome do método

            return novoAnimal;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao comunicar com o banco de dados durante o cadastro do animal.", e);
        }
    }

    public List<Animais> listarAnimaisPorDono(long idCliente) throws ServiceException {
        try {
            Cliente dono = new Cliente();
            dono.setId(idCliente);
            return animalRepository.buscarPorDono(dono);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar os animais do cliente.", e);
        }
    }

    //busca o animal pelo id (pk)
    public Animais encontrarAnimalPorId(long idAnimal) throws ServiceException {
        try {
            return animalRepository.buscarPorIdAnimal(idAnimal);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar o animal por ID.", e);
        }
    }
}
