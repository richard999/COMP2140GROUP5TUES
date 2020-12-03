/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author IT-NEW
 */
public class DatabaseConnection {
    private Connection conn;
    
    public DatabaseConnection(){
        try {
            Connection connect;
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eatspossible?zeroDateTimeBehavior=convertToNull","root", "");
            connect.setAutoCommit(true);
            _setConnection(connect);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void _setConnection(Connection conn){
        this.conn = conn;
    }
    
    protected Connection _getConnection(){
        return conn;
    }
}
