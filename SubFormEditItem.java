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
 * @author Siblings Solutions
 */
public class SubFormEditItem extends JPanel{
    JLabel lblTitle, lblItemId, lblItemName, lblItemDesc, lblMinOrder, lblItemRemaining;
    JTextField txtItemId, txtItemName, txtItemDesc, txtMinOrder, txtItemRemaining, txtItemUnit;
    
    public SubFormEditItem(int itemId, String itemName, String itemDesc, String itemMinOrder, String itemRemaining){
        lblTitle = new JLabel("Edit Selected Item");
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblItemId = new JLabel("Item ID");
        lblItemName = new JLabel("Item Name");
        lblItemDesc = new JLabel("Item Description");
        lblMinOrder = new JLabel("Minimum Order");
        lblItemRemaining = new JLabel("Remaining");
        
        txtItemId = new JTextField();
        txtItemId.setPreferredSize(new Dimension(200, 30));
        txtItemId.setEditable(false);
        txtItemName = new JTextField();
        txtItemName.setPreferredSize(new Dimension(200, 30));
        txtItemDesc = new JTextField();
        txtItemDesc.setPreferredSize(new Dimension(200, 30));
        txtMinOrder = new JTextField();
        txtMinOrder.setPreferredSize(new Dimension(200, 30));
        txtItemRemaining = new JTextField();
        txtItemRemaining.setPreferredSize(new Dimension(100, 30));
        txtItemUnit = new JTextField();
        txtItemUnit.setPreferredSize(new Dimension(100, 30));
        txtItemUnit.setEditable(false);
        
        txtItemId.setText(Integer.toString(itemId));
        txtItemName.setText(itemName);
        txtItemDesc.setText(itemDesc);
        txtMinOrder.setText(itemMinOrder);
        String word = "kg";
        if(itemRemaining.contains(word)){
            itemRemaining = itemRemaining.replace(word, "");
            txtItemUnit.setText(word);
        }
        txtItemRemaining.setText(itemRemaining);
        
        JPanel remPanel = new JPanel();
        remPanel.add(txtItemRemaining);
        remPanel.add(txtItemUnit);
        
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
        
        hg1.addComponent(lblItemId);
        hg1.addComponent(lblItemName);
        hg1.addComponent(lblItemDesc);
        hg1.addComponent(lblMinOrder);
        hg1.addComponent(lblItemRemaining);
        
        hg2.addComponent(txtItemId);
        hg2.addComponent(txtItemName);
        hg2.addComponent(txtItemDesc);
        hg2.addComponent(txtMinOrder);
        hg2.addComponent(remPanel);
        
        vg1.addComponent(lblItemId);
        vg1.addComponent(txtItemId);
        
        vg2.addComponent(lblItemName);
        vg2.addComponent(txtItemName);
        
        vg3.addComponent(lblItemDesc);
        vg3.addComponent(txtItemDesc);
        
        vg4.addComponent(lblMinOrder);
        vg4.addComponent(txtMinOrder);
        
        vg5.addComponent(lblItemRemaining);
        vg5.addComponent(remPanel);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        vseq1.addGroup(vg4);
        vseq1.addGroup(vg5);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        setLayout(new BorderLayout());
        
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    protected boolean _updateItem(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        try {
            String itemId = txtItemId.getText();
            String itemName = txtItemName.getText();
            String itemDesc = txtItemDesc.getText();
            String itemRemaining;
            if ("".equals(txtItemUnit.getText())){
                int intItemRem = Integer.parseInt(txtItemRemaining.getText());
                itemRemaining = Integer.toString(intItemRem);
            } else {
                float floatItemRem = Float.parseFloat(txtItemRemaining.getText());
                itemRemaining = Float.toString(floatItemRem);
            }
            
            String itemUnit = txtItemUnit.getText();
            
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_items SET Item_Name='"+ itemName.toLowerCase() +"', Item_Description='"+ itemDesc.toLowerCase() +"',"
                    + "Item_Remaining='"+ itemRemaining + itemUnit +"', Item_MinOrder='"+txtMinOrder.getText()+"'"
                    + "WHERE Item_Id="+ itemId +"");
            return false;
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "WRONG INPUT \n\n Item Remaining = Number Only.", "FAILED", JOptionPane.ERROR_MESSAGE);
        } catch ( SQLException ex){
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
