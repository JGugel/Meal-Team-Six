Drop schema Inventory_System;

CREATE DATABASE Inventory_System;

USE Inventory_System;

CREATE TABLE Category(
	catCode int(3) NOT NULL,
	categoryName varchar(60) NOT NULL,
	PRIMARY KEY (catCode)
);

/*remember to add a auto incrementer
 once you find the largest number*/
CREATE TABLE Product(
	ProductID int NOT NULL,
	UPC int(12) NOT NULL,
	invName varchar(80) NOT NULL,
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
	PRIMARY KEY (ProductID),
	FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Inventory_List(
	ProductID int NOT NULL,
	prod_size double NOT NULL,
	uom varchar(6) NOT NULL,
	use_by date,
	avg_usage double (4,2) NOT NULL,
	PRIMARY KEY (ProductID),
	FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Usage_data(
	ProductID int NOT NULL,
	usageNum int NOT NULL AUTO_INCREMENT,
	`usage` double(4,2) NOT NULL,
	uom varchar(6) NOT NULL,
	PRIMARY KEY (usageNum),
	FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);
/*start auto increment with table update*/
CREATE TABLE List_Pointer(
	ListID int NOT NULL AUTO_INCREMENT,
	ListName varchar(40) NOT NULL,
	PRIMARY KEY(ListID)
);

CREATE TABLE Shopping_List(
	ListID int NOT NULL,
	ProductID int NOT NULL,
	cat_code int(3) NOT Null,
	ProductName varchar(80) NOT NULL,
	PRIMARY KEY(ListID, ProductID),
	FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
	FOREIGN KEY (ListID) REFERENCES List_Pointer(ListID)
);

ALTER TABLE List_Pointer AUTO_INCREMENT=100;
