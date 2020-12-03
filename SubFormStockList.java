/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
 * @author IT-Earl
 */
public class SubFormStockList extends JPanel{
    private final JTextField txtFilter;
    JTable tableStocks;
    DefaultTableModel tableStocksModel;
    
    public SubFormStockList(){
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(200, 25));
        
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Filter"));
        topPanel.add(txtFilter);
        
        String[] tableHeader = {
            "Name", "Unit"
        };

        tableStocksModel = new DefaultTableModel();
        tableStocksModel.setColumnIdentifiers(tableHeader);
        tableStocks = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableStocks.setModel(tableStocksModel);
        tableStocks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableStocks.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableStocks.getColumnModel().getColumn(0).setPreferredWidth(270);
        tableStocks.getColumnModel().getColumn(1).setPreferredWidth(180);

        JScrollPane scrollPane = new JScrollPane(tableStocks);
        
        setLayout(new BorderLayout());
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        _loadTableData();
    }
    
    private void _loadTableData() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableStocksModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_stocks.Stock_Name, tbl_stockunit.StockUnit_Unit"
                    + " FROM tbl_stockunit INNER JOIN tbl_stocks"
                    + " ON tbl_stockunit.StockUnit_Id = tbl_stocks.StockUnit_Id");
            StringFunction sFunction = new StringFunction();
            while (rs.next()) {
                String stockName = sFunction._Capitalized(rs.getString("tbl_stocks.Stock_Name"));
                String stockUnit = rs.getString("tbl_stockunit.StockUnit_Unit");
                tableStocksModel.addRow(new Object[]{stockName, stockUnit});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    protected String[] _getValues(){
        String[] value = new String[2];
        try {
            value[0] = tableStocks.getValueAt(tableStocks.getSelectedRow(), 0).toString();
            value[1] = tableStocks.getValueAt(tableStocks.getSelectedRow(), 1).toString();
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Please select from the table", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        return value;
    }
    
}
