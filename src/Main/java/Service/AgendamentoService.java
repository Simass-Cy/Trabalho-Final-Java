package Service;

import Entities.Agendamento;
import Entities.Animais;
import Entities.Cliente;
import Entities.Funcionario;
import Repositories.*;
import Application.StatusAgendamento;
import Exceptions.RepositoryException;
import Exceptions.ServiceException;
import java.time.LocalDateTime;
public class AgendamentoService {

    private final IAgendamentoRepository agendamentoRepository = new AgendamentoRepositoryDB();
    private final IClienteRepository clienteRepository = new ClienteRepositoryDB();
    private final IAnimalRepository animalRepository = new AnimalRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();


    public Agendamento agendarNovaConsulta(long idCliente, long idAnimal, long idFuncionario, LocalDateTime dataHora) throws ServiceException {
        try {

            Cliente cliente = clienteRepository.buscarPorIdCliente(idCliente);
            if (cliente == null) {
                throw new ServiceException("Cliente com ID " + idCliente + " não encontrado.");
            }

            Animais animal = animalRepository.buscarPorIdAnimal(idAnimal);
            if (animal == null) {
                throw new ServiceException("Animal com ID " + idAnimal + " não encontrado.");
            }

            if (animal.getDono().getId() != cliente.getId()) {
                throw new ServiceException("O animal '" + animal.getNomeAnimal() + "' não pertence ao cliente '" + cliente.getNome() + "'.");
            }

            Funcionario funcionario = funcionarioRepository.buscarPorIdFuncionario(idFuncionario);
            if (funcionario == null) {
                throw new ServiceException("Funcionário com ID " + idFuncionario + " não encontrado.");
            }

            if (dataHora == null || dataHora.isBefore(LocalDateTime.now())) {
                throw new ServiceException("A data e hora do agendamento deve ser uma data futura.");
            }

            Agendamento novoAgendamento = new Agendamento(0, dataHora, cliente, animal, funcionario);
            agendamentoRepository.salvarAgendamento(novoAgendamento);

            return novoAgendamento;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro na camada de dados ao tentar agendar a consulta.", e);
        }
    }


    public void atualizarStatus(long idAgendamento, StatusAgendamento novoStatus) throws ServiceException {
        try {
            Agendamento agendamento = agendamentoRepository.buscarPorIdAgendamento(idAgendamento);
            if (agendamento == null) {
                throw new ServiceException("Agendamento com ID " + idAgendamento + " não encontrado.");
            }
            agendamentoRepository.alterarStatusAgendamento(idAgendamento, novoStatus);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro na camada de dados ao atualizar o status do agendamento.", e);
        }
    }
    public void deletarAgendamento(long idAgendamento) throws ServiceException {
        try {
            // Regra de Negócio 1: Verificar se o agendamento existe.
            Agendamento agendamento = agendamentoRepository.buscarPorIdAgendamento(idAgendamento);
            if (agendamento == null) {
                throw new ServiceException("Agendamento com ID " + idAgendamento + " não encontrado. Nada a deletar.");
            }

            // Regra de Negócio 2 (Exemplo): Não permitir deletar agendamentos que já foram realizados.
            if (agendamento.getStatus() == StatusAgendamento.REALIZADO) {
                throw new ServiceException("Não é possível deletar um agendamento que já foi realizado, pois ele faz parte do histórico.");
            }

            // Se as regras de negócio passaram, chama o repositório para deletar.
            agendamentoRepository.deletarAgendamento(idAgendamento);
            System.out.println("SERVICE: Agendamento ID " + idAgendamento + " deletado com sucesso.");

        } catch (RepositoryException e) {
            // "Traduz" o erro técnico do repositório para um erro de negócio mais claro.
            throw new ServiceException("Erro na camada de dados ao tentar deletar o agendamento.", e);
        }
    }

}
