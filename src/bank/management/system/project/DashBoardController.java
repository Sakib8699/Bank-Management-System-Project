package bank.management.system.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    // UI Components
    @FXML private Button dbBtn;
    @FXML private Button avabtn;
    @FXML private Button logOutBtn;
    @FXML private Button minimize;
    @FXML private Button close;
    
    // Table Components
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableColumn<User, String> nameCol;
    @FXML private TableColumn<User, String> phoneCol;
    @FXML private TableColumn<User, String> passwordCol;
    @FXML private TableColumn<User, String> accountNoCol;
    @FXML private TableColumn<User, String> dobCol;
    @FXML private TableColumn<User, String> addressCol;
    @FXML private TableColumn<User, String> genderCol;

    private double x = 0;
    private double y = 0;
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableColumns();
        loadUserData();
    }

    private void initializeTableColumns() {
        try {
            usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
            accountNoCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
            dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        } catch (Exception e) {
            System.err.println("Error initializing table columns: " + e.getMessage());
        }
    }

    private void loadUserData() {
        try (Connection connect = database.ConnectDB();
             PreparedStatement statement = connect.prepareStatement("SELECT * FROM users");
             ResultSet result = statement.executeQuery()) {
            
            userList.clear();
            
            while (result.next()) {
                userList.add(new User(
                    result.getString("username"),
                    result.getString("name"),
                    result.getString("phone"),
                    result.getString("password"),
                    result.getString("account_number"),
                    result.getString("Dob"),
                    result.getString("Address"),
                    result.getString("gender")
                ));
            }
            
            userTable.setItems(userList);
            System.out.println("Loaded " + userList.size() + " users from database");
            
        } catch (Exception e) {
            System.err.println("Error loading user data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Navigation Methods
    @FXML
    private void openDb(ActionEvent event) {
        loadScene(event, "Home.fxml");
    }

    @FXML
    private void handAvaRoom(ActionEvent event) {
        loadScene(event, "AddEmployees.fxml");
    }

    @FXML
    private void logOut(ActionEvent event) {
        loadScene(event, "Login.fxml");
    }

    // Window Control Methods
    @FXML
    private void mini(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void exit(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            setupDraggableWindow(root, stage);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error loading scene: " + e.getMessage());
        }
    }

    private void setupDraggableWindow(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            stage.setOpacity(0.8);
        });

        root.setOnMouseReleased(event -> {
            stage.setOpacity(1);
        });
    }

    // User Model Class
    public static class User {
        private final String username;
        private final String name;
        private final String phone;
        private final String password;
        private final String accountNumber;
        private final String dob;
        private final String address;
        private final String gender;

        public User(String username, String name, String phone, String password, 
                   String accountNumber, String dob, String address, String gender) {
            this.username = username;
            this.name = name;
            this.phone = phone;
            this.password = password;
            this.accountNumber = accountNumber;
            this.dob = dob;
            this.address = address;
            this.gender = gender;
        }

        public String getUsername() { return username; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getPassword() { return password; }
        public String getAccountNumber() { return accountNumber; }
        public String getDob() { return dob; }
        public String getAddress() { return address; }
        public String getGender() { return gender; }
    }
}