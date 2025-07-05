package Service;

import Entities.Agendamento;
import Entities.Animais;
import Entities.Cliente;
import Entities.Funcionario;
import Repositories.*;
import Application.StatusAgendamento;
import java.time.LocalDateTime;

public class AgendamentoService {

    private final IAgendamentoRepository agendamentoRepository = new AgendamentoRepositoryDB();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();
    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();


    public Agendamento agendarNovaConsulta(long idCliente, long idAnimal, long idFuncionario, LocalDateTime dataHora) throws Exception {
        System.out.println("SERVICE: Iniciando processo de novo agendamento...");

        // Regras de Negócio e Validações
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        if (cliente == null) {
            throw new Exception("Cliente com ID " + idCliente + " não encontrado.");
        }

        Animais animal = animalRepository.buscarPorId(idAnimal);
        if (animal == null) {
            throw new Exception("Animal com ID " + idAnimal + " não encontrado.");
        }

        // Validação extra: o animal realmente pertence a este cliente?
        if (animal.getDono().getId() != cliente.getId()) {
            throw new Exception("O animal '" + animal.getNomeAnimal() + "' não pertence ao cliente '" + cliente.getNome() + "'.");
        }

        Funcionario funcionario = funcionarioRepository.buscarPorId(idFuncionario);
        if (funcionario == null) {
            throw new Exception("Funcionário com ID " + idFuncionario + " não encontrado.");
        }

        if (dataHora == null || dataHora.isBefore(LocalDateTime.now())) {
            throw new Exception("A data e hora do agendamento deve ser uma data futura.");
        }

        // Se tudo estiver certo, cria o objeto e salva
        Agendamento novoAgendamento = new Agendamento(0, dataHora, cliente, animal, funcionario);
        agendamentoRepository.salvarAgendamento(novoAgendamento);

        System.out.println("SERVICE: Agendamento para o animal '" + animal.getNomeAnimal() + "' realizado com sucesso.");
        return novoAgendamento;
    }


    public void atualizarStatus(long idAgendamento, StatusAgendamento novoStatus) throws Exception {
        Agendamento agendamento = agendamentoRepository.buscarPorIdAgendamento(idAgendamento);
        if (agendamento == null) {
            throw new Exception("Agendamento com ID " + idAgendamento + " não encontrado.");
        }
        agendamentoRepository.alterarStatusAgendamento(idAgendamento, novoStatus);
        System.out.println("SERVICE: Status do agendamento ID " + idAgendamento + " atualizado para " + novoStatus);
    }



}
