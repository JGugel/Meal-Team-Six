package PerfectPantryApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Josh, Michelle
 */
// Reference:
// http://www.mysqltutorial.org/connecting-to-mysql-using-jdbc-driver/
//https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
public class JDBC {
    protected  DefaultTableModel tModel=null;
    
    public DefaultTableModel GetModel() {
        return tModel;
    }
    /**
     * Get database connection
     *
     * @return a Connection object
     * @throws SQLException
     */
    public  void GetConnection(String s) {
        Connection conn = null;
        Statement st = null;
        tModel= new DefaultTableModel(
    			new String[] { "#", "upc", "name", "size","uom", "category", "expiration" }, 0);
        try (FileInputStream f = new FileInputStream("db.properties")) {
            //load the properties file
            Properties pros = new Properties();
            pros.load(f);
 
            // assign db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
            String query = " select p.upc, p.invName, i.prod_size,i.uom, c.categoryName, i.use_by\r\n"
                    + " from inventory_list i inner join product p on p.ProductID= i.ProductID\r\n"
                    + " inner join category c  on c.catCode=p.Category\r\n" ;
            //switch case to perform different searches from database
            switch (s) {
                case "default":
                    query +=" ORDER by p.upc;";
                    break;
                case "Categories":
                    query +=" ORDER by c.categoryName;";
                    break;
                case "Name":
                    query +=  " ORDER by p.invName;";
                    break;
                case "date":
                    query += " ORDER by i.use_by;";
                case "bakingCategory":
                    query += " WHERE c.categoryName LIKE '%Baking, Herbs, and Spices%'";
                    break;
                case "beverageCategory":
                    query += " WHERE c.categoryName LIKE '%Beverages%'";
                    break;
                case "breadsBCategory":
                    query += " WHERE c.categoryName LIKE '%Breads and Bakery%'";
                    break;
                case "dairyRCategory":
                    query += " WHERE c.categoryName LIKE '%Dairy and Refrigerated%'";
                    break;
                case "housholdSCategory":
                    query += " WHERE c.categoryName LIKE '%Household Supplies%'";
                    break;
                case "meatsPCategory":
                    query += " WHERE c.categoryName LIKE '%Meats, Poultry, and Seafood%'";
                    break;
                case "miscellaneousCategory":
                    query += " WHERE c.categoryName LIKE '%Miscellaneous%'";
                    break;
                case "pantryCategory":
                    query += " WHERE c.categoryName LIKE '%Pantry%'";
                    break;
                default:
                    break;
            }
            st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(query);//performs query
            int count = 1;
            while (rs.next()) { //gets string from db
                String number = Integer.toString(count);
                String upc = rs.getString("UPC");
                String name = rs.getString("invName");
                String size = rs.getString("prod_size");
                String uom = rs.getString("uom");
                String category = rs.getString("categoryName");
                String expiration = rs.getString("use_by");
                tModel.addRow(new Object[] { number, upc, name, size,uom, category, expiration }); //applies data to table model
                count++;
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if (st != null && conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }  
    }
}



