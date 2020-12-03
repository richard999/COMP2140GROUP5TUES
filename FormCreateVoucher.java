/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FormCreateVoucher extends JInternalFrame{
    private final JLabel lblVoucher;
    private final JTextField txtName, txtDate;
    DefaultTableModel tableItemModel;
    private final JTable tableItem;
    private final JButton btnAdd, btnRemove, btnPrint;
    private final JPanel centerPanel;
    
    private int tableIndex = 0;
    private boolean running = true;
    
    GetTotalThread getTotalThread;
    
    public FormCreateVoucher(){
        JLabel lblTitle = new JLabel("Create Voucher");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblVoucherHeader = new JLabel("<html><body>  "
                + "<center>"
                + "Eats Possible<br>"
                + "Lapu-Lapu St., Tagum City <br>"
                + "09173140439 or  084-308-1946"
                + "</center>"
                + " </body></html>");
        
        JPanel centerTopPanel = new JPanel();
        centerTopPanel.add(lblVoucherHeader);
        
        JLabel lblCenterHeader = new JLabel("PETTY  CASH  VOUCHER");
        
        JPanel voucherHeaderPanel = new JPanel();
        voucherHeaderPanel.add(lblCenterHeader);
        
        JLabel lblVoucherNo = new JLabel("No.");
        lblVoucherNo.setFont(new Font(bigFont.getName(), Font.PLAIN, 14));
        lblVoucher = new JLabel("                ");
        lblVoucher.setFont(new Font(bigFont.getName(), Font.PLAIN, 14));
        
        JPanel voucherNoPanel = new JPanel();
        voucherNoPanel.add(lblVoucherNo);
        voucherNoPanel.add(lblVoucher);
        
        JPanel centerHeaderPanel = new JPanel(new BorderLayout());
        centerHeaderPanel.add(voucherHeaderPanel, BorderLayout.NORTH);
        centerHeaderPanel.add(voucherNoPanel, BorderLayout.EAST);
        
        JLabel lblName = new JLabel("Name");
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 25));
        
        JPanel midTopLeftPanel = new JPanel();
        midTopLeftPanel.add(lblName);
        midTopLeftPanel.add(txtName);
        
        JLabel lblDate = new JLabel("Date");
        txtDate = new JTextField();
        txtDate.setPreferredSize(new Dimension(150, 25));
        txtDate.setEditable(false);
        txtDate.setBackground(Color.white);
        
        JPanel midTopRightPanel = new JPanel();
        midTopRightPanel.add(lblDate);
        midTopRightPanel.add(txtDate);
        
        String[] tableHeader = {
            "No.", "Particulars", "Amount"
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
        
        JPanel midTopPanel = new JPanel(new BorderLayout());
        midTopPanel.add(midTopLeftPanel, BorderLayout.WEST);
        midTopPanel.add(midTopRightPanel, BorderLayout.EAST);
        
        JLabel lblApprovedBy = new JLabel("<html><body>"
                + "<center>"
                + "_______________________<br>"
                + "Approved By"
                + "</center>"
                + "</body></html>");
        
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setPreferredSize(new Dimension(120, 40));
        bottomLeftPanel.add(lblApprovedBy);
        
        JLabel lblPreparedBy = new JLabel("<html><body>"
                + "<center>"
                + "_______________________<br>"
                + "Prepared By"
                + "</center>"
                + "</body></html>");
        
        JPanel bottomCenterPanel = new JPanel();
        bottomCenterPanel.setPreferredSize(new Dimension(120, 40));
        bottomCenterPanel.add(lblPreparedBy);
        
        JLabel lblReceivedBy = new JLabel("<html><body>"
                + "<center>"
                + "_______________________<br>"
                + "Received By"
                + "</center>"
                + "</body></html>");
        
        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setPreferredSize(new Dimension(120, 40));
        bottomRightPanel.add(lblReceivedBy);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 45));
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomPanel.add(bottomCenterPanel, BorderLayout.CENTER);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        
        JPanel midPanel = new JPanel(new BorderLayout());
        midPanel.add(midTopPanel, BorderLayout.NORTH);
        midPanel.add(scrollPane, BorderLayout.CENTER);
        midPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        JPanel centerCenterPanel = new JPanel(new BorderLayout());
        centerCenterPanel.add(centerHeaderPanel, BorderLayout.NORTH);
        centerCenterPanel.add(midPanel, BorderLayout.CENTER);
        
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(centerTopPanel, BorderLayout.NORTH);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        
        JPanel container = new JPanel(new BorderLayout());
        //container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        btnAdd = new JButton("Add");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(new ButtonFunction());
        btnRemove = new JButton("Remove");
        btnRemove.setPreferredSize(new Dimension(100, 36));
        btnRemove.addActionListener(new ButtonFunction());
        btnPrint = new JButton("Print");
        btnPrint.setPreferredSize(new Dimension(100, 36));
        btnPrint.addActionListener(new ButtonFunction());
        JLabel lblBlank = new JLabel("    ");
        lblBlank.setPreferredSize(new Dimension(100, 150));
        
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(110, container.getPreferredSize().height));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.add(lblBlank);
        rightPanel.add(btnAdd);
        rightPanel.add(btnRemove);
        rightPanel.add(btnPrint);
        
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(container, BorderLayout.CENTER);
        mainContainer.add(rightPanel, BorderLayout.EAST);
        
        _putItemNo();
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                _loadVoucherNo();
                _loadCurrentDate();
                running = true;
            }
            
            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                running = false;
            }

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                getTotalThread = new GetTotalThread();
                getTotalThread.start();
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                _clear();
            }
            
        });
        
        setContentPane(mainContainer);
        setTitle("View Voucher Item");
        setBounds(50, 10, 700, 410);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _clear(){
        txtName.setText("");
        tableIndex = 0;
        for(int i=0; i<tableItem.getRowCount()-1; i++){
            tableItem.setValueAt("", i, 1);
            tableItem.setValueAt("", i, 2);
        }
    }
    
    private class GetTotalThread extends Thread{
        
        @Override
        public void run() {
            while(true){
                while(running){
                    _getTotal();
                    try {
                        GetTotalThread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FormCreateVoucher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    GetTotalThread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FormCreateVoucher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
        }
        
        private void _getTotal(){
            float total = 0;
            NumberFunction nFunction = new NumberFunction();
            for(int i=0; i<tableItemModel.getRowCount()-1; i++){
                if(!tableItem.getValueAt(i, 2).toString().equals("")){
                    float amount = _stripValue(tableItem.getValueAt(i, 2).toString());
                    total += amount;
                    double newAmount = Math.round(amount*100.0)/100.0;
                    tableItem.setValueAt("Php "+nFunction._getFormattedNumber(Double.toString(newAmount)), i, 2);
                }
            }
            double newKB = Math.round(total*100.0)/100.0;
            tableItem.setValueAt("Php "+nFunction._getFormattedNumber(Double.toString(newKB)), tableItemModel.getRowCount()-1, 2);
        }
    }
    
    
    private void _loadVoucherNo(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String value = null;
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Max(Voucher_No) FROM tbl_voucher");
            if(rs.next()){
                value = rs.getString("Max(Voucher_No)");
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
        
        if (value == null){
            value = "100";
        } else {
            int temp = Integer.parseInt(value);
            temp += 1;
            value = Integer.toString(temp);
        }
        lblVoucher.setText(value);
    }
    
    private void _loadCurrentDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String stringDate = sdf.format(date);
        String[] tempDate = stringDate.split(" ");
        String[] time = tempDate[1].split(":");
        if(Integer.parseInt(time[0]) > 12){
            time[0] = Integer.toString(Integer.parseInt(time[0])-12);
            stringDate = tempDate[0] + " " + time[0] + ":" +time[1] + " PM";
        } else if (Integer.parseInt(time[0]) == 12){
            stringDate = tempDate[0] + " " + time[0] + ":" +time[1] + " PM";
        } else {
            stringDate = tempDate[0] + " " + time[0] + ":" +time[1] + " AM";
        }
        txtDate.setText(stringDate);
    }
    
    private void _putItemNo(){
        for(int i=0; i<9 ; i++){
            tableItemModel.addRow(new Object[]{i+1, "", ""});
        }
        tableItemModel.addRow(new Object[]{"", "<html><body><b> Total </b></body></html>", ""});
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
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAdd){
                _add();
            }else if (e.getSource() == btnRemove){
                _remove();
            }else if(e.getSource() == btnPrint){
                _print();
            }
            
        }
        
        private void _remove(){
            if(!tableItem.getValueAt(tableItem.getSelectedRow(), 1).toString().equals("")){
                tableItem.setValueAt("", tableItem.getSelectedRow(), 1);
                tableItem.setValueAt("", tableItem.getSelectedRow(), 2);
                if(tableItem.getSelectedRow() != tableItem.getRowCount()-2){
                    for(int i=tableItem.getSelectedRow(); i<tableItem.getRowCount()-1; i++){
                        if(!tableItem.getValueAt(i+1, 1).toString().equals("")){
                            String item = tableItem.getValueAt(i+1, 1).toString();
                            tableItem.setValueAt(item, i, 1);
                            String amount = tableItem.getValueAt(i+1, 2).toString();
                            tableItem.setValueAt(amount, i, 2);
                            tableItem.setValueAt("", i+1, 1);
                            tableItem.setValueAt("", i+1, 2);
                        } else {
                            break;
                        }
                    }
                }
                tableIndex--;
            }
        }
        
        private void _add(){
            SubFormVoucherItem subFormVoucherItem = new SubFormVoucherItem();
            if(JOptionPane.showConfirmDialog(null, subFormVoucherItem, "Voucher Items",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                if(subFormVoucherItem._checkValue()){
                    String value = subFormVoucherItem._getValue();
                    if(_ifExist(value)){
                        JOptionPane.showMessageDialog(null, "Item already added.", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if(tableIndex < 9){
                            tableItem.setValueAt(value, tableIndex, 1);
                            tableIndex++;
                        } else {
                            JOptionPane.showMessageDialog(null, "Item cannot be added. \n\n 9 items only.", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        }
        
        private boolean _ifExist(String item){
            for (int i=0; i<tableItemModel.getRowCount()-1; i++){
                if(item.equals(tableItem.getValueAt(i, 1).toString())){
                    return true;
                }
            }
            return false;
        }
        
        private void _print(){
            if(txtName.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "FAILED! \n\nName is required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else if(tableIndex == 0){
                JOptionPane.showMessageDialog(null, "FAILED! \n\nItem(s) not found", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else if (tableItem.getValueAt(tableItem.getRowCount()-1, 2).toString().equals("Php 0.0")){
                JOptionPane.showMessageDialog(null, "FAILED! \n\nAmount Column is required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                FormPrintVoucher formPrintVoucher = new FormPrintVoucher();
                formPrintVoucher._setPanel(centerPanel);
                if(formPrintVoucher._goPrint()){
                    _save();
                    _clear();
                }
            }
            
        }
        
        private void _save(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                StringFunction sFunction = new StringFunction();
                String name = sFunction._removeWhiteSpaces(txtName.getText().trim());
                float total = _stripValue(tableItem.getValueAt(tableItem.getRowCount()-1, 2).toString());
                String date = _getFormattedDate();
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_voucher (Voucher_No, Voucher_Date,"
                        + " Voucher_TotalAmount, Voucher_IssuedTo)"
                        + " VALUES ('"+lblVoucher.getText()+"', '"+date+"',"
                        + " '"+total+"', '"+name+"')");
                for(int i=0; i<tableItem.getRowCount()-1; i++){
                    if(tableItem.getValueAt(i, 1).toString().equals("")){
                        break;
                    } else {
                        String id = _getItemId(tableItem.getValueAt(i, 1).toString());
                        float amount = _stripValue(tableItem.getValueAt(i, 2).toString());
                        st.executeUpdate("INSERT INTO tbl_voucherorder (Voucher_No,"
                                + " VoucherItem_Id, VoucherItem_Amount)"
                                + " VALUES ('"+lblVoucher.getText()+"', '"+id+"', '"+amount+"')");
                    }
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
        
        private String _getItemId(String item){
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
        
        private String _getFormattedDate(){
            String date = txtDate.getText();
            String wordAM  = "AM";
            String wordPM  = "PM";
            if(date.contains(wordAM)){
                date = date.replace(wordAM, "");
                date = date.trim();
            } else if(date.contains(wordPM)){
                date = date.replace(wordPM, "");
                date = date.trim();
                String[] tempDate = date.split(" ");
                String[] time = tempDate[1].split(":");
                if(Integer.parseInt(time[0])<12){
                    time[0] = Integer.toString(Integer.parseInt(time[0])+12);
                }
                date  = tempDate[0] + " " + time[0] + ":" + time[1];
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date newDate = null;
            try {
                newDate = sdf.parse(date);
            } catch (ParseException ex) {
                Logger.getLogger(FormCreateVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(newDate);
        }
        
    }
    
}
