package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class TelaInicialController implements Initializable {

    private final String admPassword = "desktop2018";
    
    @FXML
    private ImageView background;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private JFXButton administrationBtn;

    @FXML
    private JFXButton exitBtn;

    @FXML
    private JFXButton saleBtn;

    @FXML
    private AnchorPane loginPanel;

    @FXML
    private JFXTextField loginUserCPF;

    @FXML
    private JFXPasswordField loginUserPassword;

    @FXML
    private JFXButton loginEnter;

    @FXML
    private JFXButton loginRegister;

    @FXML
    private AnchorPane registerPanel;

    @FXML
    private JFXTextField registerCPF;

    @FXML
    private JFXPasswordField registerPassword;

    @FXML
    private JFXButton registerAdd;

    @FXML
    private JFXButton registerBack;

    @FXML
    private JFXTextField registerName;

    @FXML
    private JFXTextField registerPhone;
    
    @FXML
    private AnchorPane administratorLoginPanel;
    
    @FXML
    private JFXPasswordField administratorPassword;
    
    @FXML
    private void administrationButtonAction(ActionEvent evt){
        mainPanel.setVisible(false);
        administratorLoginPanel.setVisible(true);
    }
    
    @FXML
    private void saleButtonAction(ActionEvent evt){
        mainPanel.setVisible(false);
        loginPanel.setVisible(true);
    }
    
    @FXML
    private void exitButtonAction(ActionEvent evt){
        System.exit(0);
    }
    
    @FXML
    private void loginEnterButtonAction(ActionEvent evt){
        //Code
    }
    
    @FXML
    private void loginRegisterAction(ActionEvent evt){
        loginPanel.setVisible(true);
        registerPanel.setVisible(true);
    }
    
    @FXML
    private void registerAddAction(ActionEvent evt){
        //Code
    }
    
    @FXML
    private void registerBackAction(ActionEvent evt){
        registerPanel.setVisible(false);
        loginPanel.setVisible(true);
    }
    
    @FXML
    private void loginAdministratorButtonAction(ActionEvent evt){
        if(validateAdministratorPassword(administratorPassword.getText())){
            JOptionPane.showMessageDialog(null, "Deu Bom");
        } else {
            JOptionPane.showMessageDialog(null, "Senha incorreta!", "alerta", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @FXML
    private void backAdministratorButtonAction(ActionEvent evt){
        administratorLoginPanel.setVisible(false);
        mainPanel.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        background.setImage(new Image("/images/background.png"));
    }
    
    private boolean validateAdministratorPassword(String toConfirmPassword){
        if(admPassword.equals(toConfirmPassword)){
            return true;
        } else {
            return false;
        }
    }

}
