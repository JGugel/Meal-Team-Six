/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michelle This class handles all data specific to the shopping list
 * tables
 */
public class ShoppingData {

    private int quantity = 0, listID = 0;
    private String shopProdName = "", listName = "";
    protected DefaultTableModel sTable = null;
    protected static int category;

    /**
     * sets the default table model for the shopping list
     *
     * @param name
     * @return
     */
    public DefaultTableModel setShoppingList(String name) {
        this.listName = name;
        sTable = new DefaultTableModel(new String[]{"UPC", "Product Name",
            "Quantity Needed", "Edit", "Delete"}, 0);
        String query = "select p.upc,s.productName, s.quantity, c.categoryName "
                + " from shopping_list s join list_pointer l on s.ListID=l.listID"
                + " join category c on s.cat_code = c.catCode"
                + " left join product p on s.ProductID = p.ProductID"
                + " where l.ListName='" + listName + "'";

        try (Connection conn = JDBC.getConnection()) {
            Statement st = (Statement) conn.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(query); //performs query
            while (rs.next()) { //gets string from db
                String prodUPC = rs.getString("upc");
                String prodName = rs.getString("productName");
                int prodQuan = rs.getInt("quantity");
                String prodCat = rs.getString("categoryName");
                if (prodUPC == null) {
                    prodUPC = "";
                }

                sTable.addRow(new Object[]{prodUPC, prodName,
                    prodQuan, prodCat}); //applies data to table model
            }
            rs.close();
            st.close();
            conn.close();
            listName = "";
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTable;
    }

    //method to check and set List
    private Boolean checkForList(String name) {
        Boolean listExists;
        String Query = "Select ListID from list_pointer where "
                + "listName='" + name + "'";
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(Query);
            if (rs.next()) {//looking for a shoppin list
                //if verified, add item
                listExists = true;
                listID = rs.getInt("ListID");

            } else {//upc not found
                listExists = false;
            }
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        if (listExists) {
            return listExists;
        } else {
            listExists = createShoppingList(name);
        }
        return listExists;
    }
    //method to add a shopping list item
    //Input: name, quantity, category

    public boolean AddItemSL(String[] data) {

        if (!validateShoppingList(data)) {//verifies list in another method
            return false;
        } else if (!checkForList(data[0])) {
            return false;
        }

        try (Connection conn = JDBC.getConnection()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));

            String query = "insert into shopping_list (ProductName, ListID, "
                    + "quantity, cat_code) values(?,?,?,?);";

            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, shopProdName);
            pstmt.setInt(2, listID);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, category);//category set in shoppinList validation
            pstmt.execute();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * if the named list isn't created this this list creates the list and sets
     * the list id
     *
     * @param name
     * @return
     */
    public boolean createShoppingList(String name) {
        boolean successfulCreate;
        listName = name;
        String query = "insert into list_pointer(ListName)"
                + "values('" + listName + "')";
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.prepareStatement(query);
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                listID = rs.getInt(1);
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

    /**
     * validates all information for the shopping List
     *
     * @param data
     * @return
     */
    private boolean validateShoppingList(String[] data) {
        DataValidation dv = new DataValidation();
        category = dv.getCategory(data[3]);
        if (category == 0) {
            JOptionPane.showMessageDialog(null, "Not a valid Category");
            return false;
        }
        if (!dv.validateQuantity(data[2])) {
            return false;
        } else {
            quantity = dv.getQuantity();
        }
        if (!dv.validateName(data[1])) {
            return false;
        } else {
            shopProdName = dv.getName();
        }
        return true;
    }
    
    public String[] getLists(){
        ArrayList<String>tempList=new ArrayList<String>();
         String query = "Select listName from list_pointer";
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.prepareStatement(query);
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()) {
                tempList.add(rs.getString("listName"));
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(tempList.isEmpty()){
            JOptionPane.showMessageDialog(null, "No lists have been created");
        }
        String[]nameList=tempList.toArray(new String[tempList.size()]);
        return nameList;
    }
}
