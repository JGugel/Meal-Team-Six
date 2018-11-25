package PerfectPantryApp;


import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.*;




/**
 * This class creates the GUI for the Perfect Pantry application.
 * @author Hira Waqas, Josh Gugel
 */
public class PerfectPantryGUI extends JFrame {

	JDBC j= new JDBC();
    /**
     * Creates new form PerfectPantryGUI
     */
    public PerfectPantryGUI() {
        initComponents();
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
        produceCheckBox = new JCheckBox();
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

        categoriesPanel.setBorder(BorderFactory.createTitledBorder("Categories"));

        produceCheckBox.setText("Produce");
        produceCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fruitCheckBoxActionPerformed(evt);
            }
        });

        GroupLayout categoriesPanelLayout = new GroupLayout(categoriesPanel);
        categoriesPanel.setLayout(categoriesPanelLayout);
        categoriesPanelLayout.setHorizontalGroup(
            categoriesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(produceCheckBox)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        categoriesPanelLayout.setVerticalGroup(
            categoriesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(produceCheckBox)
                .addContainerGap(70, Short.MAX_VALUE))
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
        
        
			j.GetConnection("default");
		
        inventoryTable.setModel(
        	j.GetModel()
        );
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(15);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(125);
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(15);
     
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
    }// </editor-fold>//GEN-END:initComponents

    private void addInventoryButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_addInventoryButtonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_addInventoryButtonActionPerformed

    private void sortingComboBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_sortingComboBoxActionPerformed
    	String sort= (String)sortingComboBox.getSelectedItem();
    	if(sort.equals("UPC")){
        	j.GetConnection("default");
        	inventoryTable.setModel(j.GetModel());
        	inventoryTable.repaint();
        }    	else if(sort.equals("Name")){
        	j.GetConnection("Name");
        	inventoryTable.setModel(j.GetModel());
        	inventoryTable.repaint();
        }else if (sort.equals("Categories")) {
        	j.GetConnection("Categories"); 
        	inventoryTable.setModel(j.GetModel());
        inventoryTable.repaint();
        }else if (sort.equals("Expiration Date")) {
        	j.GetConnection("date"); 
        	inventoryTable.setModel(j.GetModel());
        inventoryTable.repaint();
        }
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(15);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(125);
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(15);
      
    }//GEN-LAST:event_sortingComboBoxActionPerformed

    private void fruitCheckBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_fruitCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fruitCheckBoxActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton addInventoryButton;
    private JPanel categoriesPanel;
    private JLabel filterLabel;
    private JCheckBox produceCheckBox;
    private JPanel inventoryLeftPanel;
    private JPanel inventoryRightPanel;
    private JSplitPane inventorySplitPane;
    private JTabbedPane inventoryTabPane;
    private JTable inventoryTable;
    private JScrollPane jScrollPane1;
    private JComboBox<String> sortingComboBox;
    private JLabel sortingLabel;
    private JPanel sortingPanel;
    // End of variables declaration//GEN-END:variables
}
