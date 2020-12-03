/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormEditVoucherItem extends JPanel{
    private final JTextField txtId, txtItem;
    
    public SubFormEditVoucherItem(String id, String item) {
        JLabel lblId = new JLabel("ID");
        JLabel lblItem = new JLabel("Item");
        
        txtId = new JTextField(id);
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(150, 25));
        txtItem = new JTextField(item);
        txtItem.setPreferredSize(new Dimension(150, 25));
        
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblItem);
        hg2.addComponent(txtId);
        hg2.addComponent(txtItem);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg2.addComponent(lblItem);
        vg2.addComponent(txtItem);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        add(lblId);
        add(txtId);
        add(lblItem);
        add(txtItem);
        
    }
    
    protected void _updateVoucherItem(){
        if(txtItem.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "FAILED! \n \n tem Required!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            _goUpdateVoucherItem();
        }
    }
    
    private void _goUpdateVoucherItem(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            String item = txtItem.getText().trim().toLowerCase();
            String id = txtId.getText();
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_voucheritem SET VoucherItem_Name='"+item+"'"
                    + " WHERE VoucherItem_Id='"+id+"'");
            JOptionPane.showMessageDialog(null, "Updated Successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            String message = "FAILED! \n\n " + ex.getMessage();
            JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}
