package view;

// Imports que faltavam para abrir novas janelas
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Imports que já tínhamos
import Entities.Animais;
import Exceptions.ServiceException;
import Service.AnimalService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Animaiscadastradoscontroller {

    private AnimalService animalService = new AnimalService();

    @FXML private TableView<Animais> tabelaAnimais;
    @FXML private TableColumn<Animais, Long> colunaId;
    @FXML private TableColumn<Animais, String> colunaNomeAnimal;
    @FXML private TableColumn<Animais, LocalDate> colunaDataNascimento;
    @FXML private TableColumn<Animais, String> colunaNomeDono;

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("idAnimal"));
        colunaNomeAnimal.setCellValueFactory(new PropertyValueFactory<>("nomeAnimal"));
        colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimentoAnimal"));
        colunaNomeDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDono().getNome()));
        carregarDadosDaTabela();
    }

    private void carregarDadosDaTabela() {
        try {
            List<Animais> listaDeAnimais = animalService.listarTodosOsAnimais();
            ObservableList<Animais> dadosObservaveis = FXCollections.observableArrayList(listaDeAnimais);
            tabelaAnimais.setItems(dadosObservaveis);
        } catch (ServiceException e) {
            mostrarAlerta("Erro de Carregamento", "Não foi possível carregar os dados dos animais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExcluirAnimal() {
        Animais animalSelecionado = tabelaAnimais.getSelectionModel().getSelectedItem();
        if (animalSelecionado == null) {
            mostrarAlerta("Seleção Vazia", "Por favor, selecione um animal na tabela para excluir.");
            return;
        }
        Alert alertaDeConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        alertaDeConfirmacao.setTitle("Confirmação de Exclusão");
        alertaDeConfirmacao.setHeaderText("Tem certeza que deseja excluir o animal '" + animalSelecionado.getNomeAnimal() + "'?");
        alertaDeConfirmacao.setContentText("Esta ação não pode ser desfeita.");
        Optional<ButtonType> resultado = alertaDeConfirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                animalService.deletarAnimal(animalSelecionado.getIdAnimal());
                mostrarAlerta("Sucesso", "Animal excluído com sucesso!");
                carregarDadosDaTabela();
            } catch (ServiceException e) {
                mostrarAlerta("Erro ao Excluir", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEditarAnimal() {
        Animais animalSelecionado = tabelaAnimais.getSelectionModel().getSelectedItem();
        if (animalSelecionado == null) {
            mostrarAlerta("Seleção Vazia", "Por favor, selecione um animal na tabela para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistroAnimais.fxml"));
            Parent root = loader.load();
            RegistroAnimaiscontroller controllerDoRegistro = loader.getController();
            controllerDoRegistro.prepararParaEdicao(animalSelecionado);
            Stage stage = new Stage();
            stage.setTitle("Editando Animal");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            carregarDadosDaTabela();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao Abrir Tela", "Não foi possível abrir a tela de edição.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}