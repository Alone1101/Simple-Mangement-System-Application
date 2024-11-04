-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 20, 2024 at 02:39 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `retail`
--

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `Item_ID` int(11) NOT NULL,
  `ItemName` varchar(50) NOT NULL,
  `Type` varchar(50) NOT NULL,
  `Stock` int(11) NOT NULL,
  `Price` double NOT NULL,
  `Availability` tinyint(1) NOT NULL,
  `Date` date NOT NULL,
  `Image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`Item_ID`, `ItemName`, `Type`, `Stock`, `Price`, `Availability`, `Date`, `Image`) VALUES
(1, 'Lays (Original)', 'Snacks', 19, 5.49, 1, '2024-04-13', 'C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\lays.jpeg'),
(2, '100 plus 1.5L (Original)', 'Beverages', 99, 3.49, 1, '2024-04-14', 'C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\100plus.jpeg'),
(3, 'Massimo (Original)', 'Groceries', 30, 2.99, 1, '2024-04-14', 'C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\massimo.jpeg'),
(4, '3A battery', 'Household products', 9, 12.99, 1, '2024-04-14', 'C:\\\\Users\\\\user\\\\Documents\\\\Comp1206\\\\CW\\\\CW_Submission_Wong_Jin_Xuan\\\\Images\\\\3A battery.jpeg');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `Order_ID` int(11) NOT NULL,
  `User_ID` int(11) NOT NULL,
  `Item_ID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `TotalPrice` double NOT NULL,
  `Date` date NOT NULL,
  `IsPaid` tinyint(1) NOT NULL,
  `IsDelivered` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`Order_ID`, `User_ID`, `Item_ID`, `Quantity`, `TotalPrice`, `Date`, `IsPaid`, `IsDelivered`) VALUES
(1, 4, 4, 1, 12.99, '2024-04-17', 1, 1),
(5, 1, 1, 1, 5.49, '2024-04-20', 1, 0),
(6, 1, 2, 1, 3.49, '2024-04-20', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `receipt`
--

CREATE TABLE `receipt` (
  `Receipt_ID` int(11) NOT NULL,
  `User_ID` int(11) NOT NULL,
  `Total` double NOT NULL,
  `Date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `receipt`
--

INSERT INTO `receipt` (`Receipt_ID`, `User_ID`, `Total`, `Date`) VALUES
(1, 4, 12.99, '2024-04-17'),
(4, 1, 8.98, '2024-04-20');

-- --------------------------------------------------------

--
-- Table structure for table `useraccounts`
--

CREATE TABLE `useraccounts` (
  `User_ID` int(11) NOT NULL,
  `First_Name` varchar(50) NOT NULL,
  `Last_Name` varchar(50) NOT NULL,
  `UserName` varchar(50) NOT NULL,
  `Address` varchar(256) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `Role` enum('User','Admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `useraccounts`
--

INSERT INTO `useraccounts` (`User_ID`, `First_Name`, `Last_Name`, `UserName`, `Address`, `Password`, `Role`) VALUES
(1, 'Adam', 'Lambert', 'Adam', 'Adam Lambert, Direct Management Group, Inc., 8332 Melrose Avenue, Top Floor, Los Angeles, CA 90069, USA', 'Adam0129', 'User'),
(2, 'Eunice', 'Ling', 'EuniceLWX', '37, Jalan Sutera Kuning, Taman Sutera, 81200, Johor Bahru, Johor', 'Eunice0418', 'Admin'),
(4, 'Robert', 'Downey Junior', 'Tony Stark', '916 West Burbank Blvd. Suite #206C Burbank, CA 91506', 'iamironman', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`Item_ID`),
  ADD UNIQUE KEY `ItemName` (`ItemName`),
  ADD UNIQUE KEY `Image` (`Image`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`Order_ID`);

--
-- Indexes for table `receipt`
--
ALTER TABLE `receipt`
  ADD PRIMARY KEY (`Receipt_ID`);

--
-- Indexes for table `useraccounts`
--
ALTER TABLE `useraccounts`
  ADD PRIMARY KEY (`User_ID`),
  ADD UNIQUE KEY `UserName` (`UserName`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `Item_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `Order_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `receipt`
--
ALTER TABLE `receipt`
  MODIFY `Receipt_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `useraccounts`
--
ALTER TABLE `useraccounts`
  MODIFY `User_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
