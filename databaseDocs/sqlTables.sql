Drop schema Inventory_System;

CREATE DATABASE Inventory_System;

USE Inventory_System;

/*table to indicate category of each prodcut*/
CREATE TABLE Category(
	catCode int(3) NOT NULL,
	categoryName varchar(100) NOT NULL,
	PRIMARY KEY (catCode)
);

/*master table of product data*/
CREATE TABLE Product(
	ProductID int NOT NULL AUTO_INCREMENT,
	UPC char(12) NOT NULL,
	invName varchar(200) NOT NULL,
	Category int(3) NOT NULL,
	PRIMARY KEY (ProductID),
	FOREIGN KEY (Category) REFERENCES Category(catCode)
);

/*master table of nutrition data*/
CREATE TABLE Nutrition(
	ProductID int NOT NULL, 
	Nut_Code int NOT NULL,
	Nutr_name varchar(150) NOT NULL,
	nut_val double NOT NULL,
	uom char(5) NOT NULL,
	PRIMARY KEY (ProductID, Nut_Code),
	FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

/*master table of serving sizes*/
CREATE TABLE Serving_size(
	ProductID int NOT NULL,
	ServingSize double NOT NULL,
	uom char(5) NOT NULL,
	PRIMARY KEY (ProductID)
);

/*table to store individual's products*/
CREATE TABLE Inventory_List(
	ProductID int NOT NULL,
	prod_size double NOT NULL,
	uom varchar(6) NOT NULL,
	use_by date NULL default NULL,
	avg_usage double NULL default NULL,
	quantity int NOT Null,
	PRIMARY KEY (ProductID)
);

/*command to start incrementing*/
ALTER TABLE Product AUTO_INCREMENT=45374984;