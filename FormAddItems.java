/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author ITStaff
 */
public class FormAddItems extends JInternalFrame {
    private final JLabel lblTitle, lblItemName, lblItemDesc, lblMinOrder, lblKilo;
    private final JTextField txtItemName, txtItemDesc, txtMinOrder, txtKilo, txtQuantity;
    private final JCheckBox chkQuantity;
    private final JButton btnAdd;
    
    public FormAddItems(){
        lblTitle = new JLabel("Add New Item");
        Font newFont = lblTitle.getFont();
        lblTitle.setFont(new Font(newFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblItemName = new JLabel("Item Name");
        lblItemDesc = new JLabel("Item Description");
        lblMinOrder = new JLabel("Minimum Order");
        txtItemName = new JTextField();
        txtItemName.setPreferredSize(new Dimension(200,25));
        txtItemDesc = new JTextField();
        txtItemDesc.setPreferredSize(new Dimension(200,25));
        txtMinOrder = new JTextField();
        txtMinOrder.setPreferredSize(new Dimension(200,25));
        
        JPanel firstPanel = new JPanel();
        GroupLayout layout = new GroupLayout(firstPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        firstPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        GroupLayout.Group vg3 = layout.createParallelGroup();
        
        hg1.addComponent(lblItemName);
        hg1.addComponent(lblItemDesc);
        hg1.addComponent(lblMinOrder);
        
        hg2.addComponent(txtItemName);
        hg2.addComponent(txtItemDesc);
        hg2.addComponent(txtMinOrder);
        
        vg1.addComponent(lblItemName);
        vg1.addComponent(txtItemName);
        
        vg2.addComponent(lblItemDesc);
        vg2.addComponent(txtItemDesc);
        
        vg3.addComponent(lblMinOrder);
        vg3.addComponent(txtMinOrder);
        
        GroupLayout.SequentialGroup hseq = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq = layout.createSequentialGroup();
        
        hseq.addGroup(hg1);
        hseq.addGroup(hg2);
        
        vseq.addGroup(vg1);
        vseq.addGroup(vg2);
        vseq.addGroup(vg3);
        
        layout.setHorizontalGroup(hseq);
        layout.setVerticalGroup(vseq);
        
        lblKilo = new JLabel("Kilogram(s)");
        chkQuantity = new JCheckBox("Quantity");
        txtKilo = new JTextField();
        txtKilo.setPreferredSize(new Dimension(60,20));
        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(60,20));
        txtQuantity.setEnabled(false);
        chkQuantity.addItemListener(new CheckBoxFunction());
        
        JPanel secondPanel = new JPanel();
        secondPanel.add(lblKilo);
        secondPanel.add(txtKilo);
        secondPanel.add(chkQuantity);
        secondPanel.add(txtQuantity);
        
        JPanel centerPanel  = new JPanel();
        centerPanel.add(firstPanel);
        centerPanel.add(secondPanel);
        
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ButtonFunction());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(container);
        setTitle("Add New Item");
        setBounds(10, 10, 350, 260);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class CheckBoxFunction implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getSource() == chkQuantity){
                if(chkQuantity.isSelected()){
                    txtKilo.setEnabled(false);
                    txtQuantity.setEnabled(true);
                } else {
                    txtKilo.setEnabled(true);
                    txtQuantity.setEnabled(false);
                }
            }
        }
        
    }
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAdd){
                String itemName = txtItemName.getText().trim();
                String itemMinOrder = txtMinOrder.getText().trim();
                if(itemName.trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Item Name is required", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if(itemMinOrder.trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Minimum Order is required", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if(_itemExist(itemName, itemMinOrder)){
                        JOptionPane.showMessageDialog(null, "Item already exists", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String itemMaxId = _getItemMaxId();
                        _saveItem(itemMaxId);
                    }
                }
            }
        }
        
        private boolean _itemExist(String itemName, String itemMinOrder){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            ResultSet rs;
            try {
                Statement st = conn.createStatement();
                rs = st.executeQuery("SELECT Item_Id FROM tbl_items"
                        + " WHERE Item_Name='"+itemName.toLowerCase()+"'"
                        + " AND Item_MinOrder='"+itemMinOrder+"'");
                if (rs.next()){
                    conn.close();
                    return true;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            return false;
        }
        
        private String _getItemMaxId(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String ItemMaxId = null;
            
            try {
                ResultSet rs;
                Statement st = conn.createStatement();
                rs = st.executeQuery("SELECT MAX(Item_Id) FROM tbl_items");
                if(rs.next()){
                    ItemMaxId = rs.getString("MAX(Item_Id)");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            if (ItemMaxId == null){
                ItemMaxId = "1";
            } else {
                int temp = Integer.parseInt(ItemMaxId);
                temp+=1;
                ItemMaxId = Integer.toString(temp);
            }
            
            return ItemMaxId;
        }
        
        private void _saveItem(String itemMaxId){
            try{
                StringFunction sFunction = new StringFunction();
                String itemName = sFunction._removeWhiteSpaces(txtItemName.getText().trim());
                String itemDesc = sFunction._removeWhiteSpaces(txtItemDesc.getText().trim());
                String itemMinOrder = txtMinOrder.getText().trim();
                String itemStock;
                if(chkQuantity.isSelected()){
                    int intQuantity = Integer.parseInt(txtQuantity.getText().trim());
                    itemStock = Integer.toString(intQuantity);
                } else {
                    float floatKilo = Float.parseFloat(txtKilo.getText().trim());
                    itemStock = Float.toString(floatKilo) + "kg";
                }
                
                if ("0".equals(itemStock) || "0.0".equals(itemStock)){
                    if(chkQuantity.isSelected()){
                        JOptionPane.showMessageDialog(null, "WARNING \n\n Quantity = Input must be greater than ZERO", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "WARNING \n\n Kilogram = Input must be greater than ZERO", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    DatabaseConnection dbConn = new DatabaseConnection();
                    Connection conn = dbConn._getConnection();
                    try {
                        Statement st = conn.createStatement();
                        st.executeUpdate("INSERT INTO tbl_items (Item_Id, Item_Name, Item_Description, Item_Remaining, Item_MinOrder) "
                                + "VALUES ('"+itemMaxId+"', '"+ itemName.toLowerCase() +"', '"+itemDesc.toLowerCase()+"', '"+itemStock+"', '"+itemMinOrder+"')");
                        st.executeUpdate("INSERT INTO tbl_inventory (Item_Id) VALUES ('"+itemMaxId+"')");
                        JOptionPane.showMessageDialog(null, "Item successfully added!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (NumberFormatException | HeadlessException ex) {
                if(chkQuantity.isSelected()){
                    JOptionPane.showMessageDialog(null, "WARNING \n\n Quantity = Numbers Only!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "WARNING \n\n Kilogram = Numbers Only!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
            
        }
        
        
        
    }
    
}
