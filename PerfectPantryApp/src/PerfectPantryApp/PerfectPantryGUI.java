package PerfectPantryApp;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableCellRenderer;


/**
 * This class creates the GUI for the Perfect Pantry application.
 * @author Hira Waqas, Josh Gugel
 */
public class PerfectPantryGUI extends JFrame {
    private InventoryData invData;
    JFrame thisFrame;
    /**
     * Creates new form PerfectPantryGUI
     */
    public PerfectPantryGUI() {
        invData = new InventoryData();
        thisFrame=this;
        initComponents();
    }

    class AddInventoryDialog extends JDialog implements ActionListener{
        private String[] data;
        private JComboBox uomComboBox;
        private JLabel upcLabel;
        private JLabel qtyLabel;
        private JLabel uomLabel;
        private JLabel expirationLabel;
        private JLabel usageLabel;
        private JTextField upcTextField;
        private JTextField qtyTextField;
        private JTextField expirationTextField;
        private JTextField usageTextField;
        private JButton addBtn;
        private JButton cancelBtn;
        private  boolean addSuccess=false;
        //Constructor
        public AddInventoryDialog(Frame frame, InventoryData invData){
            super(frame, "Add Item", true);
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            data = new String[5];
            for (int i=0; i < data.length; i++) {
                data[i] = null;
            }
            JPanel panel = new JPanel();
            GridBagLayout grid = new GridBagLayout();
            panel.setLayout(grid);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            //UPC
            upcLabel = new JLabel("Item UPC (12 digits)*");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(upcLabel, gbc);
            upcTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(upcTextField, gbc);
            
            //Quantity
            qtyLabel = new JLabel("Quantity*");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add( qtyLabel, gbc);
            qtyTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(qtyTextField, gbc);
            
            //Unit of Measurment
            uomLabel = new JLabel("Unit of Measurment");
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(uomLabel, gbc);
            String[] uomStrings = {"unit", "pc.", "lb.", "oz.", "g", "gal", 
                                    "qt.", "cup"};
            uomComboBox = new JComboBox(uomStrings);
            uomComboBox.setSelectedIndex(0);
            gbc.gridx = 1;
            gbc.gridy = 2;
            panel.add(uomComboBox, gbc);
            
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
            usageLabel = new JLabel("Avg Qty Usage");
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
                data[1] = qtyTextField.getText();
                data[2] = (String)uomComboBox.getSelectedItem();
                data[3] = expirationTextField.getText();
                data[4] = usageTextField.getText();
                
                //Validate data
                String upcCheck = invData.ValidateUPC(data[0]);
                switch (upcCheck) {
                    case "valid":
                        //check to see if record already exists in inventory
                        if (invData.CheckExists()) {
                            //increments the quantity and estimated usage
                            if (invData.adjustQuantity(data)){
                                populatePantryList();
                                JOptionPane.showMessageDialog(this, "Quantity added to existing entry");
                                return;
                            } else {
                                JOptionPane.showMessageDialog(this, "Inventory not updated"); //failed to increment
                                return;
                            }
                        } else {
                            //UPC okay to add to Inventory
                            if(invData.AddInventory(data)) {
                                populatePantryList();
                                JOptionPane.showMessageDialog(this, "Record has been updated");
                                dispose();
                                break;
                            } else {
                                return;
                            }
                        }
                    case "empty":
                        JOptionPane.showMessageDialog(this, "Invalid Input: UPC must not be empty");
                        return;
                    case "length":
                        JOptionPane.showMessageDialog(this, "UPC must be a 12 digit integer");
                        return;
                    case "notANum":
                        JOptionPane.showMessageDialog(this, "UPC must be a numeric value");
                        return;
                    case "notFound":
                        createDialog(data[0]);
                         if(invData.AddInventory(data)) {
                                populatePantryList();
                                JOptionPane.showMessageDialog(this, "Record has been updated");
                                dispose();
                         }
                    default:
                        break;
                }

            } else if (e.getSource() == cancelBtn) {
                dispose(); 
            }
        }
        private void createDialog( String upc) {
            int n = JOptionPane.showOptionDialog(this,
                    "This Product does not exist in "
                    + "our system.\r\n Would you like to add it now?", "Add Product Now?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
            if (n == JOptionPane.YES_OPTION) {
              ProductDialog productInput= new ProductDialog(thisFrame, upc);
              if(!productInput.addSuccessful()){
                  JOptionPane.showMessageDialog(this, "Record not added");
              }
            } 
        }
         
    }
    
    class EditInventoryDialog extends JDialog implements ActionListener{
        private String[] data;
        private DefaultComboBoxModel model;
        private JComboBox uomComboBox;
        private JLabel qtyLabel;
        private JLabel uomLabel;
        private JLabel expirationLabel;
        private JLabel usageLabel;
        private JTextField qtyTextField;
        private JTextField expirationTextField;
        private JTextField usageTextField;
        private JButton updateBtn;
        private JButton cancelBtn;
        private  boolean editSuccess=false;
        //Constructor
        public EditInventoryDialog(Frame frame, String[] dataIn){
            super(frame, "Edit Item", true);
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            data = dataIn; // String[5]
            JPanel panel = new JPanel();
            GridBagLayout grid = new GridBagLayout();
            panel.setLayout(grid);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            //Quantity
            qtyLabel = new JLabel("Quantity*");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add( qtyLabel, gbc);
            qtyTextField = new JTextField(data[1], 10);
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(qtyTextField, gbc);
            
            //Unit of Measurment
            uomLabel = new JLabel("Unit of Measurment");
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(uomLabel, gbc);
            String[] uomStrings = {"unit", "pc.", "lb.", "oz.", "g", "gal", 
                                    "qt.", "cup"};
            model = new DefaultComboBoxModel(uomStrings);
            uomComboBox = new JComboBox(model);
            int n = model.getIndexOf(data[2]); 
            if (n == -1) {   //set the uom if it matches
                uomComboBox.setSelectedIndex(0);
            } else {
                uomComboBox.setSelectedIndex(n);
            }
            gbc.gridx = 1;
            gbc.gridy = 2;
            panel.add(uomComboBox, gbc);
            
            //Expiration
            expirationLabel = new JLabel("Expiration");
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(expirationLabel, gbc);
            expirationTextField = new JTextField(data[3], 10);
            gbc.gridx = 1;
            gbc.gridy = 3;
            panel.add(expirationTextField, gbc);
            
            //Average Usage
            usageLabel = new JLabel("Avg Qty Usage");
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(usageLabel, gbc);
            usageTextField = new JTextField(data[4], 10);
            gbc.gridx = 1;
            gbc.gridy = 4;
            panel.add(usageTextField, gbc);
            
            //Update and Cancel Buttons
            updateBtn = new JButton("Update");
            updateBtn.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(updateBtn, gbc);
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
            if (e.getSource() == updateBtn) {
                data[1] = qtyTextField.getText();
                data[2] = (String)uomComboBox.getSelectedItem();
                data[3] = expirationTextField.getText();
                data[4] = usageTextField.getText();
                
                
                //Validate data
                System.out.println("UPC is : " + data[0]); //test line josh
                String upcCheck = invData.ValidateUPC(data[0]);
                //check to see if record already exists in inventory
                if (invData.CheckExists()) {
                    //should always get here
                    if (invData.EditInventory(data)){
                        populatePantryList();
                        JOptionPane.showMessageDialog(this, "Item Updated");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Inventory not updated"); //failed to increment
                        return;
                    }
                } else {
                    //should never get here?
                    System.out.println("UPC does not exist in Inventory list");
                    return;
                }
            } else if (e.getSource() == cancelBtn) {
                dispose(); 
            }
        }
    }
    
    class AddItemSLDialog extends JDialog implements ActionListener{
        private String[] data;
        private JLabel nameLabel;
        private JTextField nameTextField;
        private JComboBox catComboBox;
        private JLabel catLabel;
        private JComboBox uomComboBox;
        private JLabel qtyLabel;
        private JLabel uomLabel;
        private JTextField qtyTextField;
        private JButton addBtn;
        private JButton cancelBtn;
        private  boolean addSuccess=false;
        //Constructor
        public AddItemSLDialog(Frame frame, InventoryData invData){
            super(frame, "Add Item", true);
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            data = new String[4];
            for (int i=0; i < data.length; i++) {
                data[i] = null;
            }
            JPanel panel = new JPanel();
            GridBagLayout grid = new GridBagLayout();
            panel.setLayout(grid);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            //Name
            nameLabel = new JLabel("Item UPC (12 digits)*");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(nameLabel, gbc);
            nameTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(nameTextField, gbc);
            
            //Quantity
            qtyLabel = new JLabel("Quantity*");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add( qtyLabel, gbc);
            qtyTextField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(qtyTextField, gbc);
            
            //Unit of Measurment
            uomLabel = new JLabel("Unit of Measurment");
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(uomLabel, gbc);
            String[] uomStrings = {"unit", "pc.", "lb.", "oz.", "g", "gal", 
                                    "qt.", "cup"};
            uomComboBox = new JComboBox(uomStrings);
            uomComboBox.setSelectedIndex(0);
            gbc.gridx = 1;
            gbc.gridy = 2;
            panel.add(uomComboBox, gbc);

            //Category
            catLabel = new JLabel("Category");
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(catLabel, gbc);
            String[] catStrings = {"misc", "Baking, Herbs, & Spices", "Beverages",
                "Breads & Bakery", "Dairy & Refrigeration", "Household Supplies", 
                "Meats & Poultry", "Pantry", "Produce"};
            catComboBox = new JComboBox(catStrings);
            catComboBox.setSelectedIndex(0);
            gbc.gridx = 1;
            gbc.gridy = 3;
            panel.add(catComboBox, gbc);
            
            //Add and Cancel Buttons
            addBtn = new JButton("Add");
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
        
        public String [] run() {
            this.setVisible(true);
            return data;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addBtn) {
                data[0] = nameTextField.getText();
                data[1] = qtyTextField.getText();
                data[2] = (String)uomComboBox.getSelectedItem();
                data[3] = (String)catComboBox.getSelectedItem();
                
                //Validate name is not empty
                if(data[0].equals("")){
                    JOptionPane.showMessageDialog(this, "Name must not be empty");
                    return;
                }
                if(invData.AddItemSL(data)) {
                    populatePantryList();
                    JOptionPane.showMessageDialog(this, "Record has been updated");
                    dispose();
                } else {
                    return;
                }
            } else if (e.getSource() == cancelBtn) {
                dispose(); 
            }
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        homeTabPane = new JTabbedPane();
        inventorySplitPane = new JSplitPane();
        inventoryLeftPanel = new JPanel();
        addInventoryButton = new JButton();
        categoriesPanel = new JPanel();
        bakingCB = new JCheckBox();
        beveragesCB = new JCheckBox();
        breadsBakeryCB = new JCheckBox();
        dairyRefCB = new JCheckBox();
        householdSCB = new JCheckBox();
        meatsPoultryCB = new JCheckBox();
        ProduceCB = new JCheckBox();
        pantryCB = new JCheckBox();
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
        addInventoryButton.addActionListener(e-> addInventoryButton());
        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Categories"));

        bakingCB.setText("Baking, Herbs, and Spices");
        bakingCB.addActionListener(e-> populatePantryList());

        beveragesCB.setText("Beverages");
        beveragesCB.addActionListener(e-> populatePantryList());

        breadsBakeryCB.setText("Breads and Bakery");
        breadsBakeryCB.addActionListener(e-> populatePantryList());

        dairyRefCB.setText("Dairy and Refrigerated");
        dairyRefCB.addActionListener(e-> populatePantryList());

        householdSCB.setText("Household Supplies");
        householdSCB.addActionListener(e-> populatePantryList());

        meatsPoultryCB.setText("Meats and Poultry");
        meatsPoultryCB.addActionListener(e-> populatePantryList());

        pantryCB.setText("Pantry");
        pantryCB.addActionListener(e-> populatePantryList());
        
        ProduceCB.setText("Produce");
        ProduceCB.addActionListener(e-> populatePantryList());
        
        javax.swing.GroupLayout categoriesPanelLayout = new GroupLayout(categoriesPanel);
        categoriesPanel.setLayout(categoriesPanelLayout);
        categoriesPanelLayout.setHorizontalGroup(
            categoriesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoriesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, categoriesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bakingCB))
                    .addGroup(categoriesPanelLayout.createSequentialGroup()
                        .addGroup(categoriesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(ProduceCB)
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
            categoriesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bakingCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beveragesCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(breadsBakeryCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dairyRefCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(householdSCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(meatsPoultryCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pantryCB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProduceCB))
        );

        createSearchPanel();
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
                .addComponent(searchPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        inventoryLeftPanelLayout.setVerticalGroup(
            inventoryLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(inventoryLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addInventoryButton)
                .addGap(18, 18, 18)
                .addComponent(categoriesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inventorySplitPane.setLeftComponent(inventoryLeftPanel);

        filterLabel.setText("Filter");

        sortingLabel.setText("Sorting:");

        sortingComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "UPC","Name", "Categories", "Expiration Date" }));
        sortingComboBox.addActionListener(e-> populatePantryList());

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
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 1000, GroupLayout.PREFERRED_SIZE)
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

        homeTabPane.addTab("Inventory", inventorySplitPane);

        shopListTab();
        nutritionTab();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeTabPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeTabPane)
                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
    }
  
    /**
     * This method perform action for add inventory button
     */
    private void addInventoryButton() {
        //check item UPC in database to verify
        AddInventoryDialog dialog = new AddInventoryDialog(this, invData);
        String[] data = dialog.run();
        //switch statement moved to inside dialog
    }
    
    /**
     * This method sort selected option
     * @return selected option string
     */
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
   /**
    * This method will concat the query
    */
    private String getConcatenatedWhereStatement(String query, String selectedCat) {
        if (query.length() > 0) {
            query += "," + selectedCat;
        } else {
            query = selectedCat;
        }
        return query;
    }
    /**
     * This method first check if any checkbox is checked 
     * then it will get selected categories and sorting option
     * and then populate table
     */ 
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
        if(ProduceCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Produce'");            
        }
        if(pantryCB.isSelected()){
            selectedCategories = getConcatenatedWhereStatement(selectedCategories,"'Pantry'");            
        }
        
        invData.SetTable(sortedSelectedOption(), selectedCategories);
        inventoryTable.setModel(invData.GetModel());
        JButtonRenderer jbRender = new JButtonRenderer();
        inventoryTable.setDefaultRenderer(JButton.class, jbRender);
        EditTableEditor editEditor = new EditTableEditor(new JCheckBox());
        inventoryTable.getColumnModel().getColumn(7).setCellEditor(editEditor);
        DeleteTableEditor deleteEditor = new DeleteTableEditor(new JCheckBox());
        inventoryTable.getColumnModel().getColumn(8).setCellEditor(deleteEditor);
        AddToCartTableEditor addEditor = new AddToCartTableEditor(new JCheckBox());
        inventoryTable.getColumnModel().getColumn(9).setCellEditor(addEditor);
        inventoryTable.repaint();
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(175);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(15);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(25);
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(25);
        inventoryTable.getColumnModel().getColumn(7).setPreferredWidth(5);
        inventoryTable.getColumnModel().getColumn(8).setPreferredWidth(5);
        inventoryTable.getColumnModel().getColumn(9).setPreferredWidth(20);
    }
    
    
    //JButtonRenderer provides implementation for a button in a table cell
    public class JButtonRenderer extends JButton implements TableCellRenderer{
        //constructor
        public JButtonRenderer() {
            super();
        }
        public JButtonRenderer(String name) {
            super(name);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((String)value);
            switch ((String) value) {
                case "Edit":
                    setForeground(Color.BLACK);
                    break;
                case "Delete":
                    setForeground(Color.RED);
                    break;
                case "Add to Cart":
                    setForeground(Color.GREEN);
                    break;
            }
            return this;
        }
    }

    //EditTableEditor defines the functionality of the edit table cell
    public class EditTableEditor extends DefaultCellEditor {
        JButton button;
        String label;
        boolean clicked;
        int row, col;
        JTable table;

        public EditTableEditor (JCheckBox checkBox) {
          super(checkBox);
          button = new JButton();
          button.setOpaque(true);
          button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
              fireEditingStopped();
            }
          });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
          this.table = table;
          this.row = row;
          this.col = column;

          button.setForeground(Color.black);
          button.setBackground(UIManager.getColor("Button.background"));
          label = (value == null) ? "" : value.toString();
          button.setText(label);
          clicked = true;
          return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked)
            {
                System.out.println("in getCellEditorValue of Edit");
                //Edit Item Here
                InventoryItem item = (InventoryItem)((InventoryTableModel)table.getModel()).inventory.get(row);
                String[] data = {item.upcDisplay, item.sizeDisplay, item.uomDisplay, item.expiration, item.quantityDisplay};
                EditInventoryDialog dialog = new EditInventoryDialog(null, data);
                data = dialog.run();
            }
            clicked = false;
            return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
          clicked = false;
          return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }
    
    //DeleteTableEditor defines the functionality of the delete table cell
    public class DeleteTableEditor extends DefaultCellEditor {
        JButton button;
        String label;
        boolean clicked;
        int row, col;
        JTable table;

        public DeleteTableEditor (JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            this.col = column;

            button.setForeground(Color.black);
            button.setBackground(UIManager.getColor("Button.background"));
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked)
            {
                int n = JOptionPane.showConfirmDialog(null,
                        "Delete Item?",
                        "Delete",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    //delete here todo
                    InventoryItem item = (InventoryItem)((InventoryTableModel)table.getModel()).inventory.get(row);
                   if( invData.deleteRecord(item.upcDisplay)){
                        populatePantryList();
                       JOptionPane.showMessageDialog(null, "Record deleted successfully.!");
                   } else {
                        JOptionPane.showMessageDialog(null, "Delete Failed");
                    }
                } else {
                    //do nothing
                }
            }
            clicked = false;
            return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    //Josh Todo
    //AddToCartTableEditor defines the functionality of the add to cart table cell
    public class AddToCartTableEditor extends DefaultCellEditor {
        JButton button;
        String label;
        boolean clicked;
        int row, col;
        JTable table;

        public AddToCartTableEditor (JCheckBox checkBox) {
          super(checkBox);
          button = new JButton();
          button.setOpaque(true);
          button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
              fireEditingStopped();
            }
          });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
          this.table = table;
          this.row = row;
          this.col = column;

          button.setForeground(Color.black);
          button.setBackground(UIManager.getColor("Button.background"));
          label = (value == null) ? "" : value.toString();
          button.setText(label);
          clicked = true;
          return button;
        }

        @Override
         public Object getCellEditorValue() {
          if (clicked)
          {
              // TO DO: Add to cart once tables are built
            
            JOptionPane.showMessageDialog(null, "Coming in Phase Three!");
          }
          clicked = false;
          return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
          clicked = false;
          return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }
    

    
    //TODO
    //goButton performs a search based on the input in the Search Box
    private void goButton(){
        JOptionPane.showMessageDialog(this, "Coming in Phase Three");

    }
    
    //this method will create search panel components 
    private void createSearchPanel(){
        searchPanel = new JPanel();
        searchInLabel = new JLabel();
        searchByComboBox = new JComboBox<>();
        enterKeywordLabel = new JLabel();
        keywordTextField = new JTextField();
        goButton = new JButton();
        
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));

        searchInLabel.setText("Search In:");

        searchByComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Search By Name", "Search by UPC", "Search by Nutrition" }));
        /**
         * Nutrition is a complex search...we should narrow this down by a food
         * category before trying to search.
         */
        enterKeywordLabel.setText("Enter Keyword:");

        goButton.setText("Go");
        goButton.addActionListener(e-> goButton());
     
        GroupLayout searchPanelLayout = new GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addComponent(searchInLabel)
                        .addGap(2, 2, 2)
                        .addComponent(searchByComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGroup(searchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addComponent(enterKeywordLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(keywordTextField))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(goButton))))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(searchInLabel)
                    .addComponent(searchByComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(enterKeywordLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(keywordTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(goButton))
                .addContainerGap(24, Short.MAX_VALUE))
        );
    }
    
    //
    private void shopListTab(){
        shopListTab = new JPanel();
        shopListSplitPane = new JSplitPane();
        shopListLeftPanel = new JPanel();
        createShopListButton = new JButton();
        viewShopListPanel = new JPanel();
        selectShopListLabel = new JLabel();
        selectShopListComboBox = new JComboBox<>();
        shopListRightPanel = new JPanel();
        shopListRightTopPanel = new JPanel();
        shopListNameLabel = new JLabel();
        deleteshopListButton = new JButton();
        editShopListButton = new JButton();
        shopListScrollPane = new JScrollPane();
        shopListTable = new JTable();
        addItemShopListButton = new JButton();
        
        createShopListButton.setText("Create New List");
        createShopListButton.addActionListener(e-> createShopListButtonAction());

        viewShopListPanel.setBorder(BorderFactory.createTitledBorder("View List"));

        selectShopListLabel.setText("SelectList:");

        selectShopListComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Select by Name" }));

        GroupLayout viewShopListPanelLayout = new GroupLayout(viewShopListPanel);
        viewShopListPanel.setLayout(viewShopListPanelLayout);
        viewShopListPanelLayout.setHorizontalGroup(
            viewShopListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(viewShopListPanelLayout.createSequentialGroup()
                .addGroup(viewShopListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(selectShopListLabel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectShopListComboBox, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        viewShopListPanelLayout.setVerticalGroup(
            viewShopListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(viewShopListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectShopListLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectShopListComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout shopListLeftPanelLayout = new GroupLayout(shopListLeftPanel);
        shopListLeftPanel.setLayout(shopListLeftPanelLayout);
        shopListLeftPanelLayout.setHorizontalGroup(
            shopListLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shopListLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shopListLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(createShopListButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(shopListLeftPanelLayout.createSequentialGroup()
                        .addComponent(viewShopListPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        shopListLeftPanelLayout.setVerticalGroup(
            shopListLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shopListLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewShopListPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(createShopListButton, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(210, Short.MAX_VALUE))
        );

        shopListSplitPane.setLeftComponent(shopListLeftPanel);

        shopListRightTopPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        shopListNameLabel.setText("List Name");

        deleteshopListButton.setIcon(new ImageIcon(getClass().getResource("delete.png"))); // NOI18N
        deleteshopListButton.setMaximumSize(new java.awt.Dimension(179, 147));
        deleteshopListButton.setMinimumSize(new java.awt.Dimension(179, 147));
        deleteshopListButton.addActionListener(e-> deleteShopListButtonAction());

        editShopListButton.setIcon(new ImageIcon(getClass().getResource("edit.png"))); // NOI18N
        editShopListButton.setMaximumSize(new java.awt.Dimension(44, 44));
        editShopListButton.setMinimumSize(new java.awt.Dimension(44, 44));
        editShopListButton.addActionListener(e-> editShopListButtonAction());

        GroupLayout shopListRightTopPanelLayout = new GroupLayout(shopListRightTopPanel);
        shopListRightTopPanel.setLayout(shopListRightTopPanelLayout);
        shopListRightTopPanelLayout.setHorizontalGroup(
            shopListRightTopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shopListRightTopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shopListNameLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(editShopListButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteshopListButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        shopListRightTopPanelLayout.setVerticalGroup(
            shopListRightTopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shopListRightTopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shopListRightTopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(shopListRightTopPanelLayout.createSequentialGroup()
                        .addComponent(editShopListButton, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(shopListRightTopPanelLayout.createSequentialGroup()
                        .addGroup(shopListRightTopPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(shopListNameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteshopListButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))))
        );

        shopListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product Name", "UPC", "Edit", "Delete"
            }
        ));
        shopListScrollPane.setViewportView(shopListTable);

        addItemShopListButton.setText("Add Item");
        addItemShopListButton.addActionListener(e-> addItemSLButtonAction());

        GroupLayout shopListRightPanelLayout = new GroupLayout(shopListRightPanel);
        shopListRightPanel.setLayout(shopListRightPanelLayout);
        shopListRightPanelLayout.setHorizontalGroup(
            shopListRightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shopListRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shopListRightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(shopListRightPanelLayout.createSequentialGroup()
                        .addComponent(addItemShopListButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(shopListRightTopPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shopListScrollPane, GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE))
                .addContainerGap())
        );
        shopListRightPanelLayout.setVerticalGroup(
            shopListRightPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shopListRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shopListRightTopPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shopListScrollPane, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addItemShopListButton)
                .addContainerGap(179, Short.MAX_VALUE))
        );

        shopListSplitPane.setRightComponent(shopListRightPanel);

        javax.swing.GroupLayout shopListTabLayout = new javax.swing.GroupLayout(shopListTab);
        shopListTab.setLayout(shopListTabLayout);
        shopListTabLayout.setHorizontalGroup(
            shopListTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(shopListSplitPane)
        );
        shopListTabLayout.setVerticalGroup(
            shopListTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, shopListTabLayout.createSequentialGroup()
                .addComponent(shopListSplitPane)
                .addContainerGap())
        );

        homeTabPane.addTab("Shopping List", shopListTab);
    }
    
    private void nutritionTab(){
        nutritionTab = new JSplitPane();
        nutritionLeftPanel = new JPanel();
        prductNameNutLabel = new JLabel();
        nutTextField = new JTextField();
        viewNutInfo = new JButton();
        nutritionRightScrollPane = new JScrollPane();
        nutritionTable = new JTable();
        
        nutritionLeftPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nutrition Info"));

        prductNameNutLabel.setText("Enter product Name");

        viewNutInfo.setText("View Info");

        javax.swing.GroupLayout nutritionLeftPanelLayout = new GroupLayout(nutritionLeftPanel);
        nutritionLeftPanel.setLayout(nutritionLeftPanelLayout);
        nutritionLeftPanelLayout.setHorizontalGroup(
            nutritionLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(nutritionLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nutritionLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(prductNameNutLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(nutritionLeftPanelLayout.createSequentialGroup()
                        .addComponent(nutTextField, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewNutInfo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        nutritionLeftPanelLayout.setVerticalGroup(
            nutritionLeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(nutritionLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prductNameNutLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(nutritionLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nutTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewNutInfo))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nutritionTab.setLeftComponent(nutritionLeftPanel);

        nutritionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Product name", "Nutrition name", "Calories", "Protiens", "Fat", "UOM"
            }
        ));
        nutritionRightScrollPane.setViewportView(nutritionTable);

        nutritionTab.setRightComponent(nutritionRightScrollPane);

        homeTabPane.addTab("Nutrition Tab", nutritionTab);
    }
    
    //
    private void createShopListButtonAction() {                                                     
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "create shopping list");
    }
    
    //
    private void deleteShopListButtonAction() {                                                     
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "delete shopping list");
    }
    
    //
    private void editShopListButtonAction() {                                                     
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "edit shopping list");
    }
    
    //
    private void addItemSLButtonAction() {                                                     
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(this, "add item in shopping list");
        AddItemSLDialog dialog = new AddItemSLDialog(this, invData);
        String[] data = dialog.run();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton addInventoryButton;
    private JCheckBox bakingCB;
    private JCheckBox beveragesCB;
    private JCheckBox breadsBakeryCB;
    private JPanel categoriesPanel;
    private JCheckBox dairyRefCB;
    private JLabel filterLabel;
    private JCheckBox householdSCB;
    private JCheckBox produceCheckBox;
    private JPanel inventoryLeftPanel;
    private JPanel inventoryRightPanel;
    private JSplitPane inventorySplitPane;
    private JTabbedPane homeTabPane;
    private JTable inventoryTable;
    private JScrollPane jScrollPane1;
    private JCheckBox meatsPoultryCB;
    private JCheckBox ProduceCB;
    private JCheckBox pantryCB;
    private JComboBox<String> sortingComboBox;
    private JLabel sortingLabel;
    private JPanel sortingPanel;
    private JPanel searchPanel;
    private JLabel searchInLabel;
    private JComboBox<String> searchByComboBox;
    private JLabel enterKeywordLabel;
    private JTextField keywordTextField;
    private JButton goButton;
    private JComboBox<String> selectShopListComboBox;
    private JButton addItemShopListButton;
    private JButton createShopListButton;
    private JButton deleteshopListButton;
    private JButton editShopListButton;
    private JLabel selectShopListLabel;
    private JPanel shopListLeftPanel;
    private JLabel shopListNameLabel;
    private JPanel shopListRightPanel;
    private JPanel shopListRightTopPanel;
    private JScrollPane shopListScrollPane;
    private JSplitPane shopListSplitPane;
    private JPanel shopListTab;
    private JTable shopListTable;
    private JPanel viewShopListPanel;
    private JTextField nutTextField;
    private JPanel nutritionLeftPanel;
    private JScrollPane nutritionRightScrollPane;
    private JSplitPane nutritionTab;
    private JTable nutritionTable;
    private JLabel prductNameNutLabel;
    private JButton viewNutInfo;
    
    // End of variables declaration//GEN-END:variables
}
