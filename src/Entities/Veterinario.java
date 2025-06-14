package Entities;

import Application.Cargo;

/**
 * A classe Veterinario é uma classe concreta que herda de Funcionario.
 * Ela representa um funcionário com o cargo de veterinário.
 */
public class Veterinario extends Funcionario {

    // 1. Atributo específico da classe Veterinario
    private String crmv;

    /**
     * Construtor padrão.
     */
    public Veterinario() {
        // 2. Garante que o cargo seja definido como VETERINARIO por padrão.
        super.setCargo(Cargo.VETERINARIO);
    }

    /**
     * Construtor completo para criar um Veterinario com todos os dados.
     * @param idFuncionario O ID do funcionário.
     * @param nomeFuncionario O nome do funcionário.
     * @param emailFuncionario O email do funcionário.
     * @param telefoneFuncionario O telefone do funcionário.
     * @param senhaFuncionario A senha de login do funcionário.
     * @param crmv O número de registro no Conselho Regional de Medicina Veterinária.
     */
    public Veterinario(Long idFuncionario, String nomeFuncionario, String emailFuncionario, String telefoneFuncionario, String senhaFuncionario, String crmv) {
        // 3. Chama o construtor da classe mãe (Funcionario) para inicializar os atributos herdados.
        super(idFuncionario, Cargo.VETERINARIO, "Atendimento clínico de animais", emailFuncionario, nomeFuncionario, telefoneFuncionario, senhaFuncionario);

        // 4. Inicializa o atributo específico desta classe.
        this.crmv = crmv;
    }

    // 5. Getter e Setter para o atributo específico (os outros são herdados).

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }
}