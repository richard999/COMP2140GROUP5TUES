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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SubFormVouchers extends JPanel{
    private final JTextField txtFilter;
    DefaultTableModel tableVoucherModel;
    private final JTable tableVoucher;
    
    String[] value;
    
    public SubFormVouchers(){
        JLabel lblFilter  = new JLabel();
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(100, 25));
        txtFilter.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                _searchData();
            }
        });
        
        JPanel topPanel = new JPanel();
        topPanel.add(lblFilter);
        topPanel.add(txtFilter);
        
        String[] tableHeader = {
            "Voucher No.", "Date"
        };
        tableVoucherModel = new DefaultTableModel();
        tableVoucherModel.setColumnIdentifiers(tableHeader);
        tableVoucher = new JTable(){  
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableVoucher.setModel(tableVoucherModel);
        tableVoucher.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableVoucher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableVoucher.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableVoucher.getColumnModel().getColumn(1).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableVoucher);
        scrollPane.setPreferredSize(new Dimension(230, 300));
        
        setLayout(new BorderLayout());
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        _loadTableData();
    }
    
    protected String[] _getValue(){
        return value;
    }
    
    protected boolean _checkItem(){
        try {
            value = new String[3];
            value[0] = tableVoucher.getValueAt(tableVoucher.getSelectedRow(), 0).toString();
            value[1] = tableVoucher.getValueAt(tableVoucher.getSelectedRow(), 1).toString();
            value[2] = _getName();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select an item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private String _getName(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String name = null;
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Voucher_IssuedTo FROM tbl_voucher"
                    + " WHERE Voucher_No="+tableVoucher.getValueAt(tableVoucher.getSelectedRow(), 0).toString());
            if (rs.next()){
                name = rs.getString("Voucher_IssuedTo");
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
        return name;
    }
    
    private void _searchData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableVoucherModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Voucher_No, Voucher_Date FROM tbl_voucher"
                    + " WHERE Voucher_No LIKE '%"+txtFilter.getText().trim()+"%'");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            while(rs.next()){
                String no = rs.getString("Voucher_No");
                String date = sdf.format(rs.getDate("Voucher_Date"));
                tableVoucherModel.addRow(new Object[]{no, date});
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
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableVoucherModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Voucher_No, Voucher_Date FROM tbl_voucher");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            while(rs.next()){
                String no = rs.getString("Voucher_No");
                String date = sdf.format(rs.getDate("Voucher_Date"));
                tableVoucherModel.addRow(new Object[]{no, date});
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
