/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 *
 * @author IT-Earl
 */
public class FormStockInStockOut extends JInternalFrame {

    JTextField txtStockInName, txtStockOutName;
    JTextField txtStockInQuantity, txtStockOutQuantity;
    JTextField txtStockInUnit, txtStockOutUnit;
    JButton btnStockInShowStockList, btnStockOutShowStockList;
    JButton btnStockIn, btnStockOut;

    public FormStockInStockOut() {
        TextFieldFilter textFieldFilter = new TextFieldFilter();

        JTabbedPane tabbedPane = new JTabbedPane();

        btnStockInShowStockList = new JButton(">");
        btnStockInShowStockList.addActionListener(new ButtonFunction());
        btnStockIn = new JButton("Go");
        btnStockIn.addActionListener(new ButtonFunction());
        txtStockInName = new JTextField();
        txtStockInName.setEditable(false);
        txtStockInName.setPreferredSize(new Dimension(170, btnStockInShowStockList.getPreferredSize().height));
        txtStockInQuantity = new JTextField();
        txtStockInQuantity.setPreferredSize(new Dimension(50, btnStockInShowStockList.getPreferredSize().height));
        txtStockInQuantity.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                textFieldFilter._numbersAndPeriod(e);
            }
        });
        txtStockInUnit = new JTextField();
        txtStockInUnit.setEditable(false);
        txtStockInUnit.setPreferredSize(new Dimension(50, btnStockInShowStockList.getPreferredSize().height));

        Color backColor = new Color(255, 249, 219);

        JPanel stockInStockNamePanel = new JPanel();
        stockInStockNamePanel.setBackground(backColor);
        stockInStockNamePanel.setBorder(BorderFactory.createTitledBorder("Stock"));
        stockInStockNamePanel.add(txtStockInName);
        stockInStockNamePanel.add(btnStockInShowStockList);
        stockInStockNamePanel.setPreferredSize(new Dimension(260, 59));

        JPanel stockInNoOfUnitsPanel = new JPanel();
        stockInNoOfUnitsPanel.setBackground(backColor);
        stockInNoOfUnitsPanel.setBorder(BorderFactory.createTitledBorder("# of Units"));
        stockInNoOfUnitsPanel.add(txtStockInQuantity);
        stockInNoOfUnitsPanel.add(txtStockInUnit);
        stockInNoOfUnitsPanel.setPreferredSize(new Dimension(260, 59));

        JPanel stockInPanel = new JPanel();
        stockInPanel.setBackground(backColor);
        stockInPanel.add(new JLabel("STOCK IN"));
        stockInPanel.add(stockInStockNamePanel);
        stockInPanel.add(stockInNoOfUnitsPanel);
        stockInPanel.add(btnStockIn);
        tabbedPane.addTab("Stock In", stockInPanel);

        btnStockOutShowStockList = new JButton(">");
        btnStockOutShowStockList.addActionListener(new ButtonFunction());
        btnStockOut = new JButton("Go");
        btnStockOut.addActionListener(new ButtonFunction());
        txtStockOutName = new JTextField();
        txtStockOutName.setEditable(false);
        txtStockOutName.setPreferredSize(new Dimension(170, btnStockInShowStockList.getPreferredSize().height));
        txtStockOutQuantity = new JTextField();
        txtStockOutQuantity.setPreferredSize(new Dimension(50, btnStockInShowStockList.getPreferredSize().height));
        txtStockOutUnit = new JTextField();
        txtStockOutUnit.setEditable(false);
        txtStockOutUnit.setPreferredSize(new Dimension(50, btnStockInShowStockList.getPreferredSize().height));

        Color backColor1 = new Color(141, 255, 254);

        JPanel stockOutStockNamePanel = new JPanel();
        stockOutStockNamePanel.setBackground(backColor1);
        stockOutStockNamePanel.setBorder(BorderFactory.createTitledBorder("Stock"));
        stockOutStockNamePanel.add(txtStockOutName);
        stockOutStockNamePanel.add(btnStockOutShowStockList);
        stockOutStockNamePanel.setPreferredSize(new Dimension(260, 59));

        JPanel stockOutNoOfUnitsPanel = new JPanel();
        stockOutNoOfUnitsPanel.setBackground(backColor1);
        stockOutNoOfUnitsPanel.setBorder(BorderFactory.createTitledBorder("# of Units"));
        stockOutNoOfUnitsPanel.add(txtStockOutQuantity);
        stockOutNoOfUnitsPanel.add(txtStockOutUnit);
        stockOutNoOfUnitsPanel.setPreferredSize(new Dimension(260, 59));

        JPanel stockOutPanel = new JPanel();
        stockOutPanel.setBackground(backColor1);
        stockOutPanel.add(new JLabel("STOCK OUT"));
        stockOutPanel.add(stockOutStockNamePanel);
        stockOutPanel.add(stockOutNoOfUnitsPanel);
        stockOutPanel.add(btnStockOut);
        tabbedPane.addTab("Stock Out", stockOutPanel);

        setContentPane(tabbedPane);
        setTitle("Stock In/Stock Out");
        setBounds(30, 30, 320, 250);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
    }

    private class ButtonFunction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnStockInShowStockList) {
                _showStockList(e);
            } else if (e.getSource() == btnStockOutShowStockList) {
                _showStockList(e);
            } else if (e.getSource() == btnStockIn) {
                _saveStock(e);
            } else if (e.getSource() == btnStockOut) {
                _saveStock(e);
            }
        }

        private void _showStockList(ActionEvent e) {
            SubFormStockList subFormStockList = new SubFormStockList();
            if (JOptionPane.showConfirmDialog(null, subFormStockList, "Stock List",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                String[] value = subFormStockList._getValues();
                if (value[0] != null) {
                    if (e.getSource() == btnStockInShowStockList) {
                        txtStockInName.setText(value[0]);
                        txtStockInUnit.setText(value[1]);
                    } else if (e.getSource() == btnStockOutShowStockList) {
                        txtStockOutName.setText(value[0]);
                        txtStockOutUnit.setText(value[1]);
                    }
                }
            }
        }

        private void _saveStock(ActionEvent e) {
            boolean error = false;
            String message = "FAILED!";

            if (e.getSource() == btnStockIn) {
                if (txtStockInName.getText().trim().equals("")) {
                    error = true;
                    message += "\n *Stock is REQUIRED!";
                }
                if (txtStockInQuantity.getText().trim().equals("")) {
                    error = true;
                    message += "\n *Unit is REQUIRED!";
                }
            } else if (e.getSource() == btnStockOut) {
                if (txtStockOutName.getText().trim().equals("")) {
                    error = true;
                    message += "\n *Stock is REQUIRED!";
                }
                if (txtStockOutQuantity.getText().trim().equals("")) {
                    error = true;
                    message += "\n *Unit is REQUIRED!";
                }
            }

            if (error) {
                JOptionPane.showMessageDialog(null, message, "FAILED", JOptionPane.WARNING_MESSAGE);
            } else {
                String date = _getCurrentDate();
                if (e.getSource() == btnStockIn) {
                    String unitId = _getUnitId(txtStockInUnit.getText());
                    String stockId = _getStockId(txtStockInName.getText().toLowerCase().trim(), unitId);
                    
                    if (_ifExist(stockId, date)) {
                        _goUpdateStockIn(stockId, date);
                    } else {
                        _goSaveStockIn(stockId, date);
                    }
                } else if (e.getSource() == btnStockOut) {
                    String unitId = _getUnitId(txtStockOutUnit.getText());
                    String stockId = _getStockId(txtStockOutName.getText().toLowerCase().trim(), unitId);
                    
                    if (_ifExist(stockId, date)) {
                        _goUpdateStockOut(stockId, date);
                    } else {
                        _goSaveStockOut(stockId, date);
                    }
                }
            }

        }

        private boolean _ifExist(String stockId, String date) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Stock_Id FROM tbl_stockrecord"
                        + " WHERE Stock_Id='" + stockId + "'"
                        + " AND StockRecord_Date='" + date + "'");
                if (rs.next()) {
                    conn.close();
                    return true;
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            return false;
        }

        private String _getUnitId(String unit) {
            String id = null;
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT StockUnit_Id FROM tbl_stockunit"
                        + " WHERE StockUnit_Unit='" + unit + "'");
                if (rs.next()) {
                    id = rs.getString("StockUnit_Id");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            return id;
        }

        private String _getStockId(String stockName, String unitId) {
            String id = null;
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT Stock_Id FROM tbl_stocks"
                        + " WHERE Stock_Name='" + stockName + "'"
                        + " AND StockUnit_Id='" + unitId + "'");
                if (rs.next()) {
                    id = rs.getString("Stock_Id");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormViewStock.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            return id;
        }

        private void _goSaveStockIn(String stockId, String date) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                float stockIn = Float.parseFloat(txtStockInQuantity.getText().trim());
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_stockrecord(Stock_Id, StockRecord_Date, StockRecord_StockIn)"
                        + " VALUES ('" + stockId + "','" + date + "','" + stockIn + "')");
                JOptionPane.showMessageDialog(null, "Data Save", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                _clearStockInFields();
            } catch (SQLException ex) {
                //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private String _getCurrentDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            return sdf.format(date);
        }

        private void _goUpdateStockIn(String stockId, String date) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            String inputQuantity = txtStockInQuantity.getText().trim();
            String quantity = _getStockInQuantity(stockId, date);
            String updatedQuantity = _calculateQuantity(inputQuantity, quantity);

            if (updatedQuantity != null) {
                try {
                    Statement st = conn.createStatement();
                    st.executeUpdate("UPDATE tbl_stockrecord"
                            + " SET StockRecord_StockIn='" + updatedQuantity + "'"
                            + " WHERE Stock_Id='" + stockId + "'"
                            + " AND StockRecord_Date='" + date + "'");
                    JOptionPane.showMessageDialog(null, "Success", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    _clearStockInFields();
                } catch (SQLException ex) {
                    //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
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

        private String _getStockInQuantity(String stockId, String date) {
            String stockIn = null;
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT StockRecord_StockIn FROM tbl_stockrecord"
                        + " WHERE Stock_Id='" + stockId + "'"
                        + " AND StockRecord_Date='" + date + "'");
                if (rs.next()) {
                    stockIn = rs.getString("StockRecord_StockIn");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            return stockIn;
        }

        private String _calculateQuantity(String inputQuantity, String quantity) {
            String quantityToReturn = null;
            try {
                float temp = Float.parseFloat(inputQuantity) + Float.parseFloat(quantity);
                quantityToReturn = Float.toString(temp);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "FAILED \n\n Wrong Input", "FAILED", JOptionPane.ERROR_MESSAGE);
            }

            return quantityToReturn;
        }

        private void _goUpdateStockOut(String stockId, String date) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            String inputQuantity = txtStockOutQuantity.getText().trim();
            String quantity = _getStockOutQuantity(stockId, date);
            String updatedQuantity = _calculateQuantity(inputQuantity, quantity);

            if (updatedQuantity != null) {
                try {
                    Statement st = conn.createStatement();
                    st.executeUpdate("UPDATE tbl_stockrecord"
                            + " SET StockRecord_StockOut='" + updatedQuantity + "'"
                            + " WHERE Stock_Id='" + stockId + "'"
                            + " AND StockRecord_Date='" + date + "'");
                    JOptionPane.showMessageDialog(null, "Success", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    _clearStockOutFields();
                } catch (SQLException ex) {
                    //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
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

        private String _getStockOutQuantity(String stockId, String date) {
            String stockOut = null;
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT StockRecord_StockOut FROM tbl_stockrecord"
                        + " WHERE Stock_Id='" + stockId + "'"
                        + " AND StockRecord_Date='" + date + "'");
                if (rs.next()) {
                    stockOut = rs.getString("StockRecord_StockOut");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            return stockOut;
        }

        private void _goSaveStockOut(String stockId, String date) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                float stockOut = Float.parseFloat(txtStockOutQuantity.getText().trim());
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO tbl_stockrecord(Stock_Id, StockRecord_Date, StockRecord_StockOut)"
                        + " VALUES ('" + stockId + "','" + date + "','" + stockOut + "')");
                JOptionPane.showMessageDialog(null, "Data Save", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                _clearStockOutFields();
            } catch (SQLException ex) {
                //Logger.getLogger(FormStockInStockOut.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void _clearStockInFields() {
            txtStockInName.setText("");
            txtStockInQuantity.setText("");
            txtStockInUnit.setText("");
        }
        
        private void _clearStockOutFields() {
            txtStockOutName.setText("");
            txtStockOutQuantity.setText("");
            txtStockOutUnit.setText("");
        }

    }

}
