package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

//Handle ItemMenu.fxml for displaying item card in UserDashboard.fxml
public class ItemMenuController {
    @FXML
    private Button itemAddButton;
    @FXML
    private AnchorPane itemCard;
    @FXML
    private ImageView itemImageView;
    @FXML
    private Label itemNameLabel;
    @FXML
    private Label itemPriceLabel;
    @FXML
    private Spinner<Integer> itemSpinner;
    private itemData itemD;
    private Image image;
    private SpinnerValueFactory<Integer> spinnerValueFactory;
    private int ItemID;
    private int quantity;
    private Alert alert;
    private double price;
    private Double TotalPrice;
    private UserDashboardController userDashboardController;

    public void setUserDashboardController(UserDashboardController userDashboardController) {
        this.userDashboardController = userDashboardController;
    }

    public UserDashboardController getUserDashboardController() {
        return userDashboardController;
    }

    //Set item data for item cards
    public void setItemCardListData(itemData itemD) {
        this.itemD = itemD;

        ItemID = itemD.getItem_ID();
        itemNameLabel.setText(itemD.getItemName());
        itemPriceLabel.setText("$" + itemD.getPrice());
        String path = "file:" + itemD.getImage();
        image = new Image(path, 210, 130, false, true);
        itemImageView.setImage(image);
        price = itemD.getPrice();
    }

    //Set the quantity of item to be added to order
    public void itemSpinnerOnAction() {
        spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        itemSpinner.setValueFactory(spinnerValueFactory);
    }

    //Handle and validate add item to order action
    public void itemAddButtonOnAction(ActionEvent event) {
        quantity = itemSpinner.getValue();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        boolean check = false;

        try {
            int checkS = 0;
            String checkStock = "SELECT Stock FROM Items WHERE Item_ID = '" + ItemID + "'";

            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(checkStock);

            if (resultSet.next()) {
                checkS = resultSet.getInt("Stock");
            }

            if (checkS == 0) {
                String updateAvailability = "UPDATE Items SET Availability = false WHERE Item_ID = '" + ItemID + "'";

                Statement statement0 = connectDB.createStatement();
                statement0.executeUpdate(updateAvailability);
            }

            String checkAvailability = "SELECT Availability FROM Items WHERE Item_ID = '" + ItemID + "'";

            Statement statement1 = connectDB.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(checkAvailability);

            if(resultSet1.next()) {
                check  = resultSet1.getBoolean("Availability");
            }

            if(!check || quantity == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurs which may be due to availability or quantity");
                alert.showAndWait();
            }
            else {
                double price = 0;

                String checkPrice = "SELECT Price FROM Items WHERE Item_ID = '" + ItemID + "'";

                Statement statement2 = connectDB.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(checkPrice);

                if (resultSet2.next()) {
                    price = resultSet2.getDouble("Price");
                }

                TotalPrice = (quantity * price);
                java.util.Date date = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                if (checkS < quantity) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The item is out of stock");
                    alert.showAndWait();
                }
                else {

                    String InsertIntoOrders = "INSERT INTO Orders " + "(User_ID, Item_ID, Quantity, TotalPrice, Date, IsPaid, IsDelivered)" +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement prepare = connectDB.prepareStatement(InsertIntoOrders);
                    prepare.setString(1, String.valueOf(data.User_ID));
                    prepare.setString(2, String.valueOf(ItemID));
                    prepare.setString(3, String.valueOf(quantity));
                    prepare.setString(4, String.valueOf(TotalPrice));
                    prepare.setString(5, String.valueOf(sqlDate));
                    prepare.setBoolean(6, false);
                    prepare.setBoolean(7, false);

                    prepare.executeUpdate();

                    int updatedStock = checkS - quantity;

                    String updateStock = "UPDATE Items SET Stock = '" + updatedStock + "' WHERE Item_ID = '" + ItemID + "'";

                    Statement statement3 = connectDB.createStatement();
                    statement3.executeUpdate(updateStock);

                    UserDashboardController userDashboardController = getUserDashboardController();
                    userDashboardController.menuTableViewDisplay();
                    userDashboardController.menuTotalPriceLabelDisplay();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Item successfully added to orders");
                    alert.showAndWait();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        itemSpinnerOnAction();
    };
}
