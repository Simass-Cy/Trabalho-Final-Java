package view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Agendamentocontroller {
    @FXML private ComboBox<String> combopet;


    @FXML private TextField nomeanimal;
    @FXML private TextField nometutor;
    @FXML private TextField rgtutor;
    @FXML private TextField telefonetutor;
    @FXML private DatePicker dataconsulta;
    @FXML private TextField horarioconsulta;

    @FXML
    private void initialize(){
        combopet.getItems().addAll("Cachorro","Gato","Outro");
    }

    @FXML
    private void limpar(){
        nomeanimal.setText(null);
        nometutor.setText(null);
        rgtutor.setText(null);
        telefonetutor.setText(null);
        dataconsulta.setValue(null);
        horarioconsulta.setText(null);
    }





}
