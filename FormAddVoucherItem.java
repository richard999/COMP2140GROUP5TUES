/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class FormAddVoucherItem extends JInternalFrame{
    private final JTextField txtVoucherItem;
    private final JButton btnSave;
    
    public FormAddVoucherItem(){
        JLabel lblTitle = new JLabel("Add Voucher Item");
        Font newFont = lblTitle.getFont();
        lblTitle.setFont(new Font(newFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblVoucherItem = new JLabel("Item");
        txtVoucherItem = new JTextField();
        txtVoucherItem.setPreferredSize(new Dimension(150, 25));
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(lblVoucherItem);
        centerPanel.add(txtVoucherItem);
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(new ButtonFunction());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnSave);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(container);
        setTitle("Add Voucher Item");
        setBounds(50, 10, 300, 150);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSave){
                _save();
            }
        }
        
        private void _save(){
            if(txtVoucherItem.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "Field is required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                if(_ifItemExist(txtVoucherItem.getText())){
                    JOptionPane.showMessageDialog(null, "Item already existed", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if(_saveVoucherItem()){
                        JOptionPane.showMessageDialog(null, "Successfully Saved.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                        txtVoucherItem.setText("");
                    }
                }
            }
                
        }
        
        private boolean _saveVoucherItem(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                StringFunction sFunction = new StringFunction();
                String voucherItem = sFunction._removeWhiteSpaces(txtVoucherItem.getText().toLowerCase().trim());
                String id = _getMaxId();
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_voucheritem (VoucherItem_Id, VoucherItem_Name)"
                        + " VALUES ("+id+",'"+voucherItem.trim()+"')");
                st.executeUpdate("INSERT INTO tbl_particulars (VoucherItem_Id)"
                        + " VALUES ('"+id+"')");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            return true;
        }
        
        private String _getMaxId(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String value = null;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Max(VoucherItem_Id) FROM tbl_voucheritem");
                if(rs.next()){
                    value = rs.getString("Max(VoucherItem_Id)");
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
            
            if(value == null){
                value = "1";
            } else {
                int temp = Integer.parseInt(value);
                temp +=1;
                value = Integer.toString(temp);
            }
            
            return value;
        }
        
        private boolean _ifItemExist(String itemName){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT VoucherItem_Id FROM tbl_voucheritem"
                        + " WHERE VoucherItem_Name='"+itemName.toLowerCase()+"'");
                if(rs.next()){
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
        
    }
    
}
