/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.font.TextAttribute.FONT;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Michelle
 */
public class ProductDialog extends JDialog implements ActionListener {

    String upc;
    String[] data;
    JTextField nameField;
    JTextField serv_sizeField;
    JTextField proteinField;
    JTextField caloriesField;
    JTextField fatField;
    JComboBox categoryChoices;
    JComboBox uomChoices;
    JButton addBtn;
    JButton cancelBtn;
    private boolean nutritionIsEmpty;
    productData newProd;

    ProductDialog(JFrame parent, String upc) {
        super(parent, "Adding a new product", true);
        data = new String[7];
        data[0] = upc;
        for (int i = 1; i < data.length; i++) {
            data[i] = null;
        }
        this.upc = upc;
        //defaults to misc
        String[] produceOptions = {"Miscellaneous", "Produce",
            "Meats, Poultry, and Seafood",
            "Dairy and Refrigerated",
            "Pantry",
            "Breads and Bakery",
            "Baking, Herbs, and Spices",
            "Beverages",
            "Household Supplies"
        };
        String[] uomOption = {"g", "ml"};
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        String message = "<html> <p style='font-style:italic;color:red;'>"
                + "Items indicated by * are required fields</p></html>";
        JLabel reqLabel = new JLabel(message);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(reqLabel, gbc);

        JLabel label = new JLabel("Enter the Name of the Product*");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(label, gbc);

        nameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        JLabel categoryLabel = new JLabel("Select a Category*");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(categoryLabel, gbc);

        categoryChoices = new JComboBox(produceOptions);
        categoryChoices.setSelectedIndex(0);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(categoryChoices, gbc);

        String nutMessage = "<html><p style='font-style:italic;font-weight: bold;'>"
                +"If Entering the Nutritional Data<br>"
                + "Please Complete all Fields</p></html>";
        JLabel nutritionLabel = new JLabel(nutMessage);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(nutritionLabel, gbc);
     
        JLabel servSizeLabel = new JLabel("Serving Size in g or ml");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(servSizeLabel, gbc);

        serv_sizeField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(serv_sizeField, gbc);

        JLabel uomLabel = new JLabel("Select a Unit of Measurement");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(uomLabel, gbc);

        uomChoices = new JComboBox(uomOption);
        uomChoices.setSelectedIndex(0);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(uomChoices, gbc);

        JLabel caloriesLabel = new JLabel("Enter Calories per serving");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(caloriesLabel, gbc);

        caloriesField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(caloriesField, gbc);

        JLabel proteinLabel = new JLabel("Enter Protein per serving");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(proteinLabel, gbc);

        proteinField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(proteinField, gbc);

        JLabel fatLabel = new JLabel("Enter Fat per serving");
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(fatLabel, gbc);

        fatField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(fatField, gbc);

        addBtn = new JButton("    Add     ");
        addBtn.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(addBtn, gbc);

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 9;
        panel.add(cancelBtn, gbc);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public String[] run() {
        this.setVisible(true);
        return data;
    }

    boolean addSuccessful() {
        String[] prodData = run();

        if (newProd.AddProductToInventory(upc, prodData, nutritionIsEmpty)) {
            return true;
        } else {
            return false;
        }
    }
    boolean actionAdded=false;

    @Override
    public void actionPerformed(ActionEvent e) {
        newProd = new productData();
        boolean actionAdded;
        if (e.getSource() == addBtn) {
            data[0] = nameField.getText();
            data[1] = (String) categoryChoices.getSelectedItem();
            data[2] = serv_sizeField.getText();
            data[3] = (String) uomChoices.getSelectedItem();
            data[4] = caloriesField.getText();
            data[5] = proteinField.getText();
            data[6] = fatField.getText();
            DataValidation dv = new DataValidation();
            int category = dv.getCategory(data[1]);
            if (category < 800 && category > 0 && dataIsEmpty(data)) {
                if(!createDialog()){
                   actionAdded = newProd.AddProductToInventory(upc, data, nutritionIsEmpty); 
                }
            } else {
                actionAdded = newProd.AddProductToInventory(upc, data, nutritionIsEmpty);
            }

        } else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }

    private boolean dataIsEmpty(String[] data) {
       nutritionIsEmpty = false;
        for (int i = 2; i < data.length; i++) {
            if (data[i].isEmpty()) {
              nutritionIsEmpty= true;
              return nutritionIsEmpty;
            } else {
                nutritionIsEmpty = false;
            }
        }
         return nutritionIsEmpty;
    }

    private boolean createDialog() {
        boolean addNutrition;
        int n = JOptionPane.showOptionDialog(this,
                "<html> <p style='font-style:italic;'>By not adding Nutrition "
                + "acknowledge that you will not see it in the nutritional tab"
                + " <br>Would you like to add it now?</p></html>",
                "Add Nutrition Now?",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            addNutrition=true;
        }else{
            addNutrition=false;
        }
        return addNutrition;
    }
}
