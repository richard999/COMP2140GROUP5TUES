/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewSummaryAll extends JInternalFrame {

    JDatePickerImpl datePickerFrom, datePickerTo;
    private final JButton btnGenerate;
    private final JTable tableSummary;
    DefaultTableModel tableSummaryModel;

    public FormViewSummaryAll() {
        JLabel lblFrom = new JLabel("From");
        JLabel lblTo = new JLabel("To");

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePickerFrom = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        UtilDateModel model1 = new UtilDateModel();
        Properties p1 = new Properties();
        p1.put("text.today", "Today");
        p1.put("text.month", "Month");
        p1.put("text.year", "Year");
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model1, p1);
        datePickerTo = new JDatePickerImpl(datePanel1, new DateLabelFormatter());

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.add(lblFrom);
        topLeftPanel.add(datePickerFrom);

        JPanel topRightPanel = new JPanel();
        topRightPanel.add(lblTo);
        topRightPanel.add(datePickerTo);

        JPanel topCenterPanel = new JPanel();
        topCenterPanel.add(topLeftPanel);
        topCenterPanel.add(topRightPanel);

        btnGenerate = new JButton("Generate");
        btnGenerate.addActionListener(new ButtonFunction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGenerate);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        tableSummaryModel = new DefaultTableModel();
        tableSummary = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 5) || (column == 10);
            }
        };
        _setTable();

        JScrollPane scrollPane = new JScrollPane(tableSummary);

        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        setContentPane(container);
        setTitle("Summary: All");
        setBounds(20, 100, 1200, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }

    private void _setTable() {
        String[] tableHeader = {
            "#", "Employee", "Hourly Rate", "Total Hours", "Total Pay Out", "Deductions", "Cash Advance", "Amount Paid", "Balance", "Net Pay", "Print"
        };
        tableSummaryModel.setColumnIdentifiers(tableHeader);
        tableSummary.setModel(tableSummaryModel);
        tableSummary.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableSummary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSummary.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableSummary.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableSummary.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableSummary.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableSummary.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableSummary.getColumnModel().getColumn(5).setPreferredWidth(150);
        tableSummary.getColumnModel().getColumn(6).setPreferredWidth(150);
        tableSummary.getColumnModel().getColumn(7).setPreferredWidth(120);
        tableSummary.getColumnModel().getColumn(8).setPreferredWidth(120);
        tableSummary.getColumnModel().getColumn(9).setPreferredWidth(150);

        tableSummary.getColumnModel().getColumn(5).setCellRenderer(new ButtonColumn());
        tableSummary.getColumnModel().getColumn(5).setCellEditor(new ButtonColumn());

        tableSummary.getColumnModel().getColumn(10).setCellRenderer(new ButtonColumn());
        tableSummary.getColumnModel().getColumn(10).setCellEditor(new ButtonColumn());

        tableSummary.setRowHeight(30);
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

    public class ButtonColumn implements TableCellRenderer, TableCellEditor {

        JButton btnViewDeductions, btnPrintPayslip;

        public ButtonColumn() {
            btnViewDeductions = new JButton();
            btnViewDeductions.addActionListener(new TableButtonFunction());
            btnPrintPayslip = new JButton();
            btnPrintPayslip.addActionListener(new TableButtonFunction());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component button = new JButton();
            if (column == 5) {
                btnViewDeductions.setText(value.toString());
                button = btnViewDeductions;
            } else if (column == 10) {
                btnPrintPayslip.setText("Print");
                button = btnPrintPayslip;
            }
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component button = new JButton();
            if (column == 5) {
                btnViewDeductions.setText(value.toString());
                button = btnViewDeductions;
            } else if (column == 10) {
                btnPrintPayslip.setText("Print");
                button = btnPrintPayslip;
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            return true;
        }

        @Override
        public void cancelCellEditing() {

        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {

        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {

        }

        private class TableButtonFunction implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tableSummary.getSelectedRow();
                if (e.getSource() == btnViewDeductions) {
                    _getDeductionBreakDown(row);
                    _toGenerate();
                    tableSummary.setRowSelectionInterval(row, row);
                    tableSummary.setColumnSelectionInterval(0, tableSummary.getColumnCount()-1);
                } else if (e.getSource() == btnPrintPayslip) {
                    _print();
                    _toGenerate();
                    tableSummary.setRowSelectionInterval(row, row);
                    tableSummary.setColumnSelectionInterval(0, tableSummary.getColumnCount()-1);
                }

            }

            private void _getDeductionBreakDown(int row) {
                String id = tableSummary.getValueAt(row, 0).toString();
                String name = tableSummary.getValueAt(row, 1).toString();
                String dateFrom = _formatDate((Date) datePickerFrom.getModel().getValue());
                String dateTo = _formatDate((Date) datePickerTo.getModel().getValue());
                JOptionPane.showMessageDialog(null, new SubFormViewDeductionBreakDown(id, name, dateFrom, dateTo), "BreakDown", JOptionPane.INFORMATION_MESSAGE);
            }

            private String _formatDate(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                return sdf.format(date);
            }

            private void _print() {
                int row = tableSummary.getSelectedRow();

                PrinterJob printerJob = PrinterJob.getPrinterJob();
                PageFormat pf = printerJob.defaultPage();
                /*Paper paper = new Paper();
                 paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
                 pf.setPaper(paper);*/

                String id = tableSummary.getValueAt(row, 0).toString();
                String name = tableSummary.getValueAt(row, 1).toString();
                String totalDeduction = tableSummary.getValueAt(row, 5).toString();
                String dateFrom = _formatDate((Date) datePickerFrom.getModel().getValue());
                String dateTo = _formatDate((Date) datePickerTo.getModel().getValue());
                String totalPayout = tableSummary.getValueAt(row, 4).toString();
                String totalNetPay = tableSummary.getValueAt(row, 9).toString();

                PrintPayslip printPayslip = new PrintPayslip();
                printPayslip._initializeLines(name, id, totalDeduction, dateFrom, dateTo, totalPayout, totalNetPay);
                
                Paper paper = new Paper();
                paper.setImageableArea(pf.getImageableX(), pf.getImageableY(), paper.getImageableWidth(), paper.getImageableHeight());
                pf.setPaper(paper);
                printerJob.setPrintable(printPayslip, pf);
                if (printerJob.printDialog()) {
                    try {
                        printerJob.print();
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "PRINTING ERROR", JOptionPane.ERROR_MESSAGE);
                        //Logger.getLogger(FormViewSummaryAll.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }

    }

    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnGenerate) {
                if (datePickerFrom.getModel().getValue() == null || datePickerTo.getModel().getValue() == null) {
                    JOptionPane.showMessageDialog(null, "Please select a DATE", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (tableSummary.isEditing()) {
                        tableSummary.getCellEditor().cancelCellEditing();
                    }
                    _toGenerate();
                }
            }
        }

    }

    public void _toGenerate() {
        String dateFrom = _formatDate((Date) datePickerFrom.getModel().getValue());
        String dateTo = _formatDate((Date) datePickerTo.getModel().getValue());
        _generate(dateFrom, dateTo);
    }

    private String _formatDate(Date dateToFormat) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf1.format(dateToFormat);
    }

    private void _generate(String dateFrom, String dateTo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableSummaryModel.setRowCount(0);
        tableSummaryModel.setColumnCount(0);
        _setTable();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_employees.Employee_Id, tbl_employees.Employee_Name,"
                    + " ROUND(tbl_employees.Employee_HourlyRate, 2)"
                    + " FROM tbl_employees INNER JOIN tbl_employeestatus"
                    + " ON tbl_employees.EmployeeStatus_Id=tbl_employeestatus.EmployeeStatus_Id"
                    + " WHERE tbl_employeestatus.EmployeeStatus_Status='active'");
            int i = 0;
            while (rs.next()) {
                i++;
                String id = rs.getString("tbl_employees.Employee_Id");
                String name = rs.getString("tbl_employees.Employee_Name");
                String rate = rs.getString("ROUND(tbl_employees.Employee_HourlyRate, 2)");
                _generateTotalHours(dateFrom, dateTo, i, id, name, rate);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void _generateTotalHours(String dateFrom, String dateTo, int i, String id, String name, String rate) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Time_In, Time_Out FROM tbl_time"
                    + " WHERE Employee_Id='" + id + "'"
                    + " AND Time_In BETWEEN '" + dateFrom + " 00:00:01' AND '" + dateTo + " 23:59:59'"
                    + " AND Time_Out BETWEEN '" + dateFrom + " 00:00:01' AND '" + dateTo + " 23:59:59'");
            float hours = 0;
            while (rs.next()) {
                //JOptionPane.showMessageDialog(null, _calculateTime(rs.getString("Time_In"), rs.getString("Time_Out")));
                hours += _calculateTime(rs.getString("Time_In"), rs.getString("Time_Out"));
            }
            _generateDeduction(dateFrom, dateTo, i, id, name, rate, hours);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void _generateDeduction(String dateFrom, String dateTo, int i, String id,
            String name, String rate, float hours) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM(ROUND(Deduction_Amount, 2)) FROM tbl_deductions"
                    + " WHERE Deduction_Date BETWEEN '" + dateFrom + " 00:00:01' AND '" + dateTo + " 23:59:59'"
                    + " AND Employee_Id='" + id + "'");

            while (rs.next()) {
                String deduction = rs.getString("SUM(ROUND(Deduction_Amount, 2))");
                _generateCashAdvance(dateFrom, dateTo, i, id, name, rate, hours, deduction);
                //_generateTotalPayOutAndNetPay(i, id, name, rate, hours, deduction);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(FormViewSummaryAll.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(FormViewSummaryAll.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void _generateCashAdvance(String dateFrom, String dateTo, int i, String id,
            String name, String rate, float hours, String deduction) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT ROUND(SUM(CashAdvance_Amount), 2)"
                    + " FROM tbl_cashadvance WHERE Employee_Id='" + id + "'");
            NumberFunction nFunction = new NumberFunction();
            while (rs.next()) {
                String cashAdvance = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(SUM(CashAdvance_Amount), 2)"));
                _generatePayment(i, id, name, rate, hours, deduction, cashAdvance);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(FormViewSummaryAll.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void _generatePayment(int i, String id,
            String name, String rate, float hours, String deduction, String cashAdvance) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT ROUND(SUM(Payment_Amount), 2)"
                    + " FROM tbl_cashadvancepayment WHERE Employee_Id='" + id + "'");
            NumberFunction nFunction = new NumberFunction();
            while (rs.next()) {
                String payment = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(SUM(Payment_Amount), 2)"));
                _generateTotalPayOutAndNetPay(i, name, rate, hours, deduction, cashAdvance, payment);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(FormViewSummaryAll.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void _generateTotalPayOutAndNetPay(int i, String name, String rate,
            float hours, String deduction, String cashAdvance, String payment) {

        if (deduction == null) {
            deduction = "0.00";
        }
        NumberFunction nFunction = new NumberFunction();
        float payOut = Float.parseFloat(rate) * hours;
        payOut = (float) (Math.round(payOut * 100.0) / 100.0);
        float netPay = (payOut - Float.parseFloat(deduction)) - nFunction._stripValue(payment);
        netPay = (float) (Math.round(netPay * 100.0) / 100.0);
        float balance = nFunction._stripValue(cashAdvance) - nFunction._stripValue(payment);
        String stringBalance = "Php " + nFunction._getFormattedNumber(Float.toString(balance));
        String stringNetPay = "Php " + nFunction._getFormattedNumber(Float.toString(netPay));
        deduction = "Php " + nFunction._getFormattedNumber(deduction);
        String totalPayOut = "Php " + nFunction._getFormattedNumber(Float.toString(payOut));
        hours = (float) (Math.round(hours * 100.0) / 100.0);
        rate = "Php " + nFunction._getFormattedNumber(rate);
        tableSummaryModel.addRow(new Object[]{i, name, rate, hours, totalPayOut, deduction, cashAdvance, payment, stringBalance, stringNetPay, "Print"});

    }

    private float _calculateTime(String dateTimeIn, String dateTimeOut) {
        String[] temp = dateTimeIn.split(" ");
        String dateIn = temp[0];
        String timeIn = temp[1];

        temp = dateTimeOut.split(" ");
        String dateOut = temp[0];
        String timeOut = temp[1];

        float seconds = 0;
        if (dateIn.equals(dateOut)) {
            String[] timeFrom1 = timeIn.split(":");
            String[] timeTo1 = timeOut.split(":");
            float time = Float.parseFloat(timeTo1[0]) - Float.parseFloat(timeFrom1[0]);   //hour_out minus hour_in
            seconds += (time * 60) * 60;
            time = Float.parseFloat(timeTo1[1]) - Float.parseFloat(timeFrom1[1]);    //minute_out minus minute_in
            seconds += time * 60;
        } else {

        }

        float hours = 0;
        hours += (seconds / 3600) - 1;

        return hours;
    }

}
