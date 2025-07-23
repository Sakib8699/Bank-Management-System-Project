package bank.management.system.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

public class DashBoardController {

    @FXML
    private Button dbBtn;

    @FXML
    private Button avabtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Button minimize;

    @FXML
    private Button close;

    private double x = 0;
    private double y = 0;

    @FXML
    private void openDb(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
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

            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handAvaRoom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployees.fxml"));
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

            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
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

            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mini(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void exit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
