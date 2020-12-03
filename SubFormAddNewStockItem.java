/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-Earl
 */
public class SubFormAddNewStockItem extends JPanel {

    JLabel lblBlank, lblBlank1;
    JLabel lblStockName, lblStockUnit;
    JTextField txtStockName, txtOtherUnit;
    JComboBox cmbUnit;
    JCheckBox chkOtherUnit;

    public SubFormAddNewStockItem() {
        lblBlank = new JLabel("");
        lblBlank1 = new JLabel("");
        lblStockName = new JLabel("Stock Name:");
        lblStockUnit = new JLabel("Stock Unit:");

        txtStockName = new JTextField();
        txtStockName.setPreferredSize(new Dimension(60, 25));
        txtOtherUnit = new JTextField();
        txtOtherUnit.setPreferredSize(new Dimension(60, 25));
        txtOtherUnit.setEnabled(false);

        cmbUnit = new JComboBox();
        cmbUnit.setPreferredSize(new Dimension(60, 25));

        chkOtherUnit = new JCheckBox("Other");
        chkOtherUnit.addItemListener(new CheckBoxFunction());

        JPanel unitPanel2 = new JPanel();
        unitPanel2.add(cmbUnit);
        unitPanel2.add(chkOtherUnit);
        unitPanel2.add(txtOtherUnit);

        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        GroupLayout.Group hg1 = layout.createParallelGroup();
        GroupLayout.Group hg2 = layout.createParallelGroup();
        GroupLayout.Group vg1 = layout.createParallelGroup();
        GroupLayout.Group vg2 = layout.createParallelGroup();

        hg1.addComponent(lblStockName);
        hg1.addComponent(lblStockUnit);

        hg2.addComponent(txtStockName);
        hg2.addComponent(unitPanel2);

        vg1.addComponent(lblStockName);
        vg1.addComponent(txtStockName);

        vg2.addComponent(lblStockUnit);
        vg2.addComponent(unitPanel2);

        GroupLayout.SequentialGroup hseq1 = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vseq1 = layout.createSequentialGroup();

        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);

        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);

        layout.setHorizontalGroup(hseq1);
        layout.setVerticalGroup(vseq1);

        _loadComboData();
    }

    private void _loadComboData() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT StockUnit_Unit FROM tbl_stockunit");
            while (rs.next()) {
                cmbUnit.addItem(rs.getString("StockUnit_Unit"));
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

    protected void _save() {
        boolean error = false;
        String message = "FAILED";
        if (txtStockName.getText().trim().equals("")) {
            message += "\n*Stock Name is REQUIRED";
            error = true;
        }
        if (chkOtherUnit.isSelected()) {
            if (txtOtherUnit.getText().trim().equals("")) {
                message += "\n*Stock Unit is REQUIRED";
                error = true;
            }
        } else {
            try {
                if (cmbUnit.getSelectedItem().toString().trim().equals("")) {
                    message += "\n*Stock Unit is REQUIRED";
                    error = true;
                }
            } catch (Exception ex) {
                message += "\n*Stock Unit is REQUIRED";
                error = true;
            }

        }

        if (error) {
            JOptionPane.showMessageDialog(null, message, "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            if (chkOtherUnit.isSelected()) {
                if (!_ifStockUnitExist()) {
                    _goSaveStockUnit();
                }
            }

            _goSave();
        }

    }

    private boolean _ifStockUnitExist() {
        String stockUnit = null;
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT StockUnit_Unit from tbl_stockunit"
                    + " WHERE StockUnit_Unit='" + txtOtherUnit.getText().toLowerCase().trim() + "'");
            if (rs.next()) {
                stockUnit = rs.getString("StockUnit_Unit");
            }
        } catch (SQLException ex) {
            //Logger.getLogger(SubFormAddNewStockItem.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        return stockUnit != null;
    }

    private void _goSaveStockUnit() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO tbl_stockunit(StockUnit_Unit)"
                    + " VALUES ('" + txtOtherUnit.getText().toLowerCase().trim() + "')");

            st.executeUpdate("ALTER TABLE tbl_stockunit"
                    + " auto_increment = 1");
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

    private void _goSave() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            String stockId = _getStockMaxId();
            String unitId = _getUnitId();
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO tbl_stocks(Stock_Id, Stock_Name, StockUnit_id, Stock_Remaining)"
                    + " VALUES ('" + stockId + "', '" + txtStockName.getText().toLowerCase() + "', '" + unitId + "', '0')");
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

    private String _getStockMaxId() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String maxId = null;

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(Stock_Id) FROM tbl_stocks");
            if (rs.next()) {
                maxId = rs.getString("MAX(Stock_Id)");
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

        if (maxId == null) {
            maxId = "1";
        } else {
            int temp = Integer.parseInt(maxId);
            temp += 1;
            maxId = Integer.toString(temp);
        }

        return maxId;
    }

    private String _getUnitId() {
        String unit;
        String unitId = null;
        if (chkOtherUnit.isSelected()) {
            unit = txtOtherUnit.getText().toLowerCase().trim();
        } else {
            unit = cmbUnit.getSelectedItem().toString().toLowerCase().trim();
        }

        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT StockUnit_Id from tbl_stockunit"
                    + " WHERE StockUnit_Unit='" + unit + "'");
            if (rs.next()) {
                unitId = rs.getString("StockUnit_Id");
            }
        } catch (SQLException ex) {
            //Logger.getLogger(SubFormAddNewStockItem.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        return unitId;
    }

    private class CheckBoxFunction implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (chkOtherUnit.isSelected()) {
                cmbUnit.setEnabled(false);
                txtOtherUnit.setEnabled(true);
            } else {
                cmbUnit.setEnabled(true);
                txtOtherUnit.setEnabled(false);
            }
        }

    }

}
