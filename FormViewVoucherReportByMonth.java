/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
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
public class FormViewVoucherReportByMonth  extends JInternalFrame{
    private final JComboBox cmbMonth;
    private final JTextField txtYear;
    private final JTable tableItems;
    private final JButton btnGenerate;
    DefaultTableModel tableItemsModel;
    
    public FormViewVoucherReportByMonth() {
        String[] months = {
            "", "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"
        };
        
        JLabel lblMonth = new JLabel("Month");
        JLabel lblYear = new JLabel("Year");
        cmbMonth = new JComboBox(months);
        txtYear = new JTextField();
        txtYear.setPreferredSize(new Dimension(60, cmbMonth.getPreferredSize().height));
        txtYear.addKeyListener(new KeyAdapter() {
            TextFieldFilter textFieldFilter = new TextFieldFilter();
            @Override
            public void keyTyped(KeyEvent e) {
                textFieldFilter._numbersOnly(e);
            }
        });
        
        JPanel topTopPanel = new JPanel();
        topTopPanel.add(lblMonth);
        topTopPanel.add(cmbMonth);
        topTopPanel.add(lblYear);
        topTopPanel.add(txtYear);
        
        btnGenerate = new JButton("Generate");
        btnGenerate.addActionListener(new ButtonFunction());
        
        JPanel topCenterPanel = new JPanel();
        topCenterPanel.add(btnGenerate);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topTopPanel, BorderLayout.NORTH);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        
        String[] tableHeader = {
        "Voucher Item", "Total Amount"};
        tableItemsModel = new DefaultTableModel();
        tableItemsModel.setColumnIdentifiers(tableHeader);
        tableItems = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItems.setModel(tableItemsModel);
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItems.getColumnModel().getColumn(0).setPreferredWidth(250);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(tableItems);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(container);
        setTitle("Voucher Report: By Month");
        setBounds(20, 20, 500, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnGenerate){
                _generateVoucherReportByMonth();
            }
        }

        private void _generateVoucherReportByMonth() {
            String message = "\n";
            boolean warning = false;
            if(cmbMonth.getSelectedItem().toString().equals("")){
                message += "*Month is REQUIRED. \n";
                warning = true;
            }
            if(txtYear.getText().equals("")){
                message += "*Year is REQUIRED. \n";
                warning = true;
            }
            
            if(warning){
                JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                _goGenerateVoucherItem();
            }
        }

        private void _goGenerateVoucherItem() {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableItemsModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_particulars.VoucherItem_Id"
                        + " FROM tbl_voucheritem INNER JOIN tbl_particulars"
                        + " ON tbl_voucheritem.VoucherItem_Id=tbl_particulars.VoucherItem_Id");
                while(rs.next()){
                    String month = Integer.toString(cmbMonth.getSelectedIndex());
                    String year = txtYear.getText().trim();
                    String id = rs.getString("tbl_particulars.VoucherItem_Id");
                    _goGenerateReport(id, month, year);
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormViewVoucherReportByMonth.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void _goGenerateReport(String id, String month, String year) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String dateFrom = year + "-" + month + "-" + "1 " + "00:00:01";
            String dateTo = year + "-" + month + "-" + "31 " + "23:59:59";
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_voucheritem.VoucherItem_Name,"
                        + " SUM(tbl_voucherorder.VoucherItem_Amount)"
                        + " FROM (tbl_voucheritem INNER JOIN tbl_voucherorder"
                        + " ON tbl_voucheritem.VoucherItem_Id=tbl_voucherorder.VoucherItem_Id)"
                        + " INNER JOIN tbl_voucher ON tbl_voucher.Voucher_No=tbl_voucherorder.Voucher_No"
                        + " WHERE tbl_voucheritem.VoucherItem_Id='"+id+"'"
                        + " AND tbl_voucher.Voucher_Date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'");
                while (rs.next()){
                    String item = rs.getString("tbl_voucheritem.VoucherItem_Name");
                    float amount = (float) (Math.round(rs.getFloat("SUM(tbl_voucherorder.VoucherItem_Amount)")*100.00)/100.00);
                    tableItemsModel.addRow(new Object[]{item, "Php " + amount});
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormViewVoucherReportByMonth.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(FormViewVoucherReportByMonth.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    }
    
}
