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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
public class FormViewPurchaseItems extends JInternalFrame {
    private final JTextField txtFilter;
    private DefaultTableModel tableItemModel;
    private final JTable tableItem;
    private final JButton btnEdit, btnDelete;
    
    public FormViewPurchaseItems(){
        JLabel lblTitle = new JLabel("View Purchase Items");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(150, 25));
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _searchData();
            }           
        });
        
        JPanel filterPanel = new JPanel();
        filterPanel.add(lblFilter);
        filterPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Item", "Store", "Minimum Order"
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
        tableItem.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableItem.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tableItem);
        
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 36));
        btnEdit.addActionListener(new ButtonFunction());
        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(new ButtonFunction());
        
        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.setPreferredSize(new Dimension(110, scrollPane.getPreferredSize().height));
        rightButtonPanel.add(btnEdit);
        rightButtonPanel.add(btnDelete);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(rightButtonPanel, BorderLayout.EAST);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new WindowFunction());
        
        setContentPane(container);
        setTitle("View Purchase Items");
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setBounds(40, 40, 800, 500);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _searchData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_list.PurchaseItem_Id,"
                    + " tbl_purchaselist.PurchaseItem_Name, tbl_store.Store_Name,"
                    + " tbl_purchaselist.PurchaseItem_MinOrder FROM (tbl_purchaselist INNER JOIN tbl_store"
                    + " ON tbl_purchaselist.Store_Id=tbl_store.Store_Id) INNER JOIN tbl_list"
                    + " ON tbl_list.PurchaseItem_Id=tbl_purchaselist.PurchaseItem_Id"
                    + " WHERE tbl_list.PurchaseItem_Id LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_purchaselist.PurchaseItem_Name LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_store.Store_Name LIKE '%"+txtFilter.getText().trim()+"%'");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_list.PurchaseItem_Id");
                String item = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                String store = sFunction._Capitalized(rs.getString("tbl_store.Store_Name"));
                String minorder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                tableItemModel.addRow(new Object[]{id, item, store, minorder});
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
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnEdit){
                _edit();
            } else if (e.getSource() == btnDelete){
                _delete();
            }
        }
        
        private void _delete(){
            try {
                String id = tableItem.getValueAt(tableItem.getSelectedRow(), 0).toString();
                if(JOptionPane.showConfirmDialog(null, "Are you sure?", "???", JOptionPane.YES_NO_CANCEL_OPTION, 
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    if(_deleteData(id)){
                        tableItemModel.removeRow(tableItem.getSelectedRow());
                        JOptionPane.showMessageDialog(null, "Successfully Deleted.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Please select an item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
            
        }
        
        private boolean _deleteData(String id){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_deletedlist (PurchaseItem_Id)"
                        + " VALUES ("+id+")");
                st.executeUpdate("DELETE FROM tbl_list WHERE"
                        + " PurchaseItem_Id="+id);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            return true;
        }
        
        private void _edit(){
            try {
                SubFormEditPurchaseItem subFormEditPurchaseItem = new SubFormEditPurchaseItem(
                    tableItem.getValueAt(tableItem.getSelectedRow(), 0).toString(),
                    tableItem.getValueAt(tableItem.getSelectedRow(), 1).toString(),
                    tableItem.getValueAt(tableItem.getSelectedRow(), 2).toString(),
                    tableItem.getValueAt(tableItem.getSelectedRow(), 3).toString()
                );
                if (JOptionPane.showConfirmDialog(null, subFormEditPurchaseItem, "Edit", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    subFormEditPurchaseItem._updatePurchaseItems();
                    _loadTableData();
                }
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
            
        }
        
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
        tableItemModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_list.PurchaseItem_Id,"
                    + " tbl_purchaselist.PurchaseItem_Name, tbl_store.Store_Name,"
                    + " tbl_purchaselist.PurchaseItem_MinOrder FROM (tbl_purchaselist INNER JOIN tbl_store"
                    + " ON tbl_purchaselist.Store_Id=tbl_store.Store_Id) INNER JOIN tbl_list"
                    + " ON tbl_list.PurchaseItem_Id=tbl_purchaselist.PurchaseItem_Id");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_list.PurchaseItem_Id");
                String item = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                String store = sFunction._Capitalized(rs.getString("tbl_store.Store_Name"));
                String minorder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                tableItemModel.addRow(new Object[]{id, item, store, minorder});
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
