/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import static PerfectPantryApp.InventoryData.ListID;
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
public class productData {

    private String productName = "", uom = "", upc = "";
    private int category = 0, productID = 0;
    private double servingSize = 0, protein = 0, fat = 0, calories = 0;

    boolean AddProductToInventory(String upc, String[] prodData, boolean isEmpty) {
        this.upc = upc;
        productID = 0;
        boolean addedCorrectly;
        if (!verifyDetails(prodData, isEmpty)) {
            return false;
        }
        addedCorrectly = insertIntoProduct();
        if (isEmpty && addedCorrectly) {
            JOptionPane.showMessageDialog(null, "Item add to master list successfully");
            return addedCorrectly;
        } else {
            //  insertIntoServingSize();
        }
        return addedCorrectly;
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

    private boolean verifyDetails(String[] data, boolean isEmpty) {
        boolean validData = false;
        DataValidation dv = new DataValidation();
        category = dv.getCategory(data[1]);
        uom = data[3];

        if (dv.validateName(data[0])) {
            validData = true;
            productName = dv.getName();
        } else {
            return false;
        }
        if (!isEmpty) {
            if (dv.validateServingSize(data[2])) {
                validData = true;
                servingSize = dv.getServingSize();
            } else {
                return false;
            }

            if (dv.validateCalories(data[4])) {
                validData = true;
                calories = dv.getCalories();
            } else {
                return false;
            }

            if (dv.validateProtein(data[5])) {
                validData = true;
                protein = dv.getProtein();
            } else {
                return false;
            }

            if (dv.validateFat(data[6])) {
                validData = true;
                fat = dv.getFat();
            } else {
                return false;
            }
        }

        return validData;
    }

}
