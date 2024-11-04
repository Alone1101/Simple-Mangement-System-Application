package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Handle AdminDashboard.fxml
public class AdminDashboardController {
    @FXML
    private TableColumn<itemData, String> DateColumn;
    @FXML
    private AnchorPane InventoryAnchorPane;
    @FXML
    private TableColumn<itemData, String> ItemIDColumn;
    @FXML
    private TableColumn<itemData, String> ItemNameColumn;
    @FXML
    private TableColumn<itemData, String> PriceColumn;
    @FXML
    private TableColumn<itemData, String> AvailabilityColumn;
    @FXML
    private TableColumn<itemData, String> StockColumn;
    @FXML
    private TableColumn<itemData, String> TypeColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button statisticsButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button importButton;
    @FXML
    private Button inventoryButton;
    @FXML
    private ImageView inventoryImageView;
    @FXML
    private TableView<itemData> inventoryTableView;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private AnchorPane statisticsAnchorPane;
    @FXML
    private Button signoutButton;
    @FXML
    private Button updateButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField inventoryItemIDTextField;
    @FXML
    private TextField inventoryItemNameTextField;
    @FXML
    private TextField inventoryPriceTextField;
    @FXML
    private ComboBox<?> inventoryAvailabilityComboBox;
    @FXML
    private TextField inventoryStockTextField;
    @FXML
    private ComboBox<?> inventoryTypeComboBox;
    @FXML
    private Label numberOfUserLabel;
    @FXML
    private Label totalOrdersLabel;
    @FXML
    private Label todayIncomeLabel;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private AreaChart incomeAreaChart;
    @FXML
    private BarChart orderBarChart;
    @FXML
    private Button ordersTrackerButton;
    @FXML
    private TableColumn<ordersData, String> orderAddressColumn;
    @FXML
    private TableColumn<ordersData, String> orderIsDeliveredColumn;
    @FXML
    private TableColumn<ordersData, String> orderItemNameColumn;
    @FXML
    private TableColumn<ordersData, String> orderQuantityColumn;
    @FXML
    private TableColumn<ordersData, String> orderUsernameColumn;
    @FXML
    private AnchorPane ordersTrackerAnchorPane;
    @FXML
    private TableView<ordersData> ordersTrackerTableView;
    @FXML
    private Button deliverButton;

    private Alert alert;
    private String[] inventoryTypeList = {"Beverages", "Snacks", "Pre-packaged food", "Groceries", "Personal care products", "Medicine and healthCare products", "Tobacco products", "Household products"};
    private String[] inventoryStatusList = {"true", "false"};
    private ObservableList<itemData> inventoryDataList;
    private ObservableList<ordersData> ordersTrackerDataList;
    private Image image;
    private String Address;
    private DatabaseConnection databaseConnection;
    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    //Log out from UserDashboard.fxml and return to Login.fxml
    public void signOutButtonOnAction() {
        try {
            alert  = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to sign out?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)) {
                Stage stage = (Stage) signoutButton.getScene().getWindow();
                stage.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Set inventory types for inventory type combo box
    public void setInventoryTypeComboBox() {
        List<String> inventoryTL = new ArrayList<>();

        for(String data: inventoryTypeList) {
            inventoryTL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(inventoryTL);
        inventoryTypeComboBox.setItems(listData);
    }

    //Set availability for items
    public void setInventoryStatusComboBox() {
        List<String> inventorySL = new ArrayList<>();

        for(String data: inventoryStatusList) {
            inventorySL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(inventorySL);
        inventoryAvailabilityComboBox.setItems(listData);
    }

    //Fetch all item data
    public ObservableList<itemData> setInventoryDataList() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        ObservableList<itemData> inventoryDataList = FXCollections.observableArrayList();
        String itemsTable = "SELECT * FROM  Items";

        try {
            itemData itemD;
            Statement statement1 = connectDB.createStatement();
            ResultSet queryResult = statement1.executeQuery(itemsTable);

            while(queryResult.next()) {
                Blob imageBlob = queryResult.getBlob("Image");
                Image image = null;
                if (imageBlob != null) {
                    try (InputStream iStream = imageBlob.getBinaryStream()) {
                        image = new Image(iStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                itemD = new itemData(queryResult.getInt("Item_ID"), queryResult.getString("ItemName"), queryResult.getString("Type"),queryResult.getInt("Stock"),
                        queryResult.getDouble("Price"), queryResult.getBoolean("Availability"), queryResult.getDate("Date"), queryResult.getString("Image"));

                inventoryDataList.add(itemD);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return inventoryDataList;
    }

    //Show all item data in inventory table view
    public void showInventoryData() {
        inventoryDataList = setInventoryDataList();

        ItemIDColumn.setCellValueFactory(new PropertyValueFactory<>("Item_ID"));
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        TypeColumn.setCellValueFactory(new  PropertyValueFactory<>("Type"));
        StockColumn.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        AvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("Availability"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        inventoryTableView.setItems(inventoryDataList);
    }

    //Handle and validate add item action
    public void addButtonOnAction(ActionEvent event) {
        if (inventoryItemIDTextField.getText().isEmpty() || inventoryItemNameTextField.getText().isEmpty() || inventoryTypeComboBox.getSelectionModel().getSelectedItem() == null
            || inventoryStockTextField.getText().isEmpty() || inventoryPriceTextField.getText().isEmpty() || inventoryAvailabilityComboBox.getSelectionModel().getSelectedItem() == null
                || data.path == null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        }
        else {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String checkItemID = "SELECT Item_ID FROM Items WHERE Item_ID = '"+ inventoryItemIDTextField.getText() + "'";

            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(checkItemID);

                if(queryResult.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(inventoryItemNameTextField.getText() + " is already inserted");
                    alert.showAndWait();
                }
                else {
                    String path = data.path;
                    path = path.replace("\\", "\\\\");

                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    int stock = 0;
                    try {
                        stock = Integer.parseInt(inventoryStockTextField.getText());
                    } catch (NumberFormatException e) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Stock must be an integer");
                        alert.showAndWait();
                        return;
                    }

                    double price = 0.0;
                    try {
                        price = Double.parseDouble(inventoryPriceTextField.getText());
                    } catch (NumberFormatException e) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Price must be a double");
                        alert.showAndWait();
                        return;
                    }

                    String insertData = "INSERT INTO Items" + "(Item_ID, ItemName, Type, Stock, Price, Availability, Date, Image)"
                            + "VALUES(?,?,?,?,?,?,?,?)";

                    PreparedStatement preparedStatement = connectDB.prepareStatement(insertData);
                    preparedStatement.setString(1, inventoryItemIDTextField.getText());
                    preparedStatement.setString(2, inventoryItemNameTextField.getText());
                    preparedStatement.setString(3, (String) inventoryTypeComboBox.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4, inventoryStockTextField.getText());
                    preparedStatement.setString(5, inventoryPriceTextField.getText());
                    preparedStatement.setBoolean(6, Boolean.parseBoolean((String)inventoryAvailabilityComboBox.getSelectionModel().getSelectedItem()));
                    preparedStatement.setString(7, String.valueOf(sqlDate));
                    preparedStatement.setString(8, path);

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Added successfully");
                    alert.showAndWait();

                    showInventoryData();
                    clearButtonOnAction();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Handle and validate update item data action
    public void updateButtonOnAction() {
        if (inventoryItemIDTextField.getText().isEmpty() || inventoryItemNameTextField.getText().isEmpty() || inventoryTypeComboBox.getSelectionModel().getSelectedItem() == null
                || inventoryStockTextField.getText().isEmpty() || inventoryPriceTextField.getText().isEmpty() || inventoryAvailabilityComboBox.getSelectionModel().getSelectedItem() == null
                || data.path == null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        }
        else {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE Items SET " +
                    "ItemName =  '" + inventoryItemNameTextField.getText() + "', " +
                    "Type = '" + inventoryTypeComboBox.getSelectionModel().getSelectedItem() + "', " +
                    "Stock = '" + inventoryStockTextField.getText() + "', " +
                    "Price = '" + inventoryPriceTextField.getText() + "', " +
                    "Availability = '" + (inventoryAvailabilityComboBox.getSelectionModel().getSelectedItem().equals("true") ? 1:0) + "', " +
                    "Image = '" + path + "', " +
                    "Date = '" + data.date + "' " +
                    "WHERE Item_ID = '" + inventoryItemIDTextField.getText() + "'";

            try {
                int stock = 0;
                try {
                    stock = Integer.parseInt(inventoryStockTextField.getText());
                } catch (NumberFormatException e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Stock must be an integer");
                    alert.showAndWait();
                    return;
                }

                double price = 0.0;
                try {
                    price = Double.parseDouble(inventoryPriceTextField.getText());
                } catch (NumberFormatException e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Price must be a double");
                    alert.showAndWait();
                    return;
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update the Item_ID: " + inventoryItemIDTextField.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(updateData);

                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Information");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Updated successfully");
                    alert1.showAndWait();

                    showInventoryData();
                    clearButtonOnAction();
                }
                else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Error");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Update cancelled");
                    alert2.showAndWait();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Clear all fields
    public void clearButtonOnAction() {
        inventoryItemIDTextField.clear();
        inventoryItemNameTextField.setText("");
        inventoryTypeComboBox.getSelectionModel().clearSelection();
        inventoryPriceTextField.clear();
        inventoryStockTextField.setText("");
        inventoryAvailabilityComboBox.getSelectionModel().clearSelection();
        data.path = "";
        inventoryImageView.setImage(null);
    }

    //Import image file format .png, .jpg, .jpeg and save their path
    public void importButtonOnAction(ActionEvent event) {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*.jpg", "*.jpeg"));

        File file = openFile.showOpenDialog(mainAnchorPane.getScene().getWindow());

        if(file != null) {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 141, 168, false, true);

            inventoryImageView.setImage(image);
        }
    }

    //Handle delete item action
    public void deleteButtonOnAction(ActionEvent event) {
        if (inventoryItemIDTextField.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        }
        else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete Item_ID: " + inventoryItemIDTextField.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();
                String deleteData = "DELETE FROM Items WHERE Item_ID = " + inventoryItemIDTextField.getText();

                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(deleteData);

                    alert =  new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Deleted successfully");
                    alert.showAndWait();

                    showInventoryData();
                    clearButtonOnAction();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Deletion cancelled");
                alert.showAndWait();
            }
        }
    }

   //Enable the selection of item in item inventory table view
    public void selectInventoryData() {
        itemData itemD = inventoryTableView.getSelectionModel().getSelectedItem();
        int num = inventoryTableView.getSelectionModel().getSelectedIndex();
        data.path = itemD.getImage();
        data.date = String.valueOf(itemD.getDate());
        image = new Image(data.path, 141, 168, false, true);

        if ((num-1) < -1) {
            return;
        }

        inventoryItemIDTextField.setText(String.valueOf(itemD.getItem_ID()));
        inventoryItemNameTextField.setText(itemD.getItemName());
        inventoryStockTextField.setText(String.valueOf(itemD.getStock()));
        inventoryPriceTextField.setText(String.valueOf(itemD.getPrice()));
        inventoryImageView.setImage(image);
    }

    //Switch between dashboards
    public void statisticsButtonOnAction(ActionEvent event) {
        InventoryAnchorPane.setVisible(false);
        ordersTrackerAnchorPane.setVisible(false);
        statisticsAnchorPane.setVisible(true);
    }

    //Switch between dashboards
    public void inventoryButtonOnAction(ActionEvent event) {
        statisticsAnchorPane.setVisible(false);
        ordersTrackerAnchorPane.setVisible(false);
        InventoryAnchorPane.setVisible(true);
    }

    //Switch between dashboards
    public void ordersTrackerButtonOnAction(ActionEvent event) {
        statisticsAnchorPane.setVisible(false);
        InventoryAnchorPane.setVisible(false);
        ordersTrackerAnchorPane.setVisible(true);
    }

    //Display number of user
    public void numberOfUserLabelDisplay() {
        String fetchNumberOfUser = "SELECT COUNT(User_ID) FROM UserAccounts WHERE Role = 'User'";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            int numberOfUser = 0;
            PreparedStatement preparedStatement = connectDB.prepareStatement(fetchNumberOfUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                numberOfUser = resultSet.getInt("COUNT(User_ID)");
            }
            numberOfUserLabel.setText(String.valueOf(numberOfUser));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Display total orders paid
    public void totalOrdersLabelDisplay() {
        String fetchTotalOrders = "SELECT COUNT(Order_ID) FROM Orders WHERE IsPaid = true";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            int orders = 0;
            PreparedStatement preparedStatement = connectDB.prepareStatement(fetchTotalOrders);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                orders = resultSet.getInt("COUNT(Order_ID)");
            }

            totalOrdersLabel.setText(String.valueOf(orders));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Display daily total income
    public void todayIncomeLabelDisplay() {
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(Total) FROM Receipt WHERE Date = '" + sqlDate + "'";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            double todayIncome = 0;
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                todayIncome = resultSet.getDouble("SUM(Total)");
            }

            todayIncomeLabel.setText("$" + todayIncome);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Display total income
    public void totalIncomeLabelDisplay() {
        String sql = "SELECT SUM(Total) FROM receipt";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            float totalIncome = 0;
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalIncome = resultSet.getFloat("SUM(total)");
            }

            totalIncomeLabel.setText("$" + totalIncome);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Display income over time
    public void incomeAreaChartDisplay() {
        incomeAreaChart.getData().clear();

        String sql = "SELECT date, SUM(Total) FROM Receipt GROUP BY Date ORDER BY TIMESTAMP(Date)";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        XYChart.Series chart = new XYChart.Series();

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chart.getData().add(new XYChart.Data<>(resultSet.getString(1), resultSet.getFloat(2)));
            }

            incomeAreaChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Display order over time
    public void orderBarChartDisplay(){
        orderBarChart.getData().clear();

        String sql = "SELECT Date, COUNT(Order_ID) FROM Orders WHERE IsPaid = true GROUP BY Date ORDER BY TIMESTAMP(Date)";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        XYChart.Series chart = new XYChart.Series();

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chart.getData().add(new XYChart.Data<>(resultSet.getString(1), resultSet.getInt(2)));
            }

            orderBarChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fetch all orders tracker data
    public ObservableList<ordersData> setOrdersTrackerDataList() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        ObservableList<ordersData> ordersTrackerDataList = FXCollections.observableArrayList();
        String ordersTrackerTable = "SELECT UA.UserName, I.ItemName, O.Quantity, UA.Address, O.IsDelivered FROM Orders O INNER JOIN UserAccounts UA ON O.User_ID = UA.User_ID INNER JOIN Items I ON O.Item_ID = I.Item_ID";

        try {
            ordersData ordersD;
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(ordersTrackerTable);

            while(resultSet.next()) {
                ordersD = new ordersData(resultSet.getString("UserName"), resultSet.getString("ItemName"), resultSet.getInt("Quantity"),
                        resultSet.getString("Address"), resultSet.getBoolean("IsDelivered"));

                ordersTrackerDataList.add(ordersD);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersTrackerDataList;
    }

    //Display fetched orders tracker data in orders tracker table view
    public void showOrdersTrackerData() {
        ordersTrackerDataList = setOrdersTrackerDataList();

        orderUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        orderItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        orderQuantityColumn.setCellValueFactory(new  PropertyValueFactory<>("Quantity"));
        orderAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        orderIsDeliveredColumn.setCellValueFactory(new PropertyValueFactory<>("IsDelivered"));

        ordersTrackerTableView.setItems(ordersTrackerDataList);
    }

    //Select address to deliver user's orders
    public void selectAddress() {
        ordersData ordersD = ordersTrackerTableView.getSelectionModel().getSelectedItem();
        int num = ordersTrackerTableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1) {
            return;
        }
        Address = ordersD.getAddress();
    }

    //Update delivery status for all the items ordered by user
    public void deliverButtonOnAction() {
        if (Address != null && !Address.isEmpty()) {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String updateDeliveryStatus = "UPDATE Orders O INNER JOIN UserAccounts UA ON O.User_ID = UA.User_ID SET O.IsDelivered = true WHERE UA.Address = ? AND IsDelivered = false";

            try {
                PreparedStatement preparedStatement = connectDB.prepareStatement(updateDeliveryStatus);
                preparedStatement.setString(1, Address);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Delivery status updated successfully");
                    alert.showAndWait();

                    showOrdersTrackerData();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Delivery status update failed");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an address");
            alert.showAndWait();
        }
    }

    //Initialize all necessary display and action
    public void initialize(String username) {
        usernameLabel.setText(username);
        setInventoryTypeComboBox();
        setInventoryStatusComboBox();
        showInventoryData();
        numberOfUserLabelDisplay();
        totalOrdersLabelDisplay();
        todayIncomeLabelDisplay();
        totalIncomeLabelDisplay();
        incomeAreaChartDisplay();
        orderBarChartDisplay();
        showOrdersTrackerData();
    }
}