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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-Earl
 */
public class SubFormAddCashAdvance extends JPanel{
    private final JTextField txtId, txtName, txtAmount;
    
    public SubFormAddCashAdvance(String id, String name){
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblAmount = new JLabel("Amount (Php)");
        txtId = new JTextField(id);
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(150, 25));
        txtName = new JTextField(name);
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(150, 25));
        txtAmount = new JTextField();
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
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        hg1.addComponent(lblAmount);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(txtAmount);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg3.addComponent(lblAmount);
        vg3.addComponent(txtAmount);
        
        GroupLayout.SequentialGroup hseq = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq = layout.createSequentialGroup();
        
        hseq.addGroup(hg1);
        hseq.addGroup(hg2);
        vseq.addGroup(vg1);
        vseq.addGroup(vg2);
        vseq.addGroup(vg3);
        
        layout.setHorizontalGroup(hseq);
        layout.setVerticalGroup(vseq);
        
        add(centerPanel);
    }
    
    protected boolean _add(){
        NumberFunction nFunction = new NumberFunction();
        float amount = nFunction._stripValue(txtAmount.getText());
        
        if(txtAmount.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "*Amount is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else if(amount == 0){
            JOptionPane.showMessageDialog(null, "*Amount must be greater than ZERO", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            _goAdd();
            return true;
        }
        return false;
    }
    
    private void _goAdd(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            NumberFunction nFunction = new NumberFunction();
            float amount = nFunction._stripValue(txtAmount.getText());
            Date currenDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Statement st = conn.createStatement();
            st.executeUpdate("ALTER TABLE tbl_cashadvance"
                    + " auto_increment=1");
            st.executeUpdate("INSERT INTO tbl_cashadvance (Employee_Id, CashAdvance_Amount, CashAdvance_Date)"
                    + " VALUES ('"+txtId.getText()+"', '"+amount+"', '"+sdf.format(currenDate)+"')");
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
