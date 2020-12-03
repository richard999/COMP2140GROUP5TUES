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
public class SubFormVoucherItem extends JPanel{
    private final JTextField txtFilter;
    DefaultTableModel tableItemModel;
    private final JTable tableItem;
    private String value;
    
    public SubFormVoucherItem(){
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(150, 25));
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
            "ID", "Item"
        };
        tableItemModel = new DefaultTableModel();
        tableItemModel.setColumnIdentifiers(tableHeader);
        tableItem = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItem.setModel(tableItemModel);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableItem.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tableItem);
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        _loadTableData();
    }
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_particulars.VoucherItem_Id,"
                    + " tbl_voucheritem.VoucherItem_Name FROM tbl_particulars INNER JOIN tbl_voucheritem"
                    + " ON tbl_particulars.VoucherItem_Id=tbl_voucheritem.VoucherItem_Id");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_particulars.VoucherItem_Id");
                String item = sFunction._Capitalized(rs.getString("tbl_voucheritem.VoucherItem_Name"));
                tableItemModel.addRow(new Object[]{id, item});
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
    
    private void _searchData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_particulars.VoucherItem_Id,"
                    + " tbl_voucheritem.VoucherItem_Name FROM tbl_particulars INNER JOIN tbl_voucheritem"
                    + " ON tbl_particulars.VoucherItem_Id=tbl_voucheritem.VoucherItem_Id"
                    + " WHERE tbl_particulars.VoucherItem_Id LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_voucheritem.VoucherItem_Name LIKE '%"+txtFilter.getText().trim()+"%'");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_particulars.VoucherItem_Id");
                String item = sFunction._Capitalized(rs.getString("tbl_voucheritem.VoucherItem_Name"));
                tableItemModel.addRow(new Object[]{id, item});
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
    
    protected boolean _checkValue(){
        try{
            value = tableItem.getValueAt(tableItem.getSelectedRow(), 1).toString();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select an item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    protected String _getValue(){
        return value;
    }
    
}
