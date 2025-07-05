package Service;

import Entities.*;
import Repositories.*;
import Application.StatusAgendamento;

public class ConsultaService {

    private final IAgendamentoRepository agendamentoRepository = new AgendamentoRepositoryDB();
    private final IConsultaRepository consultaRepository = new ConsultaRepositoryDB();
    private final IReceitaRepository receitaRepository = new ReceitaRepositoryDB(); // Usado para salvar a receita


    public Consulta realizarConsulta(long idAgendamento, String descricao, String descricaoReceita) throws Exception {
        System.out.println("SERVICE: Iniciando processo de realização de consulta...");

        // 1. Busca o agendamento
        Agendamento agendamento = agendamentoRepository.buscarPorIdAgendamento(idAgendamento);
        if (agendamento == null) {
            throw new Exception("Agendamento com ID " + idAgendamento + " não encontrado.");
        }

        // 2. Regra de Negócio: Verifica se o agendamento não foi cancelado ou já realizado
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO || agendamento.getStatus() == StatusAgendamento.REALIZADO) {
            throw new Exception("Este agendamento já foi finalizado ou cancelado e não pode ser realizado.");
        }

        // 3. Lida com a Receita (se houver)
        Receita receita = null;
        if (descricaoReceita != null && !descricaoReceita.trim().isEmpty()) {
            System.out.println("SERVICE: Criando e salvando nova receita...");
            receita = new Receita(0, descricaoReceita);
            receitaRepository.salvarReceita(receita); // Salva a receita para obter um ID
        }

        // 4. Cria o objeto Consulta a partir dos dados do agendamento
        Consulta novaConsulta = new Consulta(
                0,
                agendamento.getDataHora(),
                descricao,
                agendamento.getFuncionarioQueAgendou(), // Assumindo que quem agendou é o veterinário que atende
                agendamento.getAnimal(),
                agendamento
        );
        novaConsulta.setReceita(receita); // Associa a receita (se foi criada)

        // 5. Salva a consulta no banco
        consultaRepository.salvarConsulta(novaConsulta);

        // 6. Atualiza o status do agendamento original para REALIZADO
        agendamentoRepository.alterarStatusAgendamento(idAgendamento, StatusAgendamento.REALIZADO);

        System.out.println("SERVICE: Consulta (ID: " + novaConsulta.getId() + ") realizada e salva com sucesso!");
        return novaConsulta;
    }

}
