package PerfectPantryApp;


import java.awt.event.*;
import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.*;

//josh test todo
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * This class creates the GUI for the Perfect Pantry application.
 * @author Hira Waqas, Josh Gugel
 */
public class PerfectPantryGUI extends JFrame {
   
    private InventoryData invData;

    /**
     * Creates new form PerfectPantryGUI
     */
    public PerfectPantryGUI() {
        try {
            invData = new InventoryData();
        } catch (SQLException ex) {
            Logger.getLogger(PerfectPantryGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    // ref: http://www2.hawaii.edu/~takebaya/ics111/jdialog/jdialog.html
    class AddInventoryDialog extends JDialog implements ActionListener{
        private String[] data;
        private JLabel upcLabel;
        private JLabel quantityLabel;
        private JLabel uomLabel;
        private JLabel expirationLabel;
        private JLabel usageLabel;
        private JTextField upcTextField;
        private JTextField quantityTextField;
        private JTextField uomTextField;
        private JTextField expirationTextField;
        private JTextField usageTextField;
        private JButton addBtn;
        private JButton cancelBtn;
        
        //Constructor
        public AddInventoryDialog(Frame frame){
            super(frame, "Add Item", true);
            data = new String[5];
            JPanel panel = new JPanel();
            GridBagLayout grid = new GridBagLayout();
            panel.setLayout(grid);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            //UPC
            upcLabel = new JLabel("Item UPC (12 digits)");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(upcLabel, gbc);
            upcTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(upcTextField, gbc);
            
            //Quantity
            quantityLabel = new JLabel("Quantity");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(quantityLabel, gbc);
            quantityTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(quantityTextField, gbc);
            
            //Unit of Measurment
            uomLabel = new JLabel("Unit of Measurment (6 char max)");
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(uomLabel, gbc);
            uomTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 2;
            panel.add(uomTextField, gbc);
            
            //Expiration
            expirationLabel = new JLabel("Expiration");
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(expirationLabel, gbc);
            expirationTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 3;
            panel.add(expirationTextField, gbc);
            
            //Average Usage
            usageLabel = new JLabel("Average Quantity Usage");
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(usageLabel, gbc);
            usageTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 4;
            panel.add(usageTextField, gbc);
            
            //Add and Cancel Buttons
            addBtn = new JButton("Add");
            addBtn.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(addBtn, gbc);
            cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(this);
            gbc.gridx = 1;
            gbc.gridy = 5;
            panel.add(cancelBtn, gbc);
            
            getContentPane().add(panel);
            pack();
            setLocationRelativeTo(null);
             
        }
        
        public String [] run() {
            this.setVisible(true);
            return data;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addBtn) {
                data[0] = upcTextField.getText();
                data[1] = quantityTextField.getText();
                data[2] = uomTextField.getText();
                data[3] = expirationTextField.getText();
                data[4] = usageTextField.getText();
            } else {
                data = null;
            }
            dispose();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inventoryTabPane = new JTabbedPane();
        inventorySplitPane = new JSplitPane();
        inventoryLeftPanel = new JPanel();
        addInventoryButton = new JButton();
        categoriesPanel = new JPanel();
        bakingCB = new javax.swing.JCheckBox();
        beveragesCB = new javax.swing.JCheckBox();
        breadsBakeryCB = new javax.swing.JCheckBox();
        dairyRefCB = new javax.swing.JCheckBox();
        householdSCB = new javax.swing.JCheckBox();
        meatsPoultryCB = new javax.swing.JCheckBox();
        miscellaneousCB = new javax.swing.JCheckBox();
        pantryCB = new javax.swing.JCheckBox();
        inventoryRightPanel = new JPanel();
        sortingPanel = new JPanel();
        filterLabel = new JLabel();
        sortingLabel = new JLabel();
        sortingComboBox = new JComboBox<>();
        jScrollPane1 = new JScrollPane();
        inventoryTable = new JTable();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Perfect Pantry");

        addInventoryButton.setText("Add Inventory");
        addInventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addInventoryButtonActionPerformed(evt);
            }
        });
        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Categories"));

        bakingCB.setText("Baking, Herbs, and Spices");
        bakingCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

        beveragesCB.setText("Beverages");
        beveragesCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

        breadsBakeryCB.setText("Breads and Bakery");
        breadsBakeryCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

        dairyRefCB.setText("Dairy and Refrigerated");
        dairyRefCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

        householdSCB.setText("Household Supplies");
        householdSCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

        meatsPoultryCB.setText("Meats and Poultry");
        meatsPoultryCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

          pantryCB.setText("Pantry");
        pantryCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               categoriesSelectionActionPerformed(evt);
            }
        });
        
        miscellaneousCB.setText("Miscellaneous");
        miscellaneousCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoriesSelectionActionPerformed(evt);
            }
        });

      

        javax.swing.GroupLayout categoriesPanelLayout = new javax.swing.GroupLayout(categoriesPanel);
        categoriesPanel.setLayout(categoriesPanelLayout);
        categoriesPanelLayout.setHorizontalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bakingCB))
                    .addGroup(categoriesPanelLayout.createSequentialGroup()
                        .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(miscellaneousCB)
                            .addComponent(pantryCB)
                            .addComponent(beveragesCB)
                            .addComponent(breadsBakeryCB)
                            .addComponent(dairyRefCB)
                            .addComponent(householdSCB)
                            .addComponent(meatsPoultryCB))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        categoriesPanelLayout.setVerticalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bakingCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beveragesCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(breadsBakeryCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dairyRefCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(householdSCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(meatsPoultryCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pantryCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(miscellaneousCB))
        );

        GroupLayout inventoryLeftPanelLayout = new GroupLayout(inventoryLeftPanel);
        inventoryLeftPanel.setLayout(inventoryLeftPanelLayout);
        inventoryLeftPanelLayout.setHorizontalGroup(
            inventoryLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(inventoryLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(categoriesPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addInventoryButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inventoryLeftPanelLayout.setVerticalGroup(
            inventoryLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(inventoryLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addInventoryButton)
                .addGap(18, 18, 18)
                .addComponent(categoriesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(198, Short.MAX_VALUE))
        );

        inventorySplitPane.setLeftComponent(inventoryLeftPanel);

        filterLabel.setText("Filter");

        sortingLabel.setText("Sorting:");

        sortingComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "UPC","Name", "Categories", "Expiration Date" }));
        sortingComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sortingComboBoxActionPerformed(evt);
            }
        });

        GroupLayout sortingPanelLayout = new GroupLayout(sortingPanel);
        sortingPanel.setLayout(sortingPanelLayout);
        sortingPanelLayout.setHorizontalGroup(
            sortingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sortingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterLabel)
                .addGap(35, 35, 35)
                .addComponent(sortingLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortingComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sortingPanelLayout.setVerticalGroup(
            sortingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sortingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sortingPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(filterLabel)
                    .addComponent(sortingLabel)
                    .addComponent(sortingComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        populatePantryList();
        jScrollPane1.setViewportView(inventoryTable);

        GroupLayout inventoryRightPanelLayout = new GroupLayout(inventoryRightPanel);
        inventoryRightPanel.setLayout(inventoryRightPanelLayout);
        inventoryRightPanelLayout.setHorizontalGroup(
            inventoryRightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(inventoryRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryRightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sortingPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(inventoryRightPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 900, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inventoryRightPanelLayout.setVerticalGroup(
            inventoryRightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(inventoryRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sortingPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inventorySplitPane.setRightComponent(inventoryRightPanel);

        inventoryTabPane.addTab("Inventory", inventorySplitPane);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryTabPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryTabPane)
                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addInventoryButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_addInventoryButtonActionPerformed
        //check item UPC in database to verify
        AddInventoryDialog dialog = new AddInventoryDialog(this);
        String[] data = dialog.run();
        if (data == null) {
            return;
        }
        boolean addSuccess=invData.AddInventory(data);
        
        if(addSuccess){
                populatePantryList();
                JOptionPane.showMessageDialog(this, "Record has been updated");
        }
        
    }//GEN-LAST:event_addInventoryButtonActionPerformed

    private String sortedSelectedOption() {
        String sort= (String)sortingComboBox.getSelectedItem();
        String selectedOption = "";
    	if(sort.equals("UPC")){
            selectedOption = "default";
        }    	
        else if(sort.equals("Name") || sort.equals("Categories")){
            selectedOption = sort;
        }
        else if (sort.equals("Expiration Date")) {
            selectedOption = "date";
        }
        return selectedOption;
    }
    private void sortingComboBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_sortingComboBoxActionPerformed
        populatePantryList();
    }//GEN-LAST:event_sortingComboBoxActionPerformed
 
    private String getConcatenatedWhereStatement(String query, String selectedCat) {
        if (query.length() > 0) {
            query += "," + selectedCat;
        } else {
            query = selectedCat;
        }
        return query;
    }
    private void populatePantryList() {
        String selectedCategories = "";
        if(bakingCB.isSelected())
        {
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Baking, Herbs, and Spices'");
        }
        if(beveragesCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Beverages'");
        }
        if(breadsBakeryCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Breads and Bakery'");
        }
        if(dairyRefCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Dairy and Refrigerated'");
        }
        if(householdSCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Household Supplies'");
        }
        if(meatsPoultryCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Meats, Poultry, and Seafood'");            
        }
        if(miscellaneousCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Miscellaneous'");            
        }
        if(pantryCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Pantry'");            
        }
        
        invData.SetTable(sortedSelectedOption(), selectedCategories);
        inventoryTable.setModel(invData.GetModel());
        inventoryTable.repaint();
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(15);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(125);
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(15);
    }
    
    private void categoriesSelectionActionPerformed(java.awt.event.ActionEvent evt) {
        populatePantryList();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton addInventoryButton;
    private javax.swing.JCheckBox bakingCB;
    private javax.swing.JCheckBox beveragesCB;
    private javax.swing.JCheckBox breadsBakeryCB;
    private JPanel categoriesPanel;
    private javax.swing.JCheckBox dairyRefCB;
    private JLabel filterLabel;
    private javax.swing.JCheckBox householdSCB;
    private JCheckBox produceCheckBox;
    private JPanel inventoryLeftPanel;
    private JPanel inventoryRightPanel;
    private JSplitPane inventorySplitPane;
    private JTabbedPane inventoryTabPane;
    private JTable inventoryTable;
    private JScrollPane jScrollPane1;
    private javax.swing.JCheckBox meatsPoultryCB;
    private javax.swing.JCheckBox miscellaneousCB;
    private javax.swing.JCheckBox pantryCB;
    private JComboBox<String> sortingComboBox;
    private JLabel sortingLabel;
    private JPanel sortingPanel;
    // End of variables declaration//GEN-END:variables
}
