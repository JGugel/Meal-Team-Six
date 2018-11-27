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
    protected String upc = "";
    protected int productID = 0;
    protected int quantity = 0;
    protected double size;
    protected String uom;
    protected java.sql.Date sqlExp = null;

    public InventoryData() throws SQLException {
        this.conn = JDBC.getConnection2();
    }

    public DefaultTableModel GetModel() {
        return tModel;
    }

    //sets the table data for home screen
    public void SetTable(String orderBy, String selectedCategories) {
        String query = buildQuery(orderBy, selectedCategories);
        tModel = new DefaultTableModel(
                new String[]{"#", "upc", "name", "size", "uom", "category", "expiration", "Quantity"}, 0);
        try {
            this.conn = JDBC.getConnection2();
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            st = (Statement) conn.createStatement();

            ResultSet rs = null;

            rs = st.executeQuery(query); //performs query

            int count = 1;
            while (rs.next()) { //gets string from db
                String number = Integer.toString(count);
                String upcDisplay = rs.getString("UPC");
                String name = rs.getString("invName");
                String sizeDisplay = rs.getString("prod_size");
                String uomDisplay = rs.getString("uom");
                String category = rs.getString("categoryName");
                String expiration = rs.getString("use_by");
                String quantityDisplay = rs.getString("Quantity");
                tModel.addRow(new Object[]{number, upcDisplay, name, sizeDisplay,
                    uomDisplay, category, expiration, quantityDisplay}); //applies data to table model
                count++;
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //builds the query to propogate the table
    private String buildQuery(String orderBy, String selectedCategories) {
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
        return query;
    }

    //method called to initiate insertion
    public boolean AddInventory(String[] data) {
        boolean correctSize = validateSize(data[1]);
        boolean correctUOM = validateUOM(data[2]);
        boolean correctDate = validateDate(data[3]);
        boolean correctQuantity = validateQuantity(data[4]);

        if (!correctSize || !correctUOM || !correctDate || !correctQuantity) {
            return false;
        }
        boolean successfulInsert = runInsertQuery();
        return successfulInsert;
    }

    //helper method to run insert query
    private boolean runInsertQuery() {
        try (Connection conn = JDBC.getConnection2()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));

            String query = "insert into inventory_list (productID,prod_size,uom,"
                    + "use_by, quantity) values(?,?,?,?,?);";

            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, productID);
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

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    //check to see if a record already exists in inventory
    public boolean CheckExists() {
        try (Connection conn = JDBC.getConnection2()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            Statement stmt = conn.createStatement();
            String query = "select * from Inventory_List where productID=" + productID + ";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {//looking for an item
                return true;
            } else {//upc not found
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //helper method to validate and set quantity
    private boolean validateQuantity(String tempQuant) {
        if (tempQuant.isEmpty()) {
            quantity = 1;

        } else {//quantity defaults to one
            try {
                quantity = Integer.parseInt(tempQuant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity should be a numeric value");
                return false;
            }
        }
        return true;
    }

    //helper method to validate and set date
    private boolean validateDate(String tempDate) {
        SimpleDateFormat dateFormat;

        //parse data values
        if (!(tempDate.isEmpty())) {
            try {
                dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                java.util.Date exp;
                exp = dateFormat.parse(tempDate);
                sqlExp = new java.sql.Date(exp.getTime());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Date must be in yyyy-mm-dd format");
                return false;
            }
        }
        return true;
    }

    //helper method to validate and set size
    private boolean validateSize(String tempSize) {
        if (tempSize.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: Size must not be empty");
            return false;
        }
        try {
            size = Double.parseDouble(tempSize);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Size should be a numeric value");
            return false;
        }
        return true;
    }

    //helper method to validate and set units
    private boolean validateUOM(String tempUOM) {
        if (tempUOM.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: Unit of measurement must not be empty");
            return false;
        }

        if (tempUOM.length() > 6) {
            JOptionPane.showMessageDialog(null, "Unit of Measurement must only be 6 characters");
            return false;
        } else {
            uom = tempUOM;
        }
        return true;
    }

    //called from GUI
    public String ValidateUPC(String upc) {
        String regex = "[0-9]+";
        boolean exists = false;
        if (upc.isEmpty()) {
            return "empty";
        }
        if (upc.length() != 12) {
            return "length";
        }
        if (!upc.matches(regex)) {
            return "notANum";
        }
        exists = runUPCQuery(upc);
        if (exists) {
            return "valid";
        } else {
            return "notFound";
        }
    }

    //runs a upc query should be used by all methods that need a upc check
    private boolean runUPCQuery(String upc) {
        //connect to database
        try (Connection conn = JDBC.getConnection2()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            Statement stmt = conn.createStatement();
            String query = "select productID from product where upc=" + upc;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {//looking for an item
                //if verified, add item
                productID = rs.getInt("productID");
            } else {//upc not found
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

}
