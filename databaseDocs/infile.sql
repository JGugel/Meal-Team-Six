USE Inventory_System;

LOAD DATA LOCAL INFILE "C:/inventorySystem/databaseDocs/Category.csv"
INTO TABLE Category
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
ESCAPED BY '"'
LINES TERMINATED BY '\r\n'
