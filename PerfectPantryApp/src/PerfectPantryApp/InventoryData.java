package PerfectPantryApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
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
    protected static int productID = 0;

    protected String upc = "";
    protected static double usage;
    protected static double size;
    protected static String uom;
    protected java.sql.Date sqlExp = null;

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
            Statement st = (Statement) conn.createStatement();

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
        DataValidation validData = new DataValidation(data);
        if (!validateData(validData)) {
            return false;
        }
        boolean successfulInsert = runInsertQuery();
        return successfulInsert;
    }

    //method called to initiate edit
    public boolean EditInventory(String[] data) {
        DataValidation validData = new DataValidation(data);
        boolean updatedSuccefully = true;
        if (!validateData(validData)) {
            return false;
        }
        updatedSuccefully = runUpdateQuery();
        return updatedSuccefully;
    }

    //helper method to run insert query
    private boolean runInsertQuery() {
        try (Connection conn = JDBC.getConnection()) {
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
        String dateString = "Null";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");

        if (sqlExp != null) {
            dateString = "'" + formatter.format(sqlExp) + "'";
        }
        String sqlUpdate = "UPDATE inventory_list SET prod_size=" + size
                + ", uom='" + uom + "', use_by=" + dateString + ", avg_usage="
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
    //called from GUI
    public String ValidateUPC(String interfaceUpc) {
        DataValidation data = new DataValidation();
        String validationMessage = data.validateUPC(interfaceUpc);
        if (validationMessage.equals("NotInvalid")) {
            upc = data.getUPC();
            if (runUPCQuery(upc)) {
                return "valid";
            } else {
                return "notFound";
            }
        }
        return validationMessage;
    }

    //runs a upc query should be used by all methods that need a upc check
    private boolean runUPCQuery(String upc) {
        //connect to database
        try (Connection conn = JDBC.getConnection()) {
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
    boolean adjustInventory(String[] data) {
        double prod = 0;
        double use = 0;
        boolean updatedSuccefully;
        DataValidation validData = new DataValidation(data);
        if (!validateData(validData)) {
            return false;
        }    
        String query = "select i.prod_size, i.avg_usage from "
                + "inventory_list i where ProductID=" + productID;
        
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                prod = rs.getDouble("prod_size");
                use = rs.getInt("avg_usage");
            }
           
            updatedSuccefully = updateInventoryList();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
   
    //actually updates the invenotry
    private boolean updateInventoryList() {
        boolean updated = false;

        String sqlUpdate = "update inventory_list set prod_size=" + size + ", avg_usage="
                + usage + " where productId=" + productID;
        try (Connection conn = JDBC.getConnection()) {
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

    private boolean validateData(DataValidation data) {
        boolean correctSize = data.checkValidSize();
        boolean correctUOM = data.checkValidUOM();
        boolean correctDate = data.checkValidDate();
        boolean correctUsage = data.CheckValidUsage();
        if (correctSize && correctUOM && correctDate && correctUsage) {
            setFields(data);
            return true;
        } else {
            return false;
        }
    }

    private void setFields(DataValidation data) {
        usage = data.getUsage();
        size = data.getSize();
        uom = data.getUOM();
        sqlExp = data.getExpiration();
    }
}
