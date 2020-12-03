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
public class FormViewDeletedMenu extends JInternalFrame{
    JLabel lblFilter;
    JTextField txtFilter;
    JTable tableMenu;
    JButton btnRestore;
    DefaultTableModel tableModel = new DefaultTableModel();
    
    public FormViewDeletedMenu(){
        JLabel lblTitle = new JLabel("View Deleted Menu Items");
        Font newFont = lblTitle.getFont();
        lblTitle.setFont(new Font(newFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(200, 20));
        txtFilter.addKeyListener(new TextFieldFunction());
        
        JPanel topPanel = new JPanel();
        topPanel.add(lblFilter);
        topPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Menu Item", "Price"
        };
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
        
        tableMenu = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMenu.setModel(tableModel);
        tableMenu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMenu.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableMenu.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tableMenu);
        
        btnRestore = new JButton("Restore");
        btnRestore.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRestore);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new WindowFunction());
        
        setContentPane(container);
        setTitle("View Deleted Menu Items");
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setBounds(100, 100, 700, 400);
    }
    
    private class WindowFunction implements InternalFrameListener{

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            _loadTableData();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_deletedmenu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                    + " tbl_menuitems.MenuItem_Price"
                    + " FROM tbl_deletedmenu INNER JOIN tbl_menuitems"
                    + " ON tbl_deletedmenu.MenuItem_Id=tbl_menuitems.MenuItem_Id");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_deletedmenu.MenuItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                String price = rs.getString("tbl_menuitems.MenuItem_Price");
                tableModel.addRow(new Object[]{id, name, price});
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
    
    private class TextFieldFunction implements KeyListener{

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
            if(e.getSource() == txtFilter){
                _searchMenu();
            }
        }
        
        private void _searchMenu(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_deletedmenu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                        + " tbl_menuitems.MenuItem_Price"
                        + " FROM tbl_deletedmenu INNER JOIN tbl_menuitems"
                        + " ON tbl_deletedmenu.MenuItem_Id=tbl_menuitems.MenuItem_Id"
                        + " WHERE tbl_deletedmenu.MenuItem_Id LIKE '%"+txtFilter.getText().trim()+"%'"
                        + " OR tbl_menuitems.MenuItem_Name LIKE '%"+txtFilter.getText().trim()+"%'");
                StringFunction sFunction = new StringFunction();
                while(rs.next()){
                    String id = rs.getString("tbl_deletedmenu.MenuItem_Id");
                    String name = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                    String price = rs.getString("tbl_menuitems.MenuItem_Price");
                    tableModel.addRow(new Object[]{id, name, price});
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
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnRestore){
                _restoreMenu();
                _loadTableData();
            }
        }
        
        private void _restoreMenu(){
            try {
                String id = tableMenu.getValueAt(tableMenu.getSelectedRow(), 0).toString();
                DatabaseConnection dbConn = new DatabaseConnection();
                Connection conn = dbConn._getConnection();
                try {
                    Statement st = conn.createStatement();
                    st.executeUpdate("INSERT INTO tbl_menu(MenuItem_Id) VALUES ('"+id+"')");
                    st.executeUpdate("DELETE FROM tbl_deletedmenu WHERE MenuItem_Id="+id);
                    JOptionPane.showMessageDialog(null, "Successfully Restored", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
            
                
        }
        
    }
}
