/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
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
public class FormAddDeduction extends JInternalFrame{
    private final JTextField txtId, txtName, txtAmount;
    private final JTextArea txtDescription;
    private final JButton btnSearch, btnAdd;
    
    public FormAddDeduction(){
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblDescription = new JLabel("Description");
        JLabel lblAmount = new JLabel("Amount (Php)");
        JLabel lblBlank1 = new JLabel("");
        JLabel lblBlank2 = new JLabel("");
        JLabel lblBlank3 = new JLabel("");
        btnSearch = new JButton(">");
        btnSearch.addActionListener(new ButtonFunction());
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(150, 36));
        txtName = new JTextField();
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(150, 36));
        txtAmount = new JTextField();
        txtAmount.setPreferredSize(new Dimension(150, 36));
        txtAmount.addKeyListener(new KeyAdapter() {
            TextFieldFilter textFieldFilter = new TextFieldFilter();
            @Override
            public void keyTyped(KeyEvent e) {
                textFieldFilter._numbersPeriodComma(e);
            }
        });
        txtDescription = new JTextArea();
        txtDescription.setWrapStyleWord(true);
        txtDescription.setLineWrap(true);
        JScrollPane descriptionScrollPane = new JScrollPane(txtDescription);
        descriptionScrollPane.setPreferredSize(new Dimension(150, 60));
        
        JPanel centerPanel = new JPanel();
        GroupLayout layout = new GroupLayout(centerPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        centerPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group hg3 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        GroupLayout.Group vg3 = layout.createParallelGroup();
        GroupLayout.Group vg4 = layout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        hg1.addComponent(lblDescription);
        hg1.addComponent(lblAmount);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(descriptionScrollPane);
        hg2.addComponent(txtAmount);
        
        hg3.addComponent(btnSearch);
        hg3.addComponent(lblBlank1);
        hg3.addComponent(lblBlank2);
        hg3.addComponent(lblBlank3);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg1.addComponent(btnSearch);
        
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg2.addComponent(lblBlank1);
        
        vg3.addComponent(lblDescription);
        vg3.addComponent(descriptionScrollPane);
        vg3.addComponent(lblBlank2);
        
        vg4.addComponent(lblAmount);
        vg4.addComponent(txtAmount);
        vg4.addComponent(lblBlank3);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        hseq1.addGroup(hg3);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        vseq1.addGroup(vg4);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        btnAdd = new JButton("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(container);
        setTitle("Add Deduction");
        setBounds(300, 100, 330, 240);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSearch){
                _search();
            } else if (e.getSource() == btnAdd){
                _add();
            }
        }
        
        private void _search(){
            SubFormEmployees subFormEmployees = new SubFormEmployees();
            subFormEmployees._loadTableData();
            if(JOptionPane.showConfirmDialog(null, subFormEmployees, "Employees", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                String[] value = subFormEmployees._getValues();
                if(!value[0].equals("")){
                    txtId.setText(value[0]);
                    txtName.setText(value[1]);
                    btnAdd.setEnabled(true);
                }
            }
        }
        
        private void _add(){
            NumberFunction nFunction = new NumberFunction();
            if(txtDescription.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Description is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else if(txtAmount.getText().equals("") || nFunction._stripValue(txtAmount.getText())<=0){
                JOptionPane.showMessageDialog(null, "Amount is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                if(_goAdd()){
                    txtId.setText("");
                    txtName.setText("");
                    txtDescription.setText("");
                    txtAmount.setText("");
                    btnAdd.setEnabled(false);
                }
            }
        }
        
        private boolean _goAdd(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                String description = sFunction._removeWhiteSpaces(txtDescription.getText().trim());
                String id = txtId.getText().trim();
                float amount = nFunction._stripValue(txtAmount.getText().trim());
                String date  = _getFormattedDate();
                
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_deductions (Employee_Id, Deduction_Description,"
                        + " Deduction_Amount, Deduction_Date)"
                        + " VALUES ('"+id+"', '"+description.trim()+"',"
                        + " '"+amount+"', '"+date+"')");
                JOptionPane.showMessageDialog(null, "Success", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        
        private String _getFormattedDate(){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = new Date();
            return sdf1.format(date);
        }
        
    }
    
}
