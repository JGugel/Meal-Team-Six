/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Michelle
 */
public class DataValidation {

    private static String upc;
    private static double usage;
    private static double size;
    private static String uom;
    private java.sql.Date sqlExp = null;
  

    DataValidation() {
        upc = "";
        usage = 0;
        size = 0;
        uom = "";
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

    //helper to validate and set size
  boolean validateSize(String tempSize) {
        size = 0;
        if (tempSize.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid Input: Quantity must not be empty");
            return false;
        }
        try {
            size = Double.parseDouble(tempSize);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Quantity should be a numeric value");
            return false;
        }
        return true;
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
        }else {
            uom = tempUOM;
        }
        return true;
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

   
}
