package com.example.javafx;

import java.sql.Date;

public class ordersData {
    private Integer Order_ID;
    private Integer Item_ID;
    private Integer User_ID;
    private Integer Quantity;
    private Double TotalPrice;
    private Date date;
    private String ItemName;
    public Boolean IsPaid;
    public Boolean IsDelivered;
    private String UserName;
    private String Address;

    public ordersData(Integer Order_ID, Integer Item_ID, Integer User_ID, Integer Quantity, Double TotalPrice, Date date, String ItemName, Boolean IsPaid, Boolean IsDelivered) {
        this.Order_ID = Order_ID;
        this.Item_ID = Item_ID;
        this.User_ID = User_ID;
        this.Quantity = Quantity;
        this.TotalPrice = TotalPrice;
        this.date = date;
        this.ItemName = ItemName;
        this.IsPaid = IsPaid;
        this.IsDelivered = IsDelivered;
    }

    public ordersData(String UserName, String ItemName, Integer Quantity, String Address, Boolean IsDelivered) {
        this.UserName = UserName;
        this.ItemName = ItemName;
        this.Quantity = Quantity;
        this.Address = Address;
        this.IsDelivered =IsDelivered;
    }

    public Integer getOrder_ID() {
        return Order_ID;
    }

    public Integer getItem_ID() {
        return Item_ID;
    }

    public Integer getUser_ID() {
        return User_ID;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public Date getDate() {
        return date;
    }

    public String getItemName() {
        return ItemName;
    }

    public Boolean getIsPaid() {
        return IsPaid;
    }

    public Boolean getIsDelivered() {
        return IsDelivered;
    }

    public String getUserName() {
        return UserName;
    }

    public String getAddress() {
        return Address;
    }

    public void setIsPaid(Boolean IsPaid) {
        this.IsPaid = IsPaid;
    }

    public void setIsDelivered(Boolean IsDelivered) {
        this.IsDelivered = IsDelivered;
    }
}
