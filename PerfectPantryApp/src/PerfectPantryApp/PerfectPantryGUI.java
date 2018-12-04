package PerfectPantryApp;


import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableCellRenderer;




/**
 * This class creates the GUI for the Perfect Pantry application.
 * @author Hira Waqas, Josh Gugel
 */
public class PerfectPantryGUI extends JFrame {
    private static final Logger logger  = Logger.getLogger(AppDriver.class.getName());
    private InventoryData invData;

    /**
     * Creates new form PerfectPantryGUI
     */
    public PerfectPantryGUI() {
        invData = new InventoryData();
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
                            //todo josh - should maybe bring up the edit item dialog instead
                            if (invData.incrementInventory(data)){
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
                        /**
                         * We need to add code to this to allow a user to enter
                         * information to the master product table. The data variable
                         * should remain intact because once it is entered as a product
                         * it should also be added as an inventory item.
                         */
                        JOptionPane.showMessageDialog(this, "UPC not found in database");
                        return; //break;
                    default:
                        break;
                }

            } else if (e.getSource() == cancelBtn) {
                dispose(); 
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
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inventoryTabPane = new JTabbedPane();
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

    //Josh Todo
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
//                for (int i=0; i<4; i++) {
//                    System.out.println(data[i]); //testing todo josh
//                }
                EditInventoryDialog dialog = new EditInventoryDialog(null, data);
                data = dialog.run();
                //verify data todo
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
    

    
    
    //this method will perform action for go button
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
    private JTabbedPane inventoryTabPane;
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
    // End of variables declaration//GEN-END:variables
}
