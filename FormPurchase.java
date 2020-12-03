/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IT-NEW
 */
public class FormPurchase extends JFrame {

    JTable tableItems;
    JTable tableLowItems;
    JLabel lblNoOfCustomer;
    JLabel lblNoOfSenior;
    DefaultTableModel tableModel;
    DefaultTableModel tableLowModel;
    JTextField txtPurchaseNo;
    JTextField txtDownPayment, txtTotalAmount, txtCashTendered, txtChange;
    JTextField txtCustomerNo, txtSenior;
    JButton btnAddItem, btnRemoveItem;
    JButton btnNewTrans, btnSave, btnDownPayment, btnCashTend, btnDelete, btnSearch, btnCancel, btnAlert;
    JButton btnDiscount;
    JRadioButton rbSiblings, rbSenior, rbNone;
    ButtonGroup radioButtonGroup;

    AlertThread alertThread;
    AlertButtonThread alertButtonThread;

    boolean searched = false;
    boolean alertButton = false;

    public FormPurchase() {
//        URL img = getClass().getResource("Images/eats header.png");
        URL img = getClass().getResource("");
        JLabel lblPurchase = new JLabel(new ImageIcon(img));
        Font titleFont = lblPurchase.getFont();
        lblPurchase.setFont(new Font(titleFont.getName(), Font.PLAIN, 40));

        JPanel titlePanel = new JPanel();
        titlePanel.add(lblPurchase);

        JLabel lblPurchaseNo = new JLabel("Purchase No.");
        txtPurchaseNo = new JTextField();
        txtPurchaseNo.setPreferredSize(new Dimension(300, 30));
        Font purchaseFont = txtPurchaseNo.getFont();
        txtPurchaseNo.setFont(new Font(purchaseFont.getName(), Font.PLAIN, 20));

        JPanel purPanel = new JPanel();
        purPanel.add(lblPurchaseNo);
        purPanel.add(txtPurchaseNo);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(purPanel, BorderLayout.WEST);

        String[] tableHeader = {
            "Item #", "Menu Item", "Unit Price", "# of Order", " Sub-Total"
        };
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(tableHeader);
        tableItems = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItems.setModel(tableModel);
        Font bigFont = tableItems.getFont();
        tableItems.setFont(new Font(bigFont.getName(), Font.PLAIN, 15));
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItems.setRowHeight(25);
        tableItems.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableItems.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableItems.getColumnModel().getColumn(4).setPreferredWidth(150);
        JScrollPane scrollPane = new JScrollPane(tableItems);

        btnAddItem = new JButton("<html><center> Add <br> (F11) </center></html>");
        btnAddItem.setPreferredSize(new Dimension(95, 80));
        btnAddItem.setVerticalTextPosition(JButton.BOTTOM);
        btnAddItem.setHorizontalTextPosition(JButton.CENTER);
        btnAddItem.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Add Item.png");
        btnAddItem.setIcon(new ImageIcon(img));
        btnRemoveItem = new JButton("<html><center> Remove <br> (F12) </center></html>");
        btnRemoveItem.setPreferredSize(new Dimension(95, 80));
        btnRemoveItem.setVerticalTextPosition(JButton.BOTTOM);
        btnRemoveItem.setHorizontalTextPosition(JButton.CENTER);
        btnRemoveItem.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Remove Item.png");
        btnRemoveItem.setIcon(new ImageIcon(img));

        JPanel tableButtonPanel = new JPanel();
        tableButtonPanel.add(btnAddItem);
        tableButtonPanel.add(btnRemoveItem);
        tableButtonPanel.setPreferredSize(new Dimension(120, tableItems.getPreferredSize().height));

        lblNoOfCustomer = new JLabel("No. of Customer");
        lblNoOfCustomer.setPreferredSize(new Dimension(100, 20));
        lblNoOfSenior = new JLabel("path number");
        lblNoOfSenior.setPreferredSize(new Dimension(100, 20));
        txtCustomerNo = new JTextField("0");
        txtCustomerNo.addKeyListener(new Keys());
        txtCustomerNo.addFocusListener(new FocusFunction());
        txtCustomerNo.setPreferredSize(new Dimension(90, 20));
        txtCustomerNo.setHorizontalAlignment(JTextField.CENTER);
        txtSenior = new JTextField("0");
        txtSenior.addKeyListener(new Keys());
        txtSenior.addFocusListener(new FocusFunction());
        txtSenior.setPreferredSize(new Dimension(90, 20));
        txtSenior.setHorizontalAlignment(JTextField.CENTER);
        rbSiblings = new JRadioButton("Honor Roll (5% Discount)");
        rbSiblings.addItemListener(new RadioButtonFunction());
        rbSenior = new JRadioButton("Path Student (20% Discount)");
        rbSenior.addItemListener(new RadioButtonFunction());
        rbNone = new JRadioButton("None");
        rbNone.setSelected(true);
        rbNone.addItemListener(new RadioButtonFunction());
        rbSenior.addKeyListener(new Keys());
        rbSenior.addItemListener(new RadioButtonFunction());
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(rbNone);
        radioButtonGroup.add(rbSiblings);
        radioButtonGroup.add(rbSenior);

        btnDiscount = new JButton("Apply Discount");
        btnDiscount.addActionListener(new ButtonFunctions());

        JPanel discountPanel = new JPanel();
        discountPanel.setBorder(BorderFactory.createTitledBorder("Discount"));
        discountPanel.setPreferredSize(new Dimension(200, discountPanel.getPreferredSize().height));
        discountPanel.add(rbNone);
        discountPanel.add(rbSenior);
        discountPanel.add(lblNoOfCustomer);
        discountPanel.add(txtCustomerNo);
        discountPanel.add(lblNoOfSenior);
        discountPanel.add(txtSenior);
        discountPanel.add(rbSiblings);
        discountPanel.add(btnDiscount);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(tableButtonPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(discountPanel, BorderLayout.WEST);

        btnNewTrans = new JButton("<html><center> New Trans <br> (F1) </center></html>");
        btnNewTrans.setPreferredSize(new Dimension(95, 80));
        btnNewTrans.setVerticalTextPosition(JButton.BOTTOM);
        btnNewTrans.setHorizontalTextPosition(JButton.CENTER);
        btnNewTrans.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon New Transaction.png");
        btnNewTrans.setIcon(new ImageIcon(img));
        btnSave = new JButton("<html><center> Save <br> (F2) </center></html>");
        btnSave.setPreferredSize(new Dimension(95, 80));
        btnSave.setVerticalTextPosition(JButton.BOTTOM);
        btnSave.setHorizontalTextPosition(JButton.CENTER);
        btnSave.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Save.png");
        btnSave.setIcon(new ImageIcon(img));
        btnDownPayment = new JButton("<html><center> Down <br> (F3) </center></html>");
        btnDownPayment.setPreferredSize(new Dimension(95, 80));
        btnDownPayment.setVerticalTextPosition(JButton.BOTTOM);
        btnDownPayment.setHorizontalTextPosition(JButton.CENTER);
        btnDownPayment.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Down Payment.png");
        btnDownPayment.setIcon(new ImageIcon(img));
        btnCashTend = new JButton("<html><center> Cash Tend <br> (F4) </center></html>");
        btnCashTend.setPreferredSize(new Dimension(95, 80));
        btnCashTend.setVerticalTextPosition(JButton.BOTTOM);
        btnCashTend.setHorizontalTextPosition(JButton.CENTER);
        btnCashTend.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Cash Tend.png");
        btnCashTend.setIcon(new ImageIcon(img));
        btnDelete = new JButton("<html><center> Delete <br> (F5) </center></html>");
        btnDelete.setPreferredSize(new Dimension(95, 80));
        btnDelete.setVerticalTextPosition(JButton.BOTTOM);
        btnDelete.setHorizontalTextPosition(JButton.CENTER);
        btnDelete.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Delete.png");
        btnDelete.setIcon(new ImageIcon(img));
        btnSearch = new JButton("<html><center> Search <br> (F6) </center></html>");
        btnSearch.setPreferredSize(new Dimension(95, 80));
        btnSearch.setVerticalTextPosition(JButton.BOTTOM);
        btnSearch.setHorizontalTextPosition(JButton.CENTER);
        btnSearch.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Search.png");
        btnSearch.setIcon(new ImageIcon(img));
        btnCancel = new JButton("<html><center> Cancel <br> (F7) </center></html>");
        btnCancel.setPreferredSize(new Dimension(95, 80));
        btnCancel.setVerticalTextPosition(JButton.BOTTOM);
        btnCancel.setHorizontalTextPosition(JButton.CENTER);
        btnCancel.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Cancel.png");
        btnCancel.setIcon(new ImageIcon(img));
        btnAlert = new JButton("<html><center> Alert <br> (F9) </center></html>");
        btnAlert.setPreferredSize(new Dimension(95, 80));
        btnAlert.setVerticalTextPosition(JButton.BOTTOM);
        btnAlert.setHorizontalTextPosition(JButton.CENTER);
        btnAlert.addActionListener(new ButtonFunctions());
        img = getClass().getResource("Images/Icon Checklist.png");
        btnAlert.setIcon(new ImageIcon(img));

        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.add(btnNewTrans);
        bottomLeftPanel.add(btnSave);
        bottomLeftPanel.add(btnDownPayment);
        bottomLeftPanel.add(btnCashTend);
        bottomLeftPanel.add(btnDelete);
        //bottomLeftPanel.add(btnPrint);
        bottomLeftPanel.add(btnSearch);
        bottomLeftPanel.add(btnCancel);
        bottomLeftPanel.add(btnAlert);

        JLabel lblTotalAmount = new JLabel("Total Amount:");
        lblTotalAmount.setForeground(Color.GREEN);
        Font rightFont = lblTotalAmount.getFont();
        Font newRightFont = new Font(rightFont.getName(), Font.PLAIN, 20);
        lblTotalAmount.setFont(newRightFont);
        JLabel lblDownPayment = new JLabel("Down Payment:");
        lblDownPayment.setForeground(Color.GREEN);
        lblDownPayment.setFont(newRightFont);
        JLabel lblCashTend = new JLabel("Cash Tendered:");
        lblCashTend.setForeground(Color.GREEN);
        lblCashTend.setFont(newRightFont);
        JLabel lblChange = new JLabel("Change");
        lblChange.setForeground(Color.GREEN);
        lblChange.setFont(newRightFont);

        txtTotalAmount = new JTextField();
        txtTotalAmount.setFont(newRightFont);
        txtTotalAmount.setEditable(false);
        txtTotalAmount.setForeground(Color.GREEN);
        txtTotalAmount.setBackground(Color.BLACK);
        txtTotalAmount.setFocusable(false);
        txtTotalAmount.setPreferredSize(new Dimension(300, 20));

        txtDownPayment = new JTextField();
        txtDownPayment.setFont(newRightFont);
        txtDownPayment.setEditable(false);
        txtDownPayment.setForeground(Color.GREEN);
        txtDownPayment.setBackground(Color.BLACK);
        txtDownPayment.setFocusable(false);
        txtDownPayment.setPreferredSize(new Dimension(300, 20));

        txtCashTendered = new JTextField();
        txtCashTendered.setFont(newRightFont);
        txtCashTendered.setEditable(false);
        txtCashTendered.setForeground(Color.GREEN);
        txtCashTendered.setBackground(Color.BLACK);
        txtCashTendered.setFocusable(false);
        txtCashTendered.setPreferredSize(new Dimension(300, 20));

        txtChange = new JTextField();
        txtChange.setFont(newRightFont);
        txtChange.setEditable(false);
        txtChange.setForeground(Color.GREEN);
        txtChange.setBackground(Color.BLACK);
        txtChange.setFocusable(false);
        txtChange.setPreferredSize(new Dimension(300, 20));

        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setBackground(Color.BLACK);
        GroupLayout rightLayout = new GroupLayout(bottomRightPanel);
        rightLayout.setAutoCreateContainerGaps(true);
        rightLayout.setAutoCreateGaps(true);
        bottomRightPanel.setLayout(rightLayout);

        GroupLayout.Group rightHg1 = rightLayout.createParallelGroup();
        GroupLayout.Group rightHg2 = rightLayout.createParallelGroup();

        GroupLayout.Group rightVg1 = rightLayout.createParallelGroup();
        GroupLayout.Group rightVg2 = rightLayout.createParallelGroup();
        GroupLayout.Group rightVg3 = rightLayout.createParallelGroup();
        GroupLayout.Group rightVg4 = rightLayout.createParallelGroup();

        rightHg1.addComponent(lblTotalAmount);
        rightHg1.addComponent(lblDownPayment);
        rightHg1.addComponent(lblCashTend);
        rightHg1.addComponent(lblChange);

        rightHg2.addComponent(txtTotalAmount);
        rightHg2.addComponent(txtDownPayment);
        rightHg2.addComponent(txtCashTendered);
        rightHg2.addComponent(txtChange);

        rightVg1.addComponent(lblTotalAmount);
        rightVg1.addComponent(txtTotalAmount);
        rightVg2.addComponent(lblDownPayment);
        rightVg2.addComponent(txtDownPayment);
        rightVg3.addComponent(lblCashTend);
        rightVg3.addComponent(txtCashTendered);
        rightVg4.addComponent(lblChange);
        rightVg4.addComponent(txtChange);

        GroupLayout.SequentialGroup rightHseq1 = rightLayout.createSequentialGroup();
        GroupLayout.SequentialGroup rightVseq1 = rightLayout.createSequentialGroup();

        rightHseq1.addGroup(rightHg1);
        rightHseq1.addGroup(rightHg2);
        rightVseq1.addGroup(rightVg1);
        rightVseq1.addGroup(rightVg2);
        rightVseq1.addGroup(rightVg3);
        rightVseq1.addGroup(rightVg4);

        rightLayout.setHorizontalGroup(rightHseq1);
        rightLayout.setVerticalGroup(rightVseq1);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);

        JPanel container = new JPanel(new BorderLayout());
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);//<<<<<<<<<<---------Add another panel for the Discount
        container.add(bottomPanel, BorderLayout.SOUTH);

        txtPurchaseNo.setEditable(false);
        btnAddItem.setEnabled(false);
        btnRemoveItem.setEnabled(false);
        btnSave.setEnabled(false);
        btnDownPayment.setEnabled(false);
        btnCashTend.setEnabled(false);
        btnDelete.setEnabled(false);
        
        btnCancel.setEnabled(false);
        txtCustomerNo.setEnabled(false);
        txtSenior.setEnabled(false);

        lblNoOfCustomer.setEnabled(false);
        lblNoOfSenior.setEnabled(false);

        rbNone.setEnabled(false);
        rbSenior.setEnabled(false);
        rbSiblings.setEnabled(false);

        btnDiscount.setEnabled(false);

        txtPurchaseNo.addKeyListener(new Keys());
        tableItems.addKeyListener(new Keys());

        alertThread = new AlertThread();
        alertButtonThread = new AlertButtonThread();
        _setTable();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                alertThread.start();
                alertButtonThread.start();
            }
        });
        setContentPane(container);
        setTitle("Eats Possible Purchase");
        setMinimumSize(new Dimension(1200, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void _setTable(){
        String[] tableHeader = {
            "Item", "Quantity"
        };
        tableLowModel = new DefaultTableModel();
        tableLowModel.setColumnIdentifiers(tableHeader);
        
        tableLowItems = new JTable(){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        tableLowItems.setModel(tableLowModel);
        
    }
    
    private class LowTablePanel extends JPanel{

        public LowTablePanel(){
            JScrollPane scrollPane = new JScrollPane(tableLowItems);
            
            setLayout(new BorderLayout());
            add(scrollPane, BorderLayout.CENTER);
        }
        
    }

    private class RadioButtonFunction implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (rbSenior.isSelected()) {
                lblNoOfCustomer.setEnabled(true);
                lblNoOfSenior.setEnabled(true);
                txtCustomerNo.setEnabled(true);
                txtSenior.setEnabled(true);
            } else if (rbSiblings.isSelected()) {
                lblNoOfCustomer.setEnabled(false);
                lblNoOfSenior.setEnabled(false);
                txtCustomerNo.setEnabled(false);
                txtSenior.setEnabled(false);
            } else {
                lblNoOfCustomer.setEnabled(false);
                lblNoOfSenior.setEnabled(false);
                txtCustomerNo.setEnabled(false);
                txtSenior.setEnabled(false);
            }

        }

    }

    private class ButtonFunctions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAddItem) {
                _addItem();
            } else if (e.getSource() == btnRemoveItem) {
                _removeItem();
            } else if (e.getSource() == btnNewTrans) {
                _newTrans();
            } else if (e.getSource() == btnSave) {
                _saveTrans();
            } else if (e.getSource() == btnDownPayment) {
                _downPayment();
            } else if (e.getSource() == btnCashTend) {
                _cashTend();
            } else if (e.getSource() == btnDelete) {
                _deletePurchase(Integer.parseInt(txtPurchaseNo.getText()));
            } /*else if (e.getSource() == btnPrint){
             _print();
             }*/ else if (e.getSource() == btnSearch) {
                _searchPurchase();
            } else if (e.getSource() == btnCancel) {
                _cancelTrans();
            } else if (e.getSource() == btnDiscount) {
                _goDiscount();
            } else if (e.getSource() == btnAlert) {
                JOptionPane.showMessageDialog(null, new LowTablePanel(), "Items", JOptionPane.WARNING_MESSAGE);
            }
        }

    }

    private void _print() {
        FormPurchaseReceipt receipt = new FormPurchaseReceipt();
        String[] itemName = new String[tableModel.getRowCount()];
        int[] itemOrder = new int[tableModel.getRowCount()];
        String[] itemSubTotal = new String[tableModel.getRowCount()];
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            itemName[i] = tableItems.getValueAt(i, 1).toString();
            itemOrder[i] = Integer.parseInt(tableItems.getValueAt(i, 3).toString());
            itemSubTotal[i] = tableItems.getValueAt(i, 4).toString();
        }
        String total = txtTotalAmount.getText();
        String No = txtPurchaseNo.getText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String downPayment = txtDownPayment.getText();
        String cashTendered = txtCashTendered.getText();
        String cashChange = txtChange.getText();
        String discount = null;
        if (rbNone.isSelected()) {
            discount = rbNone.getText();
        } else if (rbSenior.isSelected()) {
            discount = rbSenior.getText();
        } else if (rbSiblings.isSelected()) {
            discount = rbSiblings.getText();
        }

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(1, 1, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);
        printerJob.setPrintable(receipt, pageFormat);

        receipt._setValues(No, dateFormat.format(date), itemName, itemOrder, itemSubTotal,
                discount, total, downPayment, cashTendered, cashChange);
        printerJob.setPrintable(receipt);
        try {
            printerJob.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void _newTrans() {
        txtPurchaseNo.setEditable(true);

        btnAddItem.setEnabled(true);
        btnRemoveItem.setEnabled(true);
        btnSave.setEnabled(true);
        btnDownPayment.setEnabled(true);

        btnNewTrans.setEnabled(false);
        btnSearch.setEnabled(false);
        btnCancel.setEnabled(true);

        txtCustomerNo.setText("0");

        txtTotalAmount.setText("Php 0.00");
        txtDownPayment.setText("Php 0.00");
        txtCashTendered.setText("Php 0.00");
        txtChange.setText("Php 0.00");
    }

    private float _getDiscount() {
        float Discount = 0;
        float total = _getTotalAmount();

        if (rbSenior.isSelected()) {
            int NoOfCustomer = Integer.parseInt(txtCustomerNo.getText());
            if (NoOfCustomer == 0) {
                Discount = 0;
                return Discount;
            }
            Discount = (float) (total / NoOfCustomer);
            int NoOfSenior = Integer.parseInt(txtSenior.getText());
            Discount = Discount * (float) NoOfSenior;
            Discount = (float) (Discount * 0.20);
        } else if (rbSiblings.isSelected()) {
            Discount = (float) (total * 0.05);
        }

        return Discount;
    }

    private void _goDiscount() {
        float discount = _getDiscount();
        float total = _getTotalAmount();
        total -= discount;
        total = (float) (Math.round(total * 100.00) / 100.00);
        NumberFunction nFunction = new NumberFunction();
        txtTotalAmount.setText("Php " + nFunction._getFormattedNumber(Float.toString(total)));
        txtCashTendered.setText("Php 0.00");
        txtChange.setText("Php 0.00");
    }

    private void _saveTrans() {
        if (searched) {
            if (tableItems.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "CANNOT BE MODIFIED! \n\n Items not found", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                _modifyPurchase(Integer.parseInt(txtPurchaseNo.getText()));
                if (!txtCashTendered.getText().equals("Php 0.00") && !txtCashTendered.getText().equals("Php 0.0")) {
                    _updates();
                    _updateSales(Integer.parseInt(txtPurchaseNo.getText()));
                    _print();   //--------print the receipt-------------New Update 1/6/2016
                }
                //_print();     //--------print the receipt-------------Old Update 10/13/2015
                _loadPurchaseData(Integer.parseInt(txtPurchaseNo.getText()));
            }
        } else {
            if (txtPurchaseNo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Purchase No field is Required!", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                if (tableItems.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Items not found", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (_purchaseNoExist()) {
                        JOptionPane.showMessageDialog(null, "Purchase No. " + txtPurchaseNo.getText() + " already exist", "WARNING", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (_save()) {
                            _print();     //--------print the receipt-------------New Update 10/13/2015
                            txtPurchaseNo.setText("");
                            txtTotalAmount.setText("");
                            tableModel.setRowCount(0);
                        }
                    }
                }
            }
        }

    }

    private void _updates() {
        UpdateInventory updateInventory = new UpdateInventory();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int menuItemId = Integer.parseInt(tableItems.getValueAt(i, 0).toString());
            if (updateInventory._checkIngredient(menuItemId)) {
                String[] itemId = updateInventory._getIngredientId(menuItemId);
                for (int j = 0; j < itemId.length; j++) {
                    String temp = updateInventory._getIngredientUnit(menuItemId, Integer.parseInt(itemId[j]));
                    String unit;
                    String itemRemaining = updateInventory._getItemRemaining(Integer.parseInt(itemId[j]));
                    String remaining;
                    String word = "kg";
                    if (temp.contains(word)) {
                        temp = temp.replace(word, "");
                        float floatUnit = Float.parseFloat(temp);
                        float doubleOrder = Float.parseFloat(tableItems.getValueAt(i, 3).toString());
                        floatUnit *= doubleOrder;
                        unit = Double.toString(floatUnit);

                        itemRemaining = itemRemaining.replace(word, "");
                        float floatRemaining = Float.parseFloat(itemRemaining);
                        remaining = Float.toString(floatRemaining - Float.parseFloat(unit));
                        remaining = remaining + word;
                    } else {
                        int intUnit = Integer.parseInt(temp);
                        int intOrder = Integer.parseInt(tableItems.getValueAt(i, 3).toString());
                        intUnit *= intOrder;
                        unit = Integer.toString(intUnit);

                        int intRemaining = Integer.parseInt(itemRemaining);
                        remaining = Integer.toString(intRemaining - Integer.parseInt(unit));
                    }
                    updateInventory._goUpdate(Integer.parseInt(itemId[j]), remaining);
                }
            }
        }
    }

    private void _updateSales(int purchaseNo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        float cashTendered = _stripValue(txtCashTendered.getText());
        float change = _stripValue(txtChange.getText());

        try {
            Statement st = conn.createStatement();

            String discountId = _getDiscountId();
            String discountTypeId = null;
            if (rbNone.isSelected()) {
                discountTypeId = _getDiscountTypeId("none");
                txtCustomerNo.setText("0");
                txtSenior.setText("0");
            } else if (rbSenior.isSelected()) {
                discountTypeId = _getDiscountTypeId("Senior");
            } else if (rbSiblings.isSelected()) {
                discountTypeId = _getDiscountTypeId("sibling");
                txtCustomerNo.setText("0");
                txtSenior.setText("0");
            }

            st.executeUpdate("INSERT INTO tbl_discount (Discount_Id, DiscountType_Id,"
                    + " Discount_NoOfCustomer, Discount_NoOfSenior)"
                    + " VALUES(" + discountId + ", " + discountTypeId + ","
                    + " " + txtCustomerNo.getText() + "," + txtSenior.getText() + ")");

            st.executeUpdate("UPDATE tbl_sales SET Purchase_Date='" + dateFormat.format(date) + "',"
                    + " Purchase_CashTendered=" + cashTendered + ", Purchase_Change=" + change + ","
                    + " Discount_Id=" + discountId
                    + " WHERE Purchase_No=" + purchaseNo);
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

    private String _getDiscountTypeId(String type) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String id = null;

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT DiscountType_Id FROM tbl_discounttype"
                    + " WHERE DiscountType_Type='" + type + "'");
            if (rs.next()) {
                id = rs.getString("DiscountType_Id");
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

        return id;
    }

    private String _getDiscountId() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        String id = null;

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Max(Discount_Id) FROM tbl_discount");
            if (rs.next()) {
                id = rs.getString("Max(Discount_Id)");
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
        if (id == null) {
            id = "1";
        } else {
            int temp = Integer.parseInt(id);
            temp += 1;
            id = Integer.toString(temp);
        }
        return id;
    }

    private void _modifyPurchase(int purchaseNo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM tbl_salesitems WHERE Purchase_No=" + purchaseNo);

            for (int i = 0; i < tableItems.getRowCount(); i++) {
                int id = Integer.parseInt(tableItems.getValueAt(i, 0).toString());
                int order = Integer.parseInt(tableItems.getValueAt(i, 3).toString());
                float subtotal = _stripValue(tableItems.getValueAt(i, 4).toString());
                st.executeUpdate("INSERT INTO tbl_salesitems (Purchase_No, MenuItem_Id,"
                        + " Purchase_Order, Purchase_SubTotal)"
                        + " VALUES (" + purchaseNo + "," + id + "," + order + "," + subtotal + ")");
            }
            float totalAmount;
            if (!txtCashTendered.getText().equals("Php 0.00")) {
                totalAmount = _stripValue(txtTotalAmount.getText());
            } else {
                totalAmount = _getTotalAmount();
            }
            st.executeUpdate("UPDATE tbl_sales SET Purchase_Total=" + totalAmount + ","
                    + " Purchase_DownPayment=" + _stripValue(txtDownPayment.getText())
                    + " WHERE Purchase_No=" + purchaseNo);
            JOptionPane.showMessageDialog(null, "Purchase No. " + purchaseNo + " \n\n was successfully modified", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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

    private boolean _save() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO tbl_sales (Purchase_No, Purchase_Total, Purchase_DownPayment)"
                    + " VALUES (" + txtPurchaseNo.getText() + "," + _getTotalAmount() + "," + _stripValue(txtDownPayment.getText()) + ")");
            for (int i = 0; i < tableItems.getRowCount(); i++) {
                int id = Integer.parseInt(tableItems.getValueAt(i, 0).toString());
                int order = Integer.parseInt(tableItems.getValueAt(i, 3).toString());
                double subTotal = _stripValue(tableItems.getValueAt(i, 4).toString());
                st.executeUpdate("INSERT INTO tbl_salesitems (Purchase_No, MenuItem_Id, Purchase_Order, Purchase_SubTotal)"
                        + " VALUES (" + txtPurchaseNo.getText().trim() + "," + id + "," + order + "," + subTotal + ")");
            }
            JOptionPane.showMessageDialog(null, "Purchase No. " + txtPurchaseNo.getText().trim() + " was succesfully saved", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(FormPurchase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(FormPurchase.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }
        return true;
    }

    private boolean _purchaseNoExist() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Purchase_No FROM tbl_sales"
                    + " WHERE Purchase_No='" + txtPurchaseNo.getText().trim() + "'");
            if (rs.next()) {
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

    private void _deletePurchase(int purchaseNo) {
        if (JOptionPane.showConfirmDialog(null, "Are you sure?", "DELETE", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            DatabaseConnection dbConn = new DatabaseConnection();
            Connection conn = dbConn._getConnection();

            try {
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM tbl_salesitems WHERE Purchase_No=" + purchaseNo);
                st.executeUpdate("DELETE FROM tbl_sales WHERE Purchase_No=" + purchaseNo);
                JOptionPane.showMessageDialog(null, "Purchase No: " + purchaseNo + " has been DELETED.", "DELETED", JOptionPane.INFORMATION_MESSAGE);
                _cancelTrans();
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

    }

    private void _downPayment() {
        SubFormDownPayment subFormDownPayment = new SubFormDownPayment();
        URL img = getClass().getResource("Images/Icon Down Payment.png");
        Icon cashTendIcon = new ImageIcon(img);
        if (JOptionPane.showConfirmDialog(null, subFormDownPayment, "Cash Tend", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, cashTendIcon) == JOptionPane.OK_OPTION) {
            try {
                float cash = _stripValue(txtCashTendered.getText());
                float down = _stripValue(subFormDownPayment._getValue());
                float total = _stripValue(txtTotalAmount.getText());
                if (down > total) {
                    JOptionPane.showMessageDialog(null, "INVALID INPUT", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    NumberFunction nFunction = new NumberFunction();
                    txtDownPayment.setText("Php " + nFunction._getFormattedNumber(Float.toString(down)));
                    _calculate(cash, down, total);

                    /*txtCashTendered.setText(subFormDownPayment._getValue());
                     float change = downPayment - total;
                     change = (float) (Math.round(change * 100.00) / 100.00);
                     txtChange.setText("Php " + nFunction._getFormattedNumber(Float.toString(change)));*/
                }
            } catch (NumberFormatException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, "WRONG INPUT! \n\n Numbers only.", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void _cashTend() {
        SubFormCashTend formCashTend = new SubFormCashTend();
        URL img = getClass().getResource("Images/Icon Cash Tend.png");
        Icon cashTendIcon = new ImageIcon(img);
        if (JOptionPane.showConfirmDialog(null, formCashTend, "Cash Tend", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, cashTendIcon) == JOptionPane.OK_OPTION) {
            try {
                float cash = _stripValue(formCashTend._getValue());
                float down = _stripValue(txtDownPayment.getText());     // NEW
                float total = _stripValue(txtTotalAmount.getText());
                if (cash < total) {
                    JOptionPane.showMessageDialog(null, "INVALID INPUT \n\n Input must be greater or equals to the TOTAL AMOUNT.", "WARNING", JOptionPane.WARNING_MESSAGE);
                } else if (cash >= total) {
                    txtCashTendered.setText(formCashTend._getValue());
                    _calculate(cash, down, total);

                    /*float change = (cash + down) - total; // NEW ======= down
                     change = (float) (Math.round(change * 100.00) / 100.00);
                     txtChange.setText("Php " + nFunction._getFormattedNumber(Float.toString(change)));*/
                }
            } catch (NumberFormatException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, "WRONG INPUT! \n\n Numbers only.", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void _calculate(float cash, float down, float total) {
        NumberFunction nFunction = new NumberFunction();
        float change = (cash + down) - total; // NEW ======= down
        change = (float) (Math.round(change * 100.00) / 100.00);
        txtChange.setText("Php " + nFunction._getFormattedNumber(Float.toString(change)));
    }

    private void _searchPurchase() {
        SubFormSearchPurchase searchPurchase = new SubFormSearchPurchase();
        URL img = getClass().getResource("Images/Icon Search.png");
        Icon searchIcon = new ImageIcon(img);
        if (JOptionPane.showConfirmDialog(null, searchPurchase, "SEARCH", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, searchIcon) == JOptionPane.OK_OPTION) {
            int purchaseNo = searchPurchase._getPurchaseNo();
            CheckPurchase checkPurchase = new CheckPurchase();
            if (checkPurchase._ifPurchaseExist(purchaseNo)) {
                _loadPurchaseData(purchaseNo);

                //btnPrint.setEnabled(true);
                btnCancel.setEnabled(true);

                btnNewTrans.setEnabled(false);
                btnSearch.setEnabled(false);

                searched = true;
            }
        }

    }

    private void _loadPurchaseData(int purchaseNo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();
        tableModel.setRowCount(0);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_salesitems.MenuItem_Id, tbl_menuitems.MenuItem_Name,"
                    + " ROUND(tbl_menuitems.MenuItem_Price, 2), tbl_salesitems.Purchase_Order, "
                    + " ROUND(tbl_salesitems.Purchase_SubTotal, 2)"
                    + " FROM tbl_salesitems INNER JOIN tbl_menuitems"
                    + " ON tbl_salesitems.MenuItem_Id=tbl_menuitems.MenuItem_Id"
                    + " WHERE tbl_salesitems.Purchase_No=" + purchaseNo);
            StringFunction sFunction = new StringFunction();
            NumberFunction nFunction = new NumberFunction();
            String currency = "Php ";
            while (rs.next()) {
                int itemId = rs.getInt("tbl_salesitems.MenuItem_Id");
                String itemName = sFunction._Capitalized(rs.getString("tbl_menuitems.MenuItem_Name"));
                String itemPrice = currency + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_menuitems.MenuItem_Price, 2)"));
                String itemOrder = rs.getString("tbl_salesitems.Purchase_Order");
                String itemSubTotal = currency + nFunction._getFormattedNumber(rs.getString("ROUND(tbl_salesitems.Purchase_SubTotal, 2)"));
                tableModel.addRow(new Object[]{itemId, itemName, itemPrice, itemOrder, itemSubTotal});
            }
            ResultSet rs1 = st.executeQuery("SELECT Purchase_No, ROUND(Purchase_Total, 2),"
                    + " ROUND(Purchase_DownPayment, 2), ROUND(Purchase_CashTendered, 2),"
                    + " ROUND(Purchase_Change, 2) FROM tbl_sales"
                    + " WHERE Purchase_No=" + purchaseNo);
            if (rs1.next()) {
                txtPurchaseNo.setText(rs1.getString("Purchase_No"));
                txtTotalAmount.setText(currency + nFunction._getFormattedNumber(rs1.getString("ROUND(Purchase_Total, 2)")));
                txtDownPayment.setText(currency + nFunction._getFormattedNumber(rs1.getString("ROUND(Purchase_DownPayment, 2)")));
                txtCashTendered.setText(currency + nFunction._getFormattedNumber(rs1.getString("ROUND(Purchase_CashTendered, 2)")));
                txtChange.setText(currency + nFunction._getFormattedNumber(rs1.getString("ROUND(Purchase_Change, 2)")));
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

        if (txtCashTendered.getText().equals("Php 0.00") && txtChange.getText().equals("Php 0.00")) {
            btnAddItem.setEnabled(true);
            btnRemoveItem.setEnabled(true);
            btnCashTend.setEnabled(true);
            btnSave.setEnabled(true);
            btnDelete.setEnabled(true);

            rbNone.setEnabled(true);
            rbSenior.setEnabled(true);
            rbSiblings.setEnabled(true);
            btnDiscount.setEnabled(true);

            rbNone.setSelected(true);

        } else {
            btnAddItem.setEnabled(false);
            btnRemoveItem.setEnabled(false);
            btnCashTend.setEnabled(false);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);

            _discountIdExists(purchaseNo);

            lblNoOfCustomer.setEnabled(false);
            lblNoOfSenior.setEnabled(false);
            txtCustomerNo.setEnabled(false);
            txtSenior.setEnabled(false);
            rbNone.setEnabled(false);
            rbSenior.setEnabled(false);
            rbSiblings.setEnabled(false);
            btnDiscount.setEnabled(false);
        }

        if (txtDownPayment.getText().equals("Php 0.00") && txtCashTendered.getText().equals("Php 0.00")) {
            btnDownPayment.setEnabled(true);
        } else {
            btnDownPayment.setEnabled(false);
        }
    }

    private void _discountIdExists(int purchaseNo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_discounttype.DiscountType_Type"
                    + " FROM (tbl_discounttype INNER JOIN tbl_discount"
                    + " ON tbl_discounttype.DiscountType_Id=tbl_discount.DiscountType_Id)"
                    + " INNER JOIN tbl_sales ON tbl_discount.Discount_Id=tbl_sales.Discount_Id"
                    + " WHERE tbl_sales.Purchase_No=" + purchaseNo);
            if (rs.next()) {
                switch (rs.getString("tbl_discounttype.DiscountType_Type")) {
                    case "none":
                        rbNone.setSelected(true);
                        break;
                    case "sibling":
                        rbSiblings.setSelected(true);
                        break;
                    case "senior":
                        rbSenior.setSelected(true);
                        _getDataForSenior(purchaseNo);
                        break;
                }
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

    private void _getDataForSenior(int purchaseNo) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn._getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT tbl_discount.Discount_NoOfCustomer,"
                    + " tbl_discount.Discount_NoOfSenior"
                    + " FROM tbl_discount INNER JOIN tbl_sales ON tbl_discount.Discount_Id=tbl_sales.Discount_Id"
                    + " WHERE tbl_sales.Purchase_No=" + purchaseNo);
            if (rs.next()) {
                txtCustomerNo.setText(rs.getString("tbl_discount.Discount_NoOfCustomer"));
                txtSenior.setText(rs.getString("tbl_discount.Discount_NoOfSenior"));
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

    private void _addItem() {
        SubFormMenu formMenu = new SubFormMenu();
        formMenu._loadTableData(); //load data from the database
        boolean opt = true;
        while (opt) {
            if (JOptionPane.showConfirmDialog(null, formMenu, "Menu", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                String[] value = formMenu._getValues();
                if (value[0] != null) {
                    if (tableItems.getRowCount() == 0) {
                        tableModel.addRow(new Object[]{value[0], value[1], value[2], value[3], value[4]});
                    } else {
                        CheckItem checkItem = new CheckItem();
                        if (checkItem._tableItemExist(value[0])) {
                            int i = checkItem._getIndex();
                            int order = Integer.parseInt(tableItems.getValueAt(i, 3).toString());
                            float subTotal = _stripValue(tableItems.getValueAt(i, 4).toString());
                            order += Integer.parseInt(value[3]);
                            subTotal += _stripValue(value[4]);
                            NumberFunction nFunction = new NumberFunction();
                            String subTotal1 = "Php " + nFunction._getFormattedNumber(Float.toString(subTotal));
                            tableModel.setValueAt(order, i, 3);
                            tableModel.setValueAt(subTotal1, i, 4);
                        } else {
                            tableModel.addRow(new Object[]{value[0], value[1], value[2], value[3], value[4]});
                        }
                    }
                    _goDiscount();
                }

            } else {
                opt = false;
            }
        }

    }

    private float _getTotalAmount() {
        float totalAmount = 0;
        for (int i = 0; i < tableItems.getRowCount(); i++) {
            float temp = _stripValue(tableItems.getValueAt(i, 4).toString());
            totalAmount += temp;
        }
        return totalAmount;
    }

    private float _stripValue(String stringToStrip) {
        float strippedValue;
        String word = "Php";
        if (stringToStrip.contains(word)) {
            stringToStrip = stringToStrip.replace(word, "");
        }
        word = ",";
        if (stringToStrip.contains(word)) {
            stringToStrip = stringToStrip.replace(word, "");
        }
        strippedValue = Float.parseFloat(stringToStrip);
        return strippedValue;
    }

    private class CheckItem {

        int index;

        private boolean _tableItemExist(String id) {
            for (int i = 0; i < tableItems.getRowCount(); i++) {
                String itemId = tableItems.getValueAt(i, 0).toString();
                if (itemId.equals(id)) {
                    this.index = i;
                    return true;
                }
            }
            return false;
        }

        private int _getIndex() {
            return this.index;
        }
    }

    private void _removeItem() {
        try {
            tableModel.removeRow(tableItems.getSelectedRow());
            _goDiscount();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please select an item", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void _cancelTrans() {
        btnNewTrans.setEnabled(true);
        btnSearch.setEnabled(true);

        txtPurchaseNo.setEditable(false);
        btnAddItem.setEnabled(false);
        btnRemoveItem.setEnabled(false);
        btnSave.setEnabled(false);
        btnDownPayment.setEnabled(false);
        btnCashTend.setEnabled(false);
        btnDelete.setEnabled(false);
        //btnPrint.setEnabled(false);
        btnCancel.setEnabled(false);

        txtCustomerNo.setEnabled(false);
        txtSenior.setEnabled(false);
        rbNone.setEnabled(false);
        rbSenior.setEnabled(false);
        rbSiblings.setEnabled(false);
        btnDiscount.setEnabled(false);

        radioButtonGroup.clearSelection();

        rbNone.setSelected(true);
        tableModel.setRowCount(0);
        txtPurchaseNo.setText("");
        //txtTotalAmount.setText("");
        //txtCashTendered.setText("");
        //txtChange.setText("");

        searched = false;

        txtCustomerNo.setText("0");

        txtTotalAmount.setText("Php 0.00");
        txtCashTendered.setText("Php 0.00");
        txtDownPayment.setText("Php 0.00");
        txtChange.setText("Php 0.00");

    }

    private class Keys implements KeyListener {

        TextFieldFilter textFieldFilter = new TextFieldFilter();

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getSource() == txtPurchaseNo) {
                textFieldFilter._numbersOnly(e);
            } else if (e.getSource() == txtCustomerNo) {
                textFieldFilter._numbersOnly(e);
            } else if (e.getSource() == txtSenior) {
                textFieldFilter._numbersOnly(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getSource() == txtPurchaseNo) {
                _keyFunction(e);
            } else if (e.getSource() == tableItems) {
                _keyFunction(e);
            } else if (e.getSource() == rbSenior) {
                _keyFunction(e);
            } else if (e.getSource() == rbSiblings) {
                _keyFunction(e);
            } else if (e.getSource() == txtCustomerNo) {
                _keyFunction(e);
            } else if (e.getSource() == txtSenior) {
                _keyFunction(e);
            }
        }

        private void _keyFunction(KeyEvent e) {
            if (e.getKeyCode() == 112) {
                if (btnNewTrans.isEnabled()) {
                    _newTrans();
                }
            } else if (e.getKeyCode() == 113) {
                if (btnSave.isEnabled()) {
                    _saveTrans();
                }
            } else if (e.getKeyCode() == 114) {
                if (btnDownPayment.isEnabled()) {
                    _downPayment();
                }
            } else if (e.getKeyCode() == 115) {
                if (btnCashTend.isEnabled()) {
                    _cashTend();
                }
            } else if (e.getKeyCode() == 116) {
                if (btnDelete.isEnabled()) {
                    _deletePurchase(Integer.parseInt(txtPurchaseNo.getText()));
                }
            } else if (e.getKeyCode() == 117) {
                if (btnSearch.isEnabled()) {
                    _searchPurchase();
                }
            } else if (e.getKeyCode() == 118) {
                if (btnCancel.isEnabled()) {
                    _cancelTrans();
                }
            } else if (e.getKeyCode() == 122) {
                if (btnAddItem.isEnabled()) {
                    _addItem();
                }
            } else if (e.getKeyCode() == 123) {
                if (btnRemoveItem.isEnabled()) {
                    _removeItem();
                }
            }
        }

    }

    private class FocusFunction implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource() == txtCustomerNo) {
                if (txtCustomerNo.getText().equals("")) {
                    txtCustomerNo.setText("0");
                }
            } else if (e.getSource() == txtSenior) {
                if (txtSenior.getText().equals("")) {
                    txtSenior.setText("0");
                }
            }
        }

    }

    private class AlertThread extends Thread {

        @Override
        public void run() {
            while (true) {
                

                try {
                    AlertThread.sleep(100);
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Thread ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private class AlertButtonThread extends Thread {

        @Override
        public void run() {
            ColorUIResource colorUi = new ColorUIResource(238, 238, 238);
            while (true) {
                if (alertButton) {

                    if (btnAlert.getBackground().getRGB() == colorUi.getRGB()) {
                        btnAlert.setBackground(Color.RED);
                    } else {
                        btnAlert.setBackground(colorUi);
                    }
                }

                try {
                    AlertButtonThread.sleep(400);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(FormPurchase.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Thread ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

}
