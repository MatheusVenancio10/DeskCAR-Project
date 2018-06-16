package controller;

import com.jfoenix.controls.JFXButton;
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

public class TelaAprovarVendaController implements Initializable {

    @FXML
    private ImageView background;

    @FXML
    private Label logoLabel;

    @FXML
    private JFXTreeTableView<?> toApproveTable;

    @FXML
    private AnchorPane toApproveDatailsPanel;

    @FXML
    private Label detailsModel;

    @FXML
    private Label detailsAttributes;

    @FXML
    private Label detailsValue;

    @FXML
    private JFXButton detailsAcceptBuy;

    @FXML
    private JFXButton detailsCancel;

    @FXML
    private JFXButton detailsReproveBuy;

    @FXML
    private Label detailsBuyerName;

    @FXML
    private Label detailsBuyerCPF;

    @FXML
    private Label detailsBuyerEmail;

    @FXML
    private Label detailsBuyerPhone;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                background.setImage(new Image("/images/background.png"));
    }

}
