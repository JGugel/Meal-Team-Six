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

/**
 *
 * @author Michelle
 */
public class InventoryData {

    protected InventoryTableModel tModel = null;
    protected Statement st = null;
    protected static String upc = "";
    protected static int productID = 0;
    protected static double usage = 0;
    protected static double size;
    protected static String uom;
    protected java.sql.Date sqlExp = null;
    boolean validUPC = false;

    public InventoryTableModel GetModel() {
        return tModel;
    }

    //sets the table data for home screen
    public void SetTable(String orderBy, String selectedCategories) {
        String query = buildQuery(orderBy, selectedCategories);
        tModel = new InventoryTableModel();
        try (Connection conn = JDBC.getConnection()) {

            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            st = (Statement) conn.createStatement();

            ResultSet rs = null;

            rs = st.executeQuery(query); //performs query

            while (rs.next()) { //gets string from db
                String upcDisplay = rs.getString("UPC");
                String name = rs.getString("invName");
                String sizeDisplay = rs.getString("prod_size");
                String uomDisplay = rs.getString("uom");
                String category = rs.getString("categoryName");
                String expiration = rs.getString("use_by");
                String usageDisplay = rs.getString("avg_usage");
                tModel.addInventoryItem(new InventoryItem(upcDisplay, name, sizeDisplay,
                        uomDisplay, category, expiration, usageDisplay)); //applies data to table model
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //builds the query to propogate the table
    private String buildQuery(String orderBy, String selectedCategories) {
        String query = " select p.upc, p.invName, i.prod_size,i.uom, c.categoryName, i.use_by, i.avg_usage\r\n"
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
        boolean correctUsage = validateUsage(data[4]);

        if (!correctSize || !correctUOM || !correctDate || !correctUsage) {
            return false;
        }
        boolean successfulInsert = runInsertQuery();
        return successfulInsert;
    }

    //method called to initiate edit
    public boolean EditInventory(String[] data) {
        boolean updatedSuccefully = true;
        boolean correctSize = validateSize(data[1]);
        boolean correctUOM = validateUOM(data[2]);
        boolean correctDate = validateDate(data[3]);
        boolean correctUsage = validateUsage(data[4]);

        if (!correctSize || !correctUOM || !correctDate || !correctUsage) {
            return false;
        }

        updatedSuccefully = runUpdateQuery();
        return updatedSuccefully;
    }

    //helper method to run insert query
    private boolean runInsertQuery() {
        try(Connection conn=JDBC.getConnection()){
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));

            String query = "insert into inventory_list (productID,prod_size,uom,"
                    + "use_by, avg_usage, quantity) values(?,?,?,?,?,?);";

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
            pstmt.setDouble(5, usage);
            pstmt.setInt(6, 1);
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

    //todo josh - for the edit button, editinventory
    private boolean runUpdateQuery() {
        boolean updated = false;

        String sqlUpdate = "UPDATE inventory_list SET prod_size=" + size
                + ", uom='" + uom + "', use_by='" + sqlExp + "', avg_usage="
                + usage + " where ProductID=" + productID;
        try (Connection conn = JDBC.getConnection()) {

            Statement stmt = conn.createStatement();
            int record = stmt.executeUpdate(sqlUpdate);
            if (record > 0) {
                updated = true;
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            updated = false;
        }

        return updated;
    }

    //check to see if a record already exists in inventory
    public boolean CheckExists() {
        boolean exists = false;
        try (Connection conn = JDBC.getConnection()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            Statement stmt = conn.createStatement();
            String query = "select * from Inventory_List where productID=" + productID + ";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {//looking for an item
                exists = true;
            } else {//upc not found
                exists = false;
            }
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            exists = false;
        }
        return exists;
    }

    public boolean deleteRecord(String upc) {
        boolean deleted = false;
        if (!runUPCQuery(upc)) {
            deleted = false;
        }
        String query = "DELETE from inventory_List "
                + "WHERE productID=?";

        try (Connection conn = JDBC.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, productID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                deleted = true;
            }
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            deleted = false;
        }
        return deleted;
    }

    //helper method to validate and set usage
    private boolean validateUsage(String tempQuant) {
        usage = 0;
        if (tempQuant.isEmpty()) {
            usage = 1.0;

        } else {//usage defaults to one
            try {
                usage = Double.parseDouble(tempQuant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Usage should be a numeric value");
                return false;
            }
        }
        return true;
    }

    //helper method to validate and set date
    private boolean validateDate(String tempDate) {
        SimpleDateFormat dateFormat;
        sqlExp = null;
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
        size = 0;
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
        uom = "";
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
    public String ValidateUPC(String interfaceUpc) {
        upc = interfaceUpc;
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
        try(Connection conn=JDBC.getConnection()) {
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
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    //validates information and gets quantities to add to
    boolean incrementInventory(String prodSize, String usage) {
        boolean updatedSuccefully = true;
        boolean correctSize = validateSize(prodSize);
        boolean correctUsage= validateUsage(usage);
        double prod = 0;
       double use = 0;
        if (correctSize && correctUsage) {
            String query = "select i.prod_size, i.avg_usage from "
                    + "inventory_list i where ProductID=" + productID;
            try(Connection conn=JDBC.getConnection()){
            
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    prod = rs.getDouble("prod_size");
                    use = rs.getInt("avg_usage");
                }
                size += prod;
                usage += use;
                updatedSuccefully = updateInventoryList();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex);
                Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        return updatedSuccefully;
    }

    //actually updates the invenotry
    private boolean updateInventoryList() {
        boolean updated = false;

        String sqlUpdate = "update inventory_list set prod_size=" + size + ", avg_usage="
                + usage + " where productId=" + productID;
        try(Connection conn=JDBC.getConnection()) {
            Statement stmt = conn.createStatement();
            int record = stmt.executeUpdate(sqlUpdate);
            if (record > 0) {
                updated = true;
            }
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            updated = false;
        }

        return updated;
    }

}
