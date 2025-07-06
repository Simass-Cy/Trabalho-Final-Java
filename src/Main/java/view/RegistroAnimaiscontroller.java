package view;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
