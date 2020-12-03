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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormViewVoucherItem extends JInternalFrame{
    private final JTextField txtFilter;
    DefaultTableModel tableItemModel;
    private final JTable tableItem;
    JButton btnEdit, btnDelete;
    
    public FormViewVoucherItem(){
        JLabel lblTitle = new JLabel("View Voucher Item");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));
        
        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);
        
        JLabel lblFilter = new JLabel("Filter");
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(150, 25));
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _filterData();
            }
        });
        
        JPanel centerTopPanel = new JPanel();
        centerTopPanel.add(lblFilter);
        centerTopPanel.add(txtFilter);
        
        String[] tableHeader = {
            "ID", "Item"
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
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItem.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableItem.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tableItem);
        
        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(100, 36));
        btnEdit.addActionListener(new ButtonFunction());
        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(new ButtonFunction());
        
        JPanel centerRightPanel = new JPanel();
        centerRightPanel.setPreferredSize(new Dimension(110, scrollPane.getPreferredSize().height));
        centerRightPanel.add(btnEdit);
        centerRightPanel.add(btnDelete);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(centerTopPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(centerRightPanel, BorderLayout.EAST);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        
        addInternalFrameListener(new WindowFunction());
        
        setContentPane(container);
        setTitle("View Voucher Item");
        setBounds(50, 10, 520, 400);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }
    
    private void _loadTableData(){
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_particulars.VoucherItem_Id,"
                    + " tbl_voucheritem.VoucherItem_Name FROM tbl_particulars INNER JOIN tbl_voucheritem"
                    + " ON tbl_particulars.VoucherItem_Id=tbl_voucheritem.VoucherItem_Id");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_particulars.VoucherItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_voucheritem.VoucherItem_Name"));
                tableItemModel.addRow(new Object[]{id, name});
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
    
    private void _filterData() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_particulars.VoucherItem_Id,"
                    + " tbl_voucheritem.VoucherItem_Name FROM tbl_particulars INNER JOIN tbl_voucheritem"
                    + " ON tbl_particulars.VoucherItem_Id=tbl_voucheritem.VoucherItem_Id"
                    + " WHERE tbl_particulars.VoucherItem_Id LIKE '%"+txtFilter.getText().trim()+"%'"
                    + " OR tbl_voucheritem.VoucherItem_Name LIKE '%"+txtFilter.getText().trim()+"%'");
            StringFunction sFunction = new StringFunction();
            while(rs.next()){
                String id = rs.getString("tbl_particulars.VoucherItem_Id");
                String name = sFunction._Capitalized(rs.getString("tbl_voucheritem.VoucherItem_Name"));
                tableItemModel.addRow(new Object[]{id, name});
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
    
    private class WindowFunction implements InternalFrameListener{

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            _loadTableData();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class ButtonFunction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnEdit){
                _edit();
            }else if (e.getSource() == btnDelete){
                _delete();
            }
        }

        private void _edit() {
            try {
                SubFormEditVoucherItem subFormEditVoucherItem = new SubFormEditVoucherItem(
                        tableItem.getValueAt(tableItem.getSelectedRow(), 0).toString(),
                        tableItem.getValueAt(tableItem.getSelectedRow(), 1).toString()
                );
                if(JOptionPane.showConfirmDialog(null, subFormEditVoucherItem, "Edit",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
                    subFormEditVoucherItem._updateVoucherItem();
                    _loadTableData();
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void _delete() {
            try {
                String id = tableItem.getValueAt(tableItem.getSelectedRow(), 0).toString();
                if(JOptionPane.showConfirmDialog(null, "Are your sure?", "DELETE", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    _goDelete(id);
                    _loadTableData();
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        private void _goDelete(String id){
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_deletedparticulars (VoucherItem_Id)"
                        + " VALUES ('"+id+"')");
                st.executeUpdate("DELETE FROM tbl_particulars WHERE VoucherItem_Id='"+id+"'");
            } catch (SQLException ex) {
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
        
    }
   
}
