/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormEditVoucher extends JInternalFrame{
    private final JTextField txtVoucherNo, txtIssuedTo, txtDate, txtTotal;
    private final JButton btnSearch, btnSave, btnCancel;
    DefaultTableModel tableItemModel;
    private final JTable tableItem;
    
    private boolean running = false;
    
    public FormEditVoucher(){
        JLabel lblTitle = new JLabel("Edit Voucher");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblVoucherNo = new JLabel("Voucher No.");
        JLabel lblIssuedTo = new JLabel("Issued To");
        JLabel lblDate = new JLabel("Date");
        JLabel lblBlank1 = new JLabel("");
        JLabel lblBlank2 = new JLabel("");
        txtVoucherNo = new JTextField();
        txtVoucherNo.setPreferredSize(new Dimension(150, 26));
        txtVoucherNo.setEditable(false);
        txtIssuedTo = new JTextField();
        txtIssuedTo.setPreferredSize(new Dimension(150, 26));
        txtIssuedTo.setEditable(false);
        txtDate = new JTextField();
        txtDate.setEditable(false);
        txtDate.setPreferredSize(new Dimension(150, 26));
        btnSearch = new JButton(">");
        btnSearch.addActionListener(new ButtonFunction());
        
        JPanel topLeftPanel = new JPanel();
        GroupLayout layout = new GroupLayout(topLeftPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        topLeftPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group hg3 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        GroupLayout.Group vg3 = layout.createParallelGroup();
        
        hg1.addComponent(lblVoucherNo);
        hg1.addComponent(lblIssuedTo);
        hg1.addComponent(lblDate);
        
        hg2.addComponent(txtVoucherNo);
        hg2.addComponent(txtIssuedTo);
        hg2.addComponent(txtDate);
        
        hg3.addComponent(btnSearch);
        hg3.addComponent(lblBlank1);
        hg3.addComponent(lblBlank2);
        
        vg1.addComponent(lblVoucherNo);
        vg1.addComponent(txtVoucherNo);
        vg1.addComponent(btnSearch);
        
        vg2.addComponent(lblIssuedTo);
        vg2.addComponent(txtIssuedTo);
        vg2.addComponent(lblBlank1);
        
        vg3.addComponent(lblDate);
        vg3.addComponent(txtDate);
        vg3.addComponent(lblBlank2);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        hseq1.addGroup(hg3);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        
        String[] tableHeader = {
            "No.", "Item", "Amount"
        };
        tableItemModel = new DefaultTableModel();
        tableItemModel.setColumnIdentifiers(tableHeader);
        tableItem = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 2);
            }
        };
        tableItem.setModel(tableItemModel);
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItem.getColumnModel().getColumn(0).setPreferredWidth(75);
        tableItem.getColumnModel().getColumn(1).setPreferredWidth(330);
        tableItem.getColumnModel().getColumn(2).setPreferredWidth(170);
        
        JScrollPane scrollPane = new JScrollPane(tableItem);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(100, 26));
        btnSave.addActionListener(new ButtonFunction());
        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(100, 26));
        btnCancel.addActionListener(new ButtonFunction());
        
        JLabel lblTotal = new JLabel("Total");
        txtTotal = new JTextField();
        txtTotal.setPreferredSize(new Dimension(180, 26));
        txtTotal.setEditable(false);
        
        JPanel totalPanel = new JPanel();
        totalPanel.add(lblTotal);
        totalPanel.add(txtTotal);
        
        JPanel bottomCenterPanel = new JPanel(new BorderLayout());
        bottomCenterPanel.add(totalPanel, BorderLayout.EAST);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomCenterPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        
        _disabled();
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                GetTotalThread getTotalThread = new GetTotalThread();
                getTotalThread.start();
            }
        });
        
        setContentPane(container);
        setTitle("Edit Voucher");
        setBounds(60, 40, 600, 440);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _disabled(){
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
    private void _enabled(){
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSearch){
                _search();
            } else if (e.getSource() == btnSave){
                _save();
                _searchVoucherOrder();
            } else if (e.getSource() == btnCancel){
                _cancel();
            }
        }
        
        private void _save(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE tbl_voucher SET Voucher_TotalAmount="+_stripValue(txtTotal.getText())
                        + " WHERE Voucher_No="+txtVoucherNo.getText());
                for(int i=0; i<tableItem.getRowCount(); i++){
                    String id = _getId(tableItem.getValueAt(i, 1).toString());
                    float amount = _stripValue(tableItem.getValueAt(i, 2).toString());
                    st.executeUpdate("UPDATE tbl_voucherorder SET VoucherItem_Amount="+amount
                            + " WHERE Voucher_No="+txtVoucherNo.getText().trim()
                            + " AND VoucherItem_Id="+id);
                }
                JOptionPane.showMessageDialog(null, "Successfully Saved.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
        
        private String _getId(String item){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String id = null;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT VoucherItem_Id FROM tbl_voucheritem"
                        + " WHERE VoucherItem_Name='"+item.toLowerCase()+"'");
                if(rs.next()){
                    id = rs.getString("VoucherItem_Id");
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
            return id;
        }
        
        private void _cancel(){
            txtVoucherNo.setText("");
            txtDate.setText("");
            txtIssuedTo.setText("");
            txtTotal.setText("");
            btnSearch.setEnabled(true);
            tableItemModel.setRowCount(0);
            
            running = false; //-------Pause the thread
            
            _disabled();
        }
        
        private void _search(){
            SubFormVouchers subFormVouchers = new SubFormVouchers();
            if(JOptionPane.showConfirmDialog(null, subFormVouchers, "Vouchers", JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                if(subFormVouchers._checkItem()){
                    String[] value = subFormVouchers._getValue();
                    txtVoucherNo.setText(value[0]);
                    txtDate.setText(value[1]);
                    txtIssuedTo.setText(value[2]);
                    _searchVoucherOrder();
                    _incrementColumn();
                    
                    running = true;  ///--------Resume the thread
                    
                    btnSearch.setEnabled(false);
                    _enabled();
                }
                
            }
        }
        
        private void _searchVoucherOrder(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableItemModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_voucheritem.VoucherItem_Name,"
                        + " ROUND(tbl_voucherorder.VoucherItem_Amount, 2)"
                        + " FROM tbl_voucheritem INNER JOIN tbl_voucherorder"
                        + " ON tbl_voucheritem.VoucherItem_Id=tbl_voucherorder.VoucherItem_Id"
                        + " WHERE tbl_voucherorder.Voucher_No="+txtVoucherNo.getText());
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                while (rs.next()){
                    String name = sFunction._Capitalized(rs.getString("tbl_voucheritem.VoucherItem_Name"));
                    String amount = "Php "+nFunction._getFormattedNumber(rs.getString("ROUND(tbl_voucherorder.VoucherItem_Amount, 2)"));
                    tableItemModel.addRow(new Object[]{"", name, amount});
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
        
        private void _incrementColumn(){
            for(int i=0; i<tableItem.getRowCount(); i++){
                tableItem.setValueAt(i+1, i, 0);
            }
        }
        
    }
    
    private float _stripValue(String stringToStrip){
        float strippedValue;
        String word = "Php";
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
    
    private class GetTotalThread extends Thread implements Runnable{

        @Override
        public void run() {
            while(true){
                while(running){
                    float total =0;
                    NumberFunction nFunction = new NumberFunction();
                    for(int i=0; i<tableItem.getRowCount(); i++){
                        if(tableItem.getValueAt(i, 2).toString().trim().equals("")){
                            tableItem.setValueAt("Php 0.0", i, 2);
                        }
                        total += _stripValue(tableItem.getValueAt(i, 2).toString());
                        float amount = _stripValue(tableItem.getValueAt(i, 2).toString());
                        tableItem.setValueAt("Php "+nFunction._getFormattedNumber(Float.toString(amount)), i, 2);
                    }
                    double newTotal = Math.round(total*100.0)/100.0;
                    txtTotal.setText("Php " + nFunction._getFormattedNumber(Double.toString(newTotal)));

                    try {
                        GetTotalThread.sleep(200);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                try {
                    GetTotalThread.sleep(100);
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    }
    
}
