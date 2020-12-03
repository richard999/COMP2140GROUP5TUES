/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-Earl
 */
public class FormViewStock extends JInternalFrame {

    private final JLabel lblFilter;
    private final JTextField txtFilter;
    private final JButton btnAddNewItem, btnDelete;
    JTable tableStocks;
    DefaultTableModel tableStocksModel;

    public FormViewStock() {
        lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(300, 25));
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _filterData();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(lblFilter);
        topPanel.add(txtFilter);

        btnAddNewItem = new JButton("Add New Item");
        btnAddNewItem.addActionListener(new ButtonFunction());
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ButtonFunction());
        btnDelete.setPreferredSize(new Dimension(btnAddNewItem.getPreferredSize().width, btnAddNewItem.getPreferredSize().height));

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(btnAddNewItem.getPreferredSize().width + 5, btnAddNewItem.getPreferredSize().height));
        leftPanel.add(btnAddNewItem);
        leftPanel.add(btnDelete);

        String[] tableHeader = {
            "Name", "Remaining"
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
        tableStocks.getColumnModel().getColumn(0).setPreferredWidth(300);
        tableStocks.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(tableStocks);

        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(leftPanel, BorderLayout.EAST);

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                _loadTableData();
            }
        });

        setContentPane(container);
        setTitle("Stocks");
        setBounds(30, 30, 650, 400);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }

    private void _loadTableData() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableStocksModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_stocks.Stock_Name, tbl_stockunit.StockUnit_Unit,"
                    + " tbl_stocks.Stock_Remaining"
                    + " FROM tbl_stockunit INNER JOIN tbl_stocks"
                    + " ON tbl_stockunit.StockUnit_Id = tbl_stocks.StockUnit_Id");
            StringFunction sFunction = new StringFunction();
            while (rs.next()) {
                String stockName = sFunction._Capitalized(rs.getString("tbl_stocks.Stock_Name"));
                String stockRemaining = rs.getString("tbl_stocks.Stock_Remaining") + " " + rs.getString("tbl_stockunit.StockUnit_Unit");
                tableStocksModel.addRow(new Object[]{stockName, stockRemaining});
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

    private void _filterData() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableStocksModel.setRowCount(0);

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_stocks.Stock_Name, tbl_stockunit.StockUnit_Unit,"
                    + " tbl_stocks.Stock_Remaining"
                    + " FROM tbl_stockunit INNER JOIN tbl_stocks"
                    + " ON tbl_stockunit.StockUnit_Id = tbl_stocks.StockUnit_Id"
                    + " WHERE tbl_stocks.Stock_Name LIKE '%"+txtFilter.getText().toLowerCase().trim()+"%'");
            StringFunction sFunction = new StringFunction();
            while (rs.next()) {
                String stockName = sFunction._Capitalized(rs.getString("tbl_stocks.Stock_Name"));
                String stockRemaining = rs.getString("tbl_stocks.Stock_Remaining") + " " + rs.getString("tbl_stockunit.StockUnit_Unit");
                tableStocksModel.addRow(new Object[]{stockName, stockRemaining});
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

    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAddNewItem) {
                _addNewItem();
            } else if (e.getSource() == btnDelete) {
                _delete();
            }
        }

        private void _addNewItem() {
            SubFormAddNewStockItem subFormAddNewStockItem = new SubFormAddNewStockItem();
            if (JOptionPane.showConfirmDialog(null, subFormAddNewStockItem, "Add New Stock Item",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                subFormAddNewStockItem._save();
                _loadTableData();
            }
        }

        private void _delete() {
            try {
                String stockName = tableStocks.getValueAt(tableStocks.getSelectedRow(), 0).toString();
                if(JOptionPane.showConfirmDialog(null, "Are you sure?", "DELETE",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    _goDelete(stockName);
                    _loadTableData();
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Please select from the table", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        private void _goDelete(String stockName) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                String id = _getStockId(stockName);
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_stockrecord"
                        + " WHERE Stock_Id='"+id+"'");
                
                st.executeUpdate("DELETE FROM tbl_stocks"
                        + " WHERE Stock_Id='"+id+"'");
                
                st.executeUpdate("ALTER TABLE tbl_stocks"
                        + " auto_increment = 1");
            } catch (SQLException ex) {
                //Logger.getLogger(FormViewStock.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private String _getStockId(String stockName) {
            String id = null;
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Stock_Id FROM tbl_stocks"
                        + " WHERE Stock_Name='"+stockName+"'");
                if(rs.next()){
                    id = rs.getString("Stock_Id");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormViewStock.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            return id;
        }

    }

}
