package Service;

import Entities.Funcionario;
import Entities.Veterinario;
import Repositories.FuncionarioRepositoryDB;
import Repositories.IFuncionarioRepository;

import java.util.List;
import java.util.regex.Pattern;

public class FuncionarioService {
    private final IFuncionarioRepository funcionarioRepository = new FuncionarioRepositoryDB();

    public Funcionario contratarNovoFuncionario(Funcionario funcionario) throws Exception {
        System.out.println("SERVICE: Iniciando processo de contratação...");

        // Validações
        if (funcionario == null || funcionario.getNomeFuncionario().trim().isEmpty()) {
            throw new Exception("Os dados do funcionário não podem ser nulos ou vazios.");
        }
        if (!isEmailValido(funcionario.getEmailFuncionario())) {
            throw new Exception("O formato do email do funcionário é inválido.");
        }

        // Verifica se o email já está em uso
        if (!funcionarioRepository.buscarPorEmail(funcionario.getEmailFuncionario()).isEmpty()) {
            throw new Exception("Já existe um funcionário cadastrado com este email.");
        }

        // Regra de negócio específica para veterinário
        if (funcionario instanceof Veterinario) {
            Veterinario vet = (Veterinario) funcionario;
            if (vet.getCrmv() == null || vet.getCrmv().trim().isEmpty()) {
                throw new Exception("Para contratar um veterinário, o CRMV é obrigatório.");
            }
        }

        // Se tudo estiver certo, chama o repositório para salvar
        funcionarioRepository.salvar(funcionario);

        System.out.println("SERVICE: Funcionário '" + funcionario.getNomeFuncionario() + "' contratado com sucesso!");
        return funcionario;
    }

    /**
     * Busca todos os funcionários de um determinado cargo.
     * @param cargo O cargo a ser filtrado.
     * @return Uma lista de funcionários.
     */
    public List<Funcionario> listarFuncionariosPorCargo(Application.Cargo cargo) {
        return funcionarioRepository.buscarPorCargo(cargo);
    }

    /**
     * Busca um funcionário pelo seu ID.
     */
    public Funcionario encontrarFuncionarioPorId(long id) {
        return funcionarioRepository.buscarPorId(id);
    }

    /**
     * Retorna a lista de todos os funcionários.
     */
    public List<Funcionario> listarTodosOsFuncionarios() {
        return funcionarioRepository.buscarTodos();
    }

    /**
     * Validador simples para o formato do email.
     */
    private boolean isEmailValido(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }

}
