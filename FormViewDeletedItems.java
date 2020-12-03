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
public class FormViewDeletedItems extends JInternalFrame{
    private final JTextField txtSearch;
    private final JButton btnRestoreItem, btnRefresh;
    private final JTable tableDeletedItems;
    private DefaultTableModel tableModel;
    
    public FormViewDeletedItems(){
        JLabel lblTitle = new JLabel("View Deleted Items");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblSearch = new JLabel("Search");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 25));
        txtSearch.addKeyListener(new TextFieldFunctions());
        
        JPanel searchPanel = new JPanel();
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        
        String[] header = {"Item ID", "Item Name", "Description", "Minimum Order", "Remaining"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(header);
        tableDeletedItems = new JTable(){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        tableDeletedItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableDeletedItems.setModel(tableModel);
        tableDeletedItems.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableDeletedItems.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableDeletedItems.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableDeletedItems.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableDeletedItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableDeletedItems);
        
        btnRestoreItem = new JButton("Restore Item");
        btnRestoreItem.setPreferredSize(new Dimension(107, 50));
        btnRestoreItem.addActionListener(new ButtonFunctions());
        btnRefresh = new JButton("Refresh");
        btnRefresh.setPreferredSize(new Dimension(107, 50));
        btnRefresh.addActionListener(new ButtonFunctions());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(120, 480));
        buttonPanel.add(btnRestoreItem);
        buttonPanel.add(btnRefresh);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new WindowFunctions());
        
        setContentPane(container);
        setTitle("View Deleted Items");
        setBounds(50, 50, 800, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setIconifiable(true);
        setClosable(true);
    }
    
    private class WindowFunctions implements InternalFrameListener {

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
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_deleteditems.Item_Id, tbl_Items.Item_Name,"
                    + "tbl_items.Item_Description, tbl_items.Item_MinOrder, tbl_items.Item_Remaining"
                    + " FROM tbl_items INNER JOIN tbl_deleteditems"
                    + " ON tbl_items.Item_Id=tbl_deleteditems.Item_Id"
                    + " ORDER BY tbl_deleteditems.Item_Id ASC");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                int Id = rs.getInt("tbl_deleteditems.Item_Id");
                String ItemName = sFunction._Capitalized(rs.getString("tbl_Items.Item_Name"));
                String ItemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                String ItemMinOrder = rs.getString("tbl_items.Item_MinOrder");
                String ItemRem = rs.getString("tbl_items.Item_Remaining");
                tableModel.addRow(new Object[]{Id, ItemName, ItemDesc, ItemMinOrder, ItemRem});
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
    
    private class ButtonFunctions implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnRestoreItem){
                try {
                    int itemId = (int) tableDeletedItems.getValueAt(tableDeletedItems.getSelectedRow(), 0);
                    if (JOptionPane.showConfirmDialog(null, "RESTORE SELECTED ITEM \n\n Are you sure?", "RESTORE", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        _restoreItem(itemId);
                    }
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Please select an item!", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
                
            } else if (e.getSource() == btnRefresh){
                _loadTableData();
            }
        }
        
        private void _restoreItem(int itemId){
            
            try {
                DatabaseConnection dbConn = new DatabaseConnection();
                Connection conn = dbConn._getConnection();
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_inventory (Item_Id) "
                        + "VALUES ("+itemId+")");
                st.executeUpdate("DELETE FROM tbl_deleteditems WHERE Item_Id="+itemId);
                _loadTableData();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
    }
    
    private class TextFieldFunctions implements KeyListener {

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
                _searchItem();
            }
        }
        
        private void _searchItem(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_deleteditems.Item_Id, tbl_Items.Item_Name,"
                        + "tbl_items.Item_Description, tbl_items.Item_MinOrder, tbl_items.Item_Remaining"
                        + " FROM tbl_items INNER JOIN tbl_deleteditems"
                        + " ON tbl_items.Item_Id=tbl_deleteditems.Item_Id"
                        + " WHERE tbl_deleteditems.Item_Id LIKE '%"+txtSearch.getText().trim()+"%'"
                        + " OR tbl_Items.Item_Name LIKE '%"+txtSearch.getText().toLowerCase().trim()+"%'"
                        + " OR tbl_items.Item_Description LIKE '%"+txtSearch.getText().toLowerCase().trim()+"%'");
                StringFunction sFunction = new StringFunction();
                while(rs.next()){
                    int id = rs.getInt("tbl_deleteditems.Item_Id");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_Items.Item_Name"));
                    String itemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                    String itemMinOrder = rs.getString("tbl_items.Item_MinOrder");
                    String itemRem = (rs.getString("tbl_items.Item_Remaining"));
                    tableModel.addRow(new Object[]{id, itemName, itemDesc, itemMinOrder, itemRem});
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
