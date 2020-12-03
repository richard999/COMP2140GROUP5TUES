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
 * @author Flong
 */
public class FormViewItems extends JInternalFrame{
    
    JButton btnEdit, btnDelete;
    private final JButton btnRefresh, btnAddStock;
    private final JTextField txtSearch;
    private final JTable tableItems;
    private DefaultTableModel tableModel;
    private final JScrollPane scrollPane;
    
    public FormViewItems(){
        JLabel lblTitle = new JLabel("View Items");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblSearch = new JLabel("Search");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.addKeyListener(new TextFieldFunctions());
        
        JPanel searchPanel = new JPanel();
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        
        String[] columnHeaders = {"Item No.", "Item Name", "Description", "Minimum Order", "Remaining"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnHeaders);
        
        tableItems = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tableItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItems.setModel(tableModel);
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableItems.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        scrollPane = new JScrollPane(tableItems);
        
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 40));
        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 40));
        btnAddStock = new JButton("Add Stock");
        btnAddStock.setPreferredSize(new Dimension(100, 40));
        btnRefresh = new JButton("Refresh");
        btnRefresh.setPreferredSize(new Dimension(100, 40));
        
        btnRefresh.addActionListener(new ButtonFunctions());
        btnEdit.addActionListener(new ButtonFunctions());
        btnDelete.addActionListener(new ButtonFunctions());
        btnAddStock.addActionListener(new ButtonFunctions());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(150, 500));
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAddStock);
        buttonPanel.add(btnRefresh);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane,BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("      "), BorderLayout.CENTER);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(new JLabel(" "), BorderLayout.SOUTH);
        container.add(leftPanel, BorderLayout.WEST);
        
        addInternalFrameListener(new WindowFunctions());
        
        setContentPane(container);
        setTitle("View Items");
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setBounds(200, 30, 1000, 600);
        setClosable(true);
        setIconifiable(true);
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
            _loadData();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            
        }
        
    }
    
    public void _loadData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
            
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_inventory.Item_Id, tbl_items.Item_Name, "
                    + "tbl_items.Item_Description, tbl_items.Item_MinOrder, tbl_items.Item_Remaining "
                    + "FROM tbl_items INNER JOIN tbl_inventory ON tbl_items.Item_Id = tbl_inventory.Item_Id "
                    + "ORDER BY tbl_inventory.Item_Id ASC");
            StringFunction sFunction = new StringFunction();
            while (rs.next()){
                int Id = rs.getInt("tbl_inventory.Item_Id");
                //JOptionPane.showMessageDialog(null, rs.getString("tbl_items.Item_Name"));
                String Name = sFunction._Capitalized(rs.getString("tbl_items.Item_Name"));
                String Desc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                String MinOrder = rs.getString("tbl_items.Item_MinOrder");
                String Remaining = rs.getString("tbl_items.Item_Remaining");
                tableModel.addRow(new Object[]{Id, Name, Desc, MinOrder, Remaining});
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
    
    
    private class ButtonFunctions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnEdit){
                try{
                    SubFormEditItem subFormEditItem = new SubFormEditItem(
                            (int) tableItems.getValueAt(tableItems.getSelectedRow(), 0),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 1).toString(),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 2).toString(),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 3).toString(),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 4).toString()
                    );
                    boolean option = true;
                    while(option){
                        if (JOptionPane.showConfirmDialog(null, subFormEditItem, "Edit Selected Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) 
                                == JOptionPane.OK_OPTION){
                            
                            option = subFormEditItem._updateItem();
                            _loadData();
                        } else {
                            option = false;
                        }
                    }
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                    //JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                
            }
            if (e.getSource() == btnDelete){
                    try{
                        int itemId = (int) tableItems.getValueAt(tableItems.getSelectedRow(), 0);
                        if (JOptionPane.showConfirmDialog(null, "DELETE ITEM FROM INVENTORY \n\n Are you sure?", "DELETE ITEM", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                            DatabaseConnection dbConn = new DatabaseConnection();
                            Connection conn = dbConn._getConnection();
                            try {
                                Statement st = conn.createStatement();
                                st.executeUpdate("INSERT INTO tbl_deleteditems (Item_Id)"
                                        + " VALUES("+itemId+")");
                                st.executeUpdate("DELETE FROM tbl_ingredients WHERE Item_Id="+itemId);
                                st.executeUpdate("DELETE FROM tbl_inventory WHERE Item_Id="+itemId);
                                _loadData();
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

                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
      
            }
            if (e.getSource() == btnRefresh){
                _loadData();
            }
            if (e.getSource() == btnAddStock){
                try{
                    SubFormAddStock subFormAddStock = new SubFormAddStock(
                            (int) tableItems.getValueAt(tableItems.getSelectedRow(), 0),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 1).toString(),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 2).toString(),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 3).toString(),
                            tableItems.getValueAt(tableItems.getSelectedRow(), 4).toString()
                    );
                    boolean option = true;
                    while(option){
                        if (JOptionPane.showConfirmDialog(null, subFormAddStock, "Edit Selected Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) 
                                == JOptionPane.YES_OPTION){
                            
                            option = subFormAddStock._addStock();
                        } else {
                            option = false;
                        }
                    }
                    _loadData();
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        
    }
    
    private class TextFieldFunctions implements KeyListener{

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
                _findItem();
            }
        }
        
        private void _findItem(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_inventory.Item_Id, tbl_items.Item_Name, "
                        + "tbl_items.Item_Description, tbl_items.Item_MinOrder, tbl_items.Item_Remaining "
                        + "FROM tbl_items INNER JOIN tbl_inventory ON tbl_items.Item_Id = tbl_inventory.Item_Id "
                        + "WHERE tbl_inventory.Item_Id LIKE '"+txtSearch.getText().trim()+"%' "
                        + "OR tbl_items.Item_Name LIKE '%"+txtSearch.getText().toLowerCase().trim()+"%' "
                        + "OR tbl_items.Item_Description LIKE '%"+txtSearch.getText().toLowerCase().trim()+"%'");
                StringFunction sFunction = new StringFunction();
                while (rs.next()){
                    int id = rs.getInt("tbl_inventory.Item_Id");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_items.Item_Name"));
                    String itemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                    String itemMinOrder = rs.getString("tbl_items.Item_MinOrder");
                    String itemRem = sFunction._Capitalized(rs.getString("tbl_items.Item_Remaining"));
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
