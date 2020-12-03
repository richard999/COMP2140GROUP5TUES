/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
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
 * @author IT-Earl
 */
public class PrintPayslip implements Printable {

    String name, id, totalDeduction, dateFrom, dateTo, totalPayout, totalNetPay;
    String cashAdvancePayment;

    ArrayList deduction_description, deduction_amount;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Font headerFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
        Font informationBOLDFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
        Font informationFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        g2d.setFont(headerFont);

        g2d.drawString("Eats Possible", 170, 10);

        int y = 30;
        g2d.setFont(informationBOLDFont);
        g2d.drawString("Name", 10, y);
        g2d.setFont(informationFont);
        g2d.drawString(this.name, 60, y);

        g2d.setFont(informationBOLDFont);
        g2d.drawString("Payroll", 300, y);
        g2d.setFont(informationFont);
        g2d.drawString(this.dateFrom, 370, y);
        g2d.drawString("to", 370, y + 15);
        g2d.drawString(this.dateTo, 370, y + 30);

        g2d.setFont(informationBOLDFont);
        g2d.drawString("Employee ID", 10, y + 15);
        g2d.setFont(informationFont);
        g2d.drawString(this.id, 120, y + 15);

        y = y + 55;
        g2d.setFont(informationBOLDFont);
        g2d.drawString("I.   BASIC PAY", 10, y + 15);
        g2d.drawString(this.totalPayout, 300, y + 15);

        g2d.drawString("II.  PAYMENT (CASHADVANCE)", 10, y + 45);
        g2d.drawString(this.cashAdvancePayment, 300, y + 45);

        g2d.drawString("III. OTHER DEDUCTIONS", 10, y + 75);
        g2d.drawString(this.totalDeduction, 300, y + 75);
        y = 175;
        if (this.deduction_description != null) {
            g2d.setFont(informationFont);
            for (int i = 0; i < deduction_description.size(); i++) {
                g2d.drawString(deduction_description.get(i).toString(), 80, y);
                g2d.drawString(deduction_amount.get(i).toString(), 300, y);
                y += 15;
            }
        }
        
        g2d.setFont(informationBOLDFont);
        g2d.drawString("VI.  TOTAL NET PAY", 10, y + 15);
        g2d.drawString(this.totalNetPay, 300, y + 15);

        return PAGE_EXISTS;
    }

    protected void _initializeLines(String name, String id, String totalDeduction, String dateFrom,
            String dateTo, String totalPayout, String totalNetPay) {
        this.name = name;
        this.id = id;
        this.totalDeduction = totalDeduction;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.totalPayout = totalPayout;
        this.totalNetPay = totalNetPay;
        this.cashAdvancePayment = _getCashAdvancePayment(id, dateFrom, dateTo);
        _setDeductions(id, dateFrom, dateTo);
    }

    private String _getCashAdvancePayment(String id, String dateFrom, String dateTo) {
        String valueToReturn = null;
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM(ROUND(CashAdvance_Amount, 2))"
                    + " FROM tbl_cashadvance WHERE Employee_Id='"+id+"'"
                    + " AND CashAdvance_Date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'");
            NumberFunction nFunction = new NumberFunction();
            if (rs.next()) {
                valueToReturn = "Php " + nFunction._getFormattedNumber(rs.getString("SUM(ROUND(CashAdvance_Amount, 2))"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(PrintPayslip.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(PrintPayslip.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return valueToReturn;
    }

    private void _setDeductions(String id, String dateFrom, String dateTo) {
        deduction_description = new ArrayList();
        deduction_amount = new ArrayList();
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Deduction_Description, ROUND(Deduction_Amount, 2)"
                    + " FROM tbl_deductions WHERE Employee_Id='"+id+"' AND"
                    + " Deduction_Date BETWEEN '"+dateFrom+" 00:00:01' AND '"+dateTo+" 23:59:59'");
            NumberFunction nFunction = new NumberFunction();
            while (rs.next()) {
                deduction_description.add(rs.getString("Deduction_Description"));
                deduction_amount.add("Php " + nFunction._getFormattedNumber(rs.getString("ROUND(Deduction_Amount, 2)")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(PrintPayslip.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(PrintPayslip.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

}
