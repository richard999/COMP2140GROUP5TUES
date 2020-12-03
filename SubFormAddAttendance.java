/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Dimension;
import java.sql.Connection;
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
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author IT-NEW
 */
public class SubFormAddAttendance extends JPanel{
    private final JTextField txtId, txtName;
    private final JComboBox cmbHourIn, cmbMinuteIn, cmbHourOut, cmbMinuteOut, cmbIn, cmbOut;
    private final JDatePickerImpl datePickerDate;
    
    private String timeIn, timeOut;
    
    public SubFormAddAttendance(String id, String name) {
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblDate = new JLabel("Date");
        txtId = new JTextField(id);
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(200, 25));
        txtName = new JTextField(name);
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(200, 25));
        
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePickerDate = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        JPanel firstPanel = new JPanel();
        GroupLayout firstLayout = new GroupLayout(firstPanel);
        firstLayout.setAutoCreateContainerGaps(true);
        firstLayout.setAutoCreateGaps(true);
        firstPanel.setLayout(firstLayout);
        
        GroupLayout.Group hg1 = firstLayout.createParallelGroup();
        GroupLayout.Group hg2 = firstLayout.createParallelGroup();
        GroupLayout.Group vg1 = firstLayout.createParallelGroup();
        GroupLayout.Group vg2 = firstLayout.createParallelGroup();
        GroupLayout.Group vg3 = firstLayout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        hg1.addComponent(lblDate);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        hg2.addComponent(datePickerDate);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        
        vg3.addComponent(lblDate);
        vg3.addComponent(datePickerDate);
        
        GroupLayout.SequentialGroup hseq1 = firstLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = firstLayout.createSequentialGroup();
        
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);
        vseq1.addGroup(vg3);
        
        firstLayout.setHorizontalGroup(hseq1);
        firstLayout.setVerticalGroup(vseq1);
        
        JLabel lblColon1 = new JLabel(":");
        cmbHourIn = new JComboBox();
        cmbMinuteIn = new JComboBox();
        cmbIn = new JComboBox();
        
        JPanel secondPanel = new JPanel();
        secondPanel.setPreferredSize(new Dimension(firstPanel.getPreferredSize().width, 65));
        secondPanel.setBorder(BorderFactory.createTitledBorder("Time In (hh:mm)"));
        secondPanel.add(cmbHourIn);
        secondPanel.add(lblColon1);
        secondPanel.add(cmbMinuteIn);
        secondPanel.add(cmbIn);
        
        JLabel lblColon2 = new JLabel(":");
        cmbHourOut = new JComboBox();
        cmbMinuteOut = new JComboBox();
        cmbOut = new JComboBox();
        
        JPanel thirdPanel = new JPanel();
        thirdPanel.setPreferredSize(new Dimension(firstPanel.getPreferredSize().width, 65));
        thirdPanel.setBorder(BorderFactory.createTitledBorder("Time Out (hh:mm)"));
        thirdPanel.add(cmbHourOut);
        thirdPanel.add(lblColon2);
        thirdPanel.add(cmbMinuteOut);
        thirdPanel.add(cmbOut);
        
        add(firstPanel);
        add(secondPanel);
        add(thirdPanel);
        
        setPreferredSize(new Dimension(300, 270));
        
        _loadTime();
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
    
    private boolean _parseTimeIn(String timeToParse){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date time;
        try {
            time = sdf.parse(timeToParse);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Time In: Invalid Time", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        timeIn = sdf1.format(time);
        
        return true;
    }
    
    private boolean _parseTimeOut(String timeToParse){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date time;
        try {
            time = sdf.parse(timeToParse);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Time Out: Invalid Time", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        timeOut = sdf1.format(time);
        
        return true;
    }
    
    private String _formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    protected void _add(){
        String time1 = cmbHourIn.getSelectedItem().toString() + ":" + cmbMinuteIn.getSelectedItem().toString() + " " + cmbIn.getSelectedItem().toString();
        String time2 = cmbHourOut.getSelectedItem().toString() + ":" + cmbMinuteOut.getSelectedItem().toString() + " " + cmbOut.getSelectedItem().toString();
        if(_parseTimeIn(time1) && _parseTimeOut(time2)){
            _goAdd(txtId.getText(), _formatDate((Date) datePickerDate.getModel().getValue()), timeIn, timeOut);
        }
    }
    
    private void _goAdd(String id, String date, String timeIn, String timeOut) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        timeIn = date + " " + timeIn;
        timeOut = date + " " + timeOut;
        
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO tbl_time(Employee_Id, Time_In, Time_Out)"
                    + " VALUES ('"+id+"', '"+timeIn+"', '"+timeOut+"')");
            JOptionPane.showMessageDialog(null, "Data Successfully Added", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
    
}
