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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormEditDeduction extends JPanel{
    private final JTextField txtId, txtName, txtDate, txtAmount;
    private final JTextArea txtDescription;
    
    public SubFormEditDeduction(String id, String name, String date, String desc, String amount) {
        JLabel lblId = new JLabel("Employee ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblDate = new JLabel("Date");
        JLabel lblDescription = new JLabel("Description");
        JLabel lblAmount = new JLabel("Amount");
        txtId = new JTextField(id);
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension (150, 25));
        txtName = new JTextField(name);
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension (150, 25));
        txtDate = new JTextField(date);
        txtDate.setEditable(false);
        txtDate.setPreferredSize(new Dimension (150, 25));
        txtAmount = new JTextField(amount);
        txtAmount.setPreferredSize(new Dimension (150, 25));
        txtDescription = new JTextArea(desc);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setLineWrap(true);
        JScrollPane descriptionScrollPane = new JScrollPane(txtDescription);
        descriptionScrollPane.setPreferredSize(new Dimension(150, 50));
        
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
        hg1.addComponent(lblDate);
        hg1.addComponent(lblDescription);
        hg1.addComponent(lblAmount);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(txtDate);
        hg2.addComponent(descriptionScrollPane);
        hg2.addComponent(txtAmount);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        
        vg3.addComponent(lblDate);
        vg3.addComponent(txtDate);
        
        vg4.addComponent(lblDescription);
        vg4.addComponent(descriptionScrollPane);
        
        vg5.addComponent(lblAmount);
        vg5.addComponent(txtAmount);
        
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
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    protected void _edit(){
        if(txtDescription.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Description is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else if(txtAmount.getText().trim().equals("") || _stripValue(txtAmount.getText()) <= 0){
            JOptionPane.showMessageDialog(null, "Amount is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            _goEdit();
        }
    }
    
    private void _goEdit(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            String date = _getFormattedDate(txtDate.getText());
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_deductions SET Deduction_Description='"+txtDescription.getText()+"',"
                    + " Deduction_Amount='"+_stripValue(txtAmount.getText())+"'"
                    + " WHERE Employee_Id='"+txtId.getText()+"' AND Deduction_Date='"+date+"'");
            JOptionPane.showMessageDialog(null, "Successfully Updated", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
    
    private String _getFormattedDate(String dateToFormat){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException ex) {
            Logger.getLogger(SubFormEditDeduction.class.getName()).log(Level.SEVERE, null, ex);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf1.format(date);
    }
    
    private float _stripValue(String stringToStrip){
        float strippedValue;
        String word = "Php";
        if(stringToStrip.contains(word)){
            stringToStrip = stringToStrip.replace(word, "");
        }
        word = ",";
        if(stringToStrip.contains(word)){
            stringToStrip = stringToStrip.replace(word, "");
        }
        strippedValue = Float.parseFloat(stringToStrip);
        return strippedValue;
    }
}
