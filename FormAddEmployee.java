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
import java.text.DateFormat;
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
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author IT-NEW
 */
public class FormAddEmployee extends JInternalFrame{
    private final JTextField txtName, txtContact, txtPosition, txtSSS, txtHourlyRate, txtPassword, txtPassword1;
    private final JTextArea txtAddress;
    private final JDatePickerImpl datePickerDateHired;
    private final JComboBox cmbHourIn, cmbMinuteIn, cmbHourOut, cmbMinuteOut, cmbIn, cmbOut;
    private final JButton btnSave;
    
    public FormAddEmployee(){
        JLabel lblName = new JLabel("Name");
        JLabel lblAddress = new JLabel("Address");
        JLabel lblContact = new JLabel("Contact No.");
        JLabel lblDateHired = new JLabel("Date Hired");
        JLabel lblPosition = new JLabel("Position");
        JLabel lblHourlyRate = new JLabel("Hourly Rate (JMD)");
        JLabel lblSSS = new JLabel("TRN #");
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(130, 25));
        txtContact = new JTextField();
        txtContact.setPreferredSize(new Dimension(130, 25));
        txtPosition = new JTextField();
        txtPosition.setPreferredSize(new Dimension(130, 25));
        txtHourlyRate = new JTextField();
        txtHourlyRate.setPreferredSize(new Dimension(130, 25));
        txtHourlyRate.addKeyListener(new KeyAdapter() {
            TextFieldFilter textFieldFilter = new TextFieldFilter();
            @Override
            public void keyTyped(KeyEvent e) {
                textFieldFilter._numbersAndPeriod(e);
            }
        });
        txtSSS = new JTextField();
        txtSSS.setPreferredSize(new Dimension(130, 25));
        txtAddress = new JTextArea();
        txtAddress.setWrapStyleWord(true);
        txtAddress.setLineWrap(true);
        
        JScrollPane addressScrollPane = new JScrollPane(txtAddress);
        addressScrollPane.setPreferredSize(new Dimension(txtSSS.getPreferredSize().width, txtSSS.getPreferredSize().height + 40));
        
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePickerDateHired = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        JPanel detailsPanel1 = new JPanel();
        GroupLayout addLayout = new GroupLayout(detailsPanel1);
        addLayout.setAutoCreateContainerGaps(true);
        addLayout.setAutoCreateGaps(true);
        detailsPanel1.setLayout(addLayout);
        
        GroupLayout.Group hg1 = addLayout.createParallelGroup();
        GroupLayout.Group hg2 = addLayout.createParallelGroup();
        
        GroupLayout.Group vg1 = addLayout.createParallelGroup();
        GroupLayout.Group vg2 = addLayout.createParallelGroup();
        GroupLayout.Group vg3 = addLayout.createParallelGroup();
        GroupLayout.Group vg4 = addLayout.createParallelGroup();
        GroupLayout.Group vg5 = addLayout.createParallelGroup();
        GroupLayout.Group vg6 = addLayout.createParallelGroup();
        GroupLayout.Group vg7 = addLayout.createParallelGroup();
        
        hg1.addComponent(lblName);
        hg1.addComponent(lblAddress);
        hg1.addComponent(lblContact);
        hg1.addComponent(lblDateHired);
        hg1.addComponent(lblPosition);
        hg1.addComponent(lblHourlyRate);
        hg1.addComponent(lblSSS);
        
        hg2.addComponent(txtName);
        hg2.addComponent(addressScrollPane);
        hg2.addComponent(txtContact);
        hg2.addComponent(datePickerDateHired);
        hg2.addComponent(txtPosition);
        hg2.addComponent(txtHourlyRate);
        hg2.addComponent(txtSSS);
        
        vg1.addComponent(lblName);
        vg1.addComponent(txtName);
        vg2.addComponent(lblAddress);
        vg2.addComponent(addressScrollPane);
        vg3.addComponent(lblContact);
        vg3.addComponent(txtContact);
        vg4.addComponent(lblDateHired);
        vg4.addComponent(datePickerDateHired);
        vg5.addComponent(lblPosition);
        vg5.addComponent(txtPosition);
        vg6.addComponent(lblHourlyRate);
        vg6.addComponent(txtHourlyRate);
        vg7.addComponent(lblSSS);
        vg7.addComponent(txtSSS);
        
        GroupLayout.SequentialGroup hseq1 = addLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = addLayout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        vseq1.addGroup(vg4);
        vseq1.addGroup(vg5);
        vseq1.addGroup(vg6);
        vseq1.addGroup(vg7);
        
        addLayout.setHorizontalGroup(hseq1);
        addLayout.setVerticalGroup(vseq1);
        
        JLabel lblFrom = new JLabel("FROM");
        JLabel lblTo = new JLabel("TO");
        JLabel lblColon1 = new JLabel(":");
        JLabel lblColon2 = new JLabel(":");
        cmbHourIn = new JComboBox();
        cmbMinuteIn = new JComboBox();
        cmbHourOut = new JComboBox();
        cmbMinuteOut = new JComboBox();
        cmbIn = new JComboBox();
        cmbOut = new JComboBox();
        
        JPanel shiftPanel = new JPanel();
        shiftPanel.setBorder(BorderFactory.createTitledBorder("Shift"));
        GroupLayout shiftLayout = new GroupLayout(shiftPanel);
        shiftLayout.setAutoCreateContainerGaps(true);
        shiftLayout.setAutoCreateGaps(true);
        shiftPanel.setLayout(shiftLayout);
        
        GroupLayout.Group hg11 = shiftLayout.createParallelGroup();
        GroupLayout.Group hg12 = shiftLayout.createParallelGroup();
        GroupLayout.Group hg13 = shiftLayout.createParallelGroup();
        GroupLayout.Group hg14 = shiftLayout.createParallelGroup();
        GroupLayout.Group hg15 = shiftLayout.createParallelGroup();
        
        GroupLayout.Group vg11 = shiftLayout.createParallelGroup();
        GroupLayout.Group vg12 = shiftLayout.createParallelGroup();
        
        hg11.addComponent(lblFrom);
        hg11.addComponent(lblTo);
        
        hg12.addComponent(cmbHourIn);
        hg12.addComponent(cmbHourOut);
        
        hg13.addComponent(lblColon1);
        hg13.addComponent(lblColon2);
        
        hg14.addComponent(cmbMinuteIn);
        hg14.addComponent(cmbMinuteOut);
        
        hg15.addComponent(cmbIn);
        hg15.addComponent(cmbOut);
        
        vg11.addComponent(lblFrom);
        vg11.addComponent(cmbHourIn);
        vg11.addComponent(lblColon1);
        vg11.addComponent(cmbMinuteIn);
        vg11.addComponent(cmbIn);
        
        vg12.addComponent(lblTo);
        vg12.addComponent(cmbHourOut);
        vg12.addComponent(lblColon2);
        vg12.addComponent(cmbMinuteOut);
        vg12.addComponent(cmbOut);
        
        GroupLayout.SequentialGroup hseq11 = shiftLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq11 = shiftLayout.createSequentialGroup();
        
        hseq11.addGroup(hg11);
        hseq11.addGroup(hg12);
        hseq11.addGroup(hg13);
        hseq11.addGroup(hg14);
        hseq11.addGroup(hg15);
        
        vseq11.addGroup(vg11);
        vseq11.addGroup(vg12);
        
        shiftLayout.setHorizontalGroup(hseq11);
        shiftLayout.setVerticalGroup(vseq11);
        
        JLabel lblPassword = new JLabel("Password");
        JLabel lblPassword1 = new JLabel("Retype Password");
        txtPassword = new JPasswordField();
        txtPassword1 = new JPasswordField();
        
        JPanel passwordPanel = new JPanel();
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Login Pass"));
        passwordPanel.setPreferredSize(new Dimension(240, 103));
        GroupLayout passwordLayout = new GroupLayout(passwordPanel);
        passwordLayout.setAutoCreateContainerGaps(true);
        passwordLayout.setAutoCreateGaps(true);
        passwordPanel.setLayout(passwordLayout);
        
        GroupLayout.Group hg111 = passwordLayout.createParallelGroup();
        GroupLayout.Group hg112 = passwordLayout.createParallelGroup();
        GroupLayout.Group vg111 = passwordLayout.createParallelGroup();
        GroupLayout.Group vg112 = passwordLayout.createParallelGroup();
        
        hg111.addComponent(lblPassword);
        hg111.addComponent(lblPassword1);
        hg112.addComponent(txtPassword);
        hg112.addComponent(txtPassword1);
        
        vg111.addComponent(lblPassword);
        vg111.addComponent(txtPassword);
        vg112.addComponent(lblPassword1);
        vg112.addComponent(txtPassword1);
        
        GroupLayout.SequentialGroup hseq111 = passwordLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq111 = passwordLayout.createSequentialGroup();
        
        hseq111.addGroup(hg111);
        hseq111.addGroup(hg112);
        vseq111.addGroup(vg111);
        vseq111.addGroup(vg112);
        
        passwordLayout.setHorizontalGroup(hseq111);
        passwordLayout.setVerticalGroup(vseq111);
        
        JPanel detailsPanel2 = new JPanel();
        detailsPanel2.setPreferredSize(new Dimension(250, 226));
        detailsPanel2.add(shiftPanel);
        detailsPanel2.add(passwordPanel);
        
        JPanel addPanel = new JPanel(new BorderLayout());
        addPanel.add(detailsPanel1, BorderLayout.WEST);
        addPanel.add(detailsPanel2, BorderLayout.EAST);
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(new ButtonFunctions());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(addPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                _loadTime();
            }
        });
        
        setContentPane(container);
        setTitle("Add Employees");
        setBounds(60, 60, 600, 350);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _loadTime(){
        cmbHourIn.addItem("");
        cmbHourOut.addItem("");
        cmbMinuteIn.addItem("");
        cmbMinuteOut.addItem("");
        for(int i=0; i<=12 ; i++){
            String hour = Integer.toString(i);
            if(hour.length() == 1){
                hour = "0"+hour;
            }
            cmbHourIn.addItem(hour);
            cmbHourOut.addItem(hour);
        }
        for(int i=0; i<=60 ; i++){
            String minute = Integer.toString(i);
            if(minute.length() == 1){
                minute = "0"+minute;
            }
            cmbMinuteIn.addItem(minute);
            cmbMinuteOut.addItem(minute);
        }
        cmbIn.addItem("AM");
        cmbIn.addItem("PM");
        cmbOut.addItem("AM");
        cmbOut.addItem("PM");
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
    
    private class ButtonFunctions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSave){
                _save();
            }
        }
        
        private void _save(){
            if(_ifEmployeeExist(txtName.getText())){
                JOptionPane.showMessageDialog(null, "Data already added", "WARNING", JOptionPane.ERROR_MESSAGE);
            } else {
                if(txtName.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Name is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if(datePickerDateHired.getModel().getValue() == null){
                    JOptionPane.showMessageDialog(null, "Date Hired is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if(txtPosition.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Position is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if(txtHourlyRate.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Hourly Rate is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if(cmbHourIn.getSelectedItem().toString().trim().equals("") ||
                        cmbMinuteIn.getSelectedItem().toString().trim().equals("") ||
                        cmbIn.getSelectedItem().toString().trim().equals("") ||
                        cmbHourOut.getSelectedItem().toString().trim().equals("") ||
                        cmbMinuteOut.getSelectedItem().toString().trim().equals("") ||
                        cmbOut.getSelectedItem().toString().trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Shift is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if (txtPassword.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Password is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }else {
                    String Password = txtPassword.getText().trim();
                    String Password1 = txtPassword1.getText().trim();
                    if(Password.equals(Password1)){
                        _goSave(Password);
                    } else {
                        JOptionPane.showMessageDialog(null, "Password Mismatched", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
        
        private boolean _ifEmployeeExist(String employeeName){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Employee_Id FROM tbl_employees"
                        + " WHERE Employee_Name='"+employeeName+"'");
                if(rs.next()){
                    conn.close();
                    return true;
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
            
            return false;
        }
        
        private void _goSave(String Password){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                NumberFunction nFunction = new NumberFunction();
                float hourlyRate = nFunction._stripValue(txtHourlyRate.getText().trim());
                String EmployeeStatus_Id = _getEmployeeStatus_Id();
                Date date = (Date) datePickerDateHired.getModel().getValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String TimeIn = _getTime(cmbHourIn.getSelectedItem().toString(), cmbMinuteIn.getSelectedItem().toString(), cmbIn.getSelectedItem().toString());
                String TimeOut = _getTime(cmbHourOut.getSelectedItem().toString(), cmbMinuteOut.getSelectedItem().toString(), cmbOut.getSelectedItem().toString());
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_employees (Employee_Name,"
                        + " Employee_Address,"
                        + " Employee_ContactNo,"
                        + " Employee_HourlyRate,"
                        + " Employee_DateHired,"
                        + " Employee_Position,"
                        + " Employee_SSSNo,"
                        + " EmployeeStatus_Id,"
                        + " Employee_TimeIn,"
                        + " Employee_TimeOut,"
                        + " Employee_TimeInPassword)"
                        + " VALUES ('"+txtName.getText().trim()+"',"
                        + " '"+txtAddress.getText().trim()+"',"
                        + " '"+txtContact.getText().trim()+"',"
                        + " '"+hourlyRate+"',"
                        + " '"+sdf.format(date)+"',"
                        + " '"+txtPosition.getText().trim()+"',"
                        + " '"+txtSSS.getText().trim()+"',"
                        + " '"+EmployeeStatus_Id+"',"
                        + " '"+TimeIn+"',"
                        + " '"+TimeOut+"',"
                        + " '"+Password+"')");
                JOptionPane.showMessageDialog(null, "Data Successfuully Added", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
        
        private String _getEmployeeStatus_Id(){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String EmployeeStatus_Id = null;
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT EmployeeStatus_Id FROM tbl_employeestatus"
                        + " WHERE EmployeeStatus_Status='active'");
                if(rs.next()){
                    EmployeeStatus_Id = rs.getString("EmployeeStatus_Id");
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
            
            return EmployeeStatus_Id;
        }
        
        private String _getTime(String hour, String minute, String ampm){
            String time = hour + ":" + minute + " " + ampm;
            DateFormat df1 = new SimpleDateFormat("hh:mm a");
            Date date = null;
            try {
                date = df1.parse(time);
            } catch (ParseException ex) {
                Logger.getLogger(FormAddEmployee.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
            
            return df2.format(date);
        }
        
    }
    
}
