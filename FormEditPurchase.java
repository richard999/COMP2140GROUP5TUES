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
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
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
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author IT-NEW
 */
public class FormEditPurchase extends JInternalFrame{
    private final JTextField txtPurchaseNo, txtDate;
    private final JButton btnSearch, btnAdd, btnRemove, btnSave, btnPrint, btnCancel, btnFilter;
    private final JTable tableItem;
    DefaultTableModel tableItemModel;
    JDatePickerImpl datePickerFrom, datePickerTo;
    
    public FormEditPurchase(){
        JLabel lblTitle = new JLabel("Edit Purchase");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblBlank = new JLabel("");
        JLabel lblPurchaseNo = new JLabel("Purchase No");
        JLabel lblDate = new JLabel("Date");
        txtPurchaseNo = new JTextField();
        txtPurchaseNo.setPreferredSize(new Dimension(150, 26));
        txtDate = new JTextField();
        txtDate.setPreferredSize(new Dimension(150, 26));
        btnSearch = new JButton(">");
        btnSearch.addActionListener(new ButtonFunction());
        
        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setBorder(BorderFactory.createTitledBorder("Filter by Purchase No."));
        GroupLayout layout = new GroupLayout(topLeftPanel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        topLeftPanel.setLayout(layout);
        
        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group hg3 = layout.createParallelGroup();
        
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();
        
        hg1.addComponent(lblPurchaseNo);
        hg1.addComponent(lblDate);
        
        hg2.addComponent(txtPurchaseNo);
        hg2.addComponent(txtDate);
        
        hg3.addComponent(btnSearch);
        hg3.addComponent(lblBlank);
        
        vg1.addComponent(lblPurchaseNo);
        vg1.addComponent(txtPurchaseNo);
        vg1.addComponent(btnSearch);
        
        vg2.addComponent(lblDate);
        vg2.addComponent(txtDate);
        vg2.addComponent(lblBlank);
        
        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        hseq1.addGroup(hg3);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        
        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);
        
        
        //-----------------------------------------------------------
        //-----------------------------------------------------------
        JLabel lblFrom = new JLabel("From");
        JLabel lblTo = new JLabel("To");
        btnFilter = new JButton("Filter");
        btnFilter.addActionListener(new ButtonFunction());
        
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
        
        JPanel topCenterPanel = new JPanel();
        topCenterPanel.setBorder(BorderFactory.createTitledBorder("Filter By Date"));
        topCenterPanel.add(lblFrom);
        topCenterPanel.add(datePickerFrom);
        topCenterPanel.add(lblTo);
        topCenterPanel.add(datePickerTo);
        topCenterPanel.add(btnFilter);
        
        JPanel topRightPanel = new JPanel();
        topRightPanel.setPreferredSize(new Dimension(110, topLeftPanel.getPreferredSize().height));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        
        String[] tableHeader = {
            "#", "Item", "Minimum Order", "Order", "Amount"
        };
        tableItemModel = new DefaultTableModel();
        tableItemModel.setColumnIdentifiers(tableHeader);
        tableItem = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                switch (column){
                    case 3:
                        return true;
                    case 4:
                        return true;
                }
                return false;
            }
        };
        tableItem.setModel(tableItemModel);
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Font tableFont = tableItem.getFont();
        tableItem.getTableHeader().setFont(new Font(tableFont.getName(), Font.BOLD, 15));
        tableItem.setFont(new Font(tableFont.getName(), Font.PLAIN, 15));
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItem.setRowHeight(25);
        tableItem.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableItem.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableItem.getColumnModel().getColumn(2).setPreferredWidth(130);
        tableItem.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableItem.getColumnModel().getColumn(4).setPreferredWidth(140);
        
        JScrollPane scrollPane = new JScrollPane(tableItem);
        
        btnAdd = new JButton("Add");
        btnAdd.setPreferredSize(new Dimension(90, 26));
        btnAdd.addActionListener(new ButtonFunction());
        btnRemove = new JButton("Remove");
        btnRemove.setPreferredSize(new Dimension(90, 26));
        btnRemove.addActionListener(new ButtonFunction());
        
        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.setPreferredSize(new Dimension(110, 300));
        rightButtonPanel.add(btnAdd);
        rightButtonPanel.add(btnRemove);
        
        btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(90, 26));
        btnSave.addActionListener(new ButtonFunction());
        btnPrint = new JButton("Print");
        btnPrint.setPreferredSize(new Dimension(90, 26));
        btnPrint.addActionListener(new ButtonFunction());
        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(90, 26));
        btnCancel.addActionListener(new ButtonFunction());
        
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.add(btnSave);
        bottomButtonPanel.add(btnPrint);
        bottomButtonPanel.add(btnCancel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(rightButtonPanel, BorderLayout.EAST);
        centerPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                _noTransaction();
            }
        });
        
        txtPurchaseNo.setEditable(false);
        _disabledComponent();
        
        setContentPane(container);
        setTitle("Edit Purchase");
        setBounds(30, 30, 950, 600);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
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
    
    private void _disabledComponent(){
        
        txtDate.setEnabled(false);
        btnAdd.setEnabled(false);
        btnRemove.setEnabled(false);
        btnSave.setEnabled(false);
        btnPrint.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
    private void _enabledComponent(){
        txtDate.setEnabled(true);
        btnAdd.setEnabled(true);
        btnRemove.setEnabled(true);
        btnSave.setEnabled(true);
        btnPrint.setEnabled(true);
        btnCancel.setEnabled(true);
    }
    
    private void _noTransaction(){
        _disabledComponent();
        btnSearch.setEnabled(true);
        tableItemModel.setRowCount(0);
        txtPurchaseNo.setText("");
        txtDate.setText("");
    }
    
    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnSearch){
                _search();
            } else if (e.getSource() == btnAdd){
                _add();
            } else if (e.getSource() == btnRemove){
                _remove();
            } else if (e.getSource() == btnSave){
                _save();
            } else if (e.getSource() == btnPrint){
                _goPrint();
            } else if (e.getSource() == btnCancel){
                _cancel();
            } else if (e.getSource() == btnFilter){
                _filter();
            }
        }
        
        private void _goPrint(){
            if (tableItem.getRowCount() == 0){
                JOptionPane.showMessageDialog(null, "Item(s) not found", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                MessageFormat Header = new MessageFormat("Eats Possible Purchases");
                String foot = "Purchase: " + txtPurchaseNo.getText() + ""
                        + "              Date:" + txtDate.getText();
                MessageFormat Footer = new MessageFormat(foot);
                try {
                    tableItem.print(JTable.PrintMode.FIT_WIDTH, Header, Footer);
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private void _save(){
            _deleteItems();
            _goSave();
            //_searchData(txtPurchaseNo.getText());
        }
        
        private void _deleteItems(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                for(int i=0; i<tableItem.getRowCount(); i++){
                    String id = _getId(tableItem.getValueAt(i, 1).toString());
                    String purchaseOrderNo = tableItem.getValueAt(i, 0).toString();
                    st.executeUpdate("DELETE FROM tbl_purchaseitems"
                            + " WHERE PurchaseOrder_No='"+purchaseOrderNo+"'"
                            + " AND PurchaseItem_Id='"+id+"'");
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
        
        private void _goSave(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            try {
                NumberFunction nFunction = new NumberFunction();
                for(int i=0; i<tableItemModel.getRowCount(); i++){
                    String No = tableItem.getValueAt(i, 0).toString();
                    String id = _getId(tableItem.getValueAt(i, 1).toString());
                    String order = tableItem.getValueAt(i, 3).toString();
                    String amount = tableItem.getValueAt(i, 4).toString();
                    if (amount.equals("")){
                        amount = "0";
                    }
                    Statement st = conn.createStatement();
                    st.executeUpdate("INSERT INTO tbl_purchaseitems (PurchaseOrder_No, PurchaseItem_Id,"
                            + " PurchaseItem_Order, PurchaseItem_ItemAmount)"
                            + " VALUES ('"+No+"', '"+id+"', '"+order+"', '"+nFunction._stripValue(amount)+"')");
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
        
        private String _getId(String itemName){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String id = null;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT PurchaseItem_Id FROM tbl_purchaselist"
                        + " WHERE PurchaseItem_Name='"+itemName.toLowerCase()+"'");
                if(rs.next()){
                    id=rs.getString("PurchaseItem_Id");
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
        
        private void _remove(){
            try {
                tableItemModel.removeRow(tableItem.getSelectedRow());
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item.", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        private void _add(){
            SubFormItemToPurchase subFormItemToPurchase = new SubFormItemToPurchase();
            subFormItemToPurchase._loadTableData();
            if(JOptionPane.showConfirmDialog(null, subFormItemToPurchase, "List", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                String[] value = subFormItemToPurchase._getValues();
                if(value[0] == null){
                    JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.ERROR_MESSAGE);
                } else {
                    if(_ifExist(value[0])){
                        JOptionPane.showMessageDialog(null, "Item already added", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        tableItemModel.addRow(new Object[]{txtPurchaseNo.getText(), value[0], value[1], "", ""});
                        //_incrementColumn();
                    }
                }
            }
        }
        
        private boolean _ifExist(String itemName){
            for(int i=0; i<tableItemModel.getRowCount(); i++){
                if(itemName.equals(tableItem.getValueAt(i, 1).toString())){
                    return true;
                }
            }
            return false;
        }
        
        private void _cancel(){
            _noTransaction();
        }
        
        private void _search(){
            SubFormPurchase subFormPurchase = new SubFormPurchase();
            subFormPurchase._loadTableData();
            if (JOptionPane.showConfirmDialog(null, subFormPurchase, "Purchases", JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                if(subFormPurchase._checkValues()){
                    String[] value = subFormPurchase._getValues();
                    txtPurchaseNo.setText(value[0]);
                    txtDate.setText(value[1]);
                    _searchData(txtPurchaseNo.getText());
                    _enabledComponent();
                    btnSearch.setEnabled(false);
                }
            }
        }
        
        private void _searchData(String purchaseNo){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableItemModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_purchaseitems.PurchaseOrder_No,"
                        + " tbl_purchaselist.PurchaseItem_Name, tbl_purchaselist.PurchaseItem_MinOrder,"
                        + " tbl_purchaseitems.PurchaseItem_Order, ROUND(tbl_purchaseitems.PurchaseItem_ItemAmount, 2)"
                        + " FROM tbl_purchaselist INNER JOIN tbl_purchaseitems"
                        + " ON tbl_purchaselist.PurchaseItem_Id=tbl_purchaseitems.PurchaseItem_Id"
                        + " WHERE tbl_purchaseitems.PurchaseOrder_No="+purchaseNo);
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                while (rs.next()){
                    String purchaseOrderNo = rs.getString("tbl_purchaseitems.PurchaseOrder_No");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                    String minOrder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                    String order = rs.getString("tbl_purchaseitems.PurchaseItem_Order");
                    String amount = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_purchaseitems.PurchaseItem_ItemAmount, 2)"));
                    //if (amount.equals("0.00")){
                    //    amount = "";
                    //}
                    if(order == null){
                        order = "";
                    }
                    tableItemModel.addRow(new Object[]{purchaseOrderNo, itemName, minOrder, order, amount});
                }
                //_incrementColumn();
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
        
        /*private void _incrementColumn(){
            for(int i=0; i<tableItem.getRowCount(); i++){
                tableItem.setValueAt(i+1, i, 0);
            }
        }*/
        
        private void _filter() {
            if(_filterByDate()) {
                txtDate.setEnabled(true);
                btnSave.setEnabled(true);
                btnPrint.setEnabled(true);
                btnCancel.setEnabled(true);
            }
        }

        private boolean _filterByDate() {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableItemModel.setRowCount(0);
            
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date From = (Date) datePickerFrom.getModel().getValue();
                Date To = (Date) datePickerTo.getModel().getValue();
                String dateFrom = sdf.format(From);
                String dateTo = sdf.format(To);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tbl_purchaseitems.PurchaseOrder_No,"
                        + " tbl_purchaselist.PurchaseItem_Name, tbl_purchaselist.PurchaseItem_MinOrder,"
                        + " tbl_purchaseitems.PurchaseItem_Order, ROUND(tbl_purchaseitems.PurchaseItem_ItemAmount, 2)"
                        + " FROM (tbl_purchaselist INNER JOIN tbl_purchaseitems"
                        + " ON tbl_purchaselist.PurchaseItem_Id=tbl_purchaseitems.PurchaseItem_Id)"
                        + " INNER JOIN tbl_purchaseorder"
                        + " ON tbl_purchaseitems.PurchaseOrder_No=tbl_purchaseorder.PurchaseOrder_No"
                        + " WHERE tbl_purchaseorder.PurchaseOrder_Date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'");
                StringFunction sFunction = new StringFunction();
                NumberFunction nFunction = new NumberFunction();
                while (rs.next()){
                    String purchaseOrderNo = rs.getString("tbl_purchaseitems.PurchaseOrder_No");
                    String itemName = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                    String minOrder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                    String order = rs.getString("tbl_purchaseitems.PurchaseItem_Order");
                    String amount = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_purchaseitems.PurchaseItem_ItemAmount, 2)"));
                    //if (amount.equals("0.00")){
                    //    amount = "";
                    //}
                    if(order == null){
                        order = "";
                    }
                    tableItemModel.addRow(new Object[]{purchaseOrderNo, itemName, minOrder, order, amount});
                }
                //_incrementColumn();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return true;
        }

    }
    
}
