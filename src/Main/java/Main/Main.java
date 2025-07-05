package Main;

import Application.TipoDeProduto;
import Entities.*;
import Service.*;
import Repositories.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTE COMPLETO DA CAMADA DE SERVIÇO ---");
        System.out.println("--- INICIANDO TESTE COMPLETO E AUTOMATIZADO ---");

        // 1. Instanciando todos os serviços
        ClienteService clienteService = new ClienteService();
        FuncionarioService funcionarioService = new FuncionarioService();
        AnimalService animalService = new AnimalService();
        ProdutoService produtoService = new ProdutoService();
        AgendamentoService agendamentoService = new AgendamentoService();
        ConsultaService consultaService = new ConsultaService();
        RelatorioService relatorioService = new RelatorioService();
        IReceitaRepository receitaRepo = new ReceitaRepositoryDB(); // Necessário para a limpeza

        // Variáveis para guardar os objetos criados e facilitar a limpeza
        Cliente cliente = null;
        Veterinario veterinario = null;
        Produto produto = null;
        Consulta consulta = null;
        Agendamento agendamento = null;
        Receita receita = null;

        try {
            // --- ETAPA DE CRIAÇÃO ---
            System.out.println("\n>> ETAPA 1: Cadastrando dados de base...");
            cliente = clienteService.cadastrarNovoCliente("Laura Mendes", "laura.m@teste.com", "laura123", "1111-2222");
            veterinario = (Veterinario) funcionarioService.contratarNovoFuncionario(
                    new Veterinario(null, "Dr. Ricardo", "ricardo.vet@teste.com", "3333-4444", "ricardo123", "CRMV-RJ-123")
            );
            produto = produtoService.cadastrarNovoProduto("Brinquedo de Corda", "Brinquedo para cães de porte médio.", 35.50f, TipoDeProduto.BRINQUEDO);
            System.out.println("-> Cliente, Veterinário e Produto cadastrados com sucesso.");

            System.out.println("\n>> ETAPA 2: Adicionando animal e agendando consulta...");
            Animais toto = animalService.cadastrarNovoAnimal(cliente.getId(), "Totó", LocalDate.now().minusYears(3));

            agendamento = agendamentoService.agendarNovaConsulta(cliente.getId(), toto.getIdAnimal(), veterinario.getIdFuncionario(), LocalDateTime.now().plusDays(1));


            // --- ETAPA DE TESTE DAS OPERAÇÕES ---
            System.out.println("\n>> ETAPA 3: Testando a realização da consulta...");
            String descConsulta = "Check-up de rotina.";
            String descReceita = "Nenhum medicamento necessário, apenas continuar com a alimentação atual.";
            consulta = consultaService.realizarConsulta(agendamento.getId(), descConsulta, descReceita);
            receita = consulta.getReceita(); // Pega a receita que foi criada dentro do serviço
            System.out.println("-> Consulta realizada e registrada com sucesso!");

            // --- ETAPA DE VERIFICAÇÃO ---
            System.out.println("\n>> ETAPA 4: Verificando e gerando relatório...");
            RelatorioConsulta relatorio = relatorioService.gerarESalvarRelatorioDeConsultas(veterinario.getIdFuncionario(), LocalDate.now(), LocalDate.now().plusDays(2));
            System.out.println("-> Relatório ID " + relatorio.getId() + " gerado e salvo.");
            System.out.println("   Contém " + relatorio.getConsultas().size() + " consulta(s).");


        } catch (Exception e) {
            System.err.println("\n!!! OCORREU UM ERRO DURANTE O TESTE !!!");
            e.printStackTrace();
        } finally {
            // --- ETAPA DE LIMPEZA (TEARDOWN) ---
            // Deleta todos os registros criados neste teste para que ele possa ser rodado novamente.
            System.out.println("\n--- INICIANDO LIMPEZA DOS DADOS DE TESTE ---");
            try {
                if (consulta != null) consulta.getVeterinario(); // Supondo que você precise de um método para deletar
                if (receita != null) receitaRepo.deletarReceita(receita.getId());
                if (agendamento != null) agendamentoService.deletarAgendamento(agendamento.getId());
                if (cliente != null) clienteService.deletarCliente(cliente.getId());
                if (veterinario != null) funcionarioService.deletarFuncionario(veterinario.getIdFuncionario());
                if (produto != null) produtoService.deletarProduto(produto.getIdProduto());
                System.out.println("-> Limpeza do banco de dados concluída.");
            } catch (Exception e) {
                System.err.println("ERRO DURANTE A LIMPEZA: " + e.getMessage());
            }
        }
    }
}