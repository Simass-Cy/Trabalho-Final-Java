package view;

// Imports necessários para o JavaFX e para as suas classes
import Application.Cargo;
import Entities.Funcionario;
import Entities.Veterinario; // Import que faltava
import Exceptions.ServiceException;
import Service.FuncionarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // Import que faltava
import javafx.scene.control.ButtonType;
import java.util.Optional;

import java.util.List;

public class Funcionariocontroller {

    private FuncionarioService funcionarioService = new FuncionarioService();
    private Funcionario funcionarioParaEditar;

    // --- Componentes da Tabela ---
    @FXML private TableView<Funcionario> Tabelafuncionarios;
    @FXML private TableColumn<Funcionario, Long> colunaidfuncionario;
    @FXML private TableColumn<Funcionario, String> colunanomefuncionario;
    @FXML private TableColumn<Funcionario, String> colunaemailfuncionario;
    @FXML private TableColumn<Funcionario, Cargo> colunacargofuncionario;

    // --- Componentes do Formulário ---
    @FXML private TextField txtnomefuncionario;
    @FXML private TextField txtemailfuncionario;
    @FXML private PasswordField txtsenhafuncionario; // Tipo corrigido para PasswordField
    @FXML private TextField txtcrmvfuncionario;
    @FXML private ComboBox<Cargo> comboboxcargofuncionario;

    // --- Botões ---
    @FXML private Button botaosalvar;
    @FXML private Button botaolimpar;
    @FXML private Button botaoexcluir;

    @FXML
    public void initialize() {
        // Configura as colunas da tabela
        colunaidfuncionario.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
        colunanomefuncionario.setCellValueFactory(new PropertyValueFactory<>("nomeFuncionario"));
        colunaemailfuncionario.setCellValueFactory(new PropertyValueFactory<>("emailFuncionario"));
        colunacargofuncionario.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        // Popula a ComboBox de cargos
        comboboxcargofuncionario.setItems(FXCollections.observableArrayList(Cargo.values()));

        // Carrega os dados na tabela
        carregarDadosDaTabela();
    }

    private void carregarDadosDaTabela() {
        try {
            List<Funcionario> listaDeFuncionarios = funcionarioService.listarTodosOsFuncionarios();
            ObservableList<Funcionario> dadosObservaveis = FXCollections.observableArrayList(listaDeFuncionarios);
            Tabelafuncionarios.setItems(dadosObservaveis);
        } catch (ServiceException e) {
            mostrarAlerta("Erro de Carregamento", "Não foi possível carregar os dados dos funcionários.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalvarFuncionario() {
        try {
            String nome = txtnomefuncionario.getText();
            String email = txtemailfuncionario.getText();
            String senha = txtsenhafuncionario.getText();
            Cargo cargoSelecionado = comboboxcargofuncionario.getValue();
            // ... validações ...

            if (funcionarioParaEditar == null) {
                // --- MODO CRIAÇÃO ---
                // Se funcionarioParaEditar é nulo, estamos criando um novo.
                Funcionario novoFuncionario;
                if (cargoSelecionado == Cargo.VETERINARIO) {
                    // ... lógica de criar Veterinario ...
                    novoFuncionario = new Veterinario(null, nome, email, null, senha, txtcrmvfuncionario.getText());
                } else {
                    throw new ServiceException("Cadastro para o cargo '" + cargoSelecionado + "' ainda não implementado.");
                }
                funcionarioService.contratarNovoFuncionario(novoFuncionario);
                mostrarAlerta("Sucesso", "Funcionário '" + nome + "' cadastrado com sucesso!");

            } else {
                // --- MODO EDIÇÃO ---
                // Se não é nulo, estamos atualizando o objeto existente
                funcionarioParaEditar.setNomeFuncionario(nome);
                funcionarioParaEditar.setEmailFuncionario(email);
                funcionarioParaEditar.setSenhaFuncionario(senha);
                funcionarioParaEditar.setCargo(cargoSelecionado);
                if (funcionarioParaEditar instanceof Veterinario) {
                    ((Veterinario) funcionarioParaEditar).setCrmv(txtcrmvfuncionario.getText());
                }
                // Chama o novo serviço de atualização
                funcionarioService.atualizarFuncionario(funcionarioParaEditar);
                mostrarAlerta("Sucesso", "Dados do funcionário atualizados com sucesso!");
            }

            // Limpa tudo e atualiza a tabela
            limparCampos();
            carregarDadosDaTabela();

        } catch (ServiceException e) {
            mostrarAlerta("Erro ao Salvar", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {
        // Limpa a referência ao funcionário que estava sendo editado
        this.funcionarioParaEditar = null;

        // Limpa os campos visuais
        txtnomefuncionario.clear();
        txtemailfuncionario.clear();
        txtsenhafuncionario.clear();
        txtcrmvfuncionario.clear();
        comboboxcargofuncionario.getSelectionModel().clearSelection();
        Tabelafuncionarios.getSelectionModel().clearSelection(); // Limpa a seleção na tabela
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void handleExcluirFuncionario() {
        // 1. Pega o funcionário que está selecionado na tabela
        Funcionario funcionarioSelecionado = Tabelafuncionarios.getSelectionModel().getSelectedItem();

        // 2. Verifica se um funcionário foi realmente selecionado
        if (funcionarioSelecionado == null) {
            mostrarAlerta("Erro", "Por favor, selecione um funcionário na tabela para excluir.");
            return;
        }

        // 3. Pede confirmação ao usuário antes de excluir
        Alert alertaDeConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        alertaDeConfirmacao.setTitle("Confirmação de Exclusão");
        alertaDeConfirmacao.setHeaderText("Tem certeza que deseja excluir o funcionário '" + funcionarioSelecionado.getNomeFuncionario() + "'?");
        alertaDeConfirmacao.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alertaDeConfirmacao.showAndWait();

        // 4. Se o usuário clicou em "OK", prossiga com a exclusão
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // 5. Chama o serviço para deletar o funcionário do banco de dados
                // (O método deletarFuncionario já existe no seu FuncionarioService!)
                funcionarioService.deletarFuncionario(funcionarioSelecionado.getIdFuncionario());

                // 6. Mostra um alerta de sucesso e atualiza a tabela
                Alert sucesso = new Alert(Alert.AlertType.INFORMATION); // Tipo de alerta corrigido para informação
                sucesso.setTitle("Sucesso");
                sucesso.setHeaderText(null);
                sucesso.setContentText("Funcionário excluído com sucesso!");
                sucesso.showAndWait();

                // 7. Recarrega os dados da tabela para o item sumir da tela
                carregarDadosDaTabela();

            } catch (ServiceException e) {
                // Se o serviço retornar um erro, mostra para o usuário
                mostrarAlerta("Erro ao Excluir", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Chamado ao clicar no botão 'Editar'. Carrega os dados do funcionário
     * selecionado no formulário da direita.
     */
    @FXML
    private void handleEditarFuncionario() {
        // Pega o funcionário selecionado na tabela
        Funcionario selecionado = Tabelafuncionarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Seleção Vazia", "Por favor, selecione um funcionário na tabela para editar.");
            return;
        }

        // Guarda a referência ao funcionário que estamos editando
        this.funcionarioParaEditar = selecionado;

        // Preenche os campos do formulário com os dados do funcionário
        txtnomefuncionario.setText(selecionado.getNomeFuncionario());
        txtemailfuncionario.setText(selecionado.getEmailFuncionario());
        comboboxcargofuncionario.setValue(selecionado.getCargo());
        txtsenhafuncionario.setText(selecionado.getSenhaFuncionario()); // Preenche a senha

        // Se for um veterinário, preenche o CRMV
        if (selecionado instanceof Veterinario) {
            txtcrmvfuncionario.setText(((Veterinario) selecionado).getCrmv());
        } else {
            txtcrmvfuncionario.clear();
        }
    }
}