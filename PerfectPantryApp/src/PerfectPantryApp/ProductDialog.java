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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    JComboBox categoryChoices;
    JButton addBtn;
    JButton cancelBtn;

    ProductDialog(JFrame parent, String upc) {
            super(parent,"Adding a new product",true);
        data = new String[3];
        data[0]=upc;
        for (int i = 1; i < data.length; i++) {
            data[i] = null;
        }
        setBounds(0, 0, 600, 400);
        this.upc = upc;
        String[] produceOptions = {"100- Produce",
            "200- Meats, Poultry, and Seafood",
            "300- Dairy and Refrigerated",
            "400 - Pantry",
            "500- Breads and Bakery",
            "600- Baking, Herbs, and Spices",
            "700- Beverages",
            "800- Household Supplies",
            "900 Miscellaneous"};
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        JLabel label = new JLabel("Enter the Name of the Product");
        gbc.fill = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);
        nameField = new JTextField(30);
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);
        JLabel categoryLabel = new JLabel("Select a Category");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(categoryLabel, gbc);
        categoryChoices = new JComboBox(produceOptions);
        categoryChoices.setSelectedIndex(0);
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;

        panel.add(categoryChoices, gbc);
        addBtn = new JButton("    Add     ");
        addBtn.addActionListener(this);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(addBtn, gbc);
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 3;
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
        return false;
        /*
        InventoryData newProd= new InventoryData();
        if( newProd.AddProductToInventory(prodData)){
            return true;
        }else return false;*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            data[0] = nameField.getText();
            data[1] = (String) categoryChoices.getSelectedItem();
            return;
        } else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }
}
