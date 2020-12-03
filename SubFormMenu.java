/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
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
public class SubFormMenu extends JPanel{
    JLabel lblSearch, lblOrder;
    JTextField txtSearch, txtOrder;
    JTable tableMenu;
    DefaultTableModel tableModel;
    public SubFormMenu(){
        lblSearch = new JLabel("Search");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.addKeyListener(new KeyFunctions());
        
        JPanel topPanel = new JPanel();
        topPanel.add(lblSearch);
        topPanel.add(txtSearch);
        
        String[] tableHeader = {
            "Item #", "Menu", "Unit Price"
        };
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
        tableMenu = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMenu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableMenu.setModel(tableModel);
        tableMenu.addMouseListener(new TableFunctions());
        tableMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMenu.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableMenu.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableMenu.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableMenu.addKeyListener(new KeyFunctions());
        
        JScrollPane scrollPane = new JScrollPane(tableMenu);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(scrollPane);
        
        lblOrder = new JLabel("# of Order");
        txtOrder = new JTextField();
        txtOrder.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                TextFieldFilter textFieldFilter = new TextFieldFilter();
                textFieldFilter._numbersOnly(e);
           }
        });
        txtOrder.setPreferredSize(new Dimension(50, 30));
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(lblOrder);
        bottomPanel.add(txtOrder);
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_menu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                    + " ROUND(tbl_menuitems.MenuItem_Price, 2)"
                    + " FROM tbl_menuitems INNER JOIN tbl_menu"
                    + " ON tbl_menuitems.MenuItem_Id=tbl_menu.MenuItem_Id");
            StringFunction sFunction = new StringFunction();
            NumberFunction nFunction = new NumberFunction();
            String currency = "Php ";
            while (rs.next()){
                String id = rs.getString("tbl_menu.MenuItem_Id");
                String itemName = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                String itemPrice = currency + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_menuitems.MenuItem_Price, 2)"));
                tableModel.addRow(new Object[]{id, itemName, itemPrice});
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
    
    private class TableFunctions implements MouseListener {

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
            txtOrder.requestFocus();
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
    
    private class KeyFunctions implements KeyListener {

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
                _search();
            } else if (e.getSource() == tableMenu){
                if(e.getKeyCode() == 32){
                    txtOrder.requestFocus();
                }
            }
        }
        
        public void _search(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableModel.setRowCount(0);
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT MenuItem_Id, MenuItem_Name,"
                        + " ROUND(MenuItem_Price, 2) FROM tbl_menuitems"
                        + " WHERE MenuItem_Name LIKE '%"+txtSearch.getText().trim()+"%'");
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                String currency = "Php ";
                while (rs.next()){
                    String id = rs.getString("MenuItem_Id");
                    String itemName = sFunction._Capitalized(rs.getString("MenuItem_Name"));
                    String itemPrice = currency + nFunction._getFormattedNumber(rs.getString("ROUND(MenuItem_Price, 2)"));
                    tableModel.addRow(new Object[]{id, itemName, itemPrice});
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
    
    public String[] _getValues(){
        String[] value = new String[5];
        try{
            int order = Integer.parseInt(txtOrder.getText());
            float price = _stripValue(tableMenu.getValueAt(tableMenu.getSelectedRow(), 2).toString());
            float subTotal = price * (float) order;
            
            value[0] = tableMenu.getValueAt(tableMenu.getSelectedRow(), 0).toString();
            value[1] = tableMenu.getValueAt(tableMenu.getSelectedRow(), 1).toString();
            value[2] = tableMenu.getValueAt(tableMenu.getSelectedRow(), 2).toString();
            value[3] = txtOrder.getText();
            NumberFunction nFunction = new NumberFunction();
            value[4] = "JMD " + nFunction._getFormattedNumber(Float.toString(subTotal));
        } catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "WARNINGssssss \n\n # of Orders = Numbers only.", "WARNING", JOptionPane.WARNING_MESSAGE);
        } catch (ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(null, "WARNING \n\n Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        txtOrder.setText("");
        return value;
    }
    
    private float _stripValue(String stringToStrip){
        float strippedValue;
        String word = "JMD";
        if(stringToStrip.contains(word)){
            stringToStrip = stringToStrip.replace(word, "");
        }
        word = ",";
        if(stringToStrip.contains(word)){
            stringToStrip = stringToStrip.replace(word, "");
        }
        strippedValue = Float.parseFloat(stringToStrip);
        return strippedValue;
    }
    
}
