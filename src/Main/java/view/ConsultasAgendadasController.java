package view;

import Entities.Agendamento;
import Exceptions.ServiceException;
import Service.AgendamentoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultasAgendadasController {

    // Instância do nosso serviço
    private AgendamentoService agendamentoService = new AgendamentoService();

    // Componentes da tela FXML
    @FXML private TableView<Agendamento> tabelaconsultasagendadas;
    @FXML private TableColumn<Agendamento, String> colunanomeanimal;
    @FXML private TableColumn<Agendamento, String> colunanometutor;
    @FXML private TableColumn<Agendamento, String> colunahorarioconsulta;
    @FXML private TableColumn<Agendamento, String> colunadataconsulta;
    // As colunas abaixo não correspondem diretamente aos dados do Agendamento,
    // então não vamos usá-las por enquanto.
    // @FXML private TableColumn<Agendamento, String> colunatipoanimal;
    // @FXML private TableColumn<Agendamento, String> colunacpfresponsavel;
    // @FXML private TableColumn<Agendamento, String> colunadescriçaodaconsulta;

    @FXML
    public void initialize() {
        // Configura as colunas para saberem como obter os dados de um objeto Agendamento

        // Para dados aninhados (ex: nome do animal), usamos uma lambda
        colunanomeanimal.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAnimal().getNomeAnimal())
        );
        colunanometutor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCliente().getNome())
        );

        // Para formatar data e hora, também usamos lambdas
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colunadataconsulta.setCellValueFactory(cellData -> {
            LocalDateTime dataHora = cellData.getValue().getDataHora();
            return new SimpleStringProperty(dataHora.format(formatoData));
        });

        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        colunahorarioconsulta.setCellValueFactory(cellData -> {
            LocalDateTime dataHora = cellData.getValue().getDataHora();
            return new SimpleStringProperty(dataHora.format(formatoHora));
        });

        // Carrega os dados do banco na tabela
        carregarDadosDaTabela();
    }

    private void carregarDadosDaTabela() {
        try {
            List<Agendamento> listaDeAgendamentos = agendamentoService.listarTodosOsAgendamentos();
            ObservableList<Agendamento> dadosObservaveis = FXCollections.observableArrayList(listaDeAgendamentos);
            tabelaconsultasagendadas.setItems(dadosObservaveis);
        } catch (ServiceException e) {
            mostrarAlerta("Erro de Carregamento", "Não foi possível carregar a lista de agendamentos.");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}