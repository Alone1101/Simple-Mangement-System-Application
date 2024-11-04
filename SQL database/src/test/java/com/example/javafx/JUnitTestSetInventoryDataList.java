package com.example.javafx;

import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class JUnitTestSetInventoryDataList {
    private DatabaseConnection mockConnectNow;
    private Connection mockConnectDB;
    private ResultSet mockResultSet;
    private PreparedStatement mockStatement;

    @Before
    public void setUp() throws Exception {
        mockConnectNow = Mockito.mock(DatabaseConnection.class);
        mockConnectDB = Mockito.mock(Connection.class);

        Mockito.when(mockConnectNow.getConnection()).thenReturn(mockConnectDB);

        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockResultSet.next()).thenReturn(true, false);
        Mockito.when(mockResultSet.getInt("Item_ID")).thenReturn(1);
        Mockito.when(mockResultSet.getString("ItemName")).thenReturn("Lays (Original)");
        Mockito.when(mockResultSet.getString("Type")).thenReturn("Snacks");
        Mockito.when(mockResultSet.getInt("Stock")).thenReturn(17);
        Mockito.when(mockResultSet.getDouble("Price")).thenReturn(5.49);
        Mockito.when(mockResultSet.getBoolean("Availability")).thenReturn(true);
        Mockito.when(mockResultSet.getDate("Date")).thenReturn(java.sql.Date.valueOf("2024-04-13"));
        Mockito.when(mockResultSet.getString("Image")).thenReturn("C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\lays.jpeg");

        mockStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockStatement.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);

        Mockito.when(mockConnectDB.createStatement()).thenReturn(mockStatement);
    }

    @Test
    public void testSetInventoryDataList() {
        AdminDashboardController controller = new AdminDashboardController();
        controller.setDatabaseConnection(mockConnectNow);

        ObservableList<itemData> result = controller.setInventoryDataList();

        assertEquals(1, (int) result.get(0).getItem_ID());
        assertEquals("Lays (Original)", result.get(0).getItemName());
        assertEquals("Snacks", result.get(0).getType());
        assertEquals(17, (int)result.get(0).getStock());
        assertEquals(5.49, result.get(0).getPrice(), 0.001);
        assertEquals(true, result.get(0).getAvailability());
        assertEquals("C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\lays.jpeg", result.get(0).getImage());
    }
}
