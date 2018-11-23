import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Statement;

/**
 *
 * @author Josh
 */
public class JDBC {
	protected DefaultTableModel tModel = new DefaultTableModel(
			new String[] { "Item Number", "upc", "name", "size", "category", "expiration" }, 0);

	// Reference:
	// http://www.mysqltutorial.org/connecting-to-mysql-using-jdbc-driver/
	public void GetConnection(String s) {
		Connection conn = null;
		Statement st = null;
		try {
			// db parameters please do not alter the url user and password are ok to alter
			String url = "jdbc:mysql://localhost:3306/inventory_system?zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false";
			String user = "root";
			String password = "";

			// create a connection to the database
			conn = DriverManager.getConnection(url, user, password);
			String query = "";
			//switch case to perform different searches from database
			switch (s) {
			case "default":
				query = " select p.upc, p.invName, i.prod_size, c.categoryName, i.use_by\r\n"
						+ " from inventory_list i inner join product p on p.ProductID= i.ProductID\r\n"
						+ " inner join category c  on c.catCode=p.Category\r\n" + " ORDER by p.invName;";
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
				String category = rs.getString("categoryName");
				String expiration = rs.getString("use_by");
				tModel.addRow(new Object[] { number, upc, name, size, category, expiration }); //applies data to table model
				count++;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
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
