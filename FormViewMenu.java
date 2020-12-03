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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class FormViewMenu extends JInternalFrame{
    JLabel lblTitle, lblMenu, lblMenuSearch, lblIngredient;
    JTextField txtMenuSearch;
    JButton btnDelete;
    JTable tableMenu, tableIngredient;
    DefaultTableModel tableMenuModel, tableIngredientModel;
    
    public FormViewMenu(){
        lblTitle = new JLabel("View Menu");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblMenu = new JLabel("Menu");
        
        JPanel menuTPanel  = new JPanel();
        menuTPanel.add(lblMenu);
        
        lblMenuSearch = new JLabel("Search");
        txtMenuSearch = new JTextField();
        txtMenuSearch.setPreferredSize(new Dimension(150, 20));
        txtMenuSearch.addKeyListener(new TextFieldFunctions());
        
        JPanel menuSearchPanel = new JPanel();
        menuSearchPanel.add(lblMenuSearch);
        menuSearchPanel.add(txtMenuSearch);
        
        JPanel titleMenuPanel = new JPanel(new BorderLayout());
        titleMenuPanel.add(menuTPanel, BorderLayout.NORTH);
        titleMenuPanel.add(menuSearchPanel, BorderLayout.SOUTH);
        
        String[] menuHeader = {
            "ID", "Menu Item", "Price"
        };
        tableMenuModel = new DefaultTableModel();
        tableMenuModel.setColumnIdentifiers(menuHeader);
        tableMenu = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMenu.setModel(tableMenuModel);
        tableMenu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMenu.addMouseListener(new TableMouseFunctions());
        tableMenu.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableMenu.getColumnModel().getColumn(2).setPreferredWidth(120);
        
        JScrollPane menuScrollPane = new JScrollPane(tableMenu);
        
        JPanel centerMenuPanel = new JPanel();
        centerMenuPanel.add(menuScrollPane);
        
        btnDelete  = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(80,40));
        btnDelete.addActionListener(new ButtonFunctions());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnDelete);
        
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(titleMenuPanel, BorderLayout.NORTH);
        menuPanel.add(centerMenuPanel, BorderLayout.CENTER);
        menuPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        /*---------END MENU DESIGN-------------*/
        
        lblIngredient = new JLabel("Ingredients");
        lblIngredient.setPreferredSize(new Dimension(150, 20));
        
        JPanel ingredientTPanel1 = new JPanel();
        ingredientTPanel1.add(new JLabel(" "));
        
        JPanel ingredientTPanel2 = new JPanel();
        ingredientTPanel2.add(lblIngredient);
        
        JPanel titleIngredientPanel = new JPanel(new BorderLayout());
        titleIngredientPanel.add(ingredientTPanel1, BorderLayout.NORTH);
        titleIngredientPanel.add(ingredientTPanel2, BorderLayout.SOUTH);
        
        String[] ingredientHeader = {
            "Item ID", "Item Name", "Item Desciption", "Unit"
        };
        tableIngredientModel = new DefaultTableModel();
        tableIngredientModel.setColumnIdentifiers(ingredientHeader);
        tableIngredient = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        tableIngredient.setFocusable(false);
        tableIngredient.setCellSelectionEnabled(false);
        tableIngredient.setModel(tableIngredientModel);
        tableIngredient.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableIngredient.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableIngredient.getColumnModel().getColumn(2).setPreferredWidth(180);
        JScrollPane ingredientScrollPane = new JScrollPane(tableIngredient);
        
        JPanel centerIngredientPanel = new JPanel();
        centerIngredientPanel.add(ingredientScrollPane);
        
        JPanel ingredientPanel = new JPanel(new BorderLayout());
        ingredientPanel.add(titleIngredientPanel, BorderLayout.NORTH);
        ingredientPanel.add(centerIngredientPanel, BorderLayout.CENTER);
        
        /*-----------END INGREDIENT DESIGN-----------------*/
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        centerPanel.add(menuPanel);
        centerPanel.add(ingredientPanel);
        
        JPanel container  = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new WindowFunctions());
        
        setContentPane(container);
        setTitle("View Menu");
        setBounds(60,10, 1000, 600);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private class WindowFunctions implements InternalFrameListener{

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
            _loadMenuTableData();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private void _loadMenuTableData(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableMenuModel.setRowCount(0);
            tableIngredientModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_menu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                        + " ROUND(tbl_menuitems.MenuItem_Price,2)"
                        + " FROM tbl_menuitems INNER JOIN tbl_menu"
                        + " ON tbl_menuitems.MenuItem_Id = tbl_menu.MenuItem_Id");
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                String currency = "Php ";
                while(rs.next()){
                    int menuId = rs.getInt("tbl_menu.MenuItem_Id");
                    String menuItem = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                    String menuPrice = currency + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_menuitems.MenuItem_Price,2)"));
                    tableMenuModel.addRow(new Object[]{menuId, menuItem, menuPrice});
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
    
    private class TableMouseFunctions implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableIngredientModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_ingredients.Item_Id, tbl_items.Item_Name, "
                        + "tbl_items.Item_Description, tbl_ingredients.Ingredient_Unit "
                        + "FROM (tbl_inventory INNER JOIN tbl_items ON tbl_inventory.Item_Id=tbl_items.Item_Id) "
                        + "INNER JOIN tbl_ingredients ON tbl_ingredients.Item_Id=tbl_inventory.Item_Id "
                        + "WHERE tbl_ingredients.MenuItem_Id="+tableMenu.getValueAt(tableMenu.getSelectedRow(), 0));
                StringFunction sFunction = new StringFunction();
                while(rs.next()){
                    int id = rs.getInt("tbl_ingredients.Item_Id");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_items.Item_Name"));
                    String itemDesc = sFunction._Capitalized(rs.getString("tbl_items.Item_Description"));
                    String itemUnit = rs.getString("tbl_ingredients.Ingredient_Unit");
                    tableIngredientModel.addRow(new Object[]{id, itemName, itemDesc, itemUnit});
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

        @Override
        public void mousePressed(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    private class ButtonFunctions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnDelete){
                try{
                    int menuItemId = (int) tableMenu.getValueAt(tableMenu.getSelectedRow(), 0);
                    if(JOptionPane.showConfirmDialog(null, "Are you sure?", "", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        _deleteMenuItem(menuItemId);
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Please select an item!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        
        private void _deleteMenuItem(int menuItemId){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_deletedmenu(MenuItem_Id) VALUES ("+menuItemId+")");
                st.executeUpdate("DELETE FROM tbl_menu WHERE MenuItem_Id="+menuItemId);
                JOptionPane.showMessageDialog(null, "Deleted Succesfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
            if (e.getSource() == txtMenuSearch){
                _findMenuItem();
            }
        }
        
        private void _findMenuItem(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableMenuModel.setRowCount(0);
            tableIngredientModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_menu.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                        + " ROUND(tbl_menuitems.MenuItem_Price,2)"
                        + " FROM tbl_menuitems INNER JOIN tbl_menu"
                        + " ON tbl_menuitems.MenuItem_Id = tbl_menu.MenuItem_Id"
                        + " WHERE tbl_menu.MenuItem_Id LIKE '"+txtMenuSearch.getText().trim()+"%'"
                        + " OR tbl_menuitems.MenuItem_Name LIKE '%"+txtMenuSearch.getText().toLowerCase().trim()+"%'");
                StringFunction sFunction = new StringFunction();
                String currency = "Php ";
                while(rs.next()){
                    int menuId = rs.getInt("tbl_menu.MenuItem_Id");
                    String menuItem = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                    String menuPrice = currency + rs.getString("ROUND(tbl_menuitems.MenuItem_Price,2)");
                    tableMenuModel.addRow(new Object[]{menuId, menuItem, menuPrice});
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
