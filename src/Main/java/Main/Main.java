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
        System.out.println("--- INICIANDO NOVO TESTE DE OPERAÇÕES ---");

        ClienteService clienteService = new ClienteService();
        FuncionarioService funcionarioService = new FuncionarioService();
        AnimalService animalService = new AnimalService();
        ProdutoService produtoService = new ProdutoService();
        AgendamentoService agendamentoService = new AgendamentoService();
        ConsultaService consultaService = new ConsultaService();

        try {
            System.out.println("\n>> ETAPA 1: Adicionando clientes...");

            Cliente brunoMartins = clienteService.cadastrarNovoCliente("Bruno Martins", "bruno@teste.com", "senhaabc", "4444-4444");
            Cliente carlaMendes = clienteService.cadastrarNovoCliente("Carla Mendes", "carla@teste.com", "senhaxyz", "5555-5555");
            Cliente fernandoAlves = clienteService.cadastrarNovoCliente("Fernando Alves", "fernando@teste.com", "senha987", "6666-6666");

            System.out.println("-> Clientes cadastrados com sucesso.");

            System.out.println("\n>> ETAPA 2: Excluindo cliente Fernando Alves...");
            clienteService.deletarCliente(fernandoAlves.getId());
            System.out.println("-> Cliente 'Fernando Alves' excluído com sucesso.");

            System.out.println("\n>> ETAPA 3: Buscando cliente Bruno Martins e listando seus animais...");
            Cliente clienteBuscado = clienteService.encontrarClientePorId(brunoMartins.getId());
            List<Animais> animaisBruno = animalService.listarAnimaisPorDono(clienteBuscado.getId());
            System.out.println("-> Cliente '" + clienteBuscado.getNome() + "' possui " + animaisBruno.size() + " animal(is) cadastrado(s).");

            System.out.println("\n>> ETAPA 4: Cadastrando animais...");

            Animais thor = animalService.cadastrarNovoAnimal(brunoMartins.getId(), "Thor", LocalDate.now().minusYears(4));
            Animais nina = animalService.cadastrarNovoAnimal(carlaMendes.getId(), "Nina", LocalDate.now().minusYears(1));
            try {
                Animais semDono = animalService.cadastrarNovoAnimal(999L, "Sombra", LocalDate.now().minusYears(2));
            } catch (ServiceException e) {
                System.err.println("-> ERRO TRATADO: Tentativa de cadastrar animal sem dono. Mensagem: " + e.getMessage());
            }

            System.out.println("-> Animais 'Thor' e 'Nina' cadastrados com sucesso.");

            System.out.println("\n>> ETAPA 5: Buscando animal 'Nina'...");
            Animais ninaBuscada = animalService.encontrarAnimalPorId(nina.getIdAnimal());
            System.out.println("-> Animal encontrado: " + ninaBuscada.getNomeAnimal());


            System.out.println("\n>> ETAPA 7: Adicionando veterinário e realizando agendamento...");

            Veterinario draRenata = new Veterinario(null, "Dra. Renata", "renata@vet.com", "9999-0000", "vetRenata", "CRMV-RJ-22222");
            funcionarioService.contratarNovoFuncionario(draRenata);
            System.out.println("-> Veterinária '" + draRenata.getNomeFuncionario() + "' contratada com sucesso.");

            Agendamento agendamentoNina = agendamentoService.agendarNovaConsulta(carlaMendes.getId(), nina.getIdAnimal(), draRenata.getIdFuncionario(), LocalDateTime.now().plusDays(1));
            System.out.println("-> Agendamento criado para 'Nina' com a Dra. Renata.");

            System.out.println("\n>> ETAPA 8: Realizando consulta...");
            String descConsulta = "Animal com falta de apetite e sono excessivo.";
            String descReceita = "Suplemento vitamínico diário e nova ração recomendada.";
            Consulta consultaNina = consultaService.realizarConsulta(agendamentoNina.getId(), descConsulta, descReceita);
            System.out.println("-> Consulta registrada com ID: " + consultaNina.getId() + ". Receita ID: " + consultaNina.getReceita().getId());

            System.out.println("\n>> ETAPA 9: Cadastrando produtos...");
            produtoService.cadastrarNovoProduto("Ração Premium 15kg", "Ração completa para cães adultos de porte médio e grande", 199.90f, TipoDeProduto.ALIMENTO);
            produtoService.cadastrarNovoProduto("Shampoo Antipulgas", "Shampoo veterinário antipulgas para cães e gatos", 39.99f, TipoDeProduto.HIGIENE);
            produtoService.cadastrarNovoProduto("Brinquedo Mordedor", "Mordedor resistente ideal para cães filhotes e adultos", 24.50f, TipoDeProduto.BRINQUEDO);
            System.out.println("-> Produtos cadastrados com sucesso.");

            System.out.println("\n>> ETAPA 10: Geração de relatório final...");
            System.out.println("-> Cliente: " + carlaMendes.getNome());
            System.out.println("-> Animal: " + nina.getNomeAnimal());
            System.out.println("-> Veterinária: " + draRenata.getNomeFuncionario());
            System.out.println("-> Consulta: " + consultaNina.getDescricao());
            System.out.println("-> Receita: " + consultaNina.getReceita().getDescricao());

        } catch (ServiceException e) {
            System.err.println("\n!!! ERRO INESPERADO DURANTE O PROCESSO !!!");
            e.printStackTrace();
        }

        System.out.println("\n--- TESTE CONCLUÍDO COM SUCESSO ---");
    }
}