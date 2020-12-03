/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class SubFormItemList extends JPanel {
    protected JLabel lblSearch, lblUnit;
    protected JTextField txtSearch, txtQuantity, txtUnit;
    protected JTable tableItems;
    protected DefaultTableModel tableModel;
    
    public SubFormItemList(){
        lblSearch = new JLabel("Search");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 30));
        txtSearch.addKeyListener(new TextFieldFunctions());

        JPanel searchPanel = new JPanel();
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        
        String[] tableHeader = {
            "Item ID", "Item Name", "Item Description", "Item Remaining"
        };
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
        tableItems = new JTable(){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tableItems.setModel(tableModel);
        tableItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItems.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(280);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItems.addMouseListener(new UnitDetector());
        
        JScrollPane scrollPane = new JScrollPane(tableItems);
        scrollPane.setPreferredSize(new Dimension(700,300));

        JPanel centerPanel = new JPanel();
        centerPanel.add(scrollPane);
            
        lblUnit = new JLabel("Unit");
        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(80,20));
        txtUnit = new JTextField();
        txtUnit.setEditable(false);
        txtUnit.setPreferredSize(new Dimension(40,20));
            
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(lblUnit);
        bottomPanel.add(txtQuantity);
        bottomPanel.add(txtUnit);

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    protected void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_inventory.Item_Id, tbl_items.Item_Name,"
                    + "tbl_items.Item_Description, tbl_items.Item_Remaining FROM tbl_items INNER JOIN tbl_inventory "
                    + "ON tbl_items.Item_Id = tbl_inventory.Item_Id");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                int id = rs.getInt("tbl_inventory.Item_Id");
                String itemName = sFunction._Capitalized(rs.getString("tbl_items.Item_Name"));
                String itemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                String itemRem = rs.getString("tbl_items.Item_Remaining");
                tableModel.addRow(new Object[]{id, itemName, itemDesc, itemRem});
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
    
    private class UnitDetector implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if(e.getSource() == tableItems){
                String unit = (String) tableItems.getValueAt(tableItems.getSelectedRow(), 3);
                String word = "kg";
                if(unit.contains(word)){
                    txtUnit.setText(word);
                } else {
                    txtUnit.setText("");
                }
                txtQuantity.requestFocus();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                ResultSet rs = st.executeQuery("SELECT tbl_inventory.Item_Id, tbl_items.Item_Name,"
                        + "tbl_items.Item_Description, tbl_items.Item_Remaining FROM tbl_items INNER JOIN tbl_inventory "
                        + "ON tbl_items.Item_Id = tbl_inventory.Item_Id "
                        + "WHERE tbl_inventory.Item_Id LIKE '"+txtSearch.getText()+"%' "
                        + "OR tbl_items.Item_Name LIKE '%"+txtSearch.getText().toLowerCase().trim()+"%' "
                        + "OR tbl_items.Item_Description LIKE '%"+txtSearch.getText().toLowerCase().trim()+"%'");
                StringFunction sFunction = new StringFunction();
                while(rs.next()){
                    int id = rs.getInt("tbl_inventory.Item_Id");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_items.Item_Name"));
                    String itemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                    String itemRem = rs.getString("tbl_items.Item_Remaining");
                    tableModel.addRow(new Object[]{id, itemName, itemDesc, itemRem});
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
    
    protected String[] _getValues(){
        String[] value = new String[4];
        value[0] = "";
        value[1] = "";
        value[2] = "";
        value[3] = "";
        try {
            value[0] = Integer.toString((int) tableItems.getValueAt(tableItems.getSelectedRow(), 0));
            value[1] = (String) tableItems.getValueAt(tableItems.getSelectedRow(), 1);
            value[2] = (String) tableItems.getValueAt(tableItems.getSelectedRow(), 2);
            try {
                String quantity;
                if(txtUnit.getText().contains("kg")){
                    float temp = Float.parseFloat(txtQuantity.getText());
                    quantity = Float.toString(temp);
                } else {
                    int temp = Integer.parseInt(txtQuantity.getText());
                    quantity = Integer.toString(temp);
                }
                
                value[3] = quantity + txtUnit.getText();
            } catch(NumberFormatException | HeadlessException ex){
                JOptionPane.showMessageDialog(null, "Quantity = Numbers Only", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        return value;
    }
    
}
