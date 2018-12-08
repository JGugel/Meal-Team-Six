package PerfectPantryApp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michelle, Josh
 */
public class InventoryData {

    protected InventoryTableModel tModel = null;
    protected static int productID = 0;
    protected static int category;
    protected static int ListID = 0;
    protected String upc = "";
    protected String name = "";
    protected String listName = "";
    protected static double usage;
    protected static double size;
    protected static String uom;
    protected java.sql.Date sqlExp = null;
    protected DefaultTableModel nTable = null;
   

    public InventoryTableModel GetModel() {
        return tModel;
    }

   

    //sets the table data for home screen
    public void SetTable(String createdQuery) {
        String query = createdQuery;
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

    public DefaultTableModel setNutritionalModel() {
        nTable = new DefaultTableModel(new String[]{
            "Product name", "Calories", "unit", "Protien", "unit", "Fat", "unit"
        }, 0);

        String query = "{CALL getNutrition()}";

        try (Connection conn = JDBC.getConnection()) {

            CallableStatement st = conn.prepareCall(query);

            ResultSet rs = null;

            rs = st.executeQuery(); //performs query

            while (rs.next()) { //gets string from db
                String name = rs.getString("invName");
                double protein = rs.getDouble("protein");
                double fat = rs.getDouble("fat");
                double calories = rs.getDouble("calories");
                String nUOM = rs.getString("uom");

                nTable.addRow(new Object[]{name,
                    (Math.round(calories * 100.0) / 100.0),
                    nUOM, (Math.round(protein * 100.0) / 100.0), nUOM,
                    (Math.round(fat * 100.0) / 100.0), nUOM});
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nTable;
    }

    public void buildSearchQuery(String searchType,String Keyword) {
        String appendQuery = "";
        if (searchType.equals("Search by UPC")) { 
            String upcCheck = ValidateUPC(Keyword);
            switch(upcCheck){
                case "valid":  
                    appendQuery = "WHERE p.productID=" + productID;
                    break;
                case "empty":
                    JOptionPane.showMessageDialog(null, "Invalid Input: UPC must not be empty");
                    break;
                case "length":
                    JOptionPane.showMessageDialog(null, "UPC must be a 12 digit integer");
                    break;
                case "notANum":
                    JOptionPane.showMessageDialog(null, "UPC must be a numeric value");
                    break;
                case "notFound":
                    JOptionPane.showMessageDialog(null, "No result found");
                default:
                    break;
            }
            
            //this assumes you validated UPC for proceding
//            appendQuery = "WHERE p.productID=" + productID;
        } else {
            DataValidation data = new DataValidation();
            if(data.validateName(Keyword)){
                appendQuery = "WHERE p.invName LIKE '%" + Keyword + "%'";
            } 
        }
        String query = " select p.upc, p.invName, i.prod_size,i.uom, c.categoryName, i.use_by, i.avg_usage\r\n"
                + " from inventory_list i inner join product p on p.ProductID= i.ProductID\r\n"
                + " inner join category c  on c.catCode=p.Category\r\n "
                + appendQuery;
        SetTable(query);
    }

    //builds the query to propogate the table
    public void buildQuery(String orderBy, String selectedCategories) {
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
        SetTable(query);
    }

    //method called to initiate insertion
    public boolean AddInventory(String[] data, int IncomingProductID) {
        if(productID ==0){
            productID=IncomingProductID;
        }
        if (!validateData(data)) {
            return false;
        }
        boolean successfulInsert = runInsertQuery();
        return successfulInsert;
    }

    //method called to initiate edit
    public boolean EditInventory(String[] data) {
        boolean updatedSuccefully = true;
        if (!validateData(data)) {
            return false;
        }
        updatedSuccefully = runUpdateQuery();
        return updatedSuccefully;
    }


    //method to add a shopping list item
    //Input: name, quantity, category
    public boolean AddItemSL(String[] data) {

        if (!validateShoppingList(data)) {//verifies list in another method
            return false;
        }else  if (!checkForList(data[0])) {
            return false;
        } 

        try (Connection conn = JDBC.getConnection()) {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));

            String query = "insert into shopping_list (ProductName, ListID, "
                    + "quantity, cat_code) values(?,?,?,?);";

            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, name);
            pstmt.setInt(2, ListID);
            pstmt.setDouble(3, size);
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
                 listExists=true;
                ListID = rs.getInt("ListID");
               
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
    /**
     * if the named list isn't created this
     * this list creates the list and sets
     * the list id*/    
    public boolean createShoppingList(String name) {
        boolean successfulCreate = false;
        listName = name;
        String query = "insert into list_pointer(ListName)"
                + "values('" + listName + "')";
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.prepareStatement(query);
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                ListID = rs.getInt(1);
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

    //helper method to run insert query
    private boolean runInsertQuery() {
        boolean successfulInsert = false;
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
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                successfulInsert = false;
            } else {
                successfulInsert = true;
            }
            pstmt.close();
            conn.close();
            productID=0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Oops!" + ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return successfulInsert;
    }

    //helper method to run an update for the edit button
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

    //method called to initiate deletion
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
        if (validationMessage.equals("valid")) {
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
    boolean adjustQuantity(String[] data) {
        double prod = 0;
        double use = 0;
        boolean updatedSuccefully;
        if (!validateData(data)) {
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
            size += prod;
            usage += use;
            updatedSuccefully = updateQuantity();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(InventoryData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return updatedSuccefully;
    }

    //updates the quantities only of the record
    private boolean updateQuantity() {
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

    //returns false if any data test fails, otherwise true
    private boolean validateData(String[] dataArray) {
        DataValidation data = new DataValidation();
        if (!data.validateSize(dataArray[1])) {
            return false;
        } else if (!data.validateUOM(dataArray[2])) {
            return false;
        } else if (!data.validateDate(dataArray[3])) {
            return false;
        } else if (!data.validateUsage(dataArray[4])) {
            return false;
        } else {
            setFields(data);
            return true;
        }

    }

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
            size = dv.getSize();
        }
        if (!dv.validateName(data[1])){
            return false;
        } else {
            name = dv.getName();
        }
        return true;
    }
//sets data instance variables, prepares for a query

    private void setFields(DataValidation data) {
        usage = data.getUsage();
        size = data.getSize();
        uom = data.getUOM();
        sqlExp = data.getExpiration();
    }
}
