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
                new String[]{"#", "upc", "name", "size", "uom", "category", "expiration"}, 0);
        try {
            this.conn = JDBC.getConnection2();
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = " select p.upc, p.invName, i.prod_size,i.uom, c.categoryName, i.use_by\r\n"
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
                tModel.addRow(new Object[]{number, upc, name, size, uom, category, expiration}); //applies data to table model
                count++;
            }
          conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    boolean AddInventory(PerfectPantryGUI aThis, String[] data) {
        String upc;
        double quantity;
        double avgUse;
        String uom;
        SimpleDateFormat dateFormat;
        java.sql.Date sqlExp=null;
        
        //parse data values
        if (!(data[3].isEmpty())) {
            try {
                dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                java.util.Date exp;
                exp = dateFormat.parse(data[3]);
                sqlExp = new java.sql.Date(exp.getTime());
            } catch (ParseException ex) {
                Logger.getLogger(PerfectPantryGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {
            upc = data[0];
            quantity = Double.parseDouble(data[1]);
            avgUse = Double.parseDouble(data[4]);
            uom = data[2];     
        } catch (NumberFormatException ex) {
            Logger.getLogger(PerfectPantryGUI.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
            return false;
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
            String query ="select productID from product where upc=" + upc;
            ResultSet rs = stmt.executeQuery(query);
            Integer candidateId=0;
           
            if (rs.next()) {//looking for an item
                //if verified, add item
                candidateId = rs.getInt("productID");
                query="";
                
                System.out.println(candidateId + " "+quantity+" "+uom+ " " + sqlExp+ " " +avgUse);
                query = "insert into inventory_list (productID,prod_size,uom,use_by, avg_usage) values(?,?,?,?,?);";
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, candidateId);
                pstmt.setDouble(2, quantity);
                pstmt.setString(3, uom);
                if (sqlExp!=null) {
                    pstmt.setDate(4, sqlExp);
                } else {
                    //http://www.java2s.com/Tutorials/Java/JDBC/Insert/Set_NULL_date_value_to_database_in_Java.htm
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setDouble(5, avgUse);
                pstmt.execute();
                conn.close();
                
            } else {//upc not found
               JOptionPane.showMessageDialog(null, "UPC not found");
               return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
}

   

}
