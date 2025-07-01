package view;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaInicial extends Application{
    @Override
    public void start(Stage primarystage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TelaInicial.class.getResource("TelaInicial.fxml"));
        BorderPane noRaiz = new BorderPane();
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("css/Telainicial.css").toExternalForm());


        primarystage.setTitle("Clinica Veterinaria ");
        primarystage.setScene(scene);
        primarystage.show();

    }


}
