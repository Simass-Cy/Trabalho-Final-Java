package Repositories;

import Application.StatusAgendamento;
import Entities.Agendamento;
import Entities.Cliente;
import Entities.Funcionario;
import java.time.LocalDate;
import java.util.List;

public interface IAgendamentoRepository {

    void salvarAgendamento(Agendamento agendamento);

    Agendamento buscarPorIdAgendamento(long id);

    List<Agendamento> buscarPorCliente(Cliente cliente);

    List<Agendamento> buscarPorData(LocalDate data);

    List<Agendamento> buscarPorVeterinario(Funcionario veterinario);

    void alterarStatusAgendamento(long id, StatusAgendamento novoStatus);

    void deletarAgendamento(long id);
}