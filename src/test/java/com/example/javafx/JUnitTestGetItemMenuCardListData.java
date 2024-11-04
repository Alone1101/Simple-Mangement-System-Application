package com.example.javafx;

import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class JUnitTestGetItemMenuCardListData {
    private DatabaseConnection mockConnectNow;
    private Connection mockConnectDB;
    private ResultSet mockResultSet;
    private Statement mockStatement;

    @Before
    public void setUp() throws Exception {
        mockConnectNow = Mockito.mock(DatabaseConnection.class);
        mockConnectDB = Mockito.mock(Connection.class);

        Mockito.when(mockConnectNow.getConnection()).thenReturn(mockConnectDB);

        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockResultSet.next()).thenReturn(true, false);
        Mockito.when(mockResultSet.getInt("Item_ID")).thenReturn(1);
        Mockito.when(mockResultSet.getString("ItemName")).thenReturn("Lays (Original)");
        Mockito.when(mockResultSet.getDouble("Price")).thenReturn(5.49);
        Mockito.when(mockResultSet.getString("Image")).thenReturn("C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\lays.jpeg");

        mockStatement = Mockito.mock(Statement.class);
        Mockito.when(mockStatement.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);

        Mockito.when(mockConnectDB.createStatement()).thenReturn(mockStatement);
    }

    @Test
    public void testGetItemMenuCardListData() {
        UserDashboardController controller = new UserDashboardController();
        controller.setDatabaseConnection(mockConnectNow);

        ObservableList<itemData> result = controller.getItemMenuCardListData();

        assertEquals(1, (int) result.get(0).getItem_ID());
        assertEquals("Lays (Original)", result.get(0).getItemName());
        assertEquals(5.49, result.get(0).getPrice(), 0.001);
        assertEquals("C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\lays.jpeg", result.get(0).getImage());
    }
}
