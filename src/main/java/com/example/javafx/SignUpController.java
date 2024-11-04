package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Handle SignUp.fxml
public class SignUpController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private PasswordField reconfirmPasswordPasswordField;
    @FXML
    private ComboBox<?> roleComboBox;
    @FXML
    private Button signUpButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField addressTextField;
    private String[] roleList = {"User", "Admin"};

    //Close SignUp.fxml
    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    //Create database connection to fetch and insert data for user account to sign up
    public void signUpButtonOnAction(ActionEvent event) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String address = addressTextField.getText();
        String password = passwordPasswordField.getText();
        String reconfirmPassword = reconfirmPasswordPasswordField.getText();
        String role = (String) roleComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || address.isEmpty() || password.isEmpty() || reconfirmPassword.isEmpty() || role == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        if (!password.equals(reconfirmPassword)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match");
            alert.showAndWait();
            return;
        }

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String checkUsername = "SELECT * FROM UserAccounts WHERE UserName = ?";
        String insertUser = "INSERT INTO UserAccounts (First_Name, Last_Name, UserName, Address, Password, Role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement checkUsernameStatement = null;
        PreparedStatement insertUserStatement = null;

        try {
            checkUsernameStatement = connectDB.prepareStatement(checkUsername);
            checkUsernameStatement.setString(1, username);
            ResultSet resultSet = checkUsernameStatement.executeQuery();
            if (resultSet.next()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists");
                alert.showAndWait();
                return;
            }

            insertUserStatement = connectDB.prepareStatement(insertUser);
            insertUserStatement.setString(1, firstName);
            insertUserStatement.setString(2, lastName);
            insertUserStatement.setString(3, username);
            insertUserStatement.setString(4, address);
            insertUserStatement.setString(5, password);
            insertUserStatement.setString(6, role);
            insertUserStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign Up");
            alert.setHeaderText(null);
            alert.setContentText("User account created successfully");
            alert.showAndWait();

            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (checkUsernameStatement != null) {
                    checkUsernameStatement.close();
                }
                if (insertUserStatement != null) {
                    insertUserStatement.close();
                }
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Set up roles for role combo box
    public void setRoleComboBox() {
        List<String> roleL = new ArrayList<>();

        for(String data : roleList) {
            roleL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(roleL);
        roleComboBox.setItems(listData);
    }

    public void initialize() {
        setRoleComboBox();
    }
}
