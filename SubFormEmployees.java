/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
public class SubFormEmployees extends JPanel{
    private final JTextField txtFilter;
    private final JTable tableEmployees;
    DefaultTableModel tableEmployeesModel;
    
    public SubFormEmployees(){
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(150, 25));
        
        JPanel filterPanel = new JPanel();
        filterPanel.add(lblFilter);
        filterPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Name"
        };
        tableEmployeesModel = new DefaultTableModel();
        tableEmployeesModel.setColumnIdentifiers(tableHeader);
        tableEmployees = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEmployees.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableEmployees.setModel(tableEmployeesModel);
        tableEmployees.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableEmployees.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tableEmployees);
        
        setLayout(new BorderLayout());
        
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
    }
    
    protected void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableEmployeesModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Employee_Id, Employee_Name FROM tbl_employees");
            while(rs.next()){
                String id = rs.getString("Employee_Id");
                String name = rs.getString("Employee_Name");
                tableEmployeesModel.addRow(new Object[]{id, name});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                //Logger.getLogger(SubFormEmployees.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    protected String[] _getValues(){
        String[] value = new String[2];
        
        value[0] = "";
        value[1] = "";
        
        try {
            value[0] = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
            value[1] = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 1).toString();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select an Employee", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        return value;
    }
    
}
