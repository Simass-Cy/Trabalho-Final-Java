package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

// Supondo que você tenha uma classe chamada Consulta com os campos apropriados
import Entities.Consulta;

public class ConsultasAgendadasController {

    @FXML private TableView<Consulta> tabelaconsultasagendadas;

    @FXML private TableColumn<Consulta, String> colunanomeanimal;
    @FXML private TableColumn<Consulta, String> colunatipoanimal;
    @FXML private TableColumn<Consulta, String> colunanometutor;
    @FXML private TableColumn<Consulta, String> colunahorarioconsulta;
    @FXML private TableColumn<Consulta, String> colunadataconsulta;
    @FXML private TableColumn<Consulta, String> colunacpfresponsavel;
    @FXML private TableColumn<Consulta, String> colunadescriçaodaconsulta;

    @FXML
    public void initialize() {
        colunanomeanimal.setCellValueFactory(new PropertyValueFactory<>("nomeAnimal"));
        colunatipoanimal.setCellValueFactory(new PropertyValueFactory<>("tipoAnimal"));
        colunanometutor.setCellValueFactory(new PropertyValueFactory<>("nomeTutor"));
        colunahorarioconsulta.setCellValueFactory(new PropertyValueFactory<>("horaConsulta"));
        colunadataconsulta.setCellValueFactory(new PropertyValueFactory<>("dataConsulta"));
        colunacpfresponsavel.setCellValueFactory(new PropertyValueFactory<>("cpfTutor"));
        colunadescriçaodaconsulta.setCellValueFactory(new PropertyValueFactory<>("descricao"));


    }


    }

