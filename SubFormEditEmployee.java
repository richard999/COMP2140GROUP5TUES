/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author IT-NEW
 */
public class SubFormEditEmployee extends JPanel{
    private final JTextField txtId, txtName, txtContact, txtPosition, txtSSS, txtHourlyRate, txtTimePassword;
    private final JTextArea txtAddress;
    private final JComboBox cmbStatus;
    private final JComboBox cmbHourIn, cmbMinuteIn, cmbHourOut, cmbMinuteOut, cmbIn, cmbOut;
    private final JFormattedTextField txtDateHired;
    
    public SubFormEditEmployee(String id, String name, String address, String contact, String datehired, 
            String position, String hourlyrate, String sss, String status,String timein, String timeout,
            String timepassword){
        
        JPanel detailsPanel1 = new JPanel();
        GroupLayout addLayout = new GroupLayout(detailsPanel1);
        addLayout.setAutoCreateContainerGaps(true);
        addLayout.setAutoCreateGaps(true);
        detailsPanel1.setLayout(addLayout);
        
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblAddress = new JLabel("Address");
        JLabel lblContact = new JLabel("Contact No.");
        JLabel lblDateHired = new JLabel("Date Hired");
        JLabel lblPosition = new JLabel("Position");
        JLabel lblHourlyRate = new JLabel("Hourly Rate (Php)");
        JLabel lblStatus = new JLabel("Status");
        JLabel lblSSS = new JLabel("SSS #");
        JLabel lblTimePassword = new JLabel("Time Password");
        txtId = new JTextField(id);
        txtId.setPreferredSize(new Dimension(130, 25));
        txtId.setEditable(false);
        txtName = new JTextField(name);
        txtName.setPreferredSize(new Dimension(130, 25));
        txtContact = new JTextField(contact);
        txtContact.setPreferredSize(new Dimension(130, 25));
        txtPosition = new JTextField(position);
        txtPosition.setPreferredSize(new Dimension(130, 25));
        txtHourlyRate = new JTextField(hourlyrate);
        txtHourlyRate.setPreferredSize(new Dimension(130, 25));
        txtSSS = new JTextField(sss);
        txtSSS.setPreferredSize(new Dimension(130, 25));
        txtAddress = new JTextArea(address);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setLineWrap(true);
        cmbStatus = new JComboBox();
        txtDateHired = new JFormattedTextField(datehired);
        txtDateHired.setPreferredSize(new Dimension(130, 25));
        txtTimePassword = new JTextField(timepassword);
        txtTimePassword.setPreferredSize(new Dimension(130, 25));
            
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.install(txtDateHired);
        } catch (ParseException ex) {
            //Logger.getLogger(SubFormEditEmployee.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        JScrollPane addressScrollPane = new JScrollPane(txtAddress);
        addressScrollPane.setPreferredSize(new Dimension(txtSSS.getPreferredSize().width, txtSSS.getPreferredSize().height + 40));
        
        GroupLayout.Group hg1 = addLayout.createParallelGroup();
        GroupLayout.Group hg2 = addLayout.createParallelGroup();
        
        GroupLayout.Group vg1 = addLayout.createParallelGroup();
        GroupLayout.Group vg2 = addLayout.createParallelGroup();
        GroupLayout.Group vg3 = addLayout.createParallelGroup();
        GroupLayout.Group vg4 = addLayout.createParallelGroup();
        GroupLayout.Group vg5 = addLayout.createParallelGroup();
        GroupLayout.Group vg6 = addLayout.createParallelGroup();
        GroupLayout.Group vg7 = addLayout.createParallelGroup();
        GroupLayout.Group vg8 = addLayout.createParallelGroup();
        GroupLayout.Group vg9 = addLayout.createParallelGroup();
        GroupLayout.Group vg10 = addLayout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        hg1.addComponent(lblAddress);
        hg1.addComponent(lblContact);
        hg1.addComponent(lblDateHired);
        hg1.addComponent(lblPosition);
        hg1.addComponent(lblHourlyRate);
        hg1.addComponent(lblStatus);
        hg1.addComponent(lblSSS);
        hg1.addComponent(lblTimePassword);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(addressScrollPane);
        hg2.addComponent(txtContact);
        hg2.addComponent(txtDateHired);
        hg2.addComponent(txtPosition);
        hg2.addComponent(txtHourlyRate);
        hg2.addComponent(cmbStatus);
        hg2.addComponent(txtSSS);
        hg2.addComponent(txtTimePassword);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg3.addComponent(lblAddress);
        vg3.addComponent(addressScrollPane);
        vg4.addComponent(lblContact);
        vg4.addComponent(txtContact);
        vg5.addComponent(lblDateHired);
        vg5.addComponent(txtDateHired);
        vg6.addComponent(lblPosition);
        vg6.addComponent(txtPosition);
        vg7.addComponent(lblHourlyRate);
        vg7.addComponent(txtHourlyRate);
        vg8.addComponent(lblStatus);
        vg8.addComponent(cmbStatus);
        vg9.addComponent(lblSSS);
        vg9.addComponent(txtSSS);
        vg10.addComponent(lblTimePassword);
        vg10.addComponent(txtTimePassword);
        
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
        vseq1.addGroup(vg8);
        vseq1.addGroup(vg9);
        vseq1.addGroup(vg10);
        
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
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(detailsPanel1, BorderLayout.CENTER);
        container.add(shiftPanel, BorderLayout.SOUTH);
        
        setLayout(new BorderLayout());
        
        add(container, BorderLayout.CENTER);
        _loadTime();
        _loadComboData();
        
        cmbStatus.setSelectedItem(status);
        String[] time = _splitTime(timein);
        cmbHourIn.setSelectedItem(time[0]);
        cmbMinuteIn.setSelectedItem(time[1]);
        cmbIn.setSelectedItem(time[2]);
        
        time = _splitTime(timeout);
        cmbHourOut.setSelectedItem(time[0]);
        cmbMinuteOut.setSelectedItem(time[1]);
        cmbOut.setSelectedItem(time[2]);
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
    
    private String[] _splitTime(String timeToSplit){
        String[] time = new String[3];
        String[] tempTime = timeToSplit.split(":");
        time[0] = tempTime[0];
        String[] tempTime1 = tempTime[1].split(" ");
        time[1] = tempTime1[0];
        time[2] = tempTime1[1];
        
        return time;
    }
    
    private void _loadComboData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT EmployeeStatus_Status FROM tbl_employeestatus");
            while(rs.next()){
                cmbStatus.addItem(rs.getString("EmployeeStatus_Status"));
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
    
    protected void _edit(){
        if(txtName.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Name is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else if(txtDateHired.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Date Hired is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else if(txtPosition.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Position is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else if(cmbHourIn.getSelectedItem().toString().trim().equals("") ||
                cmbMinuteIn.getSelectedItem().toString().trim().equals("") ||
                cmbIn.getSelectedItem().toString().trim().equals("") ||
                cmbHourOut.getSelectedItem().toString().trim().equals("") ||
                cmbMinuteOut.getSelectedItem().toString().trim().equals("") ||
                cmbOut.getSelectedItem().toString().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Shift is REQUIRED!", "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            _goEdit();
        }
    }
    
    private void _goEdit(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        
        try {
            String statusId = _getEmployeeStatus_Id(cmbStatus.getSelectedItem().toString());
            String timeIn = cmbHourIn.getSelectedItem().toString() + ":" + cmbMinuteIn.getSelectedItem().toString() + " " + cmbIn.getSelectedItem().toString();
            timeIn = _formatTime(timeIn);
            String timeOut = cmbHourOut.getSelectedItem().toString() + ":" + cmbMinuteOut.getSelectedItem().toString() + " " + cmbOut.getSelectedItem().toString();
            timeOut = _formatTime(timeOut);
            String dateHired = _formatDate(txtDateHired.getText());
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE tbl_employees SET Employee_Name='"+txtName.getText().trim()+"',"
                    + " Employee_Address='"+txtAddress.getText().trim()+"',"
                    + " Employee_ContactNo='"+txtContact.getText().trim()+"',"
                    + " Employee_HourlyRate='"+_stripValue(txtHourlyRate.getText())+"',"
                    + " Employee_DateHired='"+dateHired+"',"
                    + " Employee_Position='"+txtPosition.getText()+"',"
                    + " Employee_SSSNo='"+txtSSS.getText()+"',"
                    + " EmployeeStatus_Id='"+statusId+"',"
                    + " Employee_TimeIn='"+timeIn+"',"
                    + " Employee_TimeOut='"+timeOut+"',"
                    + " Employee_TimeInPassword='"+txtTimePassword.getText().trim()+"'"
                    + " WHERE Employee_Id='"+txtId.getText()+"'");
            JOptionPane.showMessageDialog(null, "Updated Successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            //Logger.getLogger(SubFormEditEmployee.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                //Logger.getLogger(SubFormEditEmployee.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String _getEmployeeStatus_Id(String status){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String EmployeeStatus_Id = null;
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT EmployeeStatus_Id FROM tbl_employeestatus"
                    + " WHERE EmployeeStatus_Status='"+status+"'");
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
    
    private String _formatTime(String time){
        DateFormat df1 = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = df1.parse(time);
        } catch (ParseException ex) {
            //Logger.getLogger(FormAddEmployee.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        
        return df2.format(date);
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
    
    private String _formatDate(String dateToFormat){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException ex) {
            //Logger.getLogger(SubFormEditEmployee.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        
        return sdf1.format(date);
    }
    
}
