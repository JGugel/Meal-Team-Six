/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PerfectPantryApp;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.font.TextAttribute.FONT;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
    JRadioButton AddNutrition;
    JRadioButton noAddingNutrition;
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
        reqLabel.setHorizontalAlignment(SwingConstants.CENTER);
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

        ButtonGroup nutritionGroup = new ButtonGroup();
        AddNutrition = new JRadioButton("Add Nutrition");
        noAddingNutrition = new JRadioButton("Do Not Add Nutrition");
        nutritionGroup.add(AddNutrition);
        nutritionGroup.add(noAddingNutrition);
        noAddingNutrition.setSelected(true);
        AddNutrition.setSelected(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(noAddingNutrition, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(AddNutrition, gbc);

        addBtn = new JButton("    Add     ");
        addBtn.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(addBtn, gbc);

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(cancelBtn, gbc);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);

    }
    boolean productAdded = false;
    int productID = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        newProd = new productData();
        if (e.getSource() == addBtn) {
            data[0] = nameField.getText();
            data[1] = (String) categoryChoices.getSelectedItem();
            productAdded = newProd.AddProductToInventory(upc, data);
            productID = newProd.getProductID();
            if (AddNutrition.isSelected() || createDialog()) {
            }
            dispose();
        } else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }

    public void run() {
        this.setVisible(true);
    }

    boolean addSuccessful() {
        run();
        return productAdded;

    }
    int getProductID(){
        return productID;
    }

    private boolean createDialog() {
        boolean addNutrition;
        int n = JOptionPane.showOptionDialog(this,
                "<html> <p style='font-style:italic;'>By not adding Nutrition "
                + "acknowledge that you will not see it in the nutritional tab"
                + " <br>Would you like to add it now?</p></html>",
                "Nutrition data will not be available",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            addNutrition = true;
        } else {
            addNutrition = false;
        }
        return addNutrition;
    }
}
