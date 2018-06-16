package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

public class TelaAdministracaoController implements Initializable {

    @FXML
    private ImageView background;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private JFXButton addCarButton;

    @FXML
    private JFXButton removeCarButton;

    @FXML
    private JFXButton acceptSaleButton;

    @FXML
    private JFXButton salesHistoryButton;

    @FXML
    private JFXButton exitButton;

    @FXML
    private AnchorPane addCarPanel;

    @FXML
    private JFXComboBox<?> addCarModel;

    @FXML
    private JFXComboBox<?> addCarAttributes;

    @FXML
    private JFXComboBox<?> addCarColor;

    @FXML
    private JFXComboBox<?> addCarEngine;

    @FXML
    private JFXComboBox<?> addCarFuel;

    @FXML
    private JFXTextField addCarTire;

    @FXML
    private JFXTextField addCarValue;

    @FXML
    private JFXButton addCarAddButton;

    @FXML
    private JFXButton addCarCancel;
    
    @FXML
    private void addCarButtonAction(ActionEvent evt){
        addCarPanel.setVisible(true);
    }
    
    @FXML
    private void removeCarButtonAction(ActionEvent evt){
        //Code
    }
    
    @FXML
    private void acceptSaleButtonAction(ActionEvent evt){
        //Code
    }
    
    @FXML
    private void salesHistoryButtonAction(ActionEvent evt){
        //Code
    }
    
    @FXML
    private void exitButtonAction(ActionEvent evt){
        
    }
    
    @FXML
    private void addCarAddButtonAction(ActionEvent evt){
        //Code
    }
    
    @FXML
    private void addCarCancelAction(ActionEvent evt){
        addCarPanel.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        background.setImage(new Image("/images/background.png"));
    }

}
