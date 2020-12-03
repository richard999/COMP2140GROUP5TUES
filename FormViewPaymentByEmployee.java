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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-Earl
 */
public class FormViewPaymentByEmployee extends JInternalFrame{
    private final JTextField txtId, txtName;
    private final JButton btnSearch, btnAdd, btnEdit, btnDelete, btnCancel;
    JTable tablePayment;
    DefaultTableModel tablePaymentModel;
    
    public FormViewPaymentByEmployee(){
        JLabel lblId = new JLabel("ID");
        JLabel lblName = new JLabel("Name");
        JLabel lblBlank = new JLabel("");
        btnSearch = new JButton(">");
        btnSearch.addActionListener(new ButtonFunction());
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(150, btnSearch.getPreferredSize().height));
        txtId.setEditable(false);
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(150, btnSearch.getPreferredSize().height));
        txtName.setEditable(false);
        
        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setBorder(BorderFactory.createTitledBorder("Employee"));
        GroupLayout topLeftLayout = new GroupLayout(topLeftPanel);
        topLeftLayout.setAutoCreateContainerGaps(true);
        topLeftLayout.setAutoCreateGaps(true);
        topLeftPanel.setLayout(topLeftLayout);
        
        GroupLayout.Group hg1 = topLeftLayout.createParallelGroup();
        GroupLayout.Group hg2 = topLeftLayout.createParallelGroup();
        GroupLayout.Group hg3 = topLeftLayout.createParallelGroup();
        
        GroupLayout.Group vg1 = topLeftLayout.createParallelGroup();
        GroupLayout.Group vg2 = topLeftLayout.createParallelGroup();
        
        hg1.addComponent(lblId);
        hg1.addComponent(lblName);
        
        hg2.addComponent(txtId);
        hg2.addComponent(txtName);
        
        hg3.addComponent(btnSearch);
        hg3.addComponent(lblBlank);
        
        vg1.addComponent(lblId);
        vg1.addComponent(txtId);
        vg1.addComponent(btnSearch);
        
        vg2.addComponent(lblName);
        vg2.addComponent(txtName);
        vg2.addComponent(lblBlank);
        
        GroupLayout.SequentialGroup hseq = topLeftLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq = topLeftLayout.createSequentialGroup();
        
        hseq.addGroup(hg1);
        hseq.addGroup(hg2);
        hseq.addGroup(hg3);
        vseq.addGroup(vg1);
        vseq.addGroup(vg2);
        
        topLeftLayout.setHorizontalGroup(hseq);
        topLeftLayout.setVerticalGroup(vseq);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        
        String[] tableHeader = {
            "ID", "Cash Advance: Payment", "Date"
        };
        tablePaymentModel = new DefaultTableModel();
        tablePaymentModel.setColumnIdentifiers(tableHeader);
        tablePayment = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePayment.setModel(tablePaymentModel);
        tablePayment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePayment.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablePayment.getColumnModel().getColumn(0).setPreferredWidth(90);
        tablePayment.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablePayment.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tablePayment);
        
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
        rightPanel.setPreferredSize(new Dimension(105, scrollPane.getPreferredSize().height));
        rightPanel.add(btnAdd);
        rightPanel.add(btnEdit);
        rightPanel.add(btnDelete);
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ButtonFunction());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCancel);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
        container.add(bottomPanel, BorderLayout.SOUTH);
        
        _disableButtons();
        
        setContentPane(container);
        setTitle("Cash Advance: Payment");
        setBounds(100, 100, 580, 500);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setIconifiable(true);
        setClosable(true);
    }
    
    private void _enableButtons(){
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(true);
    }
    
    private void _disableButtons(){
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSearch){
                _search();
            }else if(e.getSource() == btnAdd){
                _add();
            }else if(e.getSource() == btnEdit){
                _edit();
            }else if(e.getSource() == btnDelete){
                _delete();
            }else if(e.getSource() == btnCancel){
                _cancel();
            }
        }
        
        private void _search() {
            if(_goSearch()){
                _generate();
            }
        }
        
        private boolean _goSearch() {
            SubFormEmployees subFormEmployees = new SubFormEmployees();
            subFormEmployees._loadTableData();
            if(JOptionPane.showConfirmDialog(null, subFormEmployees, "List of Employee", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                String[] value = subFormEmployees._getValues();
                if(!value[0].equals("")){
                    txtId.setText(value[0]);
                    txtName.setText(value[1]);
                    btnSearch.setEnabled(false);
                    _enableButtons();
                    return true;
                }
            }
            return false;
        }
        
        private void _generate() {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            tablePaymentModel.setRowCount(0);
            
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Payment_Id, ROUND(Payment_Amount, 2), Payment_Date"
                        + " FROM tbl_cashadvancepayment WHERE Employee_Id='"+txtId.getText()+"'");
                NumberFunction nFunction = new NumberFunction();
                while(rs.next()){
                    String id = rs.getString("Payment_Id");
                    String amount = "Php " + nFunction._getFormattedNumber(rs.getString("ROUND(Payment_Amount, 2)"));
                    String date = rs.getString("Payment_Date");
                    tablePaymentModel.addRow(new Object[]{id, amount, date});
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

        private void _add() {
            SubFormAddPayment subFormAddPayment = new SubFormAddPayment(txtId.getText(), txtName.getText());
            if(JOptionPane.showConfirmDialog(null, subFormAddPayment, "Add Cash Advance", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                if(subFormAddPayment._add()){
                    _generate();
                }
            }
        }

        private void _cancel() {
            _disableButtons();
            tablePaymentModel.setRowCount(0);
            txtId.setText("");
            txtName.setText("");
            btnSearch.setEnabled(true);
        }

        private void _edit() {
            try {
                NumberFunction nFunction = new NumberFunction();
                SubFormEditPayment subFormEditPayment = new SubFormEditPayment(
                        txtId.getText(),
                        txtName.getText(),
                        tablePayment.getValueAt(tablePayment.getSelectedRow(), 0).toString(),
                        tablePayment.getValueAt(tablePayment.getSelectedRow(), 2).toString(),
                        nFunction._stripValue(tablePayment.getValueAt(tablePayment.getSelectedRow(), 1).toString())
                );
                if(JOptionPane.showConfirmDialog(null, subFormEditPayment, "Edit Payment",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    if(subFormEditPayment._edit()){
                        _generate();
                    }
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
            
        }

        private void _delete() {
            if(JOptionPane.showConfirmDialog(null, "Are you sure?", "DELETE?",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                if(_goDelete()){
                    _generate();
                }
            }
        }

        private boolean _goDelete() {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_cashadvancepayment"
                        + " WHERE Employee_Id='"+txtId.getText()+"'"
                        + " AND Payment_Id='"+tablePayment.getValueAt(tablePayment.getSelectedRow(), 0).toString()+"'");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return true;
        }
        
    }
    
}
