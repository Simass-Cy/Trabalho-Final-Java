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
            if (funcionario == null || funcionario.getNomeFuncionario().trim().isEmpty()) {
                throw new ServiceException("Os dados do funcionário não podem ser nulos ou vazios.");
            }
            if (!isEmailValido(funcionario.getEmailFuncionario())) {
                throw new ServiceException("O formato do email do funcionário é inválido.");
            }

            if (!funcionarioRepository.buscarPorEmailFuncionario(funcionario.getEmailFuncionario()).isEmpty()) {
                throw new ServiceException("Já existe um funcionário cadastrado com este email.");
            }

            if (funcionario instanceof Veterinario) {
                Veterinario vet = (Veterinario) funcionario;
                if (vet.getCrmv() == null || vet.getCrmv().trim().isEmpty()) {
                    throw new ServiceException("Para contratar um veterinário, o CRMV é obrigatório.");
                }
            }

            funcionarioRepository.salvarFuncionario(funcionario);

            return funcionario;
        } catch (RepositoryException e) {
            throw new ServiceException("Erro na camada de dados ao tentar contratar o funcionário.", e);
        }
    }


    public List<Funcionario> listarFuncionariosPorCargo(Cargo cargo) throws ServiceException {
        try {
            return funcionarioRepository.buscarPorCargo(cargo);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar funcionários por cargo.", e);
        }
    }


    public Funcionario encontrarFuncionarioPorId(long id) throws ServiceException {
        try {
            return funcionarioRepository.buscarPorIdFuncionario(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar funcionário por ID.", e);
        }
    }


    public List<Funcionario> listarTodosOsFuncionarios() throws ServiceException {
        try {
            return funcionarioRepository.buscarTodosFuncionario();
        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao buscar todos os funcionários.", e);
        }
    }

    public void deletarFuncionario(long id) throws ServiceException {
        try {
            // Verificar se o funcionário existe.
            Funcionario funcionarioExistente = funcionarioRepository.buscarPorIdFuncionario(id);
            if (funcionarioExistente == null) {
                throw new ServiceException("Funcionário com ID " + id + " não encontrado. Nada a deletar.");
            }

            funcionarioRepository.deletarFuncionario(id);

            System.out.println("SERVICE: Funcionário '" + funcionarioExistente.getNomeFuncionario() + "' deletado com sucesso.");

        } catch (RepositoryException e) {
            throw new ServiceException("Erro ao deletar o funcionário. Verifique se ele não está associado a agendamentos ou consultas existentes.", e);
        }
    }

    private boolean isEmailValido(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
