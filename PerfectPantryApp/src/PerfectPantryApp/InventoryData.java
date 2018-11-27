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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michelle
 */
public class InventoryData {

    protected DefaultTableModel tModel = null;
    protected Connection conn;
    protected Statement st = null;

    public InventoryData() throws SQLException {
        this.conn = JDBC.getConnection2();
    }

    public DefaultTableModel GetModel() {
        return tModel;
    }

    public void SetTable(String orderBy, String selectedCategories) {

        tModel = new DefaultTableModel(
                new String[]{"#", "upc", "name", "size", "uom", "category", "expiration", "Quantity"}, 0);
        try {
            this.conn = JDBC.getConnection2();
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = " select p.upc, p.invName, i.prod_size,i.uom, c.categoryName, i.use_by, i.quantity\r\n"
                + " from inventory_list i inner join product p on p.ProductID= i.ProductID\r\n"
                + " inner join category c  on c.catCode=p.Category\r\n";

        if (selectedCategories.length() > 0) {
            query += " WHERE c.categoryName IN (" + selectedCategories + ")";
        }
        //switch case to perform different searches from database
        switch (orderBy) {
            case "default":
                query += " ORDER by p.upc;";
                break;
            case "Categories":
                query += " ORDER by c.categoryName;";
                break;
            case "Name":
                query += " ORDER by p.invName;";
                break;
            case "date":
                query += " ORDER by i.use_by;";
                break;
            default:
                break;
        }

        try {
            st = (Statement) conn.createStatement();

            ResultSet rs = null;

            rs = st.executeQuery(query); //performs query

            int count = 1;
            while (rs.next()) { //gets string from db
                String number = Integer.toString(count);
                String upc = rs.getString("UPC");
                String name = rs.getString("invName");
                String size = rs.getString("prod_size");
                String uom = rs.getString("uom");
                String category = rs.getString("categoryName");
                String expiration = rs.getString("use_by");
                String quantity = rs.getString("Quantity");
                tModel.addRow(new Object[]{number, upc, name, size, uom, category, expiration, quantity}); //applies data to table model
                count++;
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean AddInventory(String[] data) {
        String upc;
        int quantity;
        double size;
        String uom;
        SimpleDateFormat dateFormat;
        java.sql.Date sqlExp = null;
        //checking for empty where there should be values
        if (data[0].isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: UPC must not be empty");
            return false;
        }
        if (data[1].isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: Size must not be empty");
            return false;
        }
        if (data[2].isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: Unit of measurement must not be empty");
            return false;
        }
        //parse data values
        if (!(data[3].isEmpty())) {
            try {
                dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                java.util.Date exp;
                exp = dateFormat.parse(data[3]);
                sqlExp = new java.sql.Date(exp.getTime());
            } catch (ParseException ex) {
                Logger.getLogger(PerfectPantryGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Date must be in yyyy-mm-dd format");
                return false;
            }
        }
        upc = data[0];
        try{
            Long temp= Long.parseLong(upc);
        }catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(null, "UPC should be a numeric value");
            return false; 
        }
        try {
            size = Double.parseDouble(data[1]);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Size should be a numeric value");
            return false;
        }
        
        uom = data[2];
        if (uom.length() > 6) {
            JOptionPane.showMessageDialog(null, "Unit of Measurement must only be 6 characters");
            return false;
        }
        
        if (data[4].isEmpty() ) {
            quantity = 1;

        } else {//quantity defaults to one
            try {
                quantity = Integer.parseInt(data[4]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity should be a numeric value");
                return false;
            }
        }

        //connect to database
        try (Connection conn = JDBC.getConnection2()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            Statement stmt = conn.createStatement();

            //verify UPC
            if (upc.length() != 12) {
                JOptionPane.showMessageDialog(null, "UPC must be a 12 digit integer");
                return false;
            }
            String query = "select productID from product where upc=" + upc;
        
            ResultSet rs = stmt.executeQuery(query);
            Integer candidateId = 0;

            if (rs.next()) {//looking for an item
                //if verified, add item
                candidateId = rs.getInt("productID");
                query = "";

                query = "insert into inventory_list (productID,prod_size,uom,use_by, quantity) values(?,?,?,?,?);";
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, candidateId);
                pstmt.setDouble(2, size);
                pstmt.setString(3, uom);
                if (sqlExp != null) {
                    pstmt.setDate(4, sqlExp);
                } else {
                    //http://www.java2s.com/Tutorials/Java/JDBC/Insert/Set_NULL_date_value_to_database_in_Java.htm
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setInt(5, quantity);
                pstmt.execute();
                conn.close();

            } else {//upc not found
                JOptionPane.showMessageDialog(null, "UPC not found");
                return false;
            }
        } catch (SQLException ex) {
           System.out.println(ex);
           return false;
        }
        return true;
    }

}
