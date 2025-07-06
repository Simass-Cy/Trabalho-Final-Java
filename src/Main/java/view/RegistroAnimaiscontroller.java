package view;

import Entities.Animais;
import Entities.Cliente;
import Exceptions.ServiceException;
import Service.AnimalService;
import Service.ClienteService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage; // Import que faltava

import java.time.LocalDate;

public class RegistroAnimaiscontroller {

    @FXML private TextField nomeregistroanimal;
    @FXML private DatePicker datanascanimal;
    @FXML private TextField nomedonoregistroanimal;
    @FXML private TextField emailDono;
    @FXML private TextField telefoneDono;

    private ClienteService clienteService = new ClienteService();
    private AnimalService animalService = new AnimalService();
    private Animais animalParaEditar; // Guarda o animal quando estamos no modo de edição

    public void prepararParaEdicao(Animais animal) {
        this.animalParaEditar = animal;
        nomeregistroanimal.setText(animal.getNomeAnimal());
        datanascanimal.setValue(animal.getDataNascimentoAnimal());
        nomedonoregistroanimal.setText(animal.getDono().getNome());
        emailDono.setText(animal.getDono().getEmail());
        telefoneDono.setText(animal.getDono().getTelefone());
        nomedonoregistroanimal.setDisable(true);
        emailDono.setDisable(true);
        telefoneDono.setDisable(true);
    }

    @FXML
    private void handleSalvar() {
        String nomeAnimal = nomeregistroanimal.getText();
        LocalDate dataNascimento = datanascanimal.getValue();

        try {
            if (animalParaEditar == null) {
                // --- MODO CRIAÇÃO ---
                String nomeDono = nomedonoregistroanimal.getText();
                String email = emailDono.getText();
                String telefone = telefoneDono.getText();

                if (nomeAnimal.isEmpty() || nomeDono.isEmpty() || email.isEmpty() || dataNascimento == null) {
                    mostrarAlerta("Erro de Validação", "Todos os campos são obrigatórios!");
                    return;
                }

                Cliente novoCliente = clienteService.cadastrarNovoCliente(nomeDono, email, "senha123", telefone);
                animalService.cadastrarNovoAnimal(novoCliente.getId(), nomeAnimal, dataNascimento);
                mostrarAlerta("Sucesso!", "Cliente e animal cadastrados com sucesso!");

            } else {
                // --- MODO EDIÇÃO ---
                animalParaEditar.setNomeAnimal(nomeAnimal);
                animalParaEditar.setDataNascimentoAnimal(dataNascimento);
                animalService.atualizarAnimal(animalParaEditar);
                mostrarAlerta("Sucesso!", "Dados do animal atualizados com sucesso!");
            }

            // Fecha a janela após salvar
            Stage palco = (Stage) nomeregistroanimal.getScene().getWindow();
            palco.close();

        } catch (ServiceException e) {
            mostrarAlerta("Erro de Negócio", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro inesperado.");
            e.printStackTrace();
        }
    }

    @FXML
    private void limpar() {
        nomeregistroanimal.clear();
        datanascanimal.setValue(null);
        nomedonoregistroanimal.clear();
        emailDono.clear();
        telefoneDono.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}