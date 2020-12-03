/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class SubFormMenuList extends JPanel{
    JLabel lblFilter;
    JTextField txtFilter;
    JTable tableMenu;
    DefaultTableModel tableModel;
    
    public SubFormMenuList(){
        lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(200, 26));
        txtFilter.addKeyListener(new SearchFunction());
        
        JPanel topPanel = new JPanel();
        topPanel.add(lblFilter);
        topPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Menu Item Name", "Price"
        };
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
        
        tableMenu = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMenu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableMenu.setModel(tableModel);
        tableMenu.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableMenu.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableMenu.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableMenu);
        
        setLayout(new BorderLayout());
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        _loadTableData();
    }
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_menu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                    + " ROUND(tbl_menuitems.MenuItem_Price, 2)"
                    + " FROM tbl_menu INNER JOIN tbl_menuitems"
                    + " ON tbl_menu.MenuItem_Id=tbl_menuitems.MenuItem_Id");
            StringFunction sFunction = new StringFunction();
            NumberFunction nFunction = new NumberFunction();
            String currency = "Php ";
            while(rs.next()){
                String id = rs.getString("tbl_menu.MenuItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                String price = currency + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_menuitems.MenuItem_Price, 2)"));
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
    
    private class SearchFunction implements KeyListener{

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
                _searchData();
            }
        }
        
    }
    
    private void _searchData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_menu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                    + " ROUND(tbl_menuitems.MenuItem_Price, 2)"
                    + " FROM tbl_menu INNER JOIN tbl_menuitems"
                    + " ON tbl_menu.MenuItem_Id=tbl_menuitems.MenuItem_Id"
                    + " WHERE tbl_menu.MenuItem_Id LIKE '%"+txtFilter.getText()+"%'"
                    + " OR tbl_menuitems.MenuItem_Name LIKE '%"+txtFilter.getText()+"%'");
            StringFunction sFunction = new StringFunction();
            String currency = "Php ";
            while(rs.next()){
                String id = rs.getString("tbl_menu.MenuItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                String price = currency + rs.getString("ROUND(tbl_menuitems.MenuItem_Price, 2)");
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
    
    protected String[] _getValues(){
        String[] value = new String[3];
        value[0] = "";
        value[1] = "";
        value[2] = "";
        try {
            value[0] = tableMenu.getValueAt(tableMenu.getSelectedRow(), 0).toString();
            value[1] = tableMenu.getValueAt(tableMenu.getSelectedRow(), 1).toString();
            value[2] = tableMenu.getValueAt(tableMenu.getSelectedRow(), 2).toString();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        return value;
    }
}
