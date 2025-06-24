package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Registroanimais extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TelaAgendamento.class.getResource("RegistroAnimais.fxml"));
        BorderPane noRaiz = new BorderPane();
        Scene scene = new Scene(fxmlLoader.load(), 529, 507);
        stage.setTitle("REGISTRO DE ANIMAIS");
        stage.setScene(scene);
        stage.show();

    }


}
