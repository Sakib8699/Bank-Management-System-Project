/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package bank.management.system.project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent; 

/**
 *
 * @author Mimman
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane main_form;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button singupBtn;
    
    private double x = 0;
    private double y = 0;
    
    
 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void Login(ActionEvent event) {
    }

    @FXML
    private void SingUp(ActionEvent event) {
       try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        root.setOnMousePressed((MouseEvent e) -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - x);
            stage.setY(e.getScreenY() - y);
            stage.setOpacity(0.8);
        });

        root.setOnMouseReleased((MouseEvent e) -> {
            stage.setOpacity(1); 
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);
    }
}
