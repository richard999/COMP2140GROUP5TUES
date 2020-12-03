/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class FormLogin extends JFrame{
    JTextField txtUsername, txtPassword;
    JButton btnLogin;
            
    public FormLogin(){
        JLabel lblTitle = new JLabel("Login");
        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        
        btnLogin = new JButton("Login");
        
        JPanel header_container = new JPanel();
        header_container.add(lblTitle);
        
        JPanel contentPanel = new JPanel();        
        GroupLayout groupLayout = new GroupLayout(contentPanel);
        groupLayout.setAutoCreateGaps(true);        
        contentPanel.setLayout(groupLayout);

        GroupLayout.Group hg1 = groupLayout.createParallelGroup();
        GroupLayout.Group hg2 = groupLayout.createParallelGroup();

        GroupLayout.Group vg1 = groupLayout.createParallelGroup();
        GroupLayout.Group vg2 = groupLayout.createParallelGroup();

        
        hg1.addComponent(lblUsername);
        hg1.addComponent(lblPassword);
        
        hg2.addComponent(txtUsername);
        hg2.addComponent(txtPassword);
        
        vg1.addComponent(lblUsername);
        vg1.addComponent(txtUsername);
        
        vg2.addComponent(lblPassword);
        vg2.addComponent(txtPassword);        
     
        // Horizontal group
        GroupLayout.SequentialGroup hseq1 = groupLayout.createSequentialGroup();
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);

        // Vertical group
        GroupLayout.SequentialGroup vseq1 = groupLayout.createSequentialGroup();
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);

        groupLayout.setHorizontalGroup(hseq1);
        groupLayout.setVerticalGroup(vseq1);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);
        
        JPanel container = new JPanel();
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(10);
        container.setLayout(borderLayout);
        container.add(header_container, BorderLayout.NORTH);
        container.add(contentPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        /*------- BUTTON START FUNCTIONS -----------*/
        
        btnLogin.addActionListener(new ButtonFunction());
        
        /*------- BUTTON END FUNCTIONS -----------*/
        
        setTitle("Eats Possible");
        setContentPane(container);
        setSize(220,160);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width/2)-(this.getSize().width/2), (dim.height/2)-(this.getSize().height/2));
        addWindowListener(new WindowFunction());
        
    }
    
    private void _hideThisForm(){
        this.setVisible(false);
    }
    
    public class WindowFunction implements WindowListener {
        
        @Override
        public void windowOpened(WindowEvent e) {
            
        }

        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(1);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowIconified(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowActivated(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class ButtonFunction implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnLogin){
                int userTypeId = _getUserTypeId(txtUsername.getText(), txtPassword.getText());
                if( userTypeId == 0){
                    JOptionPane.showMessageDialog(null, "Access Denied \n Username or Password is incorrect", "Access Denied", JOptionPane.ERROR_MESSAGE);
                }else{
                    FormPurchase formPurchase = new FormPurchase();
                    formPurchase.setVisible(true);
                    _hideThisForm();
                }
            }
        }
        
        private int _getUserTypeId(String username, String password){
            int value = 0;
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            PasswordHash passHash = new PasswordHash();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT UserTypeId FROM tbl_users"
                        + " WHERE User_Username='"+username+"' AND User_Password='"+passHash.getSecurePassword(password)+"'");
                if(rs.next()){
                    value = rs.getInt("UserTypeId");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            return value;
        }
        
    }
    
}
