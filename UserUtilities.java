/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author IT-NEW
 */
public class UserUtilities {
     protected String _getUserMaxId(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        String MaxId = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(User_Id) FROM tbl_Users");
            if(rs.next()){
                MaxId = rs.getString("MAX(User_Id)");
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
        
        if (MaxId == null){
            MaxId = "1";
        } else {
            int tempId = Integer.parseInt(MaxId);
            tempId+=001;
            MaxId = Integer.toString(tempId);
        }
        
        return MaxId;
    }
     
    protected int _getUserTypeId(String UserType){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        int UserTypeId = 0;
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT UserTypeId FROM tbl_UserType Where UserType='" + UserType + "'");
            if (rs.next()){
                UserTypeId = rs.getInt("UserTypeId");
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
        
        return UserTypeId;
    }
}
