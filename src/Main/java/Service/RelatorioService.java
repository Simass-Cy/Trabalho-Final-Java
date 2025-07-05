package Service;

import Entities.Consulta;
import Entities.Funcionario;
import Entities.RelatorioConsulta;
import Repositories.*;
import java.time.LocalDate;
import java.util.List;

public class RelatorioService {

    // 1. O Service depende dos repositórios para buscar os dados brutos.
    private final IConsultaRepository consultaRepository = new ConsultaRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();
    private final IRelatorioRepository relatorioRepository = new RelatorioRepositoryDB(); // O repositório para salvar o próprio relatório

    /**
     * Orquestra a criação e o salvamento de um relatório de consultas.
     * @param idAutor O ID do funcionário que está a gerar o relatório.
     * @param inicio A data de início do período.
     * @param fim A data de fim do período.
     * @return O objeto RelatorioConsulta que foi criado e salvo.
     * @throws Exception Se o autor não for encontrado.
     */
    public RelatorioConsulta gerarESalvarRelatorioDeConsultas(long idAutor, LocalDate inicio, LocalDate fim) throws Exception {
        System.out.println("SERVICE: A iniciar a geração de relatório...");

        // 2. USA os repositórios para buscar os dados necessários.
        Funcionario autor = funcionarioRepository.buscarPorId(idAutor);
        if (autor == null) {
            throw new Exception("Autor com ID " + idAutor + " não encontrado. Não é possível gerar o relatório.");
        }

        List<Consulta> consultas = consultaRepository.buscarPorPeriodoConsulta(inicio, fim);
        if (consultas.isEmpty()) {
            System.out.println("SERVICE_INFO: Nenhuma consulta encontrada no período. O relatório será gerado vazio.");
        }

        // 3. MONTA o objeto de domínio com os dados coletados.
        RelatorioConsulta relatorio = new RelatorioConsulta(0L, autor, inicio, fim, consultas);

        // 4. CHAMA o repositório apropriado para persistir o novo objeto Relatorio no banco de dados.
        relatorioRepository.salvarRelatorio(relatorio);

        System.out.println("SERVICE: Relatório gerado e salvo com sucesso com o ID: " + relatorio.getId());
        return relatorio;
    }

}



