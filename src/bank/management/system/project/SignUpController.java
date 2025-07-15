package bank.management.system.project;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignUpController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private TextField accountNumberField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> genderCombo;

    @FXML private Button registerBtn;
    @FXML private Button loginBtn;
    @FXML private Button cancelBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;

    private final String[] genderOptions = { "Male", "Female", "Other" };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> genderList = FXCollections.observableArrayList(genderOptions);
        genderCombo.setItems(genderList);
    }

    @FXML
    private void Login(ActionEvent event) {
        String username = usernameField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String accountNumber = accountNumberField.getText();
        String dob = (dobPicker.getValue() != null) ? dobPicker.getValue().toString() : "";
        String address = addressField.getText();
        String gender = (genderCombo.getValue() != null) ? genderCombo.getValue() : "";

        if (username.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty() ||
            accountNumber.isEmpty() || dob.isEmpty() || address.isEmpty() || gender.isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Password must be at least 8 characters long.");
            return;
        }

        String checkQuery = "SELECT username FROM user WHERE username = ?";
        String insertQuery = "INSERT INTO user (username, name, phone, password, account_number, dob, address, gender) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        connect = database.ConnectDB();

        try {
            prepare = connect.prepareStatement(checkQuery);
            prepare.setString(1, username);
            result = prepare.executeQuery();

            if (result.next()) {
                showAlert(Alert.AlertType.ERROR, username + " is already taken.");
                return;
            }

            prepare = connect.prepareStatement(insertQuery);
            prepare.setString(1, username);
            prepare.setString(2, name);
            prepare.setString(3, phone);
            prepare.setString(4, password);
            prepare.setString(5, accountNumber);
            prepare.setString(6, dob);
            prepare.setString(7, address);
            prepare.setString(8, gender);

            prepare.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Account registered successfully!");

            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);
    }

    private void showAlert(Alert.AlertType type, String message) {
        alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        usernameField.clear();
        nameField.clear();
        phoneField.clear();
        passwordField.clear();
        accountNumberField.clear();
        dobPicker.setValue(null);
        addressField.clear();
        genderCombo.getSelectionModel().clearSelection();
    }
}
