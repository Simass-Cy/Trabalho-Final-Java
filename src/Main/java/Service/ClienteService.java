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
import java.util.regex.Pattern;


public class ClienteService {

    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();
    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();


    public Cliente cadastrarNovoCliente(String nome, String email, String senha, String telefone) throws ServiceException {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new ServiceException("O nome do cliente não pode ser vazio.");
            }
            if (!isEmailValido(email)) {
                throw new ServiceException("O formato do email fornecido é inválido.");
            }

            if (!clienteRepository.buscarPorEmailCliente(email).isEmpty()) {
                throw new ServiceException("Já existe um cliente cadastrado com este email.");
            }

            Cliente novoCliente = new Cliente(0, nome, senha, email, telefone);
            clienteRepository.salvarCliente(novoCliente);
            return novoCliente;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao comunicar com o banco de dados durante o cadastro do cliente.", e);
        }
    }


    public Animais adicionarAnimalParaCliente(long idCliente, String nomeAnimal, LocalDate dataNascimento) throws ServiceException {
        try {
            Cliente dono = clienteRepository.buscarPorIdCliente(idCliente);
            if (dono == null) {
                throw new ServiceException("Cliente com ID " + idCliente + " não encontrado. Não é possível adicionar o animal.");
            }
            if (nomeAnimal == null || nomeAnimal.trim().isEmpty()) {
                throw new ServiceException("O nome do animal não pode ser vazio.");
            }

            Animais novoAnimal = new Animais(null, nomeAnimal, dataNascimento, dono);
            animalRepository.salvarAnimal(novoAnimal);
            return novoAnimal;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao comunicar com o banco de dados ao adicionar o animal.", e);
        }
    }


    public Cliente encontrarClientePorId(long id) throws ServiceException {
        try {
            return clienteRepository.buscarPorIdCliente(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar cliente por ID.", e);
        }
    }


    public List<Cliente> listarTodosOsClientes() throws ServiceException {
        try {
            return clienteRepository.buscarTodosCliente();
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar a lista de todos os clientes.", e);
        }
    }

    public void deletarCliente(long id) throws ServiceException {
        try {
            // Regra de Negócio: Verificar se o cliente realmente existe antes de tentar deletar.
            Cliente clienteExistente = clienteRepository.buscarPorIdCliente(id);
            if (clienteExistente == null) {
                throw new ServiceException("Cliente com ID " + id + " não encontrado. Nada a ser deletado.");
            }

            // Tenta deletar. O banco de dados irá impedir a deleção se houver
            // chaves estrangeiras apontando para este cliente (ex: na tabela agendamento).
            // Nosso bloco catch irá tratar esse erro.
            clienteRepository.deletarCliente(id);

            System.out.println("SERVICE: Cliente '" + clienteExistente.getNome() + "' deletado com sucesso.");

        } catch (RepositoryException e) {
            // "Traduz" o erro técnico do repositório para um erro de negócio mais claro.
            throw new ServiceException("Erro ao deletar o cliente. Verifique se ele não possui agendamentos ou consultas pendentes.", e);
        }
    }

    private boolean isEmailValido(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}