/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * This class deals only with nutritional data
 *
 * @author Michelle
 */
public class NutritionData {

    protected DefaultTableModel nTable = null;

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
                //this line rounds to two decimal places
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
}
