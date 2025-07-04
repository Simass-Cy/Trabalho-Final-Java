package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AnimaisCadastrados {
    public void start(Stage TelaAnimaisCadastrados) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AnimaisCadastrados.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        TelaAnimaisCadastrados.setTitle("Registro Agendamentos");
        TelaAnimaisCadastrados.setScene(scene);
        TelaAnimaisCadastrados.show();
    }
}