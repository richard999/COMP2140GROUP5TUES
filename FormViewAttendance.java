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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewAttendance extends JInternalFrame{
    private final JTextField txtId, txtName;
    JButton btnEdit, btnAdd, btnDelete;
    private final JButton btnEmployee, btnGenerate;
    private final JTable tableAttendance;
    DefaultTableModel tableAttendanceModel;
    JDatePickerImpl datePickerDateFrom, datePickerDateTo;
    
    public FormViewAttendance() {
        JLabel lblId = new JLabel("Employee ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblBlank = new JLabel("");
        btnEmployee = new JButton(">");
        btnEmployee.addActionListener(new ButtonFunction());
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(150, btnEmployee.getPreferredSize().height));
        txtName = new JTextField();
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(150, btnEmployee.getPreferredSize().height));
        
        JPanel topLeftPanel = new JPanel();
        GroupLayout topLeftlayout = new GroupLayout(topLeftPanel);
        topLeftlayout.setAutoCreateContainerGaps(true);
        topLeftlayout.setAutoCreateGaps(true);
        topLeftPanel.setLayout(topLeftlayout);
        topLeftPanel.setBorder(BorderFactory.createTitledBorder("Employee"));
        
        GroupLayout.Group hg1 = topLeftlayout.createParallelGroup();
        GroupLayout.Group hg2 = topLeftlayout.createParallelGroup();
        GroupLayout.Group hg3 = topLeftlayout.createParallelGroup();
        GroupLayout.Group vg1 = topLeftlayout.createParallelGroup();
        GroupLayout.Group vg2 = topLeftlayout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        
        hg3.addComponent(btnEmployee);
        hg3.addComponent(lblBlank);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg1.addComponent(btnEmployee);
        
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg2.addComponent(lblBlank);
        
        GroupLayout.SequentialGroup hseq1 = topLeftlayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = topLeftlayout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        hseq1.addGroup(hg3);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        
        topLeftlayout.setHorizontalGroup(hseq1);
        topLeftlayout.setVerticalGroup(vseq1);
        
        JLabel lblFrom = new JLabel("From");
        JLabel lblTo = new JLabel("To");
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
        
        JPanel topRightPanel = new JPanel();
        GroupLayout topRightLayout = new GroupLayout(topRightPanel);
        topRightLayout.setAutoCreateContainerGaps(true);
        topRightLayout.setAutoCreateGaps(true);
        topRightPanel.setLayout(topRightLayout);
        topRightPanel.setBorder(BorderFactory.createTitledBorder("Date"));
        
        GroupLayout.Group hg11 = topRightLayout.createParallelGroup();
        GroupLayout.Group hg22 = topRightLayout.createParallelGroup();
        GroupLayout.Group vg11 = topRightLayout.createParallelGroup();
        GroupLayout.Group vg22 = topRightLayout.createParallelGroup();
        
        hg11.addComponent(lblFrom);
        hg11.addComponent(lblTo);
        
        hg22.addComponent(datePickerDateFrom);
        hg22.addComponent(datePickerDateTo);
        
        vg11.addComponent(lblFrom);
        vg11.addComponent(datePickerDateFrom);
        
        vg22.addComponent(lblTo);
        vg22.addComponent(datePickerDateTo);
        
        GroupLayout.SequentialGroup hseq11 = topRightLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq11 = topRightLayout.createSequentialGroup();
        
        hseq11.addGroup(hg11);
        hseq11.addGroup(hg22);
        vseq11.addGroup(vg11);
        vseq11.addGroup(vg22);
        
        topRightLayout.setHorizontalGroup(hseq11);
        topRightLayout.setVerticalGroup(vseq11);
        
        btnGenerate = new JButton("Generate");
        btnGenerate.addActionListener(new ButtonFunction());
        
        JPanel topBottomPanel = new JPanel();
        topBottomPanel.add(btnGenerate);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        topPanel.add(topBottomPanel, BorderLayout.SOUTH);
        
        String[] tableHeader = {
            "Time In", "Time Out"
        };
        tableAttendanceModel = new DefaultTableModel();
        tableAttendanceModel.setColumnIdentifiers(tableHeader);
        tableAttendance = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableAttendance.setModel(tableAttendanceModel);
        tableAttendance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableAttendance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableAttendance.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableAttendance.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableAttendance.setRowHeight(25);
        Font cellFont = tableAttendance.getFont();
        tableAttendance.setFont(new Font(cellFont.getName(), Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(tableAttendance);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        btnAdd = new JButton("Add");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(new ButtonFunction());
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 36));
        btnEdit.addActionListener(new ButtonFunction());
        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(new ButtonFunction());
        
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(110, scrollPane.getPreferredSize().height));
        rightPanel.add(btnAdd);
        rightPanel.add(btnEdit);
        rightPanel.add(btnDelete);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
        
        setContentPane(container);
        setTitle("View Attendance");
        setBounds(300, 50, 605, 600);
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
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnEmployee){
                _employee();
            } else if (e.getSource() == btnGenerate){
                _generate();
            } else if(e.getSource() == btnEdit){
                _edit();
            } else if(e.getSource() == btnAdd){
                _add();
            } else if(e.getSource() == btnDelete){
                _delete();
            }
        }
        
        private void _employee(){
            SubFormEmployees subFormEmployees = new SubFormEmployees();
            subFormEmployees._loadTableData();
            if(JOptionPane.showConfirmDialog(null, subFormEmployees, "Employees", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                String[] value = subFormEmployees._getValues();
                if(!value[0].equals("")){
                    txtId.setText(value[0]);
                    txtName.setText(value[1]);
                    tableAttendanceModel.setRowCount(0);
                }
            }
        }
        
        private void _generate(){
            if(txtId.getText().equals("") || txtName.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Employee's Name and ID is Required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else if(datePickerDateFrom.getModel().getValue() == null 
                    || datePickerDateTo.getModel().getValue() == null){
                JOptionPane.showMessageDialog(null, "Date is Required", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                String dateFrom = _formatDate((Date) datePickerDateFrom.getModel().getValue());
                String dateTo = _formatDate((Date) datePickerDateTo.getModel().getValue());
                _goGenerate(dateFrom, dateTo);
            }
        }
        
        private String _formatDate(Date date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
        
        private void _goGenerate(String dateFrom, String dateTo){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tableAttendanceModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Time_In, Time_Out FROM tbl_time"
                        + " WHERE Employee_Id='"+txtId.getText()+"'"
                        + " AND Time_In BETWEEN '"+dateFrom+" 00:00:01' AND '"+dateTo+" 23:59:59'"
                        + " ORDER BY Time_In DESC");
                while(rs.next()){
                    String timeIn = _formatDateAndTime(rs.getString("Time_In"));
                    String timeOut = _formatDateAndTime(rs.getString("Time_Out"));
                    tableAttendanceModel.addRow(new Object[]{timeIn, timeOut});
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
        
        private String _formatDateAndTime(String dateAndTime){
            String formattedDateAndTime;
            if(dateAndTime == null){
                formattedDateAndTime = "";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date newDateAndTime = null;
                try {
                    newDateAndTime = sdf.parse(dateAndTime);
                } catch (ParseException ex) {
                    Logger.getLogger(FormViewAttendance.class.getName()).log(Level.SEVERE, null, ex);
                }
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                formattedDateAndTime = sdf1.format(newDateAndTime);
            }
            
            return formattedDateAndTime;
        }
        
        private void _edit(){
            try {
                SubFormEditAttendance subFormEditAttendance = new SubFormEditAttendance(
                        txtId.getText(),
                        txtName.getText(),
                        tableAttendance.getValueAt(tableAttendance.getSelectedRow(), 0).toString(),
                        tableAttendance.getValueAt(tableAttendance.getSelectedRow(), 1).toString()
                );
                if(JOptionPane.showConfirmDialog(null, subFormEditAttendance, "Edit", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    subFormEditAttendance._edit();
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select from the table", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void _add() {
            if(txtId.getText().equals("") || txtName.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please select an Employee", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                SubFormAddAttendance subFormAddAttendance = new SubFormAddAttendance(
                        txtId.getText(),
                        txtName.getText()
                );
                if(JOptionPane.showConfirmDialog(null, subFormAddAttendance, "Add",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    subFormAddAttendance._add();
                }
            }
            
        }

        private void _delete() {
            try{
                String timeIn = formatDateTime(tableAttendance.getValueAt(tableAttendance.getSelectedRow(), 0).toString());
                String timeOut = formatDateTime(tableAttendance.getValueAt(tableAttendance.getSelectedRow(), 1).toString());
                if(JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    _goDelete(txtId.getText(), timeIn, timeOut);
                }
            } catch(Exception ex){
                //Logger.getLogger(FormViewAttendance.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        private void _goDelete(String id, String timeIn, String timeOut) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_time"
                        + " WHERE Employee_Id='"+id+"'"
                        + " AND Time_In='"+timeIn+"'");
                JOptionPane.showMessageDialog(null, "Data Successfully Deleted", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                //Logger.getLogger(FormViewAttendance.class.getName()).log(Level.SEVERE, null, ex);
                String message = "FAILED! \n\n " + ex.getMessage();
                JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private String formatDateTime(String dateToFormat){
            String newStringDateTime = null;
            if(!dateToFormat.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                Date newDateTime = null;
                try {
                    newDateTime = sdf.parse(dateToFormat);
                } catch (ParseException ex) {
                    //Logger.getLogger(FormViewAttendance.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                newStringDateTime = sdf1.format(newDateTime);
            }
            return newStringDateTime;
        }
        
    }
    
}
