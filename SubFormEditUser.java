/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormEditUser extends JPanel{
    JLabel lblTitle;
    JLabel lblUserId, lblUserType, lblName, lblUsername, lblPassword;
    JTextField txtUserId, txtName, txtUsername, txtPassword;
    JComboBox cmbUserType;
    
    public SubFormEditUser(String userId, String userType, String userName, String userUsername, 
            String userPassword){
        lblTitle = new JLabel("Edit User");
        Font font = lblTitle.getFont();
        lblTitle.setFont(new Font(font.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblUserId = new JLabel("User ID");
        lblUserType = new JLabel("User Type");
        lblName = new JLabel("Name");
        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        
        txtUserId = new JTextField();
        txtUserId.setEditable(false);
        txtUserId.setPreferredSize(new Dimension(200, 25));
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 25));
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 25));
        txtPassword = new JTextField();
        txtPassword.setPreferredSize(new Dimension(200, 25));
        
        
        cmbUserType = new JComboBox();
        _loadComboBox();
        
        txtUserId.setText(userId);
        cmbUserType.setSelectedItem(userType);
        txtName.setText(userName);
        txtUsername.setText(userUsername);
        txtPassword.setText(userPassword);
        
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
        
        hg1.addComponent(lblUserId);
        hg1.addComponent(lblUserType);
        hg1.addComponent(lblName);
        hg1.addComponent(lblUsername);
        hg1.addComponent(lblPassword);
        
        hg2.addComponent(txtUserId);
        hg2.addComponent(cmbUserType);
        hg2.addComponent(txtName);
        hg2.addComponent(txtUsername);
        hg2.addComponent(txtPassword);
        
        vg1.addComponent(lblUserId);
        vg1.addComponent(txtUserId);
        vg2.addComponent(lblUserType);
        vg2.addComponent(cmbUserType);
        vg3.addComponent(lblName);
        vg3.addComponent(txtName);
        vg4.addComponent(lblUsername);
        vg4.addComponent(txtUsername);
        vg5.addComponent(lblPassword);
        vg5.addComponent(txtPassword);
        
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
        
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void _loadComboBox(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT UserType FROM tbl_usertype");
            while(rs.next()){
                cmbUserType.addItem(rs.getString("UserType"));
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
    
    protected boolean _updateUsers(){
        if(txtName.getText().trim().equals("") || txtUsername.getText().trim().equals("") || 
                txtPassword.getText().trim().equals("")){
            return false;
        } else {
            if(_goUpdate()){
                return true;
            }
        }
        return false;
    }
    
    private boolean _goUpdate(){
        String userId = txtUserId.getText();
        String userTypeId = _getUserTypeId(cmbUserType.getSelectedItem().toString());
        String userName = txtName.getText();
        String userUsername = txtUsername.getText();
        PasswordHash passHash = new PasswordHash();
        String password = txtPassword.getText();
        String securePassword = passHash.getSecurePassword(password);
        
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_users SET UserTypeId="+userTypeId+", User_Name='"+userName+"',"
                    + " User_Username='"+userUsername+"', User_Password='"+securePassword+"'"
                    + " WHERE User_Id="+userId);
            st.executeUpdate("UPDATE tbl_userhex SET User_PlainPassword='"+password+"'"
                    + " WHERE User_Id="+userId);
            conn.close();
            return true;
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
    
    private String _getUserTypeId(String userType){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String value = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT UserTypeId FROM tbl_usertype WHERE UserType='"+userType+"'");
            if(rs.next()){
                value = rs.getString("UserTypeId");
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
