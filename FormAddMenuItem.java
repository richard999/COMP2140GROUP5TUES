/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormAddMenuItem extends JInternalFrame{
    JLabel lblTitle, lblMenuName, lblMenuPrice;
    JTextField txtMenuName, txtMenuPrice;
    JButton btnRemove, btnAdd, btnSave;
    DefaultTableModel tableModel;
    JTable tableMenuItems;
    public FormAddMenuItem(){
        lblTitle  =  new JLabel("Add Items To Menu");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblMenuName = new JLabel("Menu Item Name");
        lblMenuPrice = new JLabel("Price (JMD)");
        txtMenuName = new JTextField();
        txtMenuPrice = new JTextField();
        txtMenuPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                TextFieldFilter textFieldFilter = new TextFieldFilter();
                textFieldFilter._numbersAndPeriod(e);
            }
        });
        
        JPanel namePanel = new JPanel(new GridLayout(4, 3));
        namePanel.add(lblMenuName);
        namePanel.add(new JLabel(""));
        namePanel.add(new JLabel(""));
        namePanel.add(txtMenuName);
        namePanel.add(new JLabel(""));
        namePanel.add(new JLabel(""));
        namePanel.add(lblMenuPrice);
        namePanel.add(new JLabel(""));
        namePanel.add(new JLabel(""));
        namePanel.add(txtMenuPrice);
        namePanel.add(new JLabel(""));
        namePanel.add(new JLabel(""));
        
        String[] tableHeader = {
            "Item Id", "Item Name", "Item Description", "Unit"
        };
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
//        tableMenuItems = new JTable(){
//
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//            
//        };
//        tableMenuItems.setModel(tableModel);
//        tableMenuItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        tableMenuItems.getColumnModel().getColumn(0).setPreferredWidth(90);
//        tableMenuItems.getColumnModel().getColumn(1).setPreferredWidth(180);
//        tableMenuItems.getColumnModel().getColumn(2).setPreferredWidth(250);
        
        btnAdd = new JButton("Add Item");
        btnAdd.setPreferredSize(new Dimension(108, 30));
        btnAdd.addActionListener(new ButtonFunctions());
        btnRemove = new JButton("Remove Item");
        btnRemove.setPreferredSize(new Dimension(108, 30));
        btnRemove.addActionListener(new ButtonFunctions());
        btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(108, 30));
        btnSave.addActionListener(new ButtonFunctions());
        
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setPreferredSize(new Dimension(120, 580));
//        buttonPanel.add(btnAdd);
//        buttonPanel.add(btnRemove);
//        buttonPanel.add(btnSave);
        
//        JScrollPane scrollPane = new JScrollPane(tableMenuItems);
//        JPanel tablePanel = new JPanel(new BorderLayout());
//        tablePanel.add(scrollPane, BorderLayout.CENTER);
//        tablePanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.add(namePanel, BorderLayout.NORTH);
//        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(btnSave, BorderLayout.EAST);
//        container.add(new JLabel("    "), BorderLayout.WEST);
        
        setContentPane(container);
        setTitle("Add Menu Item");
        setBounds(20, 20, 800, 400);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setIconifiable(true);
        setClosable(true);
    }
    
    private class ButtonFunctions implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAdd){
                SubFormItemList itemList = new SubFormItemList();
                itemList._loadTableData();
                    if (JOptionPane.showConfirmDialog(null, itemList, "Item List", JOptionPane.OK_CANCEL_OPTION, 
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                        String[] value;
                        value = itemList._getValues();
                        if(!value[0].equals("")){
                            if(_itemExist(value[0])){
                                JOptionPane.showMessageDialog(null, "Item already exist!", "WARNING", JOptionPane.WARNING_MESSAGE);
                            } else {
                                if(!value[3].equals("")){
                                    tableModel.addRow(new Object[]{value[0], value[1], value[2], value[3]});
                                }
                            }
                        }
                    }
            } else if (e.getSource() == btnRemove){
                try {
                    tableModel.removeRow(tableMenuItems.getSelectedRow());
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            } else if (e.getSource() == btnSave){
                if("".equals(txtMenuName.getText().trim())){
                    JOptionPane.showMessageDialog(null, "Menu Item Name -  REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if ("".equals(txtMenuPrice.getText())){
                    JOptionPane.showMessageDialog(null, "Menu Item Price -  REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if(_itemMenuExist(txtMenuName.getText().trim())){
                        JOptionPane.showMessageDialog(null, "Menu Item already exist");
                    } else {
                        _saveMenuItem(txtMenuName.getText().trim());
                    }
                }
            }
        }
        
    }
    
    private void _saveMenuItem(String menuItemName){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            NumberFunction nFunction = new NumberFunction();
            StringFunction sFunction = new StringFunction();
            menuItemName = sFunction._removeWhiteSpaces(menuItemName);
            float price = nFunction._stripValue(txtMenuPrice.getText());
            try {
                int maxId = _getMenuItemMaxId();
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_menuitems (MenuItem_Id, MenuItem_Name, MenuItem_Price) "
                        + "VALUES ('"+maxId+"', '"+menuItemName.toLowerCase().trim()+"', '"+price+"')");
                st.executeUpdate("INSERT INTO tbl_menu (MenuItem_Id) VALUES ('"+maxId+"')");
                for(int i=0; i<tableModel.getRowCount(); i++){
                    String itemId = (String) tableMenuItems.getValueAt(i, 0);
                    String ingredientUnit = (String) tableMenuItems.getValueAt(i, 3);
                    st.executeUpdate("INSERT INTO tbl_ingredients (MenuItem_Id, Item_Id, Ingredient_Unit) "
                            + "VALUES ("+maxId+", "+itemId.trim()+", '"+ingredientUnit.trim()+"')");
                }
                JOptionPane.showMessageDialog(null, "Menu Item succesfully saved!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            txtMenuName.setText("");
            txtMenuPrice.setText("");
            tableModel.setRowCount(0);
        } catch(NumberFormatException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Wrong Input \n\n Price = Numbers Only", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        
        
    }
    
    private int _getMenuItemMaxId(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        int id = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Max(MenuItem_Id) FROM tbl_menuitems");
            if (rs.next()){
                id = rs.getInt("Max(MenuItem_Id)");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        if (id == 0){
            id+=1;
        } else {
            id+=1;
        }
        
        return id;
    }
    
    private boolean _itemMenuExist(String itemMenuName){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String id = "";
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MenuItem_Id FROM tbl_menuitems "
                    + "WHERE MenuItem_Name = '"+itemMenuName.toLowerCase()+"'");
            if(rs.next()){
                id = rs.getString("MenuItem_Id");
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
        
        if("".equals(id)){
            return false;
        }
        return true;
    }
    
    private boolean _itemExist(String id){
        try {
            for(int i=0; i<tableModel.getRowCount(); i++){
                if (tableMenuItems.getValueAt(i, 0).equals(id)){
                    return true;
                }
            }
        } catch(Exception ex) {
            
        }
        
        return false;
    }
    
}
