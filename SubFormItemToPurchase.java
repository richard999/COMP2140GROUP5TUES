/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
public class SubFormItemToPurchase extends JPanel{
    private JTextField txtFilter;
    private DefaultTableModel tableListModel;
    private final JTable tableList;
    
    public SubFormItemToPurchase(){
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(150, 20));
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _searchData();
            }
        });
        
        JPanel topPanel = new JPanel();
        topPanel.add(lblFilter);
        topPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Item", "Store", "Minimum Order"
        };
        tableListModel = new DefaultTableModel();
        tableListModel.setColumnIdentifiers(tableHeader);
        tableList = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableList.setModel(tableListModel);
        tableList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableList.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableList.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableList.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableList.getColumnModel().getColumn(3).setPreferredWidth(150);
        JScrollPane scrollPane = new JScrollPane(tableList);
        
        setLayout(new BorderLayout());
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    protected void _searchData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableListModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_list.PurchaseItem_Id, tbl_purchaselist.PurchaseItem_Name,"
                    + " tbl_store.Store_Name, tbl_purchaselist.PurchaseItem_MinOrder"
                    + " FROM (tbl_purchaselist INNER JOIN tbl_store ON"
                    + " tbl_purchaselist.Store_Id=tbl_store.Store_Id) INNER JOIN tbl_list"
                    + " ON tbl_purchaselist.PurchaseItem_Id=tbl_list.PurchaseItem_Id"
                    + " WHERE tbl_list.PurchaseItem_Id LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_purchaselist.PurchaseItem_Name LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_store.Store_Name LIKE '%"+txtFilter.getText().trim()+"%'");
            StringFunction sFunction = new StringFunction();
            while (rs.next()){
                String id = rs.getString("tbl_list.PurchaseItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                String store = sFunction._Capitalized(rs.getString("tbl_store.Store_Name"));
                String minorder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                tableListModel.addRow(new Object[]{id, name, store, minorder});
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
    
    protected void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableListModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_list.PurchaseItem_Id, tbl_purchaselist.PurchaseItem_Name,"
                    + " tbl_store.Store_Name, tbl_purchaselist.PurchaseItem_MinOrder"
                    + " FROM (tbl_purchaselist INNER JOIN tbl_store ON"
                    + " tbl_purchaselist.Store_Id=tbl_store.Store_Id) INNER JOIN tbl_list"
                    + " ON tbl_purchaselist.PurchaseItem_Id=tbl_list.PurchaseItem_Id");
            StringFunction sFunction = new StringFunction();
            while (rs.next()){
                String id = rs.getString("tbl_list.PurchaseItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                String store = sFunction._Capitalized(rs.getString("tbl_store.Store_Name"));
                String minorder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                tableListModel.addRow(new Object[]{id, name, store, minorder});
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
        String[] value = new String[2];
        try {
            value[0] = tableList.getValueAt(tableList.getSelectedRow(), 1).toString();
            value[1] = tableList.getValueAt(tableList.getSelectedRow(), 3).toString();
        } catch (Exception ex){
            
        }
        
        return value;
    }
    
}
