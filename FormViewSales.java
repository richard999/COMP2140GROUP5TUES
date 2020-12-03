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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
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
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Siblings Solutions
 */
public class FormViewSales extends JInternalFrame{
    JLabel lblTitle;
    JLabel lblPurchaseNo, lblFrom, lblTo;
    JTextField txtPurchaseNo;
    JButton btnFilterPurchaseNo, btnFilterPurchaseDate, btnPrint;
    JTable tableSales;
    DefaultTableModel tableSalesModel;
    JDatePickerImpl datePickerFrom, datePickerTo;
    
    public FormViewSales(){
        lblTitle = new JLabel("View Sales");
        Font font = lblTitle.getFont();
        lblTitle.setFont(new Font(font.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        lblPurchaseNo = new JLabel("Purchase No.");
        txtPurchaseNo = new JTextField();
        txtPurchaseNo.setPreferredSize(new Dimension(150, 20));
        btnFilterPurchaseNo = new JButton("Filter");
        btnFilterPurchaseNo.addActionListener(new ButtonFunction());
        
        JPanel panel1 = new JPanel();
        panel1.setBorder(BorderFactory.createTitledBorder("Filter By Purchase No."));
        panel1.add(lblPurchaseNo);
        panel1.add(txtPurchaseNo);
        panel1.add(btnFilterPurchaseNo);
        
        lblFrom = new JLabel("From");
        lblTo = new JLabel("To");
        btnFilterPurchaseDate = new JButton("Filter");
        btnFilterPurchaseDate.addActionListener(new ButtonFunction());
        
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
        
        JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createTitledBorder("Filter by Purchase Date"));
        panel2.add(lblFrom);
        panel2.add(datePickerFrom);
        panel2.add(lblTo);
        panel2.add(datePickerTo);
        panel2.add(btnFilterPurchaseDate);
        
        btnPrint = new JButton("Print");
        btnPrint.addActionListener(new ButtonFunction());
        
        JPanel panel3 = new JPanel();
        panel3.setBorder(BorderFactory.createTitledBorder("Print"));
        panel3.add(btnPrint);
        
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.add(panel1, BorderLayout.WEST);
        topPanel.add(panel2, BorderLayout.CENTER);
        topPanel.add(panel3, BorderLayout.EAST);
        
        String[] tableHeader = {
            "Purchase No.", "Purchase Date", "ID", "Menu Item", "# of Order", "Sub-total"
        };
        tableSalesModel = new DefaultTableModel();
        tableSalesModel.setColumnIdentifiers(tableHeader);
        tableSales = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSales.setModel(tableSalesModel);
        tableSales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableSales.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableSales.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableSales.getColumnModel().getColumn(3).setPreferredWidth(300);
        tableSales.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableSales.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tableSales);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(new JLabel("  "), BorderLayout.WEST);
        container.add(new JLabel("  "), BorderLayout.EAST);
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                _loadTableData();
            }
        });
        
        setContentPane(container);
        setTitle("View Sales");
        setBounds(50,10, 1000, 600);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        
    }
    
    public class DateLabelFormatter extends AbstractFormatter {

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
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableSalesModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_sales.Purchase_No, tbl_sales.Purchase_Date,"
                    + " tbl_salesitems.MenuItem_Id, tbl_menuitems.MenuItem_Name, tbl_salesitems.Purchase_Order,"
                    + " ROUND(tbl_salesitems.Purchase_SubTotal, 2) FROM (tbl_sales INNER JOIN tbl_salesitems"
                    + " ON tbl_sales.Purchase_No=tbl_salesitems.Purchase_No) INNER JOIN tbl_menuitems"
                    + " ON tbl_salesitems.MenuItem_Id=tbl_menuitems.MenuItem_Id"
                    + " WHERE tbl_sales.Purchase_Date<>''");
            StringFunction sFunction = new StringFunction();
            NumberFunction nFunction = new NumberFunction();
            while(rs.next()){
                String purchaseNo = rs.getString("tbl_sales.Purchase_No");
                String[] temp =  rs.getString("tbl_sales.Purchase_Date").split(" ");
                String purchaseDate = temp[0];
                String menuId = rs.getString("tbl_salesitems.MenuItem_Id");
                String menuName = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                String purchaseOrder = rs.getString("tbl_salesitems.Purchase_Order");
                String subTotal = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_salesitems.Purchase_SubTotal, 2)"));
                tableSalesModel.addRow(new Object[]{purchaseNo, purchaseDate, menuId, menuName, purchaseOrder, subTotal});
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
        _getSalesTotal();
    }
    
    private void _getSalesTotal() {
        NumberFunction nFunction = new NumberFunction();
        float total = 0;
        for (int i=0; i<tableSales.getRowCount(); i++){
            total += nFunction._stripValue(tableSales.getValueAt(i, 5).toString());
        }
        String strTotal = "Php " + nFunction._getFormattedNumber(Float.toString(total));
        tableSalesModel.addRow(new Object[]{"Total", "", "", "", "", strTotal});
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnFilterPurchaseNo){
                if (txtPurchaseNo.getText().trim().equals("")){
                    _loadTableData();
                } else {
                    _filterByPurchaseNo(txtPurchaseNo.getText());
                    _getSalesTotal();
                }
            } else if (e.getSource() == btnFilterPurchaseDate){
                
                if (datePickerFrom.getModel().getValue() == null || datePickerTo.getModel().getValue() == null){
                    _loadTableData();
                } else {
                    Date dateFrom  = (Date) datePickerFrom.getModel().getValue();
                    Date dateTo = (Date) datePickerTo.getModel().getValue();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    _filterByPurchaseDate(df.format(dateFrom), df.format(dateTo));
                    _getSalesTotal();
                }
            } else if (e.getSource() == btnPrint){
                _print();
            }
        }
        
        private void _filterByPurchaseNo(String No){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableSalesModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_sales.Purchase_No, tbl_sales.Purchase_Date,"
                        + " tbl_salesitems.MenuItem_Id, tbl_menuitems.MenuItem_Name, tbl_salesitems.Purchase_Order,"
                        + " ROUND(tbl_salesitems.Purchase_SubTotal, 2) FROM (tbl_sales INNER JOIN tbl_salesitems"
                        + " ON tbl_sales.Purchase_No=tbl_salesitems.Purchase_No) INNER JOIN tbl_menuitems"
                        + " ON tbl_salesitems.MenuItem_Id=tbl_menuitems.MenuItem_Id"
                        + " WHERE tbl_sales.Purchase_No="+No);
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                while(rs.next()){
                    String purchaseNo = rs.getString("tbl_sales.Purchase_No");
                    String[] temp =  rs.getString("tbl_sales.Purchase_Date").split(" ");
                    String purchaseDate = temp[0];
                    String menuId = rs.getString("tbl_salesitems.MenuItem_Id");
                    String menuName = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                    String purchaseOrder = rs.getString("tbl_salesitems.Purchase_Order");
                    String subTotal = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_salesitems.Purchase_SubTotal, 2)"));
                    tableSalesModel.addRow(new Object[]{purchaseNo, purchaseDate, menuId, menuName, purchaseOrder, subTotal});
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
        
        private void _filterByPurchaseDate(String from, String to){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableSalesModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_sales.Purchase_No, tbl_sales.Purchase_Date,"
                        + " tbl_salesitems.MenuItem_Id, tbl_menuitems.MenuItem_Name, tbl_salesitems.Purchase_Order,"
                        + " ROUND(tbl_salesitems.Purchase_SubTotal, 2) FROM (tbl_sales INNER JOIN tbl_salesitems"
                        + " ON tbl_sales.Purchase_No=tbl_salesitems.Purchase_No) INNER JOIN tbl_menuitems"
                        + " ON tbl_salesitems.MenuItem_Id=tbl_menuitems.MenuItem_Id"
                        + " WHERE tbl_sales.Purchase_Date BETWEEN '"+from+" 00:00:01' AND '"+to+" 23:59:59'");
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                while(rs.next()){
                    String purchaseNo = rs.getString("tbl_sales.Purchase_No");
                    String[] temp =  rs.getString("tbl_sales.Purchase_Date").split(" ");
                    String purchaseDate = temp[0];
                    String menuId = rs.getString("tbl_salesitems.MenuItem_Id");
                    String menuName = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                    String purchaseOrder = rs.getString("tbl_salesitems.Purchase_Order");
                    String subTotal = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_salesitems.Purchase_SubTotal, 2)"));
                    tableSalesModel.addRow(new Object[]{purchaseNo, purchaseDate, menuId, menuName, purchaseOrder, subTotal});
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
        
        private void _print(){
            FormPrintSales formPrintSales = new FormPrintSales();
            String[] purchaseNo = new String[tableSalesModel.getRowCount()];
            String[] purchaseDate = new String[tableSalesModel.getRowCount()];
            String[] id = new String[tableSalesModel.getRowCount()];
            String[] menuItem = new String[tableSalesModel.getRowCount()];
            String[] order = new String[tableSalesModel.getRowCount()];
            String[] subTotal = new String[tableSalesModel.getRowCount()];
            for(int i=0; i<tableSalesModel.getRowCount(); i++){
                purchaseNo[i] = tableSales.getValueAt(i, 0).toString();
                purchaseDate[i] = tableSales.getValueAt(i, 1).toString();
                id[i] = tableSales.getValueAt(i, 2).toString();
                menuItem[i] = tableSales.getValueAt(i, 3).toString();
                order[i] = tableSales.getValueAt(i, 4).toString();
                subTotal[i] = tableSales.getValueAt(i, 5).toString();
            }
            formPrintSales._initTextLines(purchaseNo, purchaseDate, id, menuItem, order, subTotal);
            formPrintSales._goPrint();
        }

        
        
    }
    
}
