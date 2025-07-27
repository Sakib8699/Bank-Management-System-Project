package bank.management.system.project;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AddEmployeesController implements Initializable {

    @FXML private AnchorPane main_form;
    @FXML private TextField employeeName;
    @FXML private TextField employeePosition;
    @FXML private TextField employeePhone;
    @FXML private TextField employeeEmail;
    @FXML private TextField employeeSalary;
    @FXML private TextField searchField;
    
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> idCol;
    @FXML private TableColumn<Employee, String> nameCol;
    @FXML private TableColumn<Employee, String> positionCol;
    @FXML private TableColumn<Employee, String> phoneCol;
    @FXML private TableColumn<Employee, String> emailCol;
    @FXML private TableColumn<Employee, Double> salaryCol;
    @FXML private TableColumn<Employee, String> actionCol;
    
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    @FXML
    private Button addEmployeeBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button refreshBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableColumns();
        loadEmployeeData();
    }

    private void initializeTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }

    private void loadEmployeeData() {
        employeeList.clear();
        try (Connection connect = database.ConnectDB();
             PreparedStatement statement = connect.prepareStatement("SELECT * FROM employees");
             ResultSet result = statement.executeQuery()) {
            
            while (result.next()) {
                employeeList.add(new Employee(
                    result.getInt("employee_id"),
                    result.getString("name"),
                    result.getString("position"),
                    result.getString("phone"),
                    result.getString("email"),
                    result.getDouble("salary")
                ));
            }
            
            employeeTable.setItems(employeeList);
        } catch (Exception e) {
            showAlert("Error", "Failed to load employee data: " + e.getMessage());
        }
    }

    @FXML
    private void addEmployee() {
        String name = employeeName.getText();
        String position = employeePosition.getText();
        String phone = employeePhone.getText();
        String email = employeeEmail.getText();
        String salaryText = employeeSalary.getText();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty() || salaryText.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            
            String sql = "INSERT INTO employees (name, position, phone, email, salary, hire_date, status) " +
                         "VALUES (?, ?, ?, ?, ?, CURDATE(), 'Active')";
            
            try (Connection connect = database.ConnectDB();
                 PreparedStatement prepare = connect.prepareStatement(sql)) {
                
                prepare.setString(1, name);
                prepare.setString(2, position);
                prepare.setString(3, phone);
                prepare.setString(4, email);
                prepare.setDouble(5, salary);
                
                prepare.executeUpdate();
                
                showAlert("Success", "Employee added successfully");
                clearFields();
                loadEmployeeData();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid salary");
        } catch (Exception e) {
            showAlert("Database Error", "Error adding employee: " + e.getMessage());
        }
    }

    private void clearFields() {
        employeeName.clear();
        employeePosition.clear();
        employeePhone.clear();
        employeeEmail.clear();
        employeeSalary.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Employee {
        private final int id;
        private final String name;
        private final String position;
        private final String phone;
        private final String email;
        private final double salary;

        public Employee(int id, String name, String position, String phone, String email, double salary) {
            this.id = id;
            this.name = name;
            this.position = position;
            this.phone = phone;
            this.email = email;
            this.salary = salary;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getPosition() { return position; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public double getSalary() { return salary; }
    }
}