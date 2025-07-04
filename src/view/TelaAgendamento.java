package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaAgendamento {
    public void start(Stage Telaregistroagendamento) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TelaAgendamento.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        Telaregistroagendamento.setTitle("Registro Agendamentos");
        Telaregistroagendamento.setScene(scene);
        Telaregistroagendamento.show();
    }
}