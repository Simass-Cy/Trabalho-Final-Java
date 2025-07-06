package view;

import Repositories.FuncionarioRepositoryDB;
import Repositories.IFuncionarioRepository;
import Application.Cargo;
import Entities.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Funcionariocontroller {
    // Componentes da tabela
    @FXML private TableView<Funcionario> tabelaFuncionarios;

    // Componentes de busca
    @FXML private TextField campoBusca;
    @FXML private ComboBox<Cargo> filtroCargo;

    // Componentes do formulário
    @FXML private TextField campoId;
    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private ComboBox<Cargo> comboCargo;


}