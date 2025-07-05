package Main;

import Entities.*;
import Exceptions.ServiceException;
import Service.*;
import Application.TipoDeProduto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTE COMPLETO DA CAMADA DE SERVIÇO ---");
        System.out.println("--- INICIANDO TESTE COMPLETO E AUTOMATIZADO ---");

        ClienteService clienteService = new ClienteService();
        FuncionarioService funcionarioService = new FuncionarioService();
        AnimalService animalService = new AnimalService();
        ProdutoService produtoService = new ProdutoService();
        AgendamentoService agendamentoService = new AgendamentoService();
        ConsultaService consultaService = new ConsultaService();

        try {
            // --- ETAPA 1: ADICIONANDO NOVOS REGISTROS ---
            System.out.println("\n>> ETAPA 1: Cadastrando novos Cliente, Veterinário, Animal e Produto...");

            // Adiciona um novo Cliente
            Cliente carlaLopes = clienteService.cadastrarNovoCliente("Carla Lopes", "carla.l@teste.com", "senha789", "4444-5555");
            System.out.println("-> SUCESSO: Cliente '" + carlaLopes.getNome() + "' (ID: " + carlaLopes.getId() + ") foi criado.");

            // Adiciona um novo Veterinário
            Veterinario drGabriel = new Veterinario(null, "Dr. Gabriel", "gabriel.vet@teste.com", "6666-7777", "vetGabriel", "CRMV-SC-54321");
            funcionarioService.contratarNovoFuncionario(drGabriel);
            System.out.println("-> SUCESSO: Veterinário '" + drGabriel.getNomeFuncionario() + "' (ID: " + drGabriel.getIdFuncionario() + ") foi criado.");

            // Adiciona um novo Animal para a Carla
            Animais felix = animalService.cadastrarNovoAnimal(carlaLopes.getId(), "Félix", LocalDate.now().minusYears(5));
            System.out.println("-> SUCESSO: Animal '" + felix.getNomeAnimal() + "' (ID: " + felix.getIdAnimal() + ") foi criado para a cliente '" + carlaLopes.getNome() + "'.");

            // Adiciona um novo Produto
            Produto vitamina = produtoService.cadastrarNovoProduto("Vitamina para Pelos", "Suplemento vitamínico para fortalecimento de pelos.", 75.20f, TipoDeProduto.MEDICAMENTO);
            System.out.println("-> SUCESSO: Produto '" + vitamina.getNomeProduto() + "' (ID: " + vitamina.getIdProduto() + ") foi criado.");


            // --- ETAPA 2: REALIZANDO O FLUXO DE CONSULTA ---
            System.out.println("\n>> ETAPA 2: Realizando o fluxo completo de consulta...");

            // Agenda a consulta
            Agendamento agendamentoFelix = agendamentoService.agendarNovaConsulta(carlaLopes.getId(), felix.getIdAnimal(), drGabriel.getIdFuncionario(), LocalDateTime.now().plusHours(2));
            System.out.println("-> SUCESSO: Agendamento para '" + felix.getNomeAnimal() + "' foi criado.");

            // Realiza a consulta e cria uma receita
            String descConsulta = "Animal apresentando queda de pelos excessiva.";
            String descReceita = "Administrar suplemento vitamínico 'Vitamina para Pelos' uma vez ao dia.";
            Consulta consultaFelix = consultaService.realizarConsulta(agendamentoFelix.getId(), descConsulta, descReceita);
            System.out.println("-> SUCESSO: Consulta realizada e registrada com ID: " + consultaFelix.getId() + ". Receita associada com ID: " + consultaFelix.getReceita().getId());


            // --- ETAPA 3: VERIFICAÇÃO FINAL ---
            System.out.println("\n>> ETAPA 3: Verificando alguns dos dados recém-criados...");

            List<Animais> animaisDaCarla = animalService.listarAnimaisPorDono(carlaLopes.getId());
            System.out.println("-> Verificação: A cliente '" + carlaLopes.getNome() + "' possui " + animaisDaCarla.size() + " animal(is) cadastrado(s).");

            Produto produtoBuscado = produtoService.encontrarProdutoPorId(vitamina.getIdProduto());
            System.out.println("-> Verificação: O produto buscado por ID é '" + produtoBuscado.getNomeProduto() + "'.");


        } catch (ServiceException e) {
            System.err.println("\n!!! OCORREU UM ERRO INESPERADO DURANTE O TESTE !!!");
            e.printStackTrace();
        }

        System.out.println("\n--- TESTE FINALIZADO. OS DADOS PERMANECEM NO BANCO DE DADOS. ---");
    }
}