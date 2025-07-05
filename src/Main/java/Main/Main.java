package Main;

import Entities.*;
import Repositories.*;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTE DE LEITURA E GERAÇÃO DE RELATÓRIO ---");

        // 1. Instanciando os repositórios que vamos usar para buscar dados
        IClienteRepository clienteRepo = new ClienteRepositoryDB();
        IFuncionarioRepository funcionarioRepo = new FuncionarioRepositoryDB();
        IConsultaRepository consultaRepo = new ConsultaRepositoryDB();

        try {
            // --- CENA 1: BUSCANDO OS DADOS EXISTENTES ---
            System.out.println("\n>> Buscando dados do banco...");

            // Busca por um cliente específico que sabemos que existe
            List<Cliente> clientes = clienteRepo.buscarPorNome("Cliente Final");
            if (clientes.isEmpty()) {
                System.out.println("ERRO: O cliente 'Cliente Final Teste' não foi encontrado. Execute o teste de criação de dados primeiro.");
                return;
            }
            Cliente cliente = clientes.get(0);
            System.out.println("-> Cliente encontrado: " + cliente.getNome());

            // Busca por um veterinário específico
            List<Funcionario> vets = funcionarioRepo.buscarPorNome("Veterinário Final");
            if (vets.isEmpty()) {
                System.out.println("ERRO: O 'Veterinário Final Teste' não foi encontrado.");
                return;
            }
            Funcionario autorDoRelatorio = vets.get(0);
            System.out.println("-> Funcionário encontrado para ser o autor do relatório: " + autorDoRelatorio.getNomeFuncionario());


            // --- CENA 2: GERANDO O RELATÓRIO DE CONSULTAS ---
            System.out.println("\n>> Gerando relatório de todas as consultas do último mês...");

            LocalDate hoje = LocalDate.now();
            LocalDate umMesAtras = hoje.minusMonths(1);

            // Busca todas as consultas realizadas no período
            List<Consulta> consultasDoPeriodo = consultaRepo.buscarPorPeriodoConsulta(umMesAtras, hoje);

            if (consultasDoPeriodo.isEmpty()) {
                System.out.println("-> Nenhuma consulta encontrada no período para gerar o relatório.");
            } else {
                // Cria o objeto de relatório com os dados buscados
                RelatorioConsulta relatorio = new RelatorioConsulta(0L, autorDoRelatorio, umMesAtras, hoje, consultasDoPeriodo);
                System.out.println("-> Relatório de Consultas criado com sucesso!");

                // --- Exibindo o Relatório Gerado ---
                System.out.println("\n---------- RELATÓRIO DE CONSULTAS ----------");
                System.out.println("Relatório Gerado em: " + relatorio.getDataGeracao());
                System.out.println("Autor: " + relatorio.getAutor().getNomeFuncionario());
                System.out.println("Período Analisado: de " + relatorio.getDataInicio() + " a " + relatorio.getDataFim());
                System.out.println("--------------------------------------------");
                System.out.println("Consultas no período (" + relatorio.getConsultas().size() + "):");

                for(Consulta c : relatorio.getConsultas()){
                    System.out.println("\n  - Consulta ID: " + c.getId());
                    System.out.println("    Data: " + c.getDataHora().toLocalDate());
                    System.out.println("    Animal: " + c.getAnimal().getNomeAnimal() + " (Dono: " + c.getAnimal().getDono().getNome() + ")");
                    System.out.println("    Veterinário: " + c.getVeterinario().getNomeFuncionario());
                    System.out.println("    Descrição: " + c.getDescricao());
                    // Verifica se a consulta tem uma receita associada
                    if(c.getReceita() != null){
                        System.out.println("    Receita: Sim (ID: " + c.getReceita().getId() + ")");
                    } else {
                        System.out.println("    Receita: Não");
                    }
                }
                System.out.println("--------------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("!!! OCORREU UM ERRO DURANTE O TESTE !!!");
            e.printStackTrace();
        }

        System.out.println("\n--- TESTE FINALIZADO (nenhum dado foi alterado no banco) ---");
    }
}