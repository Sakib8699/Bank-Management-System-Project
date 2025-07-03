/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bank.management.system.project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignUpController implements Initializable {

    @FXML
    private AnchorPane main_form;

    private TextField fullNameField;

    private TextField emailField;

    private TextField usernameField;

    private PasswordField passwordField;

    private DatePicker datePicker;

    @FXML
    private ComboBox<String> genderCombo;

    @FXML
    private VBox vbox;

    @FXML
    private HBox hbox;

    @FXML
    private HBox hbox1;


    @FXML
    private Button cancelBtn;

    @FXML
    private TextField username11;

    @FXML
    private TextField username112;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username1;
    @FXML
    private DatePicker datepicer;
    @FXML
    private TextField username111;
    @FXML
    private Button loginBtn;
    @FXML
    private Label already;
    @FXML
    private Hyperlink login;

    @Override
   public void initialize(URL url, ResourceBundle rb) {
    if (genderCombo != null) {
        genderCombo.getItems().addAll("Male", "Female", "Other");
    } else {
        System.out.println("genderCombo not found in !");
    }
}

    private void signUp(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String dob = (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";
        String gender = (genderCombo.getValue() != null) ? genderCombo.getValue() : "";

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all required fields.");
            return;
        }

        System.out.println("Sign Up Info:");
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("DOB: " + dob);
        System.out.println("Gender: " + gender);

        showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful!");
    }

    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void Login(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SingUp.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
