package Service;

import Application.Cargo; // Import necessário
import Entities.Funcionario;
import Entities.Veterinario;
import Exceptions.RepositoryException; // Import necessário
import Exceptions.ServiceException;   // Import necessário
import Repositories.FuncionarioRepositoryDB;
import Repositories.IFuncionarioRepository;
import java.util.List;
import java.util.regex.Pattern;

public class FuncionarioService {

    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();

    public Funcionario contratarNovoFuncionario(Funcionario funcionario) throws ServiceException {
        try {
            // Validações
            if (funcionario == null || funcionario.getNomeFuncionario().trim().isEmpty()) {
                throw new ServiceException("Os dados do funcionário não podem ser nulos ou vazios.");
            }
            if (!isEmailValido(funcionario.getEmailFuncionario())) {
                throw new ServiceException("O formato do email do funcionário é inválido.");
            }

            // Usa o novo nome do método do repositório
            if (!funcionarioRepository.buscarPorEmailFuncionario(funcionario.getEmailFuncionario()).isEmpty()) {
                throw new ServiceException("Já existe um funcionário cadastrado com este email.");
            }

            if (funcionario instanceof Veterinario) {
                Veterinario vet = (Veterinario) funcionario;
                if (vet.getCrmv() == null || vet.getCrmv().trim().isEmpty()) {
                    throw new ServiceException("Para contratar um veterinário, o CRMV é obrigatório.");
                }
            }

            // Usa o novo nome do método e trata a exceção
            funcionarioRepository.salvarFuncionario(funcionario);

            return funcionario;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro na camada de dados ao tentar contratar o funcionário.", e);
        }
    }

    /**
     * Busca todos os funcionários de um determinado cargo.
     * @param cargo O cargo a ser filtrado.
     * @return Uma lista de funcionários.
     * @throws ServiceException Se ocorrer um erro no banco de dados.
     */
    public List<Funcionario> listarFuncionariosPorCargo(Cargo cargo) throws ServiceException {
        try {
            return funcionarioRepository.buscarPorCargo(cargo);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar funcionários por cargo.", e);
        }
    }

    /**
     * Busca um funcionário pelo seu ID.
     * @throws ServiceException Se ocorrer um erro no banco de dados.
     */
    public Funcionario encontrarFuncionarioPorId(long id) throws ServiceException {
        try {
            return funcionarioRepository.buscarPorIdFuncionario(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar funcionário por ID.", e);
        }
    }

    /**
     * Retorna a lista de todos os funcionários.
     * @throws ServiceException Se ocorrer um erro no banco de dados.
     */
    public List<Funcionario> listarTodosOsFuncionarios() throws ServiceException {
        try {
            return funcionarioRepository.buscarTodosFuncionario();
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar todos os funcionários.", e);
        }
    }

    public void deletarFuncionario(long id) throws ServiceException {
        try {
            // Regra de Negócio 1: Verificar se o funcionário realmente existe.
            Funcionario funcionarioExistente = funcionarioRepository.buscarPorIdFuncionario(id);
            if (funcionarioExistente == null) {
                throw new ServiceException("Funcionário com ID " + id + " não encontrado. Nada a deletar.");
            }

            // Tenta deletar. O banco de dados irá lançar um erro se houver dependências
            // (chaves estrangeiras em 'agendamento' ou 'consulta'). Nosso catch irá tratar isso.
            funcionarioRepository.deletarFuncionario(id);

            System.out.println("SERVICE: Funcionário '" + funcionarioExistente.getNomeFuncionario() + "' deletado com sucesso.");

        } catch (RepositoryException e) {
            // "Traduz" o erro técnico do repositório para um erro de negócio mais claro.
            throw new ServiceException("Erro ao deletar o funcionário. Verifique se ele não está associado a agendamentos ou consultas existentes.", e);
        }
    }

    private boolean isEmailValido(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
