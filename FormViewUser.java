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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewUser extends JInternalFrame {
    private final JLabel lblTitle;
    private final JLabel lblSearch;
    private final JTextField txtSearch;
    private final JButton btnEdit, btnDelete;
    private final JTable tableUsers;
    private DefaultTableModel tableModel = new DefaultTableModel();
    
    public FormViewUser() {
        lblTitle = new JLabel("View Users");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblSearch = new JLabel("Search");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.addKeyListener(new TextFieldFunctions());
        
        JPanel searchPanel = new JPanel();
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        
        String[] columnHeader = { "User ID", "User Type", "Name", "Username", "Password" };
        tableModel.setColumnIdentifiers(columnHeader);
        
        tableUsers = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableUsers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUsers.setModel(tableModel);
        tableUsers.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableUsers.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableUsers.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableUsers.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableUsers.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        
        JScrollPane scrollPane = new JScrollPane(tableUsers);
        
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 40));
        btnEdit.addActionListener(new ButtonFunction());
        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 40));
        btnDelete.addActionListener(new ButtonFunction());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(150, 500));
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(new JLabel("       "), BorderLayout.WEST);
        container.add(new JLabel("  "), BorderLayout.SOUTH);
        
        addInternalFrameListener(new WindowFunction());
        setContentPane(container);
        setTitle("View User");
        setBounds(10, 10, 800, 550);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setIconifiable(true);
        setClosable(true);
    }
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnDelete){
                if(JOptionPane.showConfirmDialog(null, "Are you sure?", "FAQ", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    _deleteUser(tableUsers.getValueAt(tableUsers.getSelectedRow(), 0).toString());
                    _loadTableData();
                }
            } else if (e.getSource() == btnEdit){
                try {
                    SubFormEditUser subFormEditUser = new SubFormEditUser(
                            tableUsers.getValueAt(tableUsers.getSelectedRow(), 0).toString(),
                            tableUsers.getValueAt(tableUsers.getSelectedRow(), 1).toString(),
                            tableUsers.getValueAt(tableUsers.getSelectedRow(), 2).toString(),
                            tableUsers.getValueAt(tableUsers.getSelectedRow(), 3).toString(),
                            tableUsers.getValueAt(tableUsers.getSelectedRow(), 4).toString()
                    );
                    boolean opt = true;
                    while(opt){
                        if(JOptionPane.showConfirmDialog(null, subFormEditUser, "Edit User", JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                            if(subFormEditUser._updateUsers()){
                                JOptionPane.showMessageDialog(null, "The user has been successfully edited", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                                _loadTableData();
                            }else {
                                JOptionPane.showMessageDialog(null, "Failed to edit the user", "FAILED", JOptionPane.WARNING_MESSAGE);
                                _loadTableData();
                            }
                            opt = false;
                        } else {
                            opt = false;
                        }
                    }
                    
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please select from the table!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        }
        
        private void _deleteUser(String userId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_userhex WHERE User_Id="+userId);
                st.executeUpdate("DELETE FROM tbl_users WHERE User_Id="+userId);
                JOptionPane.showMessageDialog(null, "User was deleted successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_users.User_Id, tbl_usertype.UserType, tbl_users.User_Name,"
                    + " tbl_users.User_Username, tbl_userhex.User_PlainPassword"
                    + " FROM (tbl_usertype INNER JOIN tbl_users ON tbl_usertype.UserTypeId = tbl_users.UserTypeId)"
                    + " INNER JOIN tbl_userhex ON tbl_users.User_Id = tbl_userhex.User_Id");
                while(rs.next()){
                    int id = rs.getInt("tbl_users.User_Id");
                    String utype = rs.getString("tbl_usertype.UserType");
                    String name = rs.getString("tbl_users.User_Name");
                    String username = rs.getString("tbl_users.User_Username");
                    String password = rs.getString("tbl_userhex.User_PlainPassword");
                    tableModel.addRow(new Object[]{id, utype, name, username, password}); 
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
    
    private class WindowFunction implements InternalFrameListener{

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            
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
            _loadTableData();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            
        }
        
    }
    
    private class TextFieldFunctions implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (e.getSource() == txtSearch){
                _searchUser();
            }
        }
        
        private void _searchUser(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_users.User_Id, tbl_usertype.UserType, tbl_users.User_Name, tbl_users.User_Username, tbl_userhex.User_PlainPassword"
                        + " FROM (tbl_usertype INNER JOIN tbl_users ON tbl_usertype.UserTypeId = tbl_users.UserTypeId)"
                        + " INNER JOIN tbl_userhex ON tbl_users.User_Id = tbl_userhex.User_Id"
                        + " WHERE tbl_users.User_Id LIKE '"+txtSearch.getText().trim()+"%'"
                        + " OR tbl_users.User_Name LIKE '%"+txtSearch.getText().trim()+"%'");
                while(rs.next()){
                    int id = rs.getInt("tbl_users.User_Id");
                    String utype = rs.getString("tbl_usertype.UserType");
                    String name = rs.getString("tbl_users.User_Name");
                    String username = rs.getString("tbl_users.User_Username");
                    String password = rs.getString("tbl_userhex.User_PlainPassword");
                    tableModel.addRow(new Object[]{id, utype, name, username, password});
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
