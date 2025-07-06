package view;

import Application.Cargo;
import Entities.Agendamento;
import Entities.Animais;
import Entities.Cliente;
import Entities.Funcionario;
import Exceptions.ServiceException;
import Service.AgendamentoService;
import Service.AnimalService;
import Service.ClienteService;
import Service.FuncionarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class Agendamentocontroller {

    // Serviços para acessar o back-end
    private ClienteService clienteService = new ClienteService();
    private AnimalService animalService = new AnimalService();
    private FuncionarioService funcionarioService = new FuncionarioService();
    private AgendamentoService agendamentoService = new AgendamentoService();

    // Componentes da tela (FXML)
    @FXML private ComboBox<Cliente> comboCliente;
    @FXML private ComboBox<Animais> comboAnimal;
    @FXML private ComboBox<Funcionario> comboVeterinario;
    @FXML private DatePicker dataconsulta;
    @FXML private TextField horarioconsulta;

    @FXML
    private void initialize() {
        // Carrega os clientes e veterinários quando a tela abre
        carregarClientes();
        carregarVeterinarios();

        // Adiciona um "ouvinte" para o ComboBox de clientes
        // Quando um cliente for selecionado, este código será executado
        comboCliente.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                // Se um novo cliente foi selecionado, carrega os animais dele
                carregarAnimaisDoCliente(newValue);
                comboAnimal.setDisable(false); // Habilita o ComboBox de animais
            } else {
                // Se nenhum cliente for selecionado, limpa e desabilita o ComboBox de animais
                comboAnimal.getItems().clear();
                comboAnimal.setDisable(true);
            }
        });
    }

    private void carregarClientes() {
        try {
            comboCliente.setItems(FXCollections.observableArrayList(clienteService.listarTodosOsClientes()));
        } catch (ServiceException e) {
            mostrarAlerta("Erro", "Não foi possível carregar a lista de clientes.");
        }
    }

    private void carregarVeterinarios() {
        try {
            comboVeterinario.setItems(FXCollections.observableArrayList(funcionarioService.listarFuncionariosPorCargo(Cargo.VETERINARIO)));
        } catch (ServiceException e) {
            mostrarAlerta("Erro", "Não foi possível carregar a lista de veterinários.");
        }
    }

    private void carregarAnimaisDoCliente(Cliente cliente) {
        try {
            // Usa o ID do cliente para buscar apenas os animais dele
            comboAnimal.setItems(FXCollections.observableArrayList(animalService.listarAnimaisPorDono(cliente.getId())));
        } catch (ServiceException e) {
            mostrarAlerta("Erro", "Não foi possível carregar os animais do cliente selecionado.");
        }
    }

    @FXML
    private void handleSalvarAgendamento() {
        try {
            // Pega os objetos selecionados nas ComboBoxes
            Cliente cliente = comboCliente.getValue();
            Animais animal = comboAnimal.getValue();
            Funcionario veterinario = comboVeterinario.getValue();
            LocalDate data = dataconsulta.getValue();

            // Validações
            if (cliente == null || animal == null || veterinario == null || data == null || horarioconsulta.getText().isEmpty()) {
                mostrarAlerta("Erro de Validação", "Todos os campos devem ser preenchidos.");
                return;
            }

            // Combina a data e a hora
            LocalTime hora = LocalTime.parse(horarioconsulta.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dataHoraCompleta = LocalDateTime.of(data, hora);

            // Chama o serviço para criar o agendamento
            agendamentoService.agendarNovaConsulta(cliente.getId(), animal.getIdAnimal(), veterinario.getIdFuncionario(), dataHoraCompleta);

            mostrarAlerta("Sucesso", "Agendamento realizado com sucesso!");
            limpar();

        } catch (ServiceException e) {
            mostrarAlerta("Erro ao Agendar", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro de Formato", "O formato da hora deve ser HH:MM (ex: 14:30).");
        }
    }

    @FXML
    private void limpar() {
        comboCliente.getSelectionModel().clearSelection();
        comboAnimal.getItems().clear();
        comboVeterinario.getSelectionModel().clearSelection();
        dataconsulta.setValue(null);
        horarioconsulta.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}