/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewDisbursement extends JInternalFrame{
    JDatePickerImpl datePickerDateFrom, datePickerDateTo;
    DefaultTableModel tableItemModel;
    private final JTable tableItem;
    private final JButton btnGenerate;
    private final JComboBox cmbHourFrom, cmbMinuteFrom, cmbFrom,cmbHourTo, cmbMinuteTo, cmbTo;
    
    public FormViewDisbursement(){
        JLabel lblTitle = new JLabel("Disbursement");
        Font titleFont = lblTitle.getFont();
        lblTitle.setFont(new Font(titleFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePickerDateFrom = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        UtilDateModel model1 = new UtilDateModel();
        Properties p1 = new Properties();
        p1.put("text.today", "Today");
        p1.put("text.month", "Month");
        p1.put("text.year", "Year");
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model1, p1);
        datePickerDateTo = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        
        JPanel abovePanel = new JPanel();
        abovePanel.setBorder(BorderFactory.createTitledBorder("Date"));
        abovePanel.add(new JLabel("From"));
        abovePanel.add(datePickerDateFrom);
        abovePanel.add(new JLabel("To"));
        abovePanel.add(datePickerDateTo);
        
        JLabel lblFromTime = new JLabel("From");
        cmbHourFrom = new JComboBox();
        cmbMinuteFrom = new JComboBox();
        cmbFrom = new JComboBox();
        JLabel lblToTime = new JLabel("     To");
        cmbHourTo = new JComboBox();
        cmbMinuteTo = new JComboBox();
        cmbTo = new JComboBox();
        
        JPanel belowPanel = new JPanel();
        belowPanel.setBorder(BorderFactory.createTitledBorder("Time"));
        belowPanel.add(lblFromTime);
        belowPanel.add(cmbHourFrom);
        belowPanel.add(new JLabel(":"));
        belowPanel.add(cmbMinuteFrom);
        belowPanel.add(cmbFrom);
        belowPanel.add(lblToTime);
        belowPanel.add(cmbHourTo);
        belowPanel.add(new JLabel(":"));
        belowPanel.add(cmbMinuteTo);
        belowPanel.add(cmbTo);
        
        btnGenerate = new JButton("Generate");
        btnGenerate.addActionListener(new ButtonFunction());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGenerate);
        
        JPanel topTopPanel = new JPanel(new BorderLayout());
        topTopPanel.add(abovePanel, BorderLayout.NORTH);
        topTopPanel.add(belowPanel, BorderLayout.CENTER);
        topTopPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel topPanel = new JPanel();
        topPanel.add(topTopPanel);
        
        String[] tableHeader = {
            "A", "B", "C", "D", "E"
        };
        tableItemModel = new DefaultTableModel();
        tableItemModel.setColumnIdentifiers(tableHeader);
        tableItem = new JTable(){ 
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItem.setModel(tableItemModel);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableItem.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableItem.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableItem.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableItem.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableItem);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                _loadTime();
            }
        });
        
        setContentPane(container);
        setTitle("View Disbursement");
        setBounds(100, 30, 1000, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _loadTime(){
        cmbHourFrom.addItem("");
        cmbHourTo.addItem("");
        cmbMinuteFrom.addItem("");
        cmbMinuteTo.addItem("");
        for(int i=0; i<=12 ; i++){
            String hour = Integer.toString(i);
            if(hour.length() == 1){
                hour = "0"+hour;
            }
            cmbHourFrom.addItem(hour);
            cmbHourTo.addItem(hour);
        }
        for(int i=0; i<=60 ; i++){
            String minute = Integer.toString(i);
            if(minute.length() == 1){
                minute = "0"+minute;
            }
            cmbMinuteFrom.addItem(minute);
            cmbMinuteTo.addItem(minute);
        }
        cmbFrom.addItem("AM");
        cmbFrom.addItem("PM");
        cmbTo.addItem("AM");
        cmbTo.addItem("PM");
    }
    
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "MM/dd/yyyy";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
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
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnGenerate){
                _generate();
            }
        }
        
        private void _generate(){
            Date date1 = (Date) datePickerDateFrom.getModel().getValue();
            Date date2 = (Date) datePickerDateTo.getModel().getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(date1 == null || date2 == null){
                JOptionPane.showMessageDialog(null, "Please select a date", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                String dateFrom = sdf.format(date1);
                String dateTo = sdf.format(date2);
                if(!cmbHourFrom.getSelectedItem().toString().equals("") &&
                        !cmbMinuteFrom.getSelectedItem().toString().equals("") &&
                        !cmbHourTo.getSelectedItem().toString().equals("") &&
                        !cmbMinuteTo.getSelectedItem().toString().equals("")){
                    dateFrom += " " + cmbHourFrom.getSelectedItem().toString() + ":" + cmbMinuteFrom.getSelectedItem().toString() + ":00";
                    dateTo += " " + cmbHourTo.getSelectedItem().toString() + ":" + cmbMinuteTo.getSelectedItem().toString() + ":00";
                    if(cmbFrom.getSelectedItem().toString().equals("PM")){
                        dateFrom = _formatDate(dateFrom);
                    }
                    if(cmbTo.getSelectedItem().toString().equals("PM")){
                        dateTo = _formatDate(dateTo);
                    }
                } else {
                    dateFrom += " " + "00:00:01";
                    dateTo += " " + "23:59:59";
                }
                //String message = dateFrom + "-----" + dateTo;
                //JOptionPane.showMessageDialog(null, message);
                tableItemModel.setRowCount(0);
                tableItemModel.addRow(new Object[]{"Sales", "", "", "", ""});
                _generateGross(dateFrom, dateTo);
                tableItemModel.addRow(new Object[]{"Expenses", "", "", "", ""});
                _generateExpenses(dateFrom, dateTo);
                _getGrossAndExpensesTotal();
                _getProfit();
            }
                
        }
        
        private String _formatDate(String date){
            String[] tempDate = date.split(" ");
            String[] time = tempDate[1].split(":");
            if(Integer.parseInt(time[0])<12){
                time[0] = Integer.toString(Integer.parseInt(time[0])+12);
            }
            date  = tempDate[0] + " " + time[0] + ":" + time[1] + ":" + time[2];
            return date;
        }
        
        private void _getProfit(){
            float profit;
            float gross = 0;
            float expenses = 0;
            if(!tableItem.getValueAt(tableItem.getRowCount()-1, 2).toString().equals("")){
                gross = _stripValue(tableItem.getValueAt(tableItem.getRowCount()-1, 2).toString());
            }
            if(!tableItem.getValueAt(tableItem.getRowCount()-1, 2).toString().equals("")){
                expenses = _stripValue(tableItem.getValueAt(tableItem.getRowCount()-1, 3).toString());
            }
            profit = gross - expenses;
            double newProfit = Math.round(profit*100.0)/100.0;
            NumberFunction nFunction = new NumberFunction();
            tableItemModel.addRow(new Object[]{"Profit", "", "" ,"" ,"Php "+nFunction._getFormattedNumber(Double.toString(newProfit))});
        }
        
        private void _getGrossAndExpensesTotal(){
            float totalGross = 0;
            float totalExpenses = 0;
            for(int i=0; i<tableItem.getRowCount(); i++){
                if(!tableItem.getValueAt(i, 2).toString().equals("")){
                    totalGross += _stripValue(tableItem.getValueAt(i, 2).toString());
                }
                if(!tableItem.getValueAt(i, 3).toString().equals("")){
                    totalExpenses += _stripValue(tableItem.getValueAt(i, 3).toString());
                }
            }
            NumberFunction nFunction = new NumberFunction();
            String newTotalGross = "Php "+nFunction._getFormattedNumber(Float.toString(totalGross));
            String newTotalExpenses = "Php "+nFunction._getFormattedNumber(Float.toString(totalExpenses));
            tableItemModel.addRow(new Object[]{"Total", "", newTotalGross, newTotalExpenses, ""});
        }
        
        private void _generateExpenses(String dateFrom, String dateTo){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT VoucherItem_Id, VoucherItem_Name FROM tbl_voucheritem");
                while(rs.next()){
                    String id = rs.getString("VoucherItem_Id");
                    String name = rs.getString("VoucherItem_Name");
                    _generateItem(id, name, dateFrom , dateTo, conn);
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
        
        private void _generateItem(String id, String name, String dateFrom , String dateTo, Connection conn){
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT SUM(ROUND(tbl_voucherorder.VoucherItem_Amount, 2))"
                        + " FROM tbl_voucherorder INNER JOIN tbl_voucher"
                        + " ON tbl_voucherorder.Voucher_No=tbl_voucher.Voucher_No"
                        + " WHERE tbl_voucherorder.VoucherItem_Id='"+id+"'"
                        + " AND tbl_voucher.Voucher_Date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'");
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                if(rs.next()){
                    name = sFunction._Capitalized(name);
                    String amount = rs.getString("SUM(ROUND(tbl_voucherorder.VoucherItem_Amount, 2))");
                    if(amount != null){
                        amount = "Php "+nFunction._getFormattedNumber(amount);
                        tableItemModel.addRow(new Object[]{"", name, "", amount, ""});
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void _generateGross(String dateFrom, String dateTo){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT ROUND(SUM(Purchase_Total), 2) FROM tbl_sales"
                        + " WHERE Purchase_Date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'"
                        + " AND Purchase_CashTendered<>'0.0000'");
                NumberFunction nFunction = new NumberFunction();
                if(rs.next()){
                    String sum = rs.getString("ROUND(SUM(Purchase_Total), 2)");
                    if(sum != null){
                        sum = "Php "+nFunction._getFormattedNumber(sum);
                        tableItemModel.addRow(new Object[]{"", "Gross", sum, "", "", ""});
                    }
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
