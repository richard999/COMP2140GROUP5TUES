/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormEditPurchaseItem extends JPanel{
    private final JTextField txtId, txtItem, txtMinOrder;
    private final JComboBox cmbStore;
    
    public SubFormEditPurchaseItem(String id, String item, String store, String minorder){
        JLabel lblId = new JLabel("ID");
        JLabel lblItem = new JLabel("Item");
        JLabel lblStore = new JLabel("Store");
        JLabel lblMinOrder = new JLabel("Minimum Order");
        
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(150, 25));
        txtId.setEditable(false);
        txtItem = new JTextField();
        txtItem.setPreferredSize(new Dimension(150, 25));
        cmbStore = new JComboBox();
        txtMinOrder = new JTextField();
        txtMinOrder.setPreferredSize(new Dimension(150, 25));
        
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
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblItem);
        hg1.addComponent(lblStore);
        hg1.addComponent(lblMinOrder);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtItem);
        hg2.addComponent(cmbStore);
        hg2.addComponent(txtMinOrder);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        
        vg2.addComponent(lblItem);
        vg2.addComponent(txtItem);
        
        vg3.addComponent(lblStore);
        vg3.addComponent(cmbStore);
        
        vg4.addComponent(lblMinOrder);
        vg4.addComponent(txtMinOrder);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        vseq1.addGroup(vg4);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        add(centerPanel);
        
        _loadComboBoxData();
        
        txtId.setText(id);
        txtItem.setText(item);
        cmbStore.setSelectedItem(store);
        txtMinOrder.setText(minorder);
        
    }
    
    private void _loadComboBoxData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        cmbStore.removeAllItems();
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Store_Name from tbl_store");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                cmbStore.addItem(sFunction._Capitalized(rs.getString("Store_Name")));
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
    }
    
    protected void _updatePurchaseItems(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            String storeid = _getStoreId();
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_purchaselist SET PurchaseItem_Name='"+txtItem.getText().toLowerCase()+"',"
                    + " Store_Id="+storeid+", PurchaseItem_MinOrder='"+txtMinOrder.getText()+"'"
                    + " WHERE PurchaseItem_Id="+txtId.getText());
            JOptionPane.showMessageDialog(null, "Success", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
    
    private String _getStoreId(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String value = null;
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Store_Id from tbl_store"
                    + " WHERE Store_Name='"+cmbStore.getSelectedItem().toString().toLowerCase()+"'");
            if(rs.next()){
                value = rs.getString("Store_Id");
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
        
        return value;
    }
}
