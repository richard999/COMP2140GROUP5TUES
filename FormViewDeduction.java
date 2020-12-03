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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewDeduction extends JInternalFrame{
    private final JTextField txtId, txtName;
    private final JButton btnEmployee;
    JButton btnEdit, btnDelete;
    private final JTable tableDeduction;
    DefaultTableModel tableDeductionModel;
    
    public FormViewDeduction() {
        JLabel lblId = new JLabel("Employee ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblBlank = new JLabel("");
        btnEmployee = new JButton(">");
        btnEmployee.addActionListener(new ButtonFunction());
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(150, btnEmployee.getPreferredSize().height));
        txtName = new JTextField();
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(150, btnEmployee.getPreferredSize().height));
        
        JPanel topLeftPanel = new JPanel();
        GroupLayout layout = new GroupLayout(topLeftPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        topLeftPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group hg3 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        
        hg3.addComponent(btnEmployee);
        hg3.addComponent(lblBlank);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg1.addComponent(btnEmployee);
        
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg2.addComponent(lblBlank);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        hseq1.addGroup(hg3);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        
        String[] tableHeader = {
            "#", "Description", "Amount", "Date"
        };
        tableDeductionModel = new DefaultTableModel();
        tableDeductionModel.setColumnIdentifiers(tableHeader);
        tableDeduction = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDeduction.setModel(tableDeductionModel);
        tableDeduction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDeduction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableDeduction.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableDeduction.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableDeduction.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableDeduction.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tableDeduction);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 36));
        btnEdit.addActionListener(new ButtonFunction());
        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(new ButtonFunction());
        
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(110, scrollPane.getPreferredSize().height));
        rightPanel.add(btnEdit);
        rightPanel.add(btnDelete);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
        
        setContentPane(container);
        setTitle("View Deduction");
        setBounds(400, 10, 810, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnEmployee){
                _employee();
            } else if(e.getSource() == btnDelete){
                _delete();
            } else if(e.getSource() == btnEdit){
                _edit();
            }
        }
        
        private void _employee(){
            SubFormEmployees subFormEmployees = new SubFormEmployees();
            subFormEmployees._loadTableData();
            if(JOptionPane.showConfirmDialog(null, subFormEmployees, "Employees", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                String[] value = subFormEmployees._getValues();
                if(!value[0].equals("")){
                    txtId.setText(value[0]);
                    txtName.setText(value[1]);
                    _getEmployeeDeductions(txtId.getText());
                }
            }
        }
        
        private void _getEmployeeDeductions(String id){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableDeductionModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Deduction_Description, ROUND(Deduction_Amount, 2),"
                        + " Deduction_Date FROM tbl_deductions"
                        + " WHERE Employee_Id='"+id+"'");
                NumberFunction nFunction = new NumberFunction();
                int i=0;
                while(rs.next()){
                    i++; 
                    String desc = rs.getString("Deduction_Description");
                    String amount = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(Deduction_Amount, 2)"));
                    String date = _formatDate(rs.getString("Deduction_Date"));
                    tableDeductionModel.addRow(new Object[]{i, desc, amount, date});
                }
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
        
        private String _formatDate(String dateToFormat){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(dateToFormat);
            } catch (ParseException ex) {
                Logger.getLogger(FormViewDeduction.class.getName()).log(Level.SEVERE, null, ex);
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            return sdf1.format(date);
        }
        
        private String _formatMysqlDate(String dateToFormat){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(dateToFormat);
            } catch (ParseException ex) {
                Logger.getLogger(FormViewDeduction.class.getName()).log(Level.SEVERE, null, ex);
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return sdf1.format(date);
        }
        
        private void _delete(){
            try {
                String date = _formatMysqlDate(tableDeduction.getValueAt(tableDeduction.getSelectedRow(), 3).toString());
                if(JOptionPane.showConfirmDialog(null, "DELETE \n \nAre you sure?", "???", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    _goDelete(txtId.getText(), date);
                    tableDeductionModel.removeRow(tableDeduction.getSelectedRow());
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select from the table", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        private void _goDelete(String employeeId, String date){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_deductions"
                        + " WHERE Employee_Id='"+employeeId+"' AND"
                        + " Deduction_Date='"+date+"'");
                JOptionPane.showMessageDialog(null, "Successfully Deleted", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
        
        private void _edit(){
            try{
                SubFormEditDeduction subFormEditDeduction = new SubFormEditDeduction(
                        txtId.getText(),
                        txtName.getText(),
                        tableDeduction.getValueAt(tableDeduction.getSelectedRow(), 3).toString(),
                        tableDeduction.getValueAt(tableDeduction.getSelectedRow(), 1).toString(),
                        tableDeduction.getValueAt(tableDeduction.getSelectedRow(), 2).toString()
                );
                if(JOptionPane.showConfirmDialog(null, subFormEditDeduction, "Edit Deduction", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    subFormEditDeduction._edit();
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select from the table", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
    }
    
}
