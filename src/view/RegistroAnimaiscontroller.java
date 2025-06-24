package view;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Entities.Agendamento;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;

import static javafx.event.Event.*;

public class RegistroAnimaiscontroller {


    @FXML private TextField nomeregistroanimal;
    @FXML private TextField nomedonoregistroanimal;
    @FXML private TextField datanascanimal;
    @FXML private TextField idadeanimal;


@FXML
    private void limpar(){
        nomeregistroanimal.setText(null);
        nomedonoregistroanimal.setText(null);
        datanascanimal.setText(null);
        idadeanimal.setText(null);

    }


}
