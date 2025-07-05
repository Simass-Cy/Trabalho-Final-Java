package Service;

import Entities.*;
import Repositories.*;
import Application.StatusAgendamento;
import Exceptions.RepositoryException;
import Exceptions.ServiceException;

public class ConsultaService {

    private final IAgendamentoRepository agendamentoRepository = new AgendamentoRepositoryDB();
    private final IConsultaRepository consultaRepository = new ConsultaRepositoryDB();
    private final IReceitaRepository receitaRepository = new ReceitaRepositoryDB();


    public Consulta realizarConsulta(long idAgendamento, String descricao, String descricaoReceita) throws ServiceException {
        try {
            Agendamento agendamento = agendamentoRepository.buscarPorIdAgendamento(idAgendamento);
            if (agendamento == null) {
                throw new ServiceException("Agendamento com ID " + idAgendamento + " não encontrado.");
            }

            if (agendamento.getStatus() == StatusAgendamento.CANCELADO || agendamento.getStatus() == StatusAgendamento.REALIZADO) {
                throw new ServiceException("Este agendamento já foi finalizado ou cancelado.");
            }
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new ServiceException("A descrição da consulta não pode ser vazia.");
            }

            Receita receita = null;
            if (descricaoReceita != null && !descricaoReceita.trim().isEmpty()) {
                receita = new Receita(0, descricaoReceita);
                receitaRepository.salvarReceita(receita);
            }

            Consulta novaConsulta = new Consulta(
                    0,
                    agendamento.getDataHora(),
                    descricao,
                    agendamento.getFuncionarioQueAgendou(),
                    agendamento.getAnimal(),
                    agendamento
            );
            novaConsulta.setReceita(receita);

            consultaRepository.salvarConsulta(novaConsulta);
            agendamentoRepository.alterarStatusAgendamento(idAgendamento, StatusAgendamento.REALIZADO);

            return novaConsulta;

        } catch (RepositoryException e) {
            throw new ServiceException("Erro na camada de dados ao tentar realizar a consulta.", e);
        }
    }
}
