package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

//Handle Login.fxml
public class LoginController {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Hyperlink signUpHyperlink;
    private UserDashboardController userDashboardController;

    public void setUserDashboardController(UserDashboardController userDashboardController) {
        this.userDashboardController = userDashboardController;
    }

    //Shut down the system
    public void cancelButtonOnAction(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to leave?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Login
    public void loginButtonOnAction(ActionEvent event) {
        if (userNameTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
            validateLogin();
        }
        else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    //HyperLink for user or admin to sign up
    public void signUpHyperlinkOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Database connection for login validation
    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        if (connectDB == null) {
            loginMessageLabel.setText("Failed to connect to database");
            return;
        }

        String verifyLogin  = "SELECT Role FROM UserAccounts WHERE UserName = '" + userNameTextField.getText() + "' AND Password = '" + passwordPasswordField.getText() + "'";

        try {
            Statement statement= connectDB.createStatement();
            ResultSet queryResult  = statement.executeQuery(verifyLogin);

            if(queryResult.next()) {
                String role = queryResult.getString("Role");
                if ("User".equals(role)) {
                    loginMessageLabel.setText("Welcome User");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sign In");
                    alert.setHeaderText(null);
                    alert.setContentText("Sign in successfully");
                    alert.showAndWait();
                    showUserDashboard();
                }
                else {
                    loginMessageLabel.setText("Welcome Admin");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sign In");
                    alert.setHeaderText(null);
                    alert.setContentText("Sign in successfully");
                    alert.showAndWait();
                    showAdminDashboard();
                }
            }
            else {
                loginMessageLabel.setText("Invalid login. Please try again.");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            loginMessageLabel.setText("An error occurred which may be due to database connection.");
        }
        finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            }
            catch (SQLException e) {
                    e.printStackTrace();
            }
        }
    }

    //If the role of user account is user, show UserDashboard.fxml
    public void showUserDashboard() {
        try {
            String username = userNameTextField.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            UserDashboardController userController = loader.getController();
            userController.initialize(username);
            setUserDashboardController(userController);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //If the role of user account is admin, show AdminDashboard.fxml
    public void showAdminDashboard() {
        try {
            String username = userNameTextField.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();
            AdminDashboardController adminController = loader.getController();
            adminController.initialize(username);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}