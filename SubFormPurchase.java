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
import java.text.SimpleDateFormat;
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
public class SubFormPurchase extends JPanel{
    JTextField txtFilter;
    DefaultTableModel  tablePurchaseModel;
    JTable tablePurchase;
    
    String[] value;
    
    public SubFormPurchase(){
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _searchData();
            }
        });
        txtFilter.setPreferredSize(new Dimension(150, 25));
        
        JPanel topPanel = new JPanel();
        topPanel.add(lblFilter);
        topPanel.add(txtFilter);
        
        String[] tableHeader = {
            "Purchase No.", "Date"
        };
        tablePurchaseModel = new DefaultTableModel();
        tablePurchaseModel.setColumnIdentifiers(tableHeader);
        tablePurchase = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePurchase.setModel(tablePurchaseModel);
        tablePurchase.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablePurchase.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePurchase.getColumnModel().getColumn(0).setPreferredWidth(200);
        tablePurchase.getColumnModel().getColumn(1).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(tablePurchase);
        
        setLayout(new BorderLayout());
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
    }
    
    protected void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tablePurchaseModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT PurchaseOrder_No, PurchaseOrder_Date FROM tbl_purchaseorder");
            while (rs.next()){
                String no = rs.getString("PurchaseOrder_No");
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String date = sdf.format(rs.getDate("PurchaseOrder_Date"));
                tablePurchaseModel.addRow(new Object[]{no, date});
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
    
    protected void _searchData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tablePurchaseModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT PurchaseOrder_No, PurchaseOrder_Date FROM tbl_purchaseorder"
                    + " WHERE PurchaseOrder_No LIKE '%"+txtFilter.getText().trim()+"%'");
            while (rs.next()){
                String no = rs.getString("PurchaseOrder_No");
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String date = sdf.format(rs.getDate("PurchaseOrder_Date"));
                tablePurchaseModel.addRow(new Object[]{no, date});
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
    
    protected boolean _checkValues(){
        value = new String[2];
        try{
            value[0] = tablePurchase.getValueAt(tablePurchase.getSelectedRow(), 0).toString();
            value[1] = tablePurchase.getValueAt(tablePurchase.getSelectedRow(), 1).toString();
            return true;
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }
    
    protected String[] _getValues(){
        
        return value;
    }
    
}
