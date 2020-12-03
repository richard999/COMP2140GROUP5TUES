/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author IT-Earl
 */
public class FormViewStockRecord extends JInternalFrame {

    private final JComboBox cmbMonth;
    private final JTextField txtYear;
    private final JButton btnGenerate;

    JTable tableStock;
    DefaultTableModel tableStockModel;

    public FormViewStockRecord() {
        String[] comboItems = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"
        };
        cmbMonth = new JComboBox(comboItems);
        txtYear = new JTextField();
        txtYear.setPreferredSize(new Dimension(cmbMonth.getPreferredSize().width, cmbMonth.getPreferredSize().height));
        btnGenerate = new JButton("Generate");

        JPanel topCenterPanel = new JPanel();
        topCenterPanel.add(new JLabel("Month"));
        topCenterPanel.add(cmbMonth);
        topCenterPanel.add(new JLabel("Year"));
        topCenterPanel.add(txtYear);

        JPanel topBottomPanel = new JPanel();
        topBottomPanel.add(btnGenerate);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topBottomPanel, BorderLayout.SOUTH);

        String[] tableHeader = {
            "Items", "Items", "Items", "Items", "Items", "Items", "Items", "Items", "Items", "Items", "Items", "Items"
        };

        tableStockModel = new DefaultTableModel();
        tableStockModel.setColumnIdentifiers(tableHeader);

        tableStock = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableStock.setModel(tableStockModel);
        tableStock.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableStock.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tableStock.getColumnModel().getColumn(1).setHeaderRenderer(null);
        
        JScrollPane scrollPane = new JScrollPane(tableStock);
        FixedColumnTable fct = new FixedColumnTable(1, scrollPane);

        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        setContentPane(container);
        setTitle("Stock Record");
        setBounds(30, 30, 650, 400);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        
        setIconifiable(true);
    }

}
