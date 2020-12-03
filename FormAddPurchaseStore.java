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
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class FormAddPurchaseStore extends JInternalFrame{
    JTextField txtStore;
    JButton btnAdd;
    
    public FormAddPurchaseStore(){
        JLabel lblTitle = new JLabel("Add Store");
        Font newFont = lblTitle.getFont();
        lblTitle.setFont(new Font(newFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblStore = new JLabel("Store");
        txtStore = new JTextField();
        txtStore.setPreferredSize(new Dimension(150, 25));
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(lblStore);
        centerPanel.add(txtStore);
        
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(container);
        setTitle("Add Store");
        setBounds(30, 30, 230, 140);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnAdd){
                _add();
            }
        }
        
        private void _add(){
            if (txtStore.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Field is required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                _addStore();
            }
        }
        
        private void _addStore(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                StringFunction sFunction = new StringFunction();
                String store = sFunction._removeWhiteSpaces(txtStore.getText().toLowerCase().trim());
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_store (Store_Name)"
                        + " VALUES ('"+store.trim()+"')");
                JOptionPane.showMessageDialog(null, "Successfully Saved", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
