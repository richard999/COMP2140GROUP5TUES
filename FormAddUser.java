/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 *
 * @author IT-NEW
 */
public class FormAddUser extends JInternalFrame {
    private final JLabel lblUserType, lblFname, lblLname, lblUsername, lblPassword;
    private final JTextField txtFname, txtLname, txtUsername;
    private final JPasswordField txtPassword;
    private final JComboBox cmbUserType;
    private final JButton btnSave;
    
    StringFunction sFunction;
    
    public FormAddUser(){
        sFunction = new StringFunction();
        JLabel lblTitle = new JLabel("Add New User");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblUserType = new JLabel("Type of User");
        lblFname = new JLabel("Enter First Name");
        lblLname = new JLabel("Enter Last Name");
        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        
        cmbUserType = new JComboBox();
        txtFname = new JTextField();
        txtLname = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        
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
        
        hg1.addComponent(lblUserType);
        hg1.addComponent(lblFname);
        hg1.addComponent(lblLname);
        hg1.addComponent(lblUsername);
        hg1.addComponent(lblPassword);
        
        hg2.addComponent(cmbUserType);
        hg2.addComponent(txtFname);
        hg2.addComponent(txtLname);
        hg2.addComponent(txtUsername);
        hg2.addComponent(txtPassword);
        
        vg1.addComponent(lblUserType);
        vg1.addComponent(cmbUserType);
        
        vg2.addComponent(lblFname);
        vg2.addComponent(txtFname);
        
        vg3.addComponent(lblLname);
        vg3.addComponent(txtLname);
        
        vg4.addComponent(lblUsername);
        vg4.addComponent(txtUsername);
        
        vg5.addComponent(lblPassword);
        vg5.addComponent(txtPassword);
        
        GroupLayout.SequentialGroup hsg1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vsg1 = layout.createSequentialGroup();
        
        hsg1.addGroup(hg1);
        hsg1.addGroup(hg2);
        
        vsg1.addGroup(vg1);
        vsg1.addGroup(vg2);
        vsg1.addGroup(vg3);
        vsg1.addGroup(vg4);
        vsg1.addGroup(vg5);
        
        layout.setHorizontalGroup(hsg1);
        layout.setVerticalGroup(vsg1);
        
        btnSave = new JButton("Save");
        
        btnSave.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        
        txtFname.addKeyListener(new GenerateUsername());
        txtLname.addKeyListener(new GenerateUsername());
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        addInternalFrameListener(new InternalFrameFunction());
        
        setContentPane(container);
        setTitle("Add New User");
        setBounds(10, 10, 300, 270);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnSave){
                _saveUser();
            }
        }
        
        public void _saveUser(){
            char[] pass = txtPassword.getPassword();
            String password = "";
            for(int i=0; i<pass.length; i++){
                password += pass[i];
            }

            String fullName = txtFname.getText().trim() + " " + txtLname.getText().trim();
            PasswordHash passHash = new PasswordHash();
            String hashedPassword = passHash.getSecurePassword(password);

            if (cmbUserType.getSelectedItem().toString().equals("") || txtFname.getText().trim().equals("")
                    || txtLname.getText().trim().equals("") || txtUsername.getText().trim().equals("")
                    || txtUsername.getText().trim().equals(".") || password.equals("")){

                JOptionPane.showMessageDialog(null, "Please fill out all the fields", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {

                if(_ifUsernameExist(txtUsername.getText(),fullName)){
                    JOptionPane.showMessageDialog(null, "Username already exist", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    UserUtilities UserUtils = new UserUtilities();
                    String MaxId = UserUtils._getUserMaxId();
                    int UserTypeId = UserUtils._getUserTypeId(cmbUserType.getSelectedItem().toString());
                    fullName = sFunction._removeWhiteSpaces(fullName);
                    fullName = sFunction._Capitalized(fullName);
                    DatabaseConnection dbConn = new DatabaseConnection();
                    Connection conn = dbConn._getConnection();

                    try {
                        try (Statement st = conn.createStatement()) {
                            st.executeUpdate("INSERT INTO tbl_Users(User_Id, UserTypeId, User_Name, User_Username, User_Password)"
                                    + " VALUES ('" + MaxId + "'," + UserTypeId + ",'" + trim(fullName) + "','" + txtUsername.getText() + "','" + hashedPassword + "')");
                            st.executeUpdate("INSERT INTO tbl_userHex(User_Id, User_PlainPassword)"
                                    + "VALUES ('" + MaxId + "','" + password + "')");
                        }
                        JOptionPane.showMessageDialog(null, "User has been succesfully saved");
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

            }

        }
        
    }
    
    
    public boolean _ifUsernameExist(String Username, String FullName){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
      
        try {
            Statement st = conn.createStatement();
            FullName = sFunction._Capitalized(FullName);
            ResultSet rs = st.executeQuery("SELECT User_Username FROM tbl_Users WHERE User_Username='" + Username + "'"
                                    + " OR User_Name='" + FullName + "'");
            if (rs.next()){
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
    
    
    private class GenerateUsername implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (e.getSource() == txtFname){
                String first = txtFname.getText().toLowerCase();
                first = first.replace(" ", "");
                txtUsername.setText(first);
            }
            if (e.getSource() == txtLname){
                String first = txtFname.getText().toLowerCase();
                first = first.replace(" ", "");
                String last = txtLname.getText().toLowerCase();
                last = last.replace(" ", "");
                txtUsername.setText(first + "." + last);
            }
        }
        
    }
    
    private class InternalFrameFunction implements InternalFrameListener{
        
        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            _loadUserType();
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            
        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            
        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
            
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            
        }
        
        private void _loadUserType(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs;
                rs = st.executeQuery("SELECT UserType FROM tbl_UserType");
                while (rs.next()){
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
        
    }
    
}
