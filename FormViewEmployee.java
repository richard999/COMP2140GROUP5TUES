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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewEmployee extends JInternalFrame{
    private final JTextField txtFilter;
    private final JTable tableEmployees;
    DefaultTableModel tableEmployeesModel;
    JButton btnEdit;
    
    public FormViewEmployee(){
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(150, 25));
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _filterData();
            }
        });
        
        JPanel filterPanel = new JPanel();
        filterPanel.add(lblFilter);
        filterPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Name", "Address", "Contact #", "Hourly Rate", "Date Hired", 
            "Position", "SSS #", "Status", "Time In", "Time Out", "Time Password"
        };
        tableEmployeesModel = new DefaultTableModel();
        tableEmployeesModel.setColumnIdentifiers(tableHeader);
        tableEmployees = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEmployees.setModel(tableEmployeesModel);
        tableEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEmployees.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableEmployees.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableEmployees.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableEmployees.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableEmployees.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableEmployees.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableEmployees.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableEmployees.getColumnModel().getColumn(6).setPreferredWidth(150);
        tableEmployees.getColumnModel().getColumn(7).setPreferredWidth(150);
        tableEmployees.getColumnModel().getColumn(8).setPreferredWidth(100);
        tableEmployees.getColumnModel().getColumn(9).setPreferredWidth(100);
        tableEmployees.getColumnModel().getColumn(10).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableEmployees);
        
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 36));
        btnEdit.addActionListener(new ButtonFunction());
        /*btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(new ButtonFunction());*/
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(110, scrollPane.getPreferredSize().height));
        buttonPanel.add(btnEdit);
        //buttonPanel.add(btnDelete);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(filterPanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                _loadTableData();
            }
        });
        
        setContentPane(container);
        setTitle("View Employees");
        setBounds(100, 100, 1000, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableEmployeesModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_employees.Employee_Id, tbl_employees.Employee_Name,"
                    + " tbl_employees.Employee_Address, tbl_employees.Employee_ContactNo,"
                    + " ROUND(tbl_employees.Employee_HourlyRate, 2), tbl_employees.Employee_DateHired,"
                    + " tbl_employees.Employee_Position, tbl_employees.Employee_SSSNo,"
                    + " tbl_employeestatus.EmployeeStatus_Status, tbl_employees.Employee_TimeIn,"
                    + " tbl_employees.Employee_TimeOut, tbl_employees.Employee_TimeInPassword"
                    + " FROM tbl_employees INNER JOIN tbl_employeestatus"
                    + " ON tbl_employees.EmployeeStatus_Id=tbl_employeestatus.EmployeeStatus_Id");
            NumberFunction nFunction = new NumberFunction();
            while(rs.next()){
                String id = rs.getString("tbl_employees.Employee_Id");
                String name = rs.getString("tbl_employees.Employee_Name");
                String address = rs.getString("tbl_employees.Employee_Address");
                String contact = rs.getString("tbl_employees.Employee_ContactNo");
                String hourlyrate = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_employees.Employee_HourlyRate, 2)"));
                String datehired = _formatDate(rs.getString("tbl_employees.Employee_DateHired"));
                String position = rs.getString("tbl_employees.Employee_Position");
                String sss = rs.getString("tbl_employees.Employee_SSSNo");
                String status = rs.getString("tbl_employeestatus.EmployeeStatus_Status");
                String timein = _formatTime(rs.getString("tbl_employees.Employee_TimeIn"));
                String timeout = _formatTime(rs.getString("tbl_employees.Employee_TimeOut"));
                String timeinpass = rs.getString("tbl_employees.Employee_TimeInPassword");
                tableEmployeesModel.addRow(new Object[]{id, name, address, contact, hourlyrate, 
                    datehired, position, sss, status, timein, timeout, timeinpass});
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
    
    private void _filterData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableEmployeesModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_employees.Employee_Id, tbl_employees.Employee_Name,"
                    + " tbl_employees.Employee_Address, tbl_employees.Employee_ContactNo,"
                    + " ROUND(tbl_employees.Employee_HourlyRate, 2), tbl_employees.Employee_DateHired,"
                    + " tbl_employees.Employee_Position, tbl_employees.Employee_SSSNo,"
                    + " tbl_employeestatus.EmployeeStatus_Status, tbl_employees.Employee_TimeIn,"
                    + " tbl_employees.Employee_TimeOut, tbl_employees.Employee_TimeInPassword"
                    + " FROM tbl_employees INNER JOIN tbl_employeestatus"
                    + " ON tbl_employees.EmployeeStatus_Id=tbl_employeestatus.EmployeeStatus_Id"
                    + " WHERE tbl_employees.Employee_Id LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_employees.Employee_Name LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_employees.Employee_Position LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_employeestatus.EmployeeStatus_Status LIKE '%"+txtFilter.getText().trim()+"%'");
            NumberFunction nFunction = new NumberFunction();
            while(rs.next()){
                String id = rs.getString("tbl_employees.Employee_Id");
                String name = rs.getString("tbl_employees.Employee_Name");
                String address = rs.getString("tbl_employees.Employee_Address");
                String contact = rs.getString("tbl_employees.Employee_ContactNo");
                String hourlyrate = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_employees.Employee_HourlyRate, 2)"));
                String datehired = _formatDate(rs.getString("tbl_employees.Employee_DateHired"));
                String position = rs.getString("tbl_employees.Employee_Position");
                String sss = rs.getString("tbl_employees.Employee_SSSNo");
                String status = rs.getString("tbl_employeestatus.EmployeeStatus_Status");
                String timein = _formatTime(rs.getString("tbl_employees.Employee_TimeIn"));
                String timeout = _formatTime(rs.getString("tbl_employees.Employee_TimeOut"));
                String timeinpass = rs.getString("tbl_employees.Employee_TimeInPassword");
                tableEmployeesModel.addRow(new Object[]{id, name, address, contact, hourlyrate, 
                    datehired, position, sss, status, timein, timeout, timeinpass});
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
    
    private String _formatDate(String date){
        Date formattedDate = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            formattedDate = df.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(FormViewEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        
        return df1.format(formattedDate);
    }
    
    private String _formatTime(String time){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date newTime = null;
        try {
            newTime = timeFormat.parse(time);
        } catch (ParseException ex) {
            Logger.getLogger(FormViewEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        DateFormat timeFormat1 = new SimpleDateFormat("hh:mm a");
        
        return timeFormat1.format(newTime);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnEdit){
                _edit();
            }
            
        }
        
        private void _edit(){
            try{
                SubFormEditEmployee subFormEditEmployee = new SubFormEditEmployee(
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 1).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 2).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 3).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 5).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 6).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 4).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 7).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 8).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 9).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 10).toString(),
                        tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 11).toString()
                );
                if(JOptionPane.showConfirmDialog(null, subFormEditEmployee, "Edit Employee", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    subFormEditEmployee._edit();
                    _loadTableData();
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an Item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
            
        }
        
        /*private void _delete(){
            try {
                String employeeId = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
                _goDelete(employeeId);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }*/
        
        /*private void _goDelete(String employeeId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_employees WHERE Employee_Id='"+employeeId+"'");
                JOptionPane.showMessageDialog(null, "DELETED Successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }*/
        
    }
    
}
