package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Registroanimais {
    public void start(Stage Telaregistroanimais) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroAnimais.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        Telaregistroanimais.setTitle("Registro Agendamentos");
        Telaregistroanimais.setScene(scene);
        Telaregistroanimais.show();
    }
}