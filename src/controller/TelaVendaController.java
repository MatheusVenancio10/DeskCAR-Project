package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class TelaVendaController implements Initializable {

    @FXML
    private ImageView background;

    @FXML
    private Label logoLabel;

    @FXML
    private JFXTreeTableView<?> saleTable;

    @FXML
    private JFXTextField searchInput;

    @FXML
    private AnchorPane saleDatailsPanel;

    @FXML
    private Label detailsModel;

    @FXML
    private Label detailsAttributes;

    @FXML
    private Label detailsColor;

    @FXML
    private Label detailsFuel;

    @FXML
    private Label detailsEngine;

    @FXML
    private Label detailsTire;

    @FXML
    private Label detailsValue;

    @FXML
    private JFXButton detailsRequestPurchaseButton;

    @FXML
    private JFXButton detailsCancelButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                background.setImage(new Image("/images/background.png"));
    }

}