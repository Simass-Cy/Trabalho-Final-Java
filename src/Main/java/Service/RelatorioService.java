package Service;

import Entities.Consulta;
import Entities.Funcionario;
import Entities.RelatorioConsulta;
import Exceptions.RepositoryException;
import Exceptions.ServiceException;
import Repositories.*;
import java.time.LocalDate;
import java.util.List;

public class RelatorioService {

    private final IConsultaRepository consultaRepository = new ConsultaRepositoryDB();
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();
    private final IRelatorioRepository relatorioRepository = new RelatorioRepositoryDB(); // O repositório para salvar o próprio relatório


    public RelatorioConsulta gerarESalvarRelatorioDeConsultas(long idAutor, LocalDate inicio, LocalDate fim) throws ServiceException {
        try {
            // procura os dados
            Funcionario autor = funcionarioRepository.buscarPorIdFuncionario(idAutor);
            if (autor == null) {
                throw new ServiceException("Autor com ID " + idAutor + " não encontrado. Não é possível gerar o relatório.");
            }

            List<Consulta> consultas = consultaRepository.buscarPorPeriodoConsulta(inicio, fim);
            if (consultas.isEmpty()) {
                System.out.println("SERVICE_INFO: Nenhuma consulta encontrada no período. O relatório será gerado vazio, mas será salvo.");
            }

            // monta o objeto
            RelatorioConsulta relatorio = new RelatorioConsulta(0L, autor, inicio, fim, consultas);

            // chama o repositorio pro objeto
            relatorioRepository.salvarRelatorio(relatorio);

            System.out.println("SERVICE: Relatório gerado e salvo com sucesso com o ID: " + relatorio.getId());
            return relatorio;

        } catch (RepositoryException e) {
            // "Traduz" qualquer erro da camada de baixo para um erro de serviço
            throw new ServiceException("Erro na camada de dados ao gerar ou salvar o relatório.", e);
        }
    }
}



