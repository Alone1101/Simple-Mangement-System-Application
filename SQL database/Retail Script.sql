Create database IF NOT EXISTS Retail;

Use Retail;

Create table IF NOT EXISTS UserAccounts (
	User_ID int auto_increment primary key,
	First_Name varchar (50) not null,
    Last_Name varchar (50) not null,
    UserName varchar (50) not null unique,
    Address varchar (256) not null,
    Password varchar (50) not null,
    Role ENUM('User', 'Admin') not null
);

Create table IF NOT EXISTS Items (
	Item_ID int auto_increment primary key,
    ItemName varchar (50) not null unique,
    Type varchar(50) not null,
    Stock int not null,
    Price double not null,
    Availability boolean not null,
    Date date not null,
    Image varchar(100) not null unique
);

Create table IF NOT EXISTS Orders (
	Order_ID int auto_increment primary key,
    User_ID int not null,
    Item_ID int not null,
    Quantity int not null,
    TotalPrice double not null,
    Date date not null,
    IsPaid boolean not null,
    IsDelivered boolean not null
);

Create table IF NOT EXISTS Receipt (
	Receipt_ID int auto_increment primary key,
	User_ID int not null,
    Total double not null,
    Date date not null
);

Insert into UserAccounts (First_Name, Last_Name, UserName, Address, Password, Role) VALUES
('Adam', 'Lambert', 'Adam', 'Adam Lambert, Direct Management Group, Inc., 8332 Melrose Avenue, Top Floor, Los Angeles, CA 90069, USA', 'Adam0129', 'User'),
('Eunice', 'Ling', 'EuniceLWX', '37, Jalan Sutera Kuning, Taman Sutera, 81200, Johor Bahru, Johor', 'Eunice0418', 'Admin');