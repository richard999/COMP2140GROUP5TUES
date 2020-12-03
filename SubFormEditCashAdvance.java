/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
 * @author IT-Earl
 */
public class SubFormEditCashAdvance extends JPanel{
    private final JTextField txtId, txtName, txtCashId, txtDate, txtAmount;
    
    public SubFormEditCashAdvance(String id, String name, String paymentId, String paymentDate, float amount){
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblCashId = new JLabel("Payment Id");
        JLabel lblDate = new JLabel("Date");
        JLabel lblAmount = new JLabel("Amount (Php)");
        txtId = new JTextField(id);
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(150, 25));
        txtName = new JTextField(name);
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(150, 25));
        txtCashId = new JTextField(paymentId);
        txtCashId.setEditable(false);
        txtCashId.setPreferredSize(new Dimension(150, 25));
        txtDate = new JTextField(paymentDate);
        txtDate.setEditable(false);
        txtDate.setPreferredSize(new Dimension(150, 25));
        txtAmount = new JTextField(Float.toString(amount));
        txtAmount.setPreferredSize(new Dimension(150, 25));
        txtAmount.addKeyListener(new KeyAdapter(){
            TextFieldFilter textFieldFilter = new TextFieldFilter();
            @Override
            public void keyTyped(KeyEvent e) {
                textFieldFilter._numbersPeriodComma(e);
            }
            
        });
        
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
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        hg1.addComponent(lblCashId);
        hg1.addComponent(lblDate);
        hg1.addComponent(lblAmount);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(txtCashId);
        hg2.addComponent(txtDate);
        hg2.addComponent(txtAmount);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg3.addComponent(lblCashId);
        vg3.addComponent(txtCashId);
        vg4.addComponent(lblDate);
        vg4.addComponent(txtDate);
        vg5.addComponent(lblAmount);
        vg5.addComponent(txtAmount);
        
        GroupLayout.SequentialGroup hseq = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq = layout.createSequentialGroup();
        
        hseq.addGroup(hg1);
        hseq.addGroup(hg2);
        vseq.addGroup(vg1);
        vseq.addGroup(vg2);
        vseq.addGroup(vg3);
        vseq.addGroup(vg4);
        vseq.addGroup(vg5);
        
        layout.setHorizontalGroup(hseq);
        layout.setVerticalGroup(vseq);
        
        add(centerPanel);
    }
    
    protected boolean _edit(){
        NumberFunction nFunction = new NumberFunction();
        float amount = nFunction._stripValue(txtAmount.getText());
        
        if(txtAmount.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "*Amount is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else if(amount == 0){
            JOptionPane.showMessageDialog(null, "*Amount must be greater than ZERO", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            _goEdit();
            return true;
        }
        return false;
    }
    
    protected void _goEdit(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            NumberFunction nFunction = new NumberFunction();
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_cashadvance SET CashAdvance_Amount='"+nFunction._stripValue(txtAmount.getText())+"'"
                    + " WHERE Employee_Id='"+txtId.getText()+"' AND CashAdvance_Id='"+txtCashId.getText()+"'");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
