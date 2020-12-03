/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author IT-NEW
 */
public class FormAddPurchaseItem extends JInternalFrame{
    JTextField txtItem, txtMinOrder;
    JComboBox cmbStore;
    JButton btnSave;
    
    public FormAddPurchaseItem(){
        JLabel lblTitle = new JLabel("Add Purchase Item");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblItem = new JLabel("Item");
        JLabel lblStore = new JLabel("Store");
        JLabel lblMinOrder = new JLabel("Minimum Order");
        txtItem = new JTextField();
        txtMinOrder = new JTextField();
        cmbStore = new JComboBox();
        
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
        
        hg1.addComponent(lblItem);
        hg1.addComponent(lblStore);
        hg1.addComponent(lblMinOrder);
        
        hg2.addComponent(txtItem);
        hg2.addComponent(cmbStore);
        hg2.addComponent(txtMinOrder);
        
        vg1.addComponent(lblItem);
        vg1.addComponent(txtItem);
        
        vg2.addComponent(lblStore);
        vg2.addComponent(cmbStore);
        
        vg3.addComponent(lblMinOrder);
        vg3.addComponent(txtMinOrder);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        addInternalFrameListener(new WindowFunction());
        
        setContentPane(container);
        setTitle("Add Purchase Item");
        setBounds(30, 30, 300, 230);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class WindowFunction implements InternalFrameListener{

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            _loadComboData();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private void _loadComboData(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            cmbStore.removeAllItems();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Store_Name FROM tbl_store");
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
        
    }
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSave){
                _save();
            }
        }
        
        private void _save(){
            if (txtItem.getText().equals("") || txtMinOrder.getText().equals("")){
                JOptionPane.showMessageDialog(null, "All field is required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                int StoreId = _getStoreId();
                if (StoreId == 0){
                    JOptionPane.showMessageDialog(null, "nFAILED!! \n Store: Data did not exists", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    if(_ifItemExists(StoreId)){
                        JOptionPane.showMessageDialog(null, "Item already exists", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String id = _getMaxId();
                        _goSave(id, StoreId);
                    }
                    
                }
            }
        }
        
        private boolean _ifItemExists(int storeId) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT PurchaseItem_Id FROM tbl_purchaselist"
                        + " WHERE PurchaseItem_Name='"+txtItem.getText().trim().toLowerCase()+"'"
                        + " AND Store_Id='"+storeId+"'");
                if(rs.next()){
                    conn.close();
                    return true;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return false;
        }
        
        private void _goSave(String id, int StoreId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                StringFunction sFunction = new StringFunction();
                String item = sFunction._removeWhiteSpaces(txtItem.getText().toLowerCase().trim());
                String minOrder = sFunction._removeWhiteSpaces(txtMinOrder.getText().trim());
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_purchaselist (PurchaseItem_Id, PurchaseItem_Name, Store_Id, PurchaseItem_MinOrder)"
                        + " VALUES ('"+id+"', '"+item.trim()+"', "+StoreId+", '"+minOrder.trim()+"')");
                st.executeUpdate("INSERT INTO tbl_list (PurchaseItem_Id)"
                        + " VALUES ('"+id+"')");
                JOptionPane.showMessageDialog(null, "Successfully Saved", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
        
        private String _getMaxId(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String id = null;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Max(PurchaseItem_Id) FROM tbl_purchaselist");
                if(rs.next()){
                    id = rs.getString("Max(PurchaseItem_Id)");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
            if (id == null){
                id = "1";
            } else {
                int temp = Integer.parseInt(id);
                temp += 1;
                id = Integer.toString(temp);
            }
            
            return id;
        }
        
        private int _getStoreId(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            int value=0;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Store_Id From tbl_store"
                        + " WHERE Store_Name='"+cmbStore.getSelectedItem().toString().toLowerCase()+"'");
                if(rs.next()){
                    value = rs.getInt("Store_Id");
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
    
}
