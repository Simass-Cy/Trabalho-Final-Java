package Main;

import Entities.*;
import Service.*;
import Repositories.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTE COMPLETO DA CAMADA DE SERVIÇO ---");

        // 1. Instanciando todos os serviços que vamos usar
        ClienteService clienteService = new ClienteService();
        FuncionarioService funcionarioService = new FuncionarioService();
        AnimalService animalService = new AnimalService();
        ProdutoService produtoService = new ProdutoService();
        AgendamentoService agendamentoService = new AgendamentoService();
        ConsultaService consultaService = new ConsultaService();
        RelatorioService relatorioService = new RelatorioService();

        try {
            // --- CENA 1: CADASTRO DE CLIENTES E TESTE DE VALIDAÇÃO ---
            System.out.println("\n>> CENA 1: Gerenciando Clientes...");

            // Cadastrando um cliente com sucesso
            Cliente novoCliente = clienteService.cadastrarNovoCliente("Marcos Andrade", "marcos.a@teste.com", "senha123", "5555-1234");
            System.out.println("-> Cliente '" + novoCliente.getNome() + "' cadastrado com sucesso!");

            // Tentando cadastrar o mesmo cliente de novo (deve falhar)
            try {
                System.out.println("\n-> Tentando cadastrar cliente com email duplicado (esperado falhar)...");
                clienteService.cadastrarNovoCliente("Outro Marcos", "marcos.a@teste.com", "outrasenha", "5555-4321");
            } catch (Exception e) {
                System.out.println("SUCESSO NO TESTE DE ERRO: " + e.getMessage());
            }

            // --- CENA 2: CADASTRO DE VETERINÁRIO E ANIMAL ---
            System.out.println("\n>> CENA 2: Contratando veterinário e cadastrando pet...");

            Veterinario draSofia = new Veterinario(null, "Dra. Sofia", "sofia.vet@teste.com", "6666-7777", "vetSof!a", "CRMV-RJ 54321");
            funcionarioService.contratarNovoFuncionario(draSofia);
            System.out.println("-> Veterinária '" + draSofia.getNomeFuncionario() + "' contratada com sucesso!");

            Animais luna = animalService.cadastrarNovoAnimal(novoCliente.getId(), "Luna", LocalDate.now().minusYears(1));
            System.out.println("-> Animal '" + luna.getNomeAnimal() + "' cadastrado para o cliente '" + novoCliente.getNome() + "'.");

            // --- CENA 3: CADASTRO DE PRODUTO ---
            System.out.println("\n>> CENA 3: Cadastrando novo produto...");
            Produto racaoPremium = produtoService.cadastrarNovoProduto("Ração Premium Gatos", "Ração para gatos castrados.", 89.90f, Application.TipoDeProduto.ALIMENTO);
            System.out.println("-> Produto '" + racaoPremium.getNomeProduto() + "' cadastrado.");


            // --- CENA 4: AGENDAMENTO E REALIZAÇÃO DA CONSULTA ---
            System.out.println("\n>> CENA 4: Fluxo completo de consulta...");

            // Agendamento
            LocalDateTime dataAgendamento = LocalDateTime.now().plusDays(3);
            Agendamento agendamento = agendamentoService.agendarNovaConsulta(novoCliente.getId(), luna.getIdAnimal(), draSofia.getIdFuncionario(), dataAgendamento);
            System.out.println("-> Agendamento para '" + luna.getNomeAnimal() + "' realizado com sucesso.");

            // Consulta
            String descricaoConsulta = "Check-up de rotina, animal perfeitamente saudável.";
            String descricaoReceita = "Continuar com a Ração Premium Gatos. Nenhum medicamento necessário.";
            Consulta consulta = consultaService.realizarConsulta(agendamento.getId(), descricaoConsulta, descricaoReceita);
            System.out.println("-> Consulta realizada e registrada com sucesso!");


            // --- CENA 5: GERAÇÃO DE RELATÓRIO ---
            System.out.println("\n>> CENA 5: Gerando relatório do dia...");

            RelatorioConsulta relatorio = relatorioService.gerarESalvarRelatorioDeConsultas(draSofia.getIdFuncionario(), LocalDate.now(), LocalDate.now().plusDays(5));
            if (relatorio != null) {
                System.out.println("-> Relatório (ID: " + relatorio.getId() + ") gerado e salvo pelo autor: " + relatorio.getAutor().getNomeFuncionario());
                System.out.println("   Contém " + relatorio.getConsultas().size() + " consulta(s).");
            }

        } catch (Exception e) {
            System.err.println("\n!!! OCORREU UM ERRO INESPERADO NO FLUXO PRINCIPAL !!!");
            e.printStackTrace();
        }
    }
}