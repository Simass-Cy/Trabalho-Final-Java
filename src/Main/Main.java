package Main;

// Importando todas as classes que vamos usar
import Application.StatusAgendamento;
import Entities.*;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("--- Iniciando Simulação da Clínica Veterinária ---");

        // 1. CRIAÇÃO DOS ATORES (FUNCIONÁRIO E CLIENTE)
        System.out.println("\n>> Passo 1: Criando Atores...");
        Veterinario drJoao = new Veterinario(1L, "Dr. João", "joao.vet@email.com", "1111-2222", "senha123", "CRMV-SP 12345");
        Cliente anaSilva = new Cliente(101L, "Ana Silva", 9876, "ana.silva@email.com", "3333-4444");
        System.out.println("-> Veterinário criado: " + drJoao.getNomeFuncionario());
        System.out.println("-> Cliente criada: " + anaSilva.getNome());

        // 2. CADASTRO DO PET
        System.out.println("\n>> Passo 2: Cadastrando o Pet...");
        Animais rex = new Animais("Rex", 501L, "2022-01-15", 3, null); // Dono será setado abaixo

        // Lógica manual que futuramente estará em uma interface/serviço
        anaSilva.getAnimais().add(rex); // Pega a lista de animais da Ana e adiciona o Rex
        rex.setDono(anaSilva);         // Define a Ana como dona do Rex (relação de mão dupla)
        System.out.println("-> " + rex.getNomeAnimal() + " agora é pet de " + rex.getDono().getNome());

        // 3. AGENDAMENTO DA CONSULTA
        System.out.println("\n>> Passo 3: Agendando a Consulta...");
        // O Dr. João mesmo agendou
        Agendamento agendamentoRex = new Agendamento(1001L, LocalDateTime.now().plusDays(1).withHour(10).withMinute(30), anaSilva, rex, drJoao);

        // Lógica manual para adicionar o agendamento ao cliente
        anaSilva.getAgendamentos().add(agendamentoRex);
        System.out.println("-> Agendamento criado para " + agendamentoRex.getAnimal().getNomeAnimal() + " em " + agendamentoRex.getDataHora().toLocalDate());
        System.out.println("-> Status inicial: " + agendamentoRex.getStatus());

        // 4. REALIZAÇÃO DA CONSULTA
        System.out.println("\n>> Passo 4: Realizando a Consulta...");
        // O agendamento é "convertido" em uma consulta
        Consulta consultaRex = new Consulta(2001L, agendamentoRex.getDataHora(), "Check-up anual e vacinação.", drJoao, rex, agendamentoRex);
        agendamentoRex.setStatus(StatusAgendamento.REALIZADO); // Atualiza o status do agendamento
        System.out.println("-> Consulta realizada por: " + consultaRex.getVeterinario().getNomeFuncionario());
        System.out.println("-> Descrição: " + consultaRex.getDescricao());
        System.out.println("-> Status do agendamento alterado para: " + agendamentoRex.getStatus());

        // 5. VERIFICANDO AS CONEXÕES
        System.out.println("\n--- Verificando Conexões ---");
        System.out.println("Cliente: " + anaSilva.getNome());
        System.out.println("  Possui " + anaSilva.getAnimais().size() + " animal(is):");
        // Imprime os animais da Ana
        for (Animais pet : anaSilva.getAnimais()) {
            System.out.println("    - " + pet.getNomeAnimal());
        }
        System.out.println("  Possui " + anaSilva.getAgendamentos().size() + " agendamento(s):");
        // Imprime os agendamentos da Ana
        for (Agendamento ag : anaSilva.getAgendamentos()) {
            System.out.println("    - Agendamento para '" + ag.getAnimal().getNomeAnimal() + "' com status " + ag.getStatus());
        }

        // Verificação cruzada: a partir da consulta, chegar no nome do cliente
        String nomeCliente = consultaRex.getAnimal().getDono().getNome();
        System.out.println("\nTeste de Rastreabilidade: O nome do cliente da consulta é '" + nomeCliente + "'.");

        System.out.println("\n--- Simulação Finalizada ---");
    }
}