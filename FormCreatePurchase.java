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
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
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

/**
 *
 * @author IT-NEW
 */
public class FormCreatePurchase extends JInternalFrame {

    private final JLabel lblPurchaseOrder, lblPurchaseOrderNo;
    private final JButton btnAdd, btnRemove;
    private final JButton btnSave, btnPrint, btnRefresh;
    private final JTextField txtFilter;
    JTable tableItem, tablePrint;
    DefaultTableModel tableItemModel, tablePrintModel;

    public FormCreatePurchase() {
        JLabel lblTitle = new JLabel("Create Purchase");
        Font bigFont = lblTitle.getFont();
        lblTitle.setFont(new Font(bigFont.getName(), Font.PLAIN, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.add(lblTitle);

        JLabel lblFilter = new JLabel("Filter");
        //lblFilter.setPreferredSize(new Dimension(200, 25));
        txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(200, 25));
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _filterData(txtFilter.getText());
            }

        });

        //JPanel leftLeftPanel = new JPanel();
        //leftLeftPanel.add(lblFilter);
        JPanel leftTopPanel = new JPanel();
        leftTopPanel.add(lblFilter);
        leftTopPanel.add(txtFilter);

        String[] tableItemHeader = {
            "Item", "Minimum Order"
        };
        tableItemModel = new DefaultTableModel();
        tableItemModel.setColumnIdentifiers(tableItemHeader);
        tableItem = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItem.setModel(tableItemModel);
        tableItem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Font tableFont = tableItem.getFont();
        tableItem.getTableHeader().setFont(new Font(tableFont.getName(), Font.BOLD, 15));
        tableItem.setFont(new Font(tableFont.getName(), Font.PLAIN, 15));
        tableItem.setRowHeight(25);
        tableItem.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableItem.getColumnModel().getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane1 = new JScrollPane(tableItem);

        JPanel centerLeftPanel = new JPanel(new BorderLayout());
        centerLeftPanel.add(leftTopPanel, BorderLayout.NORTH);
        centerLeftPanel.add(scrollPane1, BorderLayout.CENTER);
        //------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>END of center left panel

        lblPurchaseOrder = new JLabel("Purchase Order No.");
        lblPurchaseOrderNo = new JLabel("");
        lblPurchaseOrderNo.setPreferredSize(new Dimension(200, 25));

        JPanel rightLeftPanel = new JPanel();
        rightLeftPanel.add(lblPurchaseOrder);
        rightLeftPanel.add(lblPurchaseOrderNo);

        JPanel rightTopPanel = new JPanel(new BorderLayout());
        rightTopPanel.add(rightLeftPanel, BorderLayout.WEST);

        String[] tablePrintHeader = {
            "#", "Item", "Minimum Order", "Order", "Amount"
        };
        tablePrintModel = new DefaultTableModel();
        tablePrintModel.setColumnIdentifiers(tablePrintHeader);
        tablePrint = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePrint.setModel(tablePrintModel);
        tablePrint.getTableHeader().setFont(new Font(tableFont.getName(), Font.BOLD, 15));
        tablePrint.setFont(new Font(tableFont.getName(), Font.PLAIN, 15));
        tablePrint.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablePrint.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePrint.setRowHeight(25);
        tablePrint.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablePrint.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablePrint.getColumnModel().getColumn(2).setPreferredWidth(130);
        tablePrint.getColumnModel().getColumn(3).setPreferredWidth(130);
        tablePrint.getColumnModel().getColumn(4).setPreferredWidth(140);

        JScrollPane scrollPane2 = new JScrollPane(tablePrint);

        JPanel centerRightPanel = new JPanel(new BorderLayout());
        centerRightPanel.setPreferredSize(new Dimension(600, 453));
        centerRightPanel.add(rightTopPanel, BorderLayout.NORTH);
        centerRightPanel.add(scrollPane2, BorderLayout.CENTER);

        btnAdd = new JButton("Add >>");
        btnAdd.setPreferredSize(new Dimension(97, 36));
        btnAdd.addActionListener(new ButtonFunction());
        btnRemove = new JButton("<< Remove");
        btnRemove.setPreferredSize(new Dimension(97, 36));
        btnRemove.addActionListener(new ButtonFunction());
        btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(97, 36));
        btnSave.addActionListener(new ButtonFunction());
        btnPrint = new JButton("Print");
        btnPrint.setPreferredSize(new Dimension(97, 36));
        btnPrint.addActionListener(new ButtonFunction());
        btnRefresh = new JButton("Refresh");
        btnRefresh.setPreferredSize(new Dimension(97, 36));
        btnRefresh.addActionListener(new ButtonFunction());

        JPanel midPanel = new JPanel();
        midPanel.setPreferredSize(new Dimension(110, 220));
        midPanel.add(btnRefresh);
        midPanel.add(btnSave);
        midPanel.add(btnPrint);
        midPanel.add(btnAdd);
        midPanel.add(btnRemove);

        JPanel centerCenterPanel = new JPanel();
        centerCenterPanel.add(midPanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(centerLeftPanel, BorderLayout.WEST);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerRightPanel, BorderLayout.EAST);

        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                _loadTableData();
                String i = _getMaxPurchaseOrderNo();
                lblPurchaseOrderNo.setText(i);
            }
        });

        setContentPane(container);
        setTitle("Print Purchase List");
        setBounds(10, 10, 1200, 600);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }

    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnPrint) {
                _goPrint();
            } else if (e.getSource() == btnAdd) {
                _goAdd();
            } else if (e.getSource() == btnRemove) {
                _goRemove();
            } else if (e.getSource() == btnRefresh) {
                _goRefresh();
            } else if (e.getSource() == btnSave) {
                _save();
            }
        }

        private void _save() {
            if (JOptionPane.showConfirmDialog(null, "Are you sure?", "???", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                _goSavePurchaseOrder();
                for (int i = 0; i < tablePrint.getRowCount(); i++) {
                    String purchaseItemId = _getPurchaseItemId(tablePrint.getValueAt(i, 1).toString());
                    _goSave(purchaseItemId);
                }
                JOptionPane.showMessageDialog(null, "Successfully Saved.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void _goSave(String purchaseItemId) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_purchaseitems (PurchaseOrder_No, PurchaseItem_Id)"
                        + " VALUES (" + lblPurchaseOrderNo.getText() + ", " + purchaseItemId + ")");
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

        private void _goSavePurchaseOrder() {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_purchaseorder (PurchaseOrder_No, PurchaseOrder_Date)"
                        + " VALUES (" + lblPurchaseOrderNo.getText() + ",'" + sdf.format(date) + "')");
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

        private String _getPurchaseItemId(String purchaseItemName) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();
            String purchaseItemId = null;

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT PurchaseItem_Id FROM tbl_PurchaseList"
                        + " WHERE PurchaseItem_Name='" + purchaseItemName.toLowerCase() + "'");
                if (rs.next()) {
                    purchaseItemId = rs.getString("PurchaseItem_Id");
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

            return purchaseItemId;
        }

        private void _goRemove() {
            try {
                /*String item = tablePrint.getValueAt(tablePrint.getSelectedRow(), 1).toString();
                 String minorder = tablePrint.getValueAt(tablePrint.getSelectedRow(), 2).toString();
                 tableItemModel.addRow(new Object[]{item, minorder});*/
                tablePrintModel.removeRow(tablePrint.getSelectedRow());
                _incrementColumn();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please select an item from the right side", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }

        private void _goAdd() {
            try {
                String item = tableItem.getValueAt(tableItem.getSelectedRow(), 0).toString();
                if (_ifItemExist(item)) {
                    JOptionPane.showMessageDialog(null, "Item alredy exist", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    String minorder = tableItem.getValueAt(tableItem.getSelectedRow(), 1).toString();
                    tablePrintModel.addRow(new Object[]{"", item, minorder});
                    _incrementColumn();
                }
                //tableItemModel.removeRow(tableItem.getSelectedRow());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please select an item from the left side", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean _ifItemExist(String item) {
            for (int i = 0; i < tablePrint.getRowCount(); i++) {
                if (item.equals(tablePrint.getValueAt(i, 1))) {
                    return true;
                }
            }
            return false;
        }

        private void _incrementColumn() {
            for (int i = 0; i < tablePrint.getRowCount(); i++) {
                tablePrint.setValueAt(i + 1, i, 0);
            }
        }

        private void _goPrint() {
            if (tablePrint.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Item(s) not found", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                MessageFormat Header = new MessageFormat("Eats Possible Purchases");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String foot = lblPurchaseOrder.getText() + ": " + lblPurchaseOrderNo.getText() + ""
                        + "              Date:" + sdf.format(date);
                MessageFormat Footer = new MessageFormat(foot);
                try {
                    tablePrint.print(JTable.PrintMode.FIT_WIDTH, Header, Footer);
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private void _goRefresh() {
        _loadTableData();
        _clearTablePrint();
        String i = _getMaxPurchaseOrderNo();
        lblPurchaseOrderNo.setText(i);
    }

    private void _clearTablePrint() {
        tablePrintModel.setRowCount(0);
    }

    private void _loadTableData() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_purchaselist.PurchaseItem_Name,"
                    + " tbl_purchaselist.PurchaseItem_MinOrder FROM tbl_purchaselist"
                    + " INNER JOIN tbl_list"
                    + " ON tbl_purchaselist.PurchaseItem_Id=tbl_list.PurchaseItem_Id");
            StringFunction sFunction = new StringFunction();
            while (rs.next()) {
                String name = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                String minorder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                tableItemModel.addRow(new Object[]{name, minorder});
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

    private void _filterData(String itemName) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableItemModel.setRowCount(0);

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_purchaselist.PurchaseItem_Name,"
                    + " tbl_purchaselist.PurchaseItem_MinOrder FROM tbl_purchaselist"
                    + " INNER JOIN tbl_list"
                    + " ON tbl_purchaselist.PurchaseItem_Id=tbl_list.PurchaseItem_Id"
                    + " WHERE tbl_purchaselist.PurchaseItem_Name LIKE '%" + itemName.toLowerCase().trim() + "%'");
            StringFunction sFunction = new StringFunction();
            while (rs.next()) {
                String name = sFunction._Capitalized(rs.getString("tbl_purchaselist.PurchaseItem_Name"));
                String minorder = rs.getString("tbl_purchaselist.PurchaseItem_MinOrder");
                tableItemModel.addRow(new Object[]{name, minorder});
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

    private String _getMaxPurchaseOrderNo() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String maxPurchaseOrderNo = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Max(PurchaseOrder_No) FROM tbl_purchaseorder");
            if (rs.next()) {
                maxPurchaseOrderNo = rs.getString("Max(PurchaseOrder_No)");
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

        if (maxPurchaseOrderNo == null) {
            maxPurchaseOrderNo = "1000";
        } else {
            int temp = Integer.parseInt(maxPurchaseOrderNo);
            temp += 1;
            maxPurchaseOrderNo = Integer.toString(temp);
        }

        return maxPurchaseOrderNo;
    }

}
