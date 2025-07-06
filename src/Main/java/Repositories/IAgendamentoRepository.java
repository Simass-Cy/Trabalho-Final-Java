package Repositories;

import Application.StatusAgendamento;
import Entities.Agendamento;
import Entities.Cliente;
import Entities.Funcionario;
import Exceptions.RepositoryException;
import java.time.LocalDate;
import java.util.List;


public interface IAgendamentoRepository {

    void salvarAgendamento(Agendamento agendamento) throws RepositoryException;

    Agendamento buscarPorIdAgendamento(long id) throws RepositoryException;

    List<Agendamento> buscarPorCliente(Cliente cliente) throws RepositoryException;

    List<Agendamento> buscarPorData(LocalDate data) throws RepositoryException;

    List<Agendamento> buscarPorVeterinario(Funcionario veterinario) throws RepositoryException;

    void alterarStatusAgendamento(long id, StatusAgendamento novoStatus) throws RepositoryException;

    void deletarAgendamento(long id) throws RepositoryException;
}