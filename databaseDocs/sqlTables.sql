Drop schema Inventory_System;

CREATE DATABASE Inventory_System;

USE Inventory_System;

CREATE TABLE Category(
	catCode int(3) NOT NULL,
	categoryName varchar(100) NOT NULL,
	PRIMARY KEY (catCode)
);

/*remember to add a auto incrementer
 once you find the largest number*/
CREATE TABLE Product(
	ProductID int NOT NULL,
	UPC int(12) NOT NULL,
	invName varchar(100) NOT NULL,
	Category int(3) NOT NULL,
	PRIMARY KEY (ProductID),
	FOREIGN KEY (Category) REFERENCES Category(catCode)
);

CREATE TABLE Nutrition(
	ProductID int NOT NULL, 
	Nut_Code int NOT NULL,
	Nutr_name varchar(15) NOT NULL,
	nut_val double(6,2) NOT NULL,
	uom char(1) NOT NULL,
	PRIMARY KEY (ProductID, Nut_Code),
	FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Serving_size(
	ProductID int NOT NULL,
	ServingSize double(4,2) NOT NULL,
	uom char(1) NOT NULL,
	PRIMARY KEY (ProductID)
);

CREATE TABLE Inventory_List(
	ProductID int NOT NULL,
	prod_size double NOT NULL,
	uom varchar(6) NOT NULL,
	use_by date,
	avg_usage double (4,2) NOT NULL,
	PRIMARY KEY (ProductID)
);

