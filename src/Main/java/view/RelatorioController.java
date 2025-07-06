package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RelatorioController {

    // Campos do formulário
    @FXML
    private TextField txtTitulo;

    @FXML
    private TextArea txtConteudo;

    // Botões
    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnLimpar;

    // Tabela de relatórios
    @FXML
    private TableView<?> tabelaRelatorios;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colTitulo;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colAcoes;

    }