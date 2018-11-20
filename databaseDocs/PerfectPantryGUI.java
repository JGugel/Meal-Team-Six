/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hira Waqas
 */
public class PerfectPantryGUI extends javax.swing.JFrame {

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

        inventoryTabPane = new javax.swing.JTabbedPane();
        inventorySplitPane = new javax.swing.JSplitPane();
        inventoryLeftPanel = new javax.swing.JPanel();
        addInventoryButton = new javax.swing.JButton();
        categoriesPanel = new javax.swing.JPanel();
        fruitCheckBox = new javax.swing.JCheckBox();
        inventoryRightPanel = new javax.swing.JPanel();
        sortingPanel = new javax.swing.JPanel();
        filterLabel = new javax.swing.JLabel();
        sortingLabel = new javax.swing.JLabel();
        sortingComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Perfect Pantry");

        addInventoryButton.setText("Add Inventory");
        addInventoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInventoryButtonActionPerformed(evt);
            }
        });

        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Categories"));

        fruitCheckBox.setText("Fruits");
        fruitCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fruitCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout categoriesPanelLayout = new javax.swing.GroupLayout(categoriesPanel);
        categoriesPanel.setLayout(categoriesPanelLayout);
        categoriesPanelLayout.setHorizontalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fruitCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        categoriesPanelLayout.setVerticalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fruitCheckBox)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout inventoryLeftPanelLayout = new javax.swing.GroupLayout(inventoryLeftPanel);
        inventoryLeftPanel.setLayout(inventoryLeftPanelLayout);
        inventoryLeftPanelLayout.setHorizontalGroup(
            inventoryLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(categoriesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addInventoryButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inventoryLeftPanelLayout.setVerticalGroup(
            inventoryLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addInventoryButton)
                .addGap(18, 18, 18)
                .addComponent(categoriesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(198, Short.MAX_VALUE))
        );

        inventorySplitPane.setLeftComponent(inventoryLeftPanel);

        filterLabel.setText("Filter");

        sortingLabel.setText("Sorting:");

        sortingComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name", "Categories" }));
        sortingComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortingComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sortingPanelLayout = new javax.swing.GroupLayout(sortingPanel);
        sortingPanel.setLayout(sortingPanelLayout);
        sortingPanelLayout.setHorizontalGroup(
            sortingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sortingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterLabel)
                .addGap(35, 35, 35)
                .addComponent(sortingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sortingPanelLayout.setVerticalGroup(
            sortingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sortingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sortingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterLabel)
                    .addComponent(sortingLabel)
                    .addComponent(sortingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "UPC", "Product Name", "Size", "Category"
            }
        ));
        jScrollPane1.setViewportView(inventoryTable);

        javax.swing.GroupLayout inventoryRightPanelLayout = new javax.swing.GroupLayout(inventoryRightPanel);
        inventoryRightPanel.setLayout(inventoryRightPanelLayout);
        inventoryRightPanelLayout.setHorizontalGroup(
            inventoryRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sortingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(inventoryRightPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inventoryRightPanelLayout.setVerticalGroup(
            inventoryRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sortingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inventorySplitPane.setRightComponent(inventoryRightPanel);

        inventoryTabPane.addTab("Inventory", inventorySplitPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryTabPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryTabPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addInventoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInventoryButtonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_addInventoryButtonActionPerformed

    private void sortingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortingComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortingComboBoxActionPerformed

    private void fruitCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fruitCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fruitCheckBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PerfectPantryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PerfectPantryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PerfectPantryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PerfectPantryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PerfectPantryGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addInventoryButton;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JCheckBox fruitCheckBox;
    private javax.swing.JPanel inventoryLeftPanel;
    private javax.swing.JPanel inventoryRightPanel;
    private javax.swing.JSplitPane inventorySplitPane;
    private javax.swing.JTabbedPane inventoryTabPane;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> sortingComboBox;
    private javax.swing.JLabel sortingLabel;
    private javax.swing.JPanel sortingPanel;
    // End of variables declaration//GEN-END:variables
}