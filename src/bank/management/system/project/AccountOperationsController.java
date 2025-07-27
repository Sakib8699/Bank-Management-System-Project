package bank.management.system.project;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.*;

public class AccountOperationsController {

    @FXML private Label accountNumberLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label currentBalanceLabel;
    @FXML private TextField depositAmountField;
    @FXML private TextField withdrawAmountField;
    @FXML private TextField transferAmountField;
    @FXML private TextField transferAccountField;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> transactionIdCol;
    @FXML private TableColumn<Transaction, String> transactionTypeCol;
    @FXML private TableColumn<Transaction, Double> amountCol;
    @FXML private TableColumn<Transaction, Double> balanceCol;
    @FXML private TableColumn<Transaction, String> dateCol;

    private String accountNumber;
    private String username;
    private double balance;

    public void initialize() {
        transactionIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        transactionTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        amountCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        balanceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBalance()).asObject());
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
    }

    public void setUserData(String username, String accountNumber) {
        this.username = username;
        this.accountNumber = accountNumber;
        accountNumberLabel.setText(accountNumber);
        customerNameLabel.setText(username);
        fetchAccountDetails();
        refreshData();
    }

    public void handleDeposit() {
        try {
            double amount = Double.parseDouble(depositAmountField.getText());
            if (amount > 0) {
                balance += amount;
                addTransaction("DEPOSIT", amount, "Cash Deposit");
                updateBalance();
                depositAmountField.clear();
            } else {
                showAlert("Invalid Amount", "Deposit amount must be positive.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for deposit.");
        }
    }

    public void handleWithdraw() {
        try {
            double amount = Double.parseDouble(withdrawAmountField.getText());
            if (amount > 0) {
                if (amount <= balance) {
                    balance -= amount;
                    addTransaction("WITHDRAWAL", amount, "Cash Withdrawal");
                    updateBalance();
                    withdrawAmountField.clear();
                } else {
                    showAlert("Insufficient Balance", "Withdrawal amount exceeds current balance.");
                }
            } else {
                showAlert("Invalid Amount", "Withdrawal amount must be positive.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for withdrawal.");
        }
    }

    public void handleTransfer() {
        try {
            double amount = Double.parseDouble(transferAmountField.getText());
            String targetAccount = transferAccountField.getText();
            if (amount > 0) {
                if (amount <= balance) {
                    if (accountExists(targetAccount)) {
                        balance -= amount;
                        addTransaction("TRANSFER_OUT", amount, "Transfer to " + targetAccount);
                        addTransactionToTargetAccount(targetAccount, amount);
                        updateBalance();
                        transferAmountField.clear();
                        transferAccountField.clear();
                    } else {
                        showAlert("Invalid Account", "Target account does not exist.");
                    }
                } else {
                    showAlert("Insufficient Balance", "Transfer amount exceeds current balance.");
                }
            } else {
                showAlert("Invalid Amount", "Transfer amount must be positive.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for transfer.");
        }
    }

    public void refreshData() {
        fetchAccountDetails();
        ObservableList<Transaction> transactions = getTransactionsFromDatabase();
        transactionTable.setItems(transactions);
    }

    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            DashBoardController controller = loader.getController();
            controller.setUserData(username, accountNumber);
            Stage stage = (Stage) accountNumberLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load the dashboard.");
        }
    }

    public void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) accountNumberLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load the login page.");
        }
    }

    private void fetchAccountDetails() {
        try (Connection connect = database.ConnectDB()) {
            CallableStatement balanceStmt = connect.prepareCall("{CALL GetAccountBalance(?)}");
            balanceStmt.setString(1, accountNumber);
            ResultSet balanceRs = balanceStmt.executeQuery();
            if (balanceRs.next()) {
                balance = balanceRs.getDouble("balance");
                updateBalance();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load account details.");
        }
    }

    private void updateBalance() {
        currentBalanceLabel.setText(String.format("TK %.2f", balance));
    }

    private void addTransaction(String type, double amount, String description) {
        try (Connection connect = database.ConnectDB()) {
            String sql = "INSERT INTO transactions (account_number, transaction_type, amount, balance, description, transaction_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connect.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, balance);
            stmt.setString(5, description);
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            refreshData();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save transaction.");
        }
    }

    private void addTransactionToTargetAccount(String targetAccount, double amount) {
        try (Connection connect = database.ConnectDB()) {
            CallableStatement balanceStmt = connect.prepareCall("{CALL GetAccountBalance(?)}");
            balanceStmt.setString(1, targetAccount);
            ResultSet balanceRs = balanceStmt.executeQuery();
            double targetBalance = 0.0;
            if (balanceRs.next()) {
                targetBalance = balanceRs.getDouble("balance");
            }
            targetBalance += amount;

            String sql = "INSERT INTO transactions (account_number, transaction_type, amount, balance, description, transaction_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connect.prepareStatement(sql);
            stmt.setString(1, targetAccount);
            stmt.setString(2, "TRANSFER_IN");
            stmt.setDouble(3, amount);
            stmt.setDouble(4, targetBalance);
            stmt.setString(5, "Transfer from " + accountNumber);
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save transfer to target account.");
        }
    }

    private boolean accountExists(String accountNumber) {
        try (Connection connect = database.ConnectDB()) {
            String sql = "SELECT 1 FROM user WHERE account_number = ?";
            PreparedStatement stmt = connect.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to verify target account.");
            return false;
        }
    }

    private ObservableList<Transaction> getTransactionsFromDatabase() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        try (Connection connect = database.ConnectDB()) {
            String sql = "SELECT transaction_id, transaction_type, amount, balance, transaction_date FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
            PreparedStatement stmt = connect.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getDouble("balance"),
                    rs.getTimestamp("transaction_date").toString()
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load transactions.");
        }
        return transactions;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Transaction {
        private final int id;
        private final String type;
        private final double amount;
        private final double balance;
        private final String date;

        public Transaction(int id, String type, double amount, double balance, String date) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.balance = balance;
            this.date = date;
        }

        public int getId() { return id; }
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public double getBalance() { return balance; }
        public String getDate() { return date; }
    }
}