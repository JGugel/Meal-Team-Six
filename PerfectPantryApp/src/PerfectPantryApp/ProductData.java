/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Michelle
 */
public class ProductData {

    private String productName = "", uom = "", upc = "";
    private int category = 0, productID = 0;
    private double servingSize = 0, protein = 0, fat = 0, calories = 0;

    public int getProductID() {
        return productID;
    }

    boolean AddProductToInventory(String upc, String[] prodData) {
        this.upc = upc;
        productID = 0;
        boolean productAddedCorrectly;
        if (!verifyProductDetails(prodData)) {
            return false;
        }
        productAddedCorrectly = insertIntoProduct();
        if (productAddedCorrectly) {
            JOptionPane.showMessageDialog(null, "Item added to master list successfully");
        }
        return productAddedCorrectly;
    }

    boolean addNutrition(String[] nutritionData) {
        if (!verifyNutritionDetails(nutritionData)) {
            return false;
        } else if (!insertIntoServingSize()) {
            return false;
        } else if (!insertIntoNutrition()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean insertIntoProduct() {
        boolean successfulCreate = false;
        String query = "INSERT into Product(UPC, invName,Category)"
                + "VALUES('" + upc + "'," + "'" + productName + "'," + category + ")";
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.prepareStatement(query);
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                productID = rs.getInt(1);
                successfulCreate = true;
            } else {
                successfulCreate = false;
                // throw an exception from here
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return successfulCreate;
    }

    private boolean verifyProductDetails(String[] data) {
        boolean validData = false;
        DataValidation dv = new DataValidation();
        category = dv.getCategory(data[1]);
          if (dv.validateName(data[0])) {
            validData = true;
            productName = dv.getName();
        } else {
            return false;
        }

        return validData;
    }

    private boolean verifyNutritionDetails(String[] data) {
        boolean validData = false;
        uom = data[1];
        DataValidation dv = new DataValidation();

        if (dv.validateServingSize(data[0])) {
            validData = true;
            servingSize = dv.getServingSize();
        } else {
            return false;
        }

        if (dv.validateCalories(data[2])) {
            validData = true;
            calories = dv.getCalories();
            calories *= 100;
        } else {
            return false;
        }

        if (dv.validateProtein(data[3])) {
            validData = true;
            protein = dv.getProtein();
            protein *= 100;
        } else {
            return false;
        }

        if (dv.validateFat(data[4])) {
            validData = true;
            fat = dv.getFat();
            fat *= 100;
        } else {
            return false;
        }
        return validData;
    }

    private boolean insertIntoServingSize() {
        boolean successfulCreate = false;
        String query = "INSERT into serving_size(ProductID,ServingSize,uom)"
                + "VALUES(" + productID + "," + servingSize + ",'" + uom + "')";
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.createStatement();
            int rowAdded = stmt.executeUpdate(query);
            if (rowAdded > 0) {
                successfulCreate = true;
            }
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return successfulCreate;
    }

    private boolean insertIntoNutrition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
