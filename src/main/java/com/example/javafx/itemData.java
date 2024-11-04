package com.example.javafx;

import java.sql.Date;

public class itemData {
    private Integer Item_ID;
    private String ItemName;
    private String Type;
    private Integer Stock;
    private Double Price;
    private Boolean Availability;
    private Date Date;
    private String Image;

    public itemData(Integer Item_ID, String ItemName, String Type, Integer Stock, Double Price, Boolean Availability, Date Date, String Image) {
        this.Item_ID = Item_ID;
        this.ItemName = ItemName;
        this.Type = Type;
        this.Stock = Stock;
        this.Price = Price;
        this.Availability = Availability;
        this.Date = Date;
        this.Image = Image;
    }

    public itemData(Integer Item_ID, String ItemName, Double Price, String Image) {
        this.Item_ID = Item_ID;
        this.ItemName = ItemName;
        this.Price = Price;
        this.Image = Image;
    }

    public Integer getItem_ID() {
        return Item_ID;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getType() {
        return Type;
    }

    public Integer getStock() {
        return Stock;
    }

    public Double getPrice() {
        return Price;
    }

    public Boolean getAvailability() {
        return Availability;
    }

    public Date getDate() {
        return Date;
    }

    public String getImage() {
        return Image;
    }
}