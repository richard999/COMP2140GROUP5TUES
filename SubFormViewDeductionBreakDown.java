/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
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
 * @author IT-Earl
 */
public class SubFormViewDeductionBreakDown extends JPanel {

    private final JTextField txtId, txtName;
    private final JTable tableDeductions;
    DefaultTableModel tableDeductionsModel;

    public SubFormViewDeductionBreakDown(String id, String name, String dateFrom, String dateTo) {
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblDeductions = new JLabel("Deductions");
        txtId = new JTextField(id);
        txtId.setEditable(false);
        txtName = new JTextField(name);
        txtName.setEditable(false);

        String[] tableHeader = {
            "Date", "Description", "Amount"
        };
        tableDeductionsModel = new DefaultTableModel();
        tableDeductionsModel.setColumnIdentifiers(tableHeader);
        tableDeductions = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDeductions.setModel(tableDeductionsModel);
        tableDeductions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDeductions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableDeductions.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableDeductions.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableDeductions.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tableDeductions);
        scrollPane.setPreferredSize(new Dimension(450, 250));

        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        setLayout(layout);

        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        GroupLayout.Group vg3 = layout.createParallelGroup();

        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        hg1.addComponent(lblDeductions);
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(scrollPane);

        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg3.addComponent(lblDeductions);
        vg3.addComponent(scrollPane);

        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();

        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);

        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        _getData(id, dateFrom, dateTo);
    }

    private void _getData(String id, String dateFrom, String dateTo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Deduction_Date, Deduction_Description,"
                    + " ROUND(Deduction_Amount, 2) FROM tbl_deductions"
                    + " WHERE Employee_Id='"+id+"' AND"
                    + " Deduction_Date BETWEEN '"+dateFrom+" 00:00:01' AND '"+dateTo+" 23:59:59'");
            NumberFunction nFunction = new NumberFunction();
            while(rs.next()){
                String date = rs.getString("Deduction_Date");
                String desc = rs.getString("Deduction_Description");
                String amount = "Php" + nFunction._getFormattedNumber(rs.getString("ROUND(Deduction_Amount, 2)"));
                tableDeductionsModel.addRow(new Object[]{date, desc, amount});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(SubFormViewDeductionBreakDown.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION_ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(SubFormViewDeductionBreakDown.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
