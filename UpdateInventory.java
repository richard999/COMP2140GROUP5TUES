/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Group 5
 */
public class UpdateInventory {
    protected boolean _checkIngredient(int MenuItemId){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery("SELECT Item_Id FROM tbl_ingredients"
                    + " WHERE MenuItem_Id="+MenuItemId);
            if(rs.next()){
                conn.close();
                return true;
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
        return false;
    }
    
    protected String[] _getIngredientId(int menuItemId){
        ArrayList valueList = new ArrayList();
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Item_Id FROM tbl_ingredients"
                    + " WHERE MenuItem_Id="+menuItemId);
            while(rs.next()){
                valueList.add(rs.getString("Item_Id"));
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
        String[] value = new String[valueList.size()];
        valueList.toArray(value);
        valueList.clear();
        return value;
    }
    
    protected String _getIngredientUnit(int menuItemId, int itemId){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String value = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Ingredient_Unit FROM tbl_ingredients"
                    + " WHERE MenuItem_Id="+menuItemId+" AND Item_Id="+itemId);
            if(rs.next()){
                value = rs.getString("Ingredient_Unit");
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
        return value;
    }
    
    protected String _getItemRemaining(int itemId){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String value = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Item_Remaining FROM tbl_items"
                    + " WHERE Item_Id="+itemId);
            if(rs.next()){
                value = rs.getString("Item_Remaining");
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
        return value;
    }
    
    protected void _goUpdate(int itemId, String itemRemaining){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_items SET Item_Remaining='"+itemRemaining+"'"
                    + " WHERE Item_Id="+itemId);
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
