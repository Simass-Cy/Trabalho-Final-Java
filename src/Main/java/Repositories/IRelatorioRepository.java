package Repositories;

import Entities.RelatorioConsulta;
import Exceptions.RepositoryException;

public interface IRelatorioRepository {

    void salvarRelatorio(RelatorioConsulta relatorio) throws RepositoryException;

}
