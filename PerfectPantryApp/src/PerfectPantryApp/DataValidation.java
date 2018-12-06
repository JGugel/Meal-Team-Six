/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author Michelle
 */
public class DataValidation {

    private static String upc;
    private static double usage;
    private static double size, servSize, calories, protein, fat;
    private static String uom, productName;
    private java.sql.Date sqlExp = null;
    private HashMap<String, Integer> categoryMap = new HashMap();

    DataValidation() {
        upc = "";
        usage = 0;
        size = 0;
        servSize = 0;
        calories = 0;
        protein = 0;
        fat = 0;
        uom = "";
        productName = "";
        categoryMap.put("Produce", 100);
        categoryMap.put("Meats, Poultry, and Seafood", 200);
        categoryMap.put("Dairy and Refrigerated", 300);
        categoryMap.put("Pantry", 400);
        categoryMap.put("Breads and Bakery", 500);
        categoryMap.put("Baking, Herbs, and Spices", 600);
        categoryMap.put("Beverages", 700);
        categoryMap.put("Household Supplies", 800);
        categoryMap.put("Miscellaneous", 900);
    }

    public int getCategory(String s) {
        return categoryMap.getOrDefault(s, 0);
    }

    public String getUPC() {
        return upc;
    }

    public double getUsage() {
        return usage;
    }

    public double getSize() {
        return size;
    }

    public String getUOM() {
        return uom;
    }

    public java.sql.Date getExpiration() {
        return sqlExp;
    }

    public String getName() {
        return productName;
    }

    public double getServingSize() {
        return servSize;
    }

    public double getProtein() {
        return protein;
    }

    public double getCalories() {
        return calories;
    }

    public double getFat() {
        return fat;
    }

    //helper to validate and set size
    boolean validateSize(String tempSize) {
        size=0;
        return validateDouble("size", tempSize);
    }

    boolean validateServingSize(String tempSize) {
        servSize = 0;
        return validateDouble("Serving Size", tempSize);
    }

    boolean validateCalories(String tempSize) {
        calories = 0;
        return validateDouble("Calories", tempSize);
    }

    boolean validateProtein(String tempSize) {
        protein = 0;
        return validateDouble("Protein", tempSize);
    }
    boolean validateFat(String tempSize) {
        fat = 0;
        return validateDouble("Fat", tempSize);
    }
    

    // method to validate and set units
    boolean validateUOM(String tempUOM) {
        if (tempUOM.equals("unit")) {
            JOptionPane.showMessageDialog(null, "Please select a valid unit of measurement.");
            return false;
        }
        uom = "";
        if (tempUOM.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: Unit of measurement must not be empty.");
            return false;
        } else {
            uom = tempUOM;
        }
        return true;
    }

    boolean validateName(String name) {
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Name must not be empty");
            return false;
        } else if (name.length() > 80) {
            JOptionPane.showMessageDialog(null, null,
                    "Name mustless than 80 Characters", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            productName = name;
            return true;
        }
    }

    //helper method to validate and set date
    boolean validateDate(String tempDate) {
        SimpleDateFormat dateFormat;
        String formatter = "";
        sqlExp = null;
        //parse data values
        if (!(tempDate.isEmpty())) {

            dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            formatter = formatter.format(tempDate);
            try {
                sqlExp = java.sql.Date.valueOf(formatter);
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(null, "Date must be in yyyy-mm-dd format");
                return false;
            }
        }
        return true;
    }

    //special case since contains default
    boolean validateUsage(String tempQuant) {
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

    /*validates usage and returns a string if and 
        why it failed or the word valid*/
    String validateUPC(String interfaceUpc) {
        upc = interfaceUpc;
        String regex = "[0-9]+";
        if (upc.isEmpty()) {
            return "empty";
        } else if (!upc.matches(regex)) {
            return "notANum";
        } else if (upc.length() != 12) {
            return "length";
        } else {

            return "valid";
        }

    }
    //validates nullable field;
    private boolean validateDouble(String type1, String tempDub) {
        String message="Invalid Input:"+type1+" must not be empty";
        if(!type1.equals(size)){
            message= "Invalid Input:"+type1+"!"+"If enterering nutrition "
                    + "all fields must be added"; 
        }
        if (tempDub.isEmpty()) {
            JOptionPane.showMessageDialog(null,message );
            return false;
        }
         try {
            size = Double.parseDouble(tempDub);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,type1+ " should be a numeric value");
            return false;
        }
        return true;
    }

}
