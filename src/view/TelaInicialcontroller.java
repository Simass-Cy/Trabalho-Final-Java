package view;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Entities.Agendamento;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaInicialcontroller {

    @FXML
    private Menu menuagendamento;
    @FXML
    private Menu menucadastro;
    @FXML
    private Menu menuanimaiscadastrados;
    @FXML
    private Menu menurelatorios;


    @FXML
    private MenuItem menuitemagendarconsulta;
    @FXML
    private MenuItem menuitemagendarexame;
    @FXML
    private MenuItem menuitemcadastraranimais;
    @FXML
    private MenuItem menuitemrelatoriosdeconsultas;

    @FXML
    private AnchorPane anchorpane;

    @FXML

    private void abrirCadastroClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaAgendamento.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage(); // nova janela
            stage.setTitle("Agendamento de consultas");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @FXML
    private void abrirregistroanimais() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistroAnimais.fxml"));
            Parent root = loader.load();

            Stage stage1 = new Stage(); // nova janela

            stage1.setScene(new Scene(root));
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
