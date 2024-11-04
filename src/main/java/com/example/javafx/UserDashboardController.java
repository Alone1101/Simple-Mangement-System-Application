package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

//Handle UserDashboard.fxml
public class UserDashboardController{
    @FXML
    private Button itemsButton;
    @FXML
    private Button ordersButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private AnchorPane itemsMenuAnchorPane;
    @FXML
    private TextField menuAmountTextField;
    @FXML
    private Label menuChangeLabel;
    @FXML
    private GridPane menuGridPane;
    @FXML
    private TableColumn<ordersData, String> menuItemNameTableColumn;
    @FXML
    private Button menuPayButton;
    @FXML
    private TableColumn<ordersData, String> menuPriceTableColumn;
    @FXML
    private TableColumn<ordersData, String> menuQuantityTableColumn;
    @FXML
    private Button menuReceiptButton;
    @FXML
    private Button menuRemoveButton;
    @FXML
    private ScrollPane menuScrollPane;
    @FXML
    private TableView<ordersData> menuTableView;
    @FXML
    private Label menuTotalPriceLabel;
    @FXML
    private AnchorPane ordersAnchorPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox typeComboBox;
    private Alert alert;
    private ObservableList<itemData> itemMenuCardListData = FXCollections.observableArrayList();
    private ObservableList<ordersData> orderListData;
    private double total;
    private double amount;
    private double change;
    private int OID;
    private String selectedType;
    private String[] inventoryTypeList = {"Beverages", "Snacks", "Pre-packaged food", "Groceries", "Personal care products", "Medicine and healthCare products", "Tobacco products", "Household products"};
    //Get instance for instant data update
    private static UserDashboardController instance;

    public static UserDashboardController getInstance() {
        return instance;
    }

    public UserDashboardController() {
        instance = this;
    }

    private DatabaseConnection databaseConnection;
    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    //Fetch the User_ID based on username
    public void getUID() {
        String userName = userNameLabel.getText();

        if (userName.isEmpty()) {
            return;
        }

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String fetchUID = "SELECT User_ID FROM UserAccounts WHERE UserName = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(fetchUID);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            int UserID = 0;

            if (resultSet.next()) {
                UserID = resultSet.getInt("User_ID");
            }

            data.User_ID = UserID;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                Stage stage = (Stage) signOutButton.getScene().getWindow();
                stage.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fetch items to display
    public ObservableList<itemData> getItemMenuCardListData() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String fetchItems  = "SELECT * FROM Items";

        ObservableList<itemData> itemDataObservableList = FXCollections.observableArrayList();

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(fetchItems);

            itemData item;

            while(result.next()) {
                item = new itemData(result.getInt("Item_ID"), result.getString("ItemName"), result.getDouble("Price"),
                        result.getString("Image"));
                itemDataObservableList.add(item);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return itemDataObservableList;
    }

    //Fetch filtered item type
    public ObservableList<itemData> getSelectedItemMenuCardListData() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String fetchItems  = "SELECT * FROM Items WHERE Type = '" + selectedType + "'";

        ObservableList<itemData> itemDataObservableList = FXCollections.observableArrayList();

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(fetchItems);

            itemData item;

            while(result.next()) {
                item = new itemData(result.getInt("Item_ID"), result.getString("ItemName"), result.getDouble("Price"),
                        result.getString("Image"));
                itemDataObservableList.add(item);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return itemDataObservableList;
    }

    //Fetch searched item
    public ObservableList<itemData> getSearchedItemMenuCardListData(String searchText) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String fetchItems = "SELECT * FROM Items WHERE ItemName LIKE '%" + searchText + "%'";

        ObservableList<itemData> itemDataObservableList = FXCollections.observableArrayList();

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(fetchItems);

            itemData item;

            while (result.next()) {
                item = new itemData(result.getInt("Item_ID"), result.getString("ItemName"), result.getDouble("Price"),
                        result.getString("Image"));
                itemDataObservableList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemDataObservableList;
    }

    //Display all items
    public void itemsMenuDisplay() {
        itemMenuCardListData.clear();
        itemMenuCardListData.addAll(getItemMenuCardListData());

        int row = 0;
        int column = 0;

        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();

        for (int i = 0; i < itemMenuCardListData.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("ItemMenu.fxml"));
                AnchorPane pane = loader.load();
                ItemMenuController itemMenuController = loader.getController();
                itemMenuController.setItemCardListData(itemMenuCardListData.get(i));
                itemMenuController.setUserDashboardController(this);

                if(column == 3) {
                    column = 0;
                    row += 1;
                }

                menuGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(18));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Display selected items
    public void selectedItemsMenuDisplay() {
        menuGridPane.getChildren().clear();
        itemMenuCardListData.clear();
        itemMenuCardListData.addAll(getSelectedItemMenuCardListData());

        int row = 0;
        int column = 0;

        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();

        for (int i = 0; i < itemMenuCardListData.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("ItemMenu.fxml"));
                AnchorPane pane = loader.load();
                ItemMenuController itemMenuController = loader.getController();
                itemMenuController.setItemCardListData(itemMenuCardListData.get(i));
                itemMenuController.setUserDashboardController(this);

                if(column == 3) {
                    column = 0;
                    row += 1;
                }

                menuGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(18));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Display searched items
    public void searchItems(String searchText) {
        menuGridPane.getChildren().clear();

        if (searchText.isEmpty()) {
            itemsMenuDisplay();
            return;
        }

        itemMenuCardListData.clear();
        itemMenuCardListData.addAll(getSearchedItemMenuCardListData(searchText));

        int row = 0;
        int column = 0;

        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ItemMenu.fxml"));

            for (int i = 0; i < itemMenuCardListData.size(); i++) {
                AnchorPane pane = loader.load();
                ItemMenuController itemMenuController = loader.getController();
                itemMenuController.setItemCardListData(itemMenuCardListData.get(i));
                itemMenuController.setUserDashboardController(this);

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menuGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(18));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Fetch order data which haven't paid
    public ObservableList<ordersData> getOrderDataList() {
        ObservableList<ordersData> orderListData = FXCollections.observableArrayList();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        getUID();

        String fetchOrders = "SELECT o.*, i.ItemName FROM Orders o " + "JOIN Items i ON o.Item_ID = i.Item_ID " + "WHERE o.User_ID = " + data.User_ID + " AND o.IsPaid = false";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(fetchOrders);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ordersData ordersD = new ordersData(resultSet.getInt("Order_ID"), resultSet.getInt("Item_ID"), resultSet.getInt("User_ID"),
                        resultSet.getInt("Quantity"), resultSet.getDouble("TotalPrice"), resultSet.getDate("Date"),
                        resultSet.getString("ItemName"), resultSet.getBoolean("IsPaid"), resultSet.getBoolean("IsDelivered"));
                orderListData.add(ordersD);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return orderListData;
    }

    //Set up and display order data in menu table view
    public void menuTableViewDisplay() {
        orderListData = getOrderDataList();

        menuItemNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        menuQuantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        menuPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));

        menuTableView.setItems(orderListData);
    }

    //Enable the selection or orders in menu table view
    public void selectOrders() {
        ordersData ordersD = menuTableView.getSelectionModel().getSelectedItem();
        int num = menuTableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1) {
            return;
        }
        OID = ordersD.getOrder_ID();
    }

    //Calculate total price for all orders that haven't paid
    public void ordersGetTotal() {
        String getTotal = "SELECT SUM(TotalPrice) FROM Orders WHERE User_ID = ? AND IsPaid = false";
        total = 0.0;

        try (Connection connectDB = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(getTotal)) {

            preparedStatement.setInt(1, data.User_ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Display the total price in menu total price label
    public void menuTotalPriceLabelDisplay() {
        ordersGetTotal();
        DecimalFormat df = new DecimalFormat("#.##");
        menuTotalPriceLabel.setText("$" + df.format(total));
    }

    //Handle and validate menu amount text field
    public void setMenuAmountTextField() {
        ordersGetTotal();
        if (menuAmountTextField.getText().isEmpty() || total == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Amount or total price can't be zero");
            alert.showAndWait();
        }
        else {
            try {
                amount = Double.parseDouble(menuAmountTextField.getText());
                if (amount < total) {
                    menuAmountTextField.setText("");
                }
                else {
                    change = amount - total;
                    DecimalFormat df = new DecimalFormat("#.##");
                    menuChangeLabel.setText("$" + df.format(change));
                }
            } catch (NumberFormatException e) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid amount");
                alert.showAndWait();
            }
        }
    }

    //Handle and validate payment
    public void menuPayButtonOnAction(ActionEvent event) {
        if(total == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order before payment");
            alert.showAndWait();
        }
        else {
            ordersGetTotal();
            String insertReceipt = "INSERT INTO Receipt (User_ID, Total, Date)" + "VALUES(?,?,?)";
            String updateIsPaid = "UPDATE Orders SET IsPaid = true WHERE User_ID = ? AND IsPaid = false";

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            try {
                if (amount == 0) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Amount can't be zero");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Please confirm before proceeding");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        ordersGetTotal();
                        java.util.Date date = new java.util.Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        PreparedStatement preparedStatement = connectDB.prepareStatement(insertReceipt);
                        preparedStatement.setString(1, String.valueOf(data.User_ID));
                        preparedStatement.setString(2, String.valueOf(total));
                        preparedStatement.setString(3, String.valueOf(sqlDate));

                        preparedStatement.executeUpdate();

                        PreparedStatement updateStatement = connectDB.prepareStatement(updateIsPaid);
                        updateStatement.setInt(1, data.User_ID);

                        updateStatement.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Payment successful.");
                        alert.showAndWait();

                        menuTableViewDisplay();
                        menuTotalPriceLabelDisplay();
                    }
                    else {
                        alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Payment cancelled");
                        alert.showAndWait();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Handle and validate remove order action
    public void menuRemoveButtonOnAction(ActionEvent event) {
        if (OID == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        }
        else {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            //Fetch order data to return the quantity to item stock
            String fetchOrder = "SELECT Item_ID, Quantity FROM Orders WHERE Order_ID = " + OID;
            String removeOrder = "DELETE FROM Orders WHERE Order_ID = " + OID;

            try {
                Statement statement = connectDB.createStatement();
                ResultSet resultSet = statement.executeQuery(fetchOrder);

                if (resultSet.next()) {
                    int itemID = resultSet.getInt("Item_ID");
                    int quantity = resultSet.getInt("Quantity");

                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setTitle("Confirmation");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Are you sure you want to remove this order?");
                    Optional<ButtonType> result = alert1.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Statement statement1 = connectDB.createStatement();
                        statement1.executeUpdate(removeOrder);

                        int currentStock = 0;
                        String getCurrentStock = "SELECT Stock FROM Items WHERE Item_ID = '" + itemID + "'";
                        Statement statement2 = connectDB.createStatement();
                        ResultSet resultSet1 = statement2.executeQuery(getCurrentStock);

                        if (resultSet1.next()) {
                            currentStock = resultSet1.getInt("Stock");
                        }

                        int updatedStock = currentStock + quantity;
                        String updateStock = "UPDATE Items SET Stock = '" + updatedStock + "' WHERE Item_ID = '" + itemID + "'";
                        Statement statement3 = connectDB.createStatement();
                        statement3.executeUpdate(updateStock);

                        menuTableViewDisplay();
                        menuTotalPriceLabelDisplay();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Print out the receipt using JasperSoft Studio with connector and jdbc
    public void menuReceiptButtonOnAction(ActionEvent event) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        HashMap map = new HashMap();
        map.put("getReceipt", data.User_ID);

        try {
            JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\user\\IdeaProjects\\JavaFX\\src\\main\\resources\\com\\example\\javafx\\report.jrxml");
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, map, connectDB);

            JasperViewer.viewReport(jPrint, false);

            clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Clear all values
    public void clear() {
        total = 0;
        change = 0;
        amount = 0;
        ordersGetTotal();
        menuAmountTextField.setText("");
        menuChangeLabel.setText("$0.0");
        menuTableViewDisplay();
    }

    //Switch between dashboards
    public void itemsButtonOnAction(ActionEvent event) {
        ordersAnchorPane.setVisible(false);
        itemsMenuAnchorPane.setVisible(true);
    }

    //Switch between dashboards
    public void ordersButtonOnAction(ActionEvent event) {
        itemsMenuAnchorPane.setVisible(false);
        ordersAnchorPane.setVisible(true);
    }

    //Set up types of item for filter
    public void setTypeComboBox() {
        List<String> inventoryTL = new ArrayList<>();

        for(String data: inventoryTypeList) {
            inventoryTL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(inventoryTL);
        typeComboBox.setItems(listData);

        typeComboBox.setOnAction(event -> {
            selectedType = (String) typeComboBox.getSelectionModel().getSelectedItem();
            selectedItemsMenuDisplay();
        });
    }

    //Retrieve text in search text field
    public void searchTextFieldOnAction(ActionEvent event) {
        String searchText = searchTextField.getText();
        searchItems(searchText);
    }

    //Initialize all necessary display and action
    public void initialize(String username) {
        userNameLabel.setText(username);
        itemsMenuDisplay();
        menuTableViewDisplay();
        getUID();
        menuTotalPriceLabelDisplay();
        setTypeComboBox();
    }
}
