package bank.management.system.project;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleStringProperty username;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty password;
    private final SimpleStringProperty accountNumber;
    private final SimpleStringProperty dob;
    private final SimpleStringProperty address;
    private final SimpleStringProperty gender;

    public User(String username, String name, String phone, String password, 
               String accountNumber, String dob, String address, String gender) {
        this.username = new SimpleStringProperty(username);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.password = new SimpleStringProperty(password);
        this.accountNumber = new SimpleStringProperty(accountNumber);
        this.dob = new SimpleStringProperty(dob);
        this.address = new SimpleStringProperty(address);
        this.gender = new SimpleStringProperty(gender);
    }

    // Getter methods for properties
    public String getUsername() { return username.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }
    public String getPassword() { return password.get(); }
    public String getAccountNumber() { return accountNumber.get(); }
    public String getDob() { return dob.get(); }
    public String getAddress() { return address.get(); }
    public String getGender() { return gender.get(); }

    // Property getters
    public SimpleStringProperty usernameProperty() { return username; }
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleStringProperty phoneProperty() { return phone; }
    public SimpleStringProperty passwordProperty() { return password; }
    public SimpleStringProperty accountNumberProperty() { return accountNumber; }
    public SimpleStringProperty dobProperty() { return dob; }
    public SimpleStringProperty addressProperty() { return address; }
    public SimpleStringProperty genderProperty() { return gender; }
}