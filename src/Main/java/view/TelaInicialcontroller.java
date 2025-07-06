package view;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaInicialcontroller {

    @FXML
    private Button menuagendamento;
    @FXML
    private Button menucadastro;
    @FXML
    private Button menuanimaiscadastrados;
    @FXML
    private Button menurelatorios;




    @FXML
    private AnchorPane anchorpane;

    @FXML

    private void abrirRegistroConsultas() {
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
    private void abrircadastroanimais() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistroAnimais.fxml"));
            Parent root = loader.load();

            Stage stage1 = new Stage(); // nova janela
            stage1.setTitle("Registro Animais");

            stage1.setScene(new Scene(root));
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML

    private void abrirAnimaiscadastrados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimaisCadastrados.fxml"));
            Parent root = loader.load();

            Stage stage2 = new Stage(); // nova janela
            stage2.setTitle("Animais Cadastrados ");

            stage2.setScene(new Scene(root));
            stage2.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void abrirconsultasagendadas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Consultasagendadas.fxml"));
            Parent root = loader.load();

            Stage stage3 = new Stage(); // nova janela
            stage3.setTitle("LISTA CONSULTA AGENDADAS ");

            stage3.setScene(new Scene(root));
            stage3.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void abrirfuncionarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Funcionarios.fxml"));
            Parent root = loader.load();

            Stage stage4 = new Stage(); // nova janela
            stage4.setTitle("FUNCIONARIOS ");

            stage4.setScene(new Scene(root));
            stage4.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void abrirrelatorio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Relatorio.fxml"));
            Parent root = loader.load();

            Stage stage5 = new Stage(); // nova janela
            stage5.setTitle("RELATORIOS ");

            stage5.setScene(new Scene(root));
            stage5.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
