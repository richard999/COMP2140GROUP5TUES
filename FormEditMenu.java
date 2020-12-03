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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
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
public class FormEditMenu extends JInternalFrame {
    JLabel lblTitle;
    JLabel lblMenuItemId, lblMenuItemName, lblMenuItemPrice;
    JLabel lbl1, lbl2;
    JTextField txtMenuItemId, txtMenuItemName, txtMenuItemPrice;
    JTable tableItems;
    DefaultTableModel tableModel;
    JButton btnSearch;
    JButton btnAddItem, btnRemoveItem, btnSave, btnCancel;
    
    public FormEditMenu(){
        lblTitle = new JLabel("Edit Menu Item");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblMenuItemId = new JLabel("Menu Item ID");
        lblMenuItemName = new JLabel("Menu Item Name");
        lblMenuItemPrice = new JLabel("Price (Php)");
        lbl1 = new JLabel("");
        lbl2 = new JLabel("");
        
        txtMenuItemId = new JTextField();
        txtMenuItemId.setPreferredSize(new Dimension(200, 26));
        txtMenuItemId.setEditable(false);
        txtMenuItemName = new JTextField();
        txtMenuItemName.setPreferredSize(new Dimension(200, 26));
        txtMenuItemPrice = new JTextField();
        txtMenuItemPrice.setPreferredSize(new Dimension(200, 26));
        
        btnSearch = new JButton(">");
        btnSearch.addActionListener(new ButtonFunction());
        
        JPanel leftPanel = new JPanel();
        GroupLayout layout = new GroupLayout(leftPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        leftPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group hg3 = layout.createParallelGroup();
        
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        GroupLayout.Group vg3 = layout.createParallelGroup();
        
        hg1.addComponent(lblMenuItemId);
        hg1.addComponent(lblMenuItemName);
        hg1.addComponent(lblMenuItemPrice);
        
        hg2.addComponent(txtMenuItemId);
        hg2.addComponent(txtMenuItemName);
        hg2.addComponent(txtMenuItemPrice);
        
        hg3.addComponent(btnSearch);
        hg3.addComponent(lbl1);
        hg3.addComponent(lbl2);
        
        vg1.addComponent(lblMenuItemId);
        vg1.addComponent(txtMenuItemId);
        vg1.addComponent(btnSearch);
        
        vg2.addComponent(lblMenuItemName);
        vg2.addComponent(txtMenuItemName);
        vg2.addComponent(lbl1);
        
        vg3.addComponent(lblMenuItemPrice);
        vg3.addComponent(txtMenuItemPrice);
        vg3.addComponent(lbl2);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        hseq1.addGroup(hg3);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        JPanel centerTopPanel = new JPanel(new BorderLayout());
        centerTopPanel.add(leftPanel, BorderLayout.WEST);
        
        String[] tableHeader = {
            "Item ID", "Item Name", "Item Description", "Unit"
        };
        
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
        
        tableItems = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItems.setModel(tableModel);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableItems.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableItems);
        
        btnAddItem = new JButton("Add Item");
        btnAddItem.setPreferredSize(new Dimension(108, 30));
        btnAddItem.addActionListener(new ButtonFunction());
        btnRemoveItem = new JButton("Remove Item");
        btnRemoveItem.setPreferredSize(new Dimension(108, 30));
        btnRemoveItem.addActionListener(new ButtonFunction());
        
        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.setPreferredSize(new Dimension(110, 200));
        rightButtonPanel.add(btnAddItem);
        rightButtonPanel.add(btnRemoveItem);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(centerTopPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(rightButtonPanel, BorderLayout.EAST);
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(new ButtonFunction());
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ButtonFunction());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnSave);
        bottomPanel.add(btnCancel);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        
        _disableButtons();
        
        addInternalFrameListener(new WindowFunction());
        
        setTitle("Edit Menu Item");
        setContentPane(container);
        setBounds(50, 10, 800, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _disableButtons(){
        btnAddItem.setEnabled(false);
        btnRemoveItem.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
    private void _enableButtons(){
        btnAddItem.setEnabled(true);
        btnRemoveItem.setEnabled(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }
    
    private void _cleared(){
        txtMenuItemId.setText("");
            txtMenuItemName.setText("");
            txtMenuItemPrice.setText("");
            tableModel.setRowCount(0);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSearch){
                SubFormMenuList subFormMenuList = new SubFormMenuList();
                if(JOptionPane.showConfirmDialog(null, subFormMenuList, "Menu Item List", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    String[] value = subFormMenuList._getValues();
                    if(!value[0].equals("")){
                        txtMenuItemId.setText(value[0]);
                        txtMenuItemName.setText(value[1]);
                        txtMenuItemPrice.setText(value[2]);
                        _getTableData(txtMenuItemId.getText());
                        _enableButtons();
                        btnSearch.setEnabled(false);
                    }
                }
            }else if(e.getSource() == btnAddItem){
                SubFormItemList subFormItemList = new SubFormItemList();
                subFormItemList._loadTableData();
                if (JOptionPane.showConfirmDialog(null, subFormItemList, "Item List", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    _addItem(subFormItemList._getValues());
                }
            }else if(e.getSource() == btnRemoveItem){
                _removeItem();
            }else if(e.getSource() == btnCancel){
                _disableButtons();
                _cleared();
                btnSearch.setEnabled(true);
            }else if(e.getSource() == btnSave){
                _Updates(txtMenuItemId.getText());
            }
        }
        
        private void _Updates(String menuItemId){
            _deleteIngredients(menuItemId);
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String MenuPrice = txtMenuItemPrice.getText();
            String word = "Php";
            if(MenuPrice.contains(word)){
                MenuPrice = MenuPrice.replace(word, "");
            }
            word = ",";
            if(MenuPrice.contains(word)){
                MenuPrice = MenuPrice.replace(word, "");
            }
            
            try {
                StringFunction sFunction = new StringFunction();
                String menuItemName = sFunction._removeWhiteSpaces(txtMenuItemName.getText().toLowerCase().trim());
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE tbl_menuitems SET MenuItem_Name='"+menuItemName+"',"
                        + " MenuItem_Price='"+MenuPrice+"' WHERE MenuItem_Id="+menuItemId);
                for(int i=0; i<tableModel.getRowCount(); i++){
                    String itemId = tableItems.getValueAt(i, 0).toString().trim();
                    String itemUnit = tableItems.getValueAt(i, 3).toString().trim();
                    st.executeUpdate("INSERT INTO tbl_ingredients (MenuItem_Id, Item_Id, Ingredient_Unit)"
                            + " VALUES ('"+menuItemId+"','"+itemId+"','"+itemUnit+"')");
                }
                JOptionPane.showMessageDialog(null, "Successfully Updated", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
        
        private void _deleteIngredients(String menuItemId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_ingredients WHERE MenuItem_Id="+menuItemId);
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
        
        private void _removeItem(){
            try {
                tableModel.removeRow(tableItems.getSelectedRow());
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        //value[4];-----------------v
        private void _addItem(String[] value){
            if(!value[0].equals("")){
                if(_itemExist(value[0])){
                    JOptionPane.showMessageDialog(null, "Item already exist", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if(!value[3].equals("")){
                        tableModel.addRow(new Object[]{value[0], value[1], value[2], value[3]});
                    }
                }
            }
        }
        
        private boolean _itemExist(String value){
            for(int i=0; i< tableItems.getRowCount(); i++){
                if(value.equals(tableItems.getValueAt(i, 0))){
                    return true;
                }
            }
            return false;
        }
        
        private void _getTableData(String menuItemId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_inventory.Item_Id, tbl_items.Item_Name,"
                        + " tbl_items.Item_Description, tbl_ingredients.Ingredient_Unit"
                        + " FROM (tbl_inventory INNER JOIN tbl_items ON"
                        + " tbl_inventory.Item_Id=tbl_items.Item_Id) INNER JOIN tbl_ingredients ON"
                        + " tbl_inventory.Item_Id=tbl_ingredients.Item_Id"
                        + " WHERE MenuItem_Id="+menuItemId);
                StringFunction sFunction = new StringFunction();
                while(rs.next()){
                    String id = rs.getString("tbl_inventory.Item_Id");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_items.Item_Name"));
                    String itemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                    String unit = rs.getString("tbl_ingredients.Ingredient_Unit");
                    tableModel.addRow(new Object[]{id, itemName, itemDesc, unit});
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
    
    private class WindowFunction implements InternalFrameListener{

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            _cleared();
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
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
