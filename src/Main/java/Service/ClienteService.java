package Service;

import Entities.Animais;
import Entities.Cliente;
import Repositories.AnimalRepositoryDB;
import Repositories.ClienteRepositoryDB;
import Repositories.IAnimalRepository;
import Repositories.IClienteRepository;

import java.util.List;
import java.util.regex.Pattern;


public class ClienteService {

    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();
    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();


    public Cliente cadastrarNovoCliente(String nome, String email, String senha, String telefone) throws Exception {
        System.out.println("SERVICE: Iniciando processo de cadastro de novo cliente...");

        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome do cliente não pode ser vazio.");
        }
        if (!isEmailValido(email)) {
            throw new Exception("O formato do email fornecido é inválido.");
        }

        List<Cliente> clientesComMesmoEmail = clienteRepository.buscarPorEmail(email);
        if (!clientesComMesmoEmail.isEmpty()) {
            throw new Exception("Já existe um cliente cadastrado com este email.");
        }

        Cliente novoCliente = new Cliente(0, nome, senha, email, telefone);
        clienteRepository.salvar(novoCliente);

        System.out.println("SERVICE: Cliente '" + novoCliente.getNome() + "' cadastrado com sucesso!");
        return novoCliente;
    }


    public Animais adicionarAnimalParaCliente(long idCliente, String nomeAnimal, java.time.LocalDate dataNascimento) throws Exception {
        Cliente dono = clienteRepository.buscarPorId(idCliente);
        if (dono == null) {
            throw new Exception("Cliente com ID " + idCliente + " não encontrado. Não é possível adicionar o animal.");
        }

        Animais novoAnimal = new Animais(null, nomeAnimal, dataNascimento, dono);

        animalRepository.salvar(novoAnimal);

        System.out.println("SERVICE: Animal '" + nomeAnimal + "' adicionado para o cliente '" + dono.getNome() + "'.");
        return novoAnimal;
    }


    public Cliente encontrarClientePorId(long id) {
        return clienteRepository.buscarPorId(id);
    }


    public List<Animais> listarAnimaisDoCliente(long idCliente) {
        Cliente dono = new Cliente();
        dono.setId(idCliente); // Só precisamos do ID para a busca
        return animalRepository.buscarPorDono(dono);
    }


    public List<Cliente> listarTodosOsClientes() {
        return clienteRepository.buscarTodos();
    }

   /*valida o formato de email, assim ele valida a estrutura.
    *melhor do que o email.equals.(...)
    * */
    private boolean isEmailValido(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}