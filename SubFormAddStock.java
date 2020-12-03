/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormAddStock extends JPanel{
    JLabel lblTitle, lblItemId, lblItemName, lblItemDesc, lblMinOrder, lblItemRemaining, lblItemAdd;
    JTextField txtItemId, txtItemName, txtItemDesc, txtMinOrder, txtItemRemaining, txtItemAdd, txtItemUnit;
    
    public SubFormAddStock(int itemId, String itemName, String itemDesc, String itemMinOrder, String itemRemaining){
        lblTitle = new JLabel("Add Stocks");
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblItemId = new JLabel("Item ID");
        lblItemName = new JLabel("Item Name");
        lblItemDesc = new JLabel("Item Description");
        lblMinOrder = new JLabel("Minimum Order");
        lblItemRemaining = new JLabel("Remaning Stocks");
        lblItemAdd = new JLabel("Stock(s) to Add");
        
        txtItemId = new JTextField();
        txtItemId.setPreferredSize(new Dimension(200, 20));
        txtItemId.setEditable(false);
        txtItemName = new JTextField();
        txtItemName.setPreferredSize(new Dimension(200, 20));
        txtItemName.setEditable(false);
        txtItemDesc = new JTextField();
        txtItemDesc.setPreferredSize(new Dimension(200, 20));
        txtItemDesc.setEditable(false);
        txtMinOrder = new JTextField();
        txtMinOrder.setPreferredSize(new Dimension(200, 20));
        txtMinOrder.setEditable(false);
        txtItemRemaining = new JTextField();
        txtItemRemaining.setPreferredSize(new Dimension(200, 20));
        txtItemRemaining.setEditable(false);
        txtItemAdd = new JTextField();
        txtItemAdd.setPreferredSize(new Dimension(100,20));
        txtItemUnit = new JTextField();
        txtItemUnit.setEditable(false);
        txtItemUnit.setPreferredSize(new Dimension(100,20));
        
        txtItemId.setText(Integer.toString(itemId));
        txtItemName.setText(itemName);
        txtItemDesc.setText(itemDesc);
        txtMinOrder.setText(itemMinOrder);
        txtItemRemaining.setText(itemRemaining);
        String word = "kg";
        if (itemRemaining.contains(word)){
            txtItemUnit.setText(word);
        }
        
        JPanel addPanel = new JPanel();
        addPanel.add(txtItemAdd);
        addPanel.add(txtItemUnit);
        
        JPanel centerPanel = new JPanel();
        GroupLayout layout = new GroupLayout(centerPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        centerPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        GroupLayout.Group vg3 = layout.createParallelGroup();
        GroupLayout.Group vg4 = layout.createParallelGroup();
        GroupLayout.Group vg5 = layout.createParallelGroup();
        GroupLayout.Group vg6 = layout.createParallelGroup();
        
        hg1.addComponent(lblItemId);
        hg1.addComponent(lblItemName);
        hg1.addComponent(lblItemDesc);
        hg1.addComponent(lblMinOrder);
        hg1.addComponent(lblItemRemaining);
        hg1.addComponent(lblItemAdd);
        
        hg2.addComponent(txtItemId);
        hg2.addComponent(txtItemName);
        hg2.addComponent(txtItemDesc);
        hg2.addComponent(txtMinOrder);
        hg2.addComponent(txtItemRemaining);
        hg2.addComponent(addPanel);
        
        vg1.addComponent(lblItemId);
        vg1.addComponent(txtItemId);
        
        vg2.addComponent(lblItemName);
        vg2.addComponent(txtItemName);
        
        vg3.addComponent(lblItemDesc);
        vg3.addComponent(txtItemDesc);
        
        vg4.addComponent(lblMinOrder);
        vg4.addComponent(txtMinOrder);
        
        vg5.addComponent(lblItemRemaining);
        vg5.addComponent(txtItemRemaining);
        
        vg6.addComponent(lblItemAdd);
        vg6.addComponent(addPanel);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        vseq1.addGroup(vg4);
        vseq1.addGroup(vg5);
        vseq1.addGroup(vg6);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        setLayout(new BorderLayout());
        
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    protected boolean _addStock(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            String itemId = txtItemId.getText();
            String tempvar = txtItemRemaining.getText();
            String word = "kg";
            String itemNewStock;
            if (tempvar.contains(word)){
                tempvar = tempvar.replace(word, "");
                float floatItemRemaining = Float.parseFloat(tempvar);
                float floatItemAddStock = Float.parseFloat(txtItemAdd.getText());
                float floatItemStock = floatItemRemaining + floatItemAddStock;
                itemNewStock = Float.toString(floatItemStock);
            } else {
                int intItemRemaining = Integer.parseInt(tempvar);
                int intItemAddStock = Integer.parseInt(txtItemAdd.getText());
                int intItemStock = intItemRemaining + intItemAddStock;
                itemNewStock = Integer.toString(intItemStock);
            }
            
            
            Statement st = conn.createStatement();
            
            st.executeUpdate("UPDATE tbl_items SET Item_Remaining='"+ itemNewStock + txtItemUnit.getText() +"' "
                    + "WHERE Item_Id="+ itemId +"");
            return false;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "WRONG INPUT \n\n Stock(s) to Add = Number Only.", "FAILED", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        return true;
    }
    
}
