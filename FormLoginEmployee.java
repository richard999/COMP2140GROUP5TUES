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
import java.text.DateFormat;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class FormLoginEmployee extends JInternalFrame{
    private final JTextField txtId, txtTimePassword;
    
    public FormLoginEmployee(){
        JLabel lblTitle = new JLabel("Login");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 30 ));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblId = new JLabel("ID No. ");
        lblId.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        txtId = new JTextField();
        txtId.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        txtId.setPreferredSize(new Dimension(150, txtId.getPreferredSize().height));
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        txtTimePassword = new JPasswordField();
        txtTimePassword.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        txtTimePassword.setPreferredSize(new Dimension(150, txtId.getPreferredSize().height));
        
        JPanel centerPanel = new JPanel();
        GroupLayout layout = new GroupLayout(centerPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        centerPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblPassword);
        hg2.addComponent(txtId);
        hg2.addComponent(txtTimePassword);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg2.addComponent(lblPassword);
        vg2.addComponent(txtTimePassword);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(container);
        setTitle("Login");
        setBounds(100, 100, 290, 200);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    

    private class ButtonFunction implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            _login();
        }
        
        private void _login(){
            String timepassword = txtTimePassword.getText();
            String id = _getEmployeeId(txtId.getText(), timepassword);
            if(id == null){
                JOptionPane.showMessageDialog(null, "Unknow ID or Password is incorrect", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                if(_employeeIsActive(id, timepassword)){
                    if(_alreadyLoggedIn(id)){
                        JOptionPane.showMessageDialog(null, "Already LOGGED IN", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if(JOptionPane.showConfirmDialog(null, "Are your sure?", "LOGIN", JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                            _saveTime(id);
                            txtId.setText("");
                            txtTimePassword.setText("");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Employee is INACTIVE", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
                
        }
        
        private boolean _employeeIsActive(String id, String timepassword){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_employeestatus.EmployeeStatus_Status"
                        + " FROM tbl_employeestatus INNER JOIN tbl_employees"
                        + " ON tbl_employeestatus.EmployeeStatus_Id=tbl_employees.EmployeeStatus_Id"
                        + " WHERE tbl_employeestatus.EmployeeStatus_Status='active'"
                        + " AND tbl_employees.Employee_Id='"+id+"'"
                        + " AND tbl_employees.Employee_TimeInPassword='"+timepassword+"'");
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
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            return false;
        }
        
        private boolean _alreadyLoggedIn(String employeeId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Time_In FROM tbl_time"
                        + " WHERE Time_In LIKE '"+df.format(date)+"%'"
                        + " AND Employee_Id='"+employeeId+"'");
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
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return false;
        }
        
        private String _getEmployeeId(String employeeId, String timepassword){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String Id = null;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Employee_Id FROM tbl_employees"
                        + " WHERE Employee_Id='"+employeeId+"'"
                        + " AND Employee_TimeInPassword='"+timepassword+"'");
                if(rs.next()){
                    Id = rs.getString("Employee_Id");
                    //conn.close();
                    //return Id;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(FormLoginEmployee.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            return Id;
        }
        
        private void _saveTime(String employeeId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date();
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_time(Employee_Id, Time_In)"
                        + " VALUES ('"+employeeId+"', '"+df.format(date)+"')");
                JOptionPane.showMessageDialog(null, "Logged In Successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(FormLoginEmployee.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR);
                }
            }
        }
    }
    
}
