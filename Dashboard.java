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
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author IT-NEW
 */
public class Dashboard extends JFrame {
    
    /*-----MDIForm-----*/
    JDesktopPane mdiForm = new JDesktopPane();
    
    /*-------Menu Bar for MDIForm----------*/
    JMenuBar menuBar = new JMenuBar();
    
    /*-------Menu----------------*/
    JMenu userMenu = new JMenu("User");
    JMenu inventoryMenu = new JMenu("Inventory");
    JMenu salesMenu = new JMenu("Sales");
    JMenu eatsMenu = new JMenu("Menu");
    JMenu expensesMenu = new JMenu("Expenses");
    JMenu disbursementMenu = new JMenu("Disbursement");
    JMenu payrollMenu = new JMenu("Payroll");
    JMenu stockMenu = new JMenu("Stocks");
    
    JMenu purchasesMenu = new JMenu("Purchases");
    JMenu vouchersMenu = new JMenu("Vouchers");
    
    JMenu employeesMenu = new JMenu("Employee");
    JMenu attendanceMenu = new JMenu("Attendance");
    JMenu deductionMenu = new JMenu("Deduction");
    JMenu cashAdvanceMenu = new JMenu("Cash Advances");
    JMenu summaryMenu = new JMenu("Summary");
    
    JMenu voucherReportsMenu = new JMenu("Voucher Reports");
    
    /*-------Menu Items---------------*/
    JMenuItem addUserItem = new JMenuItem("Add New User");
    JMenuItem viewDeleteUserItem = new JMenuItem("View User");
    
    JMenuItem addInventoryItem = new JMenuItem("Add New Inventory Item");
    JMenuItem viewInventoryItem = new JMenuItem("View Inventory Items");
    JMenuItem viewDeletedInventoryItem = new JMenuItem("View Deleted Items");
    
    JMenuItem addEatsMenu = new JMenuItem("Add Menu Item");
    JMenuItem viewMenu = new JMenuItem("View Menu");
    JMenuItem editMenu = new JMenuItem("Edit Menu Item");
    JMenuItem viewDeletedMenu = new JMenuItem("View Deleted Menu Items");
    
    JMenuItem viewSalesItem = new JMenuItem("View Sales");
    
    JMenuItem addPurchaseItem = new JMenuItem("Add Purchase Item");
    JMenuItem addPurchaseStore = new JMenuItem("Add Store");
    JMenuItem viewPurchaseItem = new JMenuItem("View Purchase Items");
    JMenuItem createPurchaseList = new JMenuItem("Create Purchase");
    JMenuItem editPurchase = new JMenuItem("Edit Purchases");
    
    JMenuItem addVoucherItem = new JMenuItem("Add Voucher Item");
    JMenuItem viewVoucherItem = new JMenuItem("View Voucher Item");
    JMenuItem createVoucher = new JMenuItem("Create Voucher");
    JMenuItem editVoucher = new JMenuItem("Edit Voucher");
    
    JMenuItem viewDisbursement = new JMenuItem("View Disbursement");
    
    JMenuItem addEmployee = new JMenuItem("Add");
    JMenuItem viewEmployee = new JMenuItem("View");
    
    JMenuItem loginEmployee = new JMenuItem("Login");
    JMenuItem logoutEmployee = new JMenuItem("Logout");
    
    JMenuItem viewAttendance = new JMenuItem("View");
    
    JMenuItem addDeduction = new JMenuItem("Add");
    JMenuItem viewDeduction = new JMenuItem("View");
    
    JMenuItem viewCashAdvanceByEmployee = new JMenuItem("View: Break Down");
    JMenuItem viewCashAdvancePaymentByEmployee = new JMenuItem("Payment");
    
    JMenuItem viewSummaryAll = new JMenuItem("All");
    
    JMenuItem viewVoucherReportsByMonth = new JMenuItem("By Month");
    
    JMenuItem viewViewStocks = new JMenuItem("View: List of Stocks");
    JMenuItem viewViewStockRecord = new JMenuItem("View: Stock Record");
    JMenuItem stockInStockOut = new JMenuItem("Stock In/Stock Out");
    
    /*-------Internal Frames--------*/
    FormAddUser formAddUser = new FormAddUser();
    FormViewUser formViewUser = new FormViewUser();
    
    FormAddItems formAddItems = new FormAddItems();
    FormViewItems formViewItems = new FormViewItems();
    FormViewDeletedItems formViewDeletedItems = new FormViewDeletedItems();
    
    FormAddMenuItem formAddMenu = new FormAddMenuItem();
    FormViewMenu formViewMenu = new FormViewMenu();
    FormEditMenu formEditMenu = new FormEditMenu();
    FormViewDeletedMenu formViewDeleteMenu = new FormViewDeletedMenu();
    
    FormViewSales formViewSales = new FormViewSales();
    
    FormAddPurchaseItem formAddPurchaseItem = new FormAddPurchaseItem();
    FormAddPurchaseStore formAddPurchaseStore = new FormAddPurchaseStore();
    FormViewPurchaseItems formViewPurchaseItems = new FormViewPurchaseItems();
    FormCreatePurchase formCreatePurchaseList = new FormCreatePurchase();
    FormEditPurchase formEditPurchase = new FormEditPurchase();
    
    FormAddVoucherItem formAddVoucherItem = new FormAddVoucherItem();
    FormViewVoucherItem formViewVoucherItem = new FormViewVoucherItem();
    FormCreateVoucher formCreateVoucher = new FormCreateVoucher();
    FormEditVoucher formEditVoucher = new FormEditVoucher();
    FormViewVoucherReportByMonth formViewVoucherReportByMonth = new FormViewVoucherReportByMonth();
    
    FormViewDisbursement formViewDisbursementByDate = new FormViewDisbursement();
    
    FormAddEmployee formAddEmployee = new FormAddEmployee();
    FormViewEmployee formViewEmployee = new FormViewEmployee();
    
    FormLoginEmployee formLoginEmployee = new FormLoginEmployee();
    FormLogoutEmployee formLogoutEmployee = new FormLogoutEmployee();
    
    FormAddDeduction formAddDeduction = new FormAddDeduction();
    FormViewDeduction formViewDeduction = new FormViewDeduction();
    
    FormViewCashAdvanceByEmployee formViewCashAdvanceByEmployee  = new FormViewCashAdvanceByEmployee();
    FormViewPaymentByEmployee formViewPaymentByEmployee = new FormViewPaymentByEmployee();
    
    FormViewSummaryAll formViewSummaryAll = new FormViewSummaryAll();
    FormViewAttendance formViewSummaryByEmployee = new FormViewAttendance();
    
    FormViewStock formViewStock = new FormViewStock();
    FormViewStockRecord formViewStockRecord = new FormViewStockRecord();
    FormStockInStockOut formStockInStockOut = new FormStockInStockOut();
    
    DesktopManager manager;
    
    private final JLabel lblUserType;
    
    public Dashboard(String UserType){
        //InternalFrameLocation frameLocationThread = new InternalFrameLocation();
        //frameLocationThread.start();
        
        /*------------Menus----------------*/
        userMenu.add(addUserItem);
        userMenu.add(viewDeleteUserItem);
        
//        inventoryMenu.add(addInventoryItem);
//        inventoryMenu.add(viewInventoryItem);
//        inventoryMenu.add(viewDeletedInventoryItem);
        
        eatsMenu.add(addEatsMenu);
        eatsMenu.add(viewMenu);
        eatsMenu.add(editMenu);
        eatsMenu.add(viewDeletedMenu);
        
        salesMenu.add(viewSalesItem);
        
        expensesMenu.add(purchasesMenu);
        
        purchasesMenu.add(addPurchaseStore);
        purchasesMenu.add(addPurchaseItem);
        purchasesMenu.add(viewPurchaseItem);
        purchasesMenu.add(createPurchaseList);
        purchasesMenu.add(editPurchase);
            
        expensesMenu.add(vouchersMenu);
        
        vouchersMenu.add(addVoucherItem);
        vouchersMenu.add(viewVoucherItem);
        vouchersMenu.add(createVoucher);
        vouchersMenu.add(editVoucher);
        vouchersMenu.add(voucherReportsMenu);
        
        voucherReportsMenu.add(viewVoucherReportsByMonth);
        
        disbursementMenu.add(viewDisbursement);
        
        payrollMenu.add(employeesMenu);
        
        employeesMenu.add(addEmployee);
        employeesMenu.add(viewEmployee);
        
        payrollMenu.add(attendanceMenu);
        
        attendanceMenu.add(loginEmployee);
        attendanceMenu.add(logoutEmployee);
        attendanceMenu.add(viewAttendance);
        
        payrollMenu.add(deductionMenu);
        
        deductionMenu.add(addDeduction);
        deductionMenu.add(viewDeduction);
        
        payrollMenu.add(cashAdvanceMenu);
        
        cashAdvanceMenu.add(viewCashAdvanceByEmployee);
        cashAdvanceMenu.add(viewCashAdvancePaymentByEmployee);
        
        payrollMenu.add(summaryMenu);
        
        stockMenu.add(viewViewStocks);
        stockMenu.add(viewViewStockRecord);
        stockMenu.add(stockInStockOut);
        
        summaryMenu.add(viewSummaryAll);
        /*------------Menus----------------*/
        
        /*------------MenuItems----------------*/
        addUserItem.setMnemonic(KeyEvent.VK_A);
        addUserItem.addActionListener(new MenuItemFunctions());
        viewDeleteUserItem.setMnemonic(KeyEvent.VK_V);
        viewDeleteUserItem.addActionListener(new MenuItemFunctions());
        
        addInventoryItem.setMnemonic(KeyEvent.VK_A);
        addInventoryItem.addActionListener(new MenuItemFunctions());
        viewInventoryItem.setMnemonic(KeyEvent.VK_V);
        viewInventoryItem.addActionListener(new MenuItemFunctions());
        viewDeletedInventoryItem.setMnemonic(KeyEvent.VK_D);
        viewDeletedInventoryItem.addActionListener(new MenuItemFunctions());
        
        addEatsMenu.setMnemonic(KeyEvent.VK_A);
        addEatsMenu.addActionListener(new MenuItemFunctions());
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.addActionListener(new MenuItemFunctions());
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.addActionListener(new MenuItemFunctions());
        viewDeletedMenu.setMnemonic(KeyEvent.VK_D);
        viewDeletedMenu.addActionListener(new MenuItemFunctions());
        
        viewSalesItem.setMnemonic(KeyEvent.VK_S);
        viewSalesItem.addActionListener(new MenuItemFunctions());
        
        addPurchaseItem.setMnemonic(KeyEvent.VK_A);
        addPurchaseItem.addActionListener(new MenuItemFunctions());
        addPurchaseStore.setMnemonic(KeyEvent.VK_S);
        addPurchaseStore.addActionListener(new MenuItemFunctions());
        viewPurchaseItem.setMnemonic(KeyEvent.VK_V);
        viewPurchaseItem.addActionListener(new MenuItemFunctions());
        createPurchaseList.setMnemonic(KeyEvent.VK_C);
        createPurchaseList.addActionListener(new MenuItemFunctions());
        editPurchase.setMnemonic(KeyEvent.VK_E);
        editPurchase.addActionListener(new MenuItemFunctions());
        
        addVoucherItem.setMnemonic(KeyEvent.VK_A);
        addVoucherItem.addActionListener(new MenuItemFunctions());
        viewVoucherItem.setMnemonic(KeyEvent.VK_V);
        viewVoucherItem.addActionListener(new MenuItemFunctions());
        createVoucher.setMnemonic(KeyEvent.VK_C);
        createVoucher.addActionListener(new MenuItemFunctions());
        editVoucher.setMnemonic(KeyEvent.VK_E);
        editVoucher.addActionListener(new MenuItemFunctions());
        
        viewDisbursement.setMnemonic(KeyEvent.VK_D);
        viewDisbursement.addActionListener(new MenuItemFunctions());
        
        addEmployee.setMnemonic(KeyEvent.VK_A);
        addEmployee.addActionListener(new MenuItemFunctions());
        viewEmployee.setMnemonic(KeyEvent.VK_V);
        viewEmployee.addActionListener(new MenuItemFunctions());
        
        loginEmployee.setMnemonic(KeyEvent.VK_I);
        loginEmployee.addActionListener(new MenuItemFunctions());
        logoutEmployee.setMnemonic(KeyEvent.VK_O);
        logoutEmployee.addActionListener(new MenuItemFunctions());
        viewAttendance.setMnemonic(KeyEvent.VK_V);
        viewAttendance.addActionListener(new MenuItemFunctions());
        
        addDeduction.setMnemonic(KeyEvent.VK_A);
        addDeduction.addActionListener(new MenuItemFunctions());
        viewDeduction.setMnemonic(KeyEvent.VK_V);
        viewDeduction.addActionListener(new MenuItemFunctions());
        
        viewCashAdvanceByEmployee.setMnemonic(KeyEvent.VK_V);
        viewCashAdvanceByEmployee.addActionListener(new MenuItemFunctions());
        
        viewCashAdvancePaymentByEmployee.setMnemonic(KeyEvent.VK_P);
        viewCashAdvancePaymentByEmployee.addActionListener(new MenuItemFunctions());
        
        viewSummaryAll.setMnemonic(KeyEvent.VK_A);
        viewSummaryAll.addActionListener(new MenuItemFunctions());
        
        viewVoucherReportsByMonth.setMnemonic(KeyEvent.VK_M);
        viewVoucherReportsByMonth.addActionListener(new MenuItemFunctions());
        
        viewViewStocks.setMnemonic(KeyEvent.VK_V);
        viewViewStocks.addActionListener(new MenuItemFunctions());
        
        viewViewStockRecord.setMnemonic(KeyEvent.VK_R);
        viewViewStockRecord.addActionListener(new MenuItemFunctions());
        
        stockInStockOut.setMnemonic(KeyEvent.VK_S);
        stockInStockOut.addActionListener(new MenuItemFunctions());
        /*------------MenuItems----------------*/
        
        userMenu.setMnemonic(KeyEvent.VK_U);
//        inventoryMenu.setMnemonic(KeyEvent.VK_I);
        eatsMenu.setMnemonic(KeyEvent.VK_M);
        salesMenu.setMnemonic(KeyEvent.VK_S);
        expensesMenu.setMnemonic(KeyEvent.VK_E);
        disbursementMenu.setMnemonic(KeyEvent.VK_D);
        payrollMenu.setMnemonic(KeyEvent.VK_P);
        stockMenu.setMnemonic(KeyEvent.VK_T);
        
        purchasesMenu.setMnemonic(KeyEvent.VK_P);
        vouchersMenu.setMnemonic(KeyEvent.VK_V);
        
        employeesMenu.setMnemonic(KeyEvent.VK_E);
        attendanceMenu.setMnemonic(KeyEvent.VK_A);
        deductionMenu.setMnemonic(KeyEvent.VK_D);
        cashAdvanceMenu.setMnemonic(KeyEvent.VK_C);
        summaryMenu.setMnemonic(KeyEvent.VK_S);
        
        voucherReportsMenu.setMnemonic(KeyEvent.VK_R);
        
        menuBar.add(userMenu);
//        menuBar.add(inventoryMenu);
        menuBar.add(eatsMenu);
        menuBar.add(salesMenu);
        menuBar.add(expensesMenu);
        menuBar.add(disbursementMenu);
        menuBar.add(payrollMenu);
        menuBar.add(stockMenu);
        
        manager = new DefaultDesktopManager();
        mdiForm.setDesktopManager(manager);
        
        mdiForm.add(formAddUser);
        mdiForm.add(formViewUser);
        mdiForm.add(formAddItems);
        mdiForm.add(formViewItems);
        mdiForm.add(formViewDeletedItems);
        mdiForm.add(formAddMenu);
        mdiForm.add(formViewMenu);
        mdiForm.add(formEditMenu);
        mdiForm.add(formViewDeleteMenu);
        mdiForm.add(formViewSales);
        mdiForm.add(formAddPurchaseItem);
        mdiForm.add(formAddPurchaseStore);
        mdiForm.add(formViewPurchaseItems);
        mdiForm.add(formCreatePurchaseList);
        mdiForm.add(formEditPurchase);
        mdiForm.add(formAddVoucherItem);
        mdiForm.add(formViewVoucherItem);
        mdiForm.add(formCreateVoucher);
        mdiForm.add(formEditVoucher);
        mdiForm.add(formViewVoucherReportByMonth);
        mdiForm.add(formViewDisbursementByDate);
        mdiForm.add(formAddEmployee);
        mdiForm.add(formViewEmployee);
        mdiForm.add(formLoginEmployee);
        mdiForm.add(formLogoutEmployee);
        mdiForm.add(formAddDeduction);
        mdiForm.add(formViewDeduction);
        mdiForm.add(formViewSummaryAll);
        mdiForm.add(formViewSummaryByEmployee);
        mdiForm.add(formViewCashAdvanceByEmployee);
        mdiForm.add(formViewPaymentByEmployee);
        mdiForm.add(formViewStock);
        mdiForm.add(formViewStockRecord);
        mdiForm.add(formStockInStockOut);
        mdiForm.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        
        lblUserType = new JLabel(UserType);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(mdiForm, BorderLayout.CENTER);
        container.add(lblUserType, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened(WindowEvent e) {
                _disableComponents();
            }
        });
        
        setContentPane(container);
        setJMenuBar(menuBar);
        setTitle("Dashboard");
        setMinimumSize(new Dimension(1024, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    public void _disableComponents(){
        if(!lblUserType.getText().equals("Admin")){
            userMenu.setEnabled(false);
//            inventoryMenu.setEnabled(false);
            salesMenu.setEnabled(false);
            eatsMenu.setEnabled(false);
            expensesMenu.setEnabled(false);
            disbursementMenu.setEnabled(false);
            
            employeesMenu.setEnabled(false);
            deductionMenu.setEnabled(false);
            summaryMenu.setEnabled(false);
            
            addEmployee.setEnabled(false);
            viewEmployee.setEnabled(false);
            
            formViewSummaryByEmployee.btnAdd.setEnabled(false);
            formViewSummaryByEmployee.btnDelete.setEnabled(false);
            formViewSummaryByEmployee.btnEdit.setEnabled(false);
        }
    }
    
    public class MenuItemFunctions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addUserItem){
                formAddUser.setVisible(true);
                if (formAddUser.isIcon()){
                    manager.deiconifyFrame(formAddUser);
                }
                manager.activateFrame(formAddUser);
                
            } else if (e.getSource() == viewDeleteUserItem){
                formViewUser.setVisible(true);
                if (formViewUser.isIcon()){
                    manager.deiconifyFrame(formViewUser);
                }
                manager.activateFrame(formViewUser);
                
            } else if (e.getSource() == addInventoryItem){
                formAddItems.setVisible(true);
                if (formAddItems.isIcon()){
                    manager.deiconifyFrame(formAddItems);
                }
                manager.activateFrame(formAddItems);
                
            } else if (e.getSource() == viewInventoryItem){
                formViewItems.setVisible(true);
                if (formViewItems.isIcon()){
                    manager.deiconifyFrame(formViewItems);
                }
                manager.activateFrame(formViewItems);
                
            }else if (e.getSource() == viewDeletedInventoryItem){
                formViewDeletedItems.setVisible(true);
                if (formViewDeletedItems.isIcon()){
                    manager.deiconifyFrame(formViewDeletedItems);
                }
                manager.activateFrame(formViewDeletedItems);
                
            }else if(e.getSource() == addEatsMenu){
                formAddMenu.setVisible(true);
                if (formAddMenu.isIcon()){
                    manager.deiconifyFrame(formAddMenu);
                }
                manager.activateFrame(formAddMenu);
                
            }else if (e.getSource() == viewMenu){
                formViewMenu.setVisible(true);
                if (formViewMenu.isIcon()){
                    manager.deiconifyFrame(formViewMenu);
                }
                manager.activateFrame(formViewMenu);
                
            }else if (e.getSource() == editMenu){
                formEditMenu.setVisible(true);
                if (formEditMenu.isIcon()){
                    manager.deiconifyFrame(formEditMenu);
                }
                manager.activateFrame(formEditMenu);
                
            }else if (e.getSource() == viewDeletedMenu){
                formViewDeleteMenu.setVisible(true);
                if (formViewDeleteMenu.isIcon()){
                    manager.deiconifyFrame(formViewDeleteMenu);
                }
                manager.activateFrame(formViewDeleteMenu);
                
            }else if (e.getSource() == viewSalesItem){
                formViewSales.setVisible(true);
                if (formViewSales.isIcon()){
                    manager.deiconifyFrame(formViewSales);
                }
                manager.activateFrame(formViewSales);
                
            }else if (e.getSource() == addPurchaseStore){
                formAddPurchaseStore.setVisible(true);
                if (formAddPurchaseStore.isIcon()){
                    manager.deiconifyFrame(formAddPurchaseStore);
                }
                manager.activateFrame(formAddPurchaseStore);
                
            }else if (e.getSource() == viewPurchaseItem){
                formViewPurchaseItems.setVisible(true);
                if (formViewPurchaseItems.isIcon()){
                    manager.deiconifyFrame(formViewPurchaseItems);
                }
                manager.activateFrame(formViewPurchaseItems);
                
            }else if (e.getSource() == addPurchaseItem){
                formAddPurchaseItem.setVisible(true);
                if (formAddPurchaseItem.isIcon()){
                    manager.deiconifyFrame(formAddPurchaseItem);
                }
                manager.activateFrame(formAddPurchaseItem);
                
            }else if (e.getSource() == createPurchaseList){
                formCreatePurchaseList.setVisible(true);
                if (formCreatePurchaseList.isIcon()){
                    manager.deiconifyFrame(formCreatePurchaseList);
                }
                manager.activateFrame(formCreatePurchaseList);
                
            }else if (e.getSource() == editPurchase){
                formEditPurchase.setVisible(true);
                if (formEditPurchase.isIcon()){
                    manager.deiconifyFrame(formEditPurchase);
                }
                manager.activateFrame(formEditPurchase);
                
            }else if (e.getSource() == addVoucherItem){
                formAddVoucherItem.setVisible(true);
                if (formAddVoucherItem.isIcon()){
                    manager.deiconifyFrame(formAddVoucherItem);
                }
                manager.activateFrame(formAddVoucherItem);
                
            }else if (e.getSource() == viewVoucherItem){
                formViewVoucherItem.setVisible(true);
                if (formViewVoucherItem.isIcon()){
                    manager.deiconifyFrame(formViewVoucherItem);
                }
                manager.activateFrame(formViewVoucherItem);
                
            }else if (e.getSource() == createVoucher){
                formCreateVoucher.setVisible(true);
                if (formCreateVoucher.isIcon()){
                    manager.deiconifyFrame(formCreateVoucher);
                }
                manager.activateFrame(formCreateVoucher);
                
            }else if (e.getSource() == editVoucher){
                formEditVoucher.setVisible(true);
                if (formEditVoucher.isIcon()){
                    manager.deiconifyFrame(formEditVoucher);
                }
                manager.activateFrame(formEditVoucher);
                
            }else if (e.getSource() == viewDisbursement){
                formViewDisbursementByDate.setVisible(true);
                if (formViewDisbursementByDate.isIcon()){
                    manager.deiconifyFrame(formViewDisbursementByDate);
                }
                manager.activateFrame(formViewDisbursementByDate);
                
            }else if (e.getSource() == addEmployee){
                formAddEmployee.setVisible(true);
                if (formAddEmployee.isIcon()){
                    manager.deiconifyFrame(formAddEmployee);
                }
                manager.activateFrame(formAddEmployee);
                
            }else if (e.getSource() == viewEmployee){
                formViewEmployee.setVisible(true);
                if (formViewEmployee.isIcon()){
                    manager.deiconifyFrame(formViewEmployee);
                }
                manager.activateFrame(formViewEmployee);
                
            }else if (e.getSource() == loginEmployee){
                formLoginEmployee.setVisible(true);
                if (formLoginEmployee.isIcon()){
                    manager.deiconifyFrame(formLoginEmployee);
                }
                manager.activateFrame(formLoginEmployee);
                
            }else if (e.getSource() == logoutEmployee){
                formLogoutEmployee.setVisible(true);
                if (formLogoutEmployee.isIcon()){
                    manager.deiconifyFrame(formLogoutEmployee);
                }
                manager.activateFrame(formLogoutEmployee);
                
            }else if (e.getSource() == viewAttendance){
                formViewSummaryByEmployee.setVisible(true);
                if (formViewSummaryByEmployee.isIcon()){
                    manager.deiconifyFrame(formViewSummaryByEmployee);
                }
                manager.activateFrame(formViewSummaryByEmployee);
                
            }else if (e.getSource() == addDeduction){
                formAddDeduction.setVisible(true);
                if (formAddDeduction.isIcon()){
                    manager.deiconifyFrame(formAddDeduction);
                }
                manager.activateFrame(formAddDeduction);
                
            }else if (e.getSource() == viewDeduction){
                formViewDeduction.setVisible(true);
                if (formViewDeduction.isIcon()){
                    manager.deiconifyFrame(formViewDeduction);
                }
                manager.activateFrame(formViewDeduction);
                
            }else if (e.getSource() == viewSummaryAll){
                formViewSummaryAll.setVisible(true);
                if (formViewSummaryAll.isIcon()){
                    manager.deiconifyFrame(formViewSummaryAll);
                }
                manager.activateFrame(formViewSummaryAll);
                
            }else if (e.getSource() == viewVoucherReportsByMonth){
                formViewVoucherReportByMonth.setVisible(true);
                if (formViewVoucherReportByMonth.isIcon()){
                    manager.deiconifyFrame(formViewVoucherReportByMonth);
                }
                manager.activateFrame(formViewVoucherReportByMonth);
                
            }else if(e.getSource() == viewCashAdvanceByEmployee){
                formViewCashAdvanceByEmployee.setVisible(true);
                if (formViewCashAdvanceByEmployee.isIcon()){
                    manager.deiconifyFrame(formViewCashAdvanceByEmployee);
                }
                manager.activateFrame(formViewCashAdvanceByEmployee);
                
            }else if(e.getSource() == viewCashAdvancePaymentByEmployee){
                formViewPaymentByEmployee.setVisible(true);
                if (formViewPaymentByEmployee.isIcon()){
                    manager.deiconifyFrame(formViewPaymentByEmployee);
                }
                manager.activateFrame(formViewPaymentByEmployee);
                
            }else if(e.getSource() == viewViewStocks){
                formViewStock.setVisible(true);
                if (formViewStock.isIcon()){
                    manager.deiconifyFrame(formViewStock);
                }
                manager.activateFrame(formViewStock);
            }else if(e.getSource() == viewViewStockRecord){
                formViewStockRecord.setVisible(true);
                if (formViewStockRecord.isIcon()){
                    manager.deiconifyFrame(formViewStockRecord);
                }
                manager.activateFrame(formViewStockRecord);
            }else if(e.getSource() == stockInStockOut){
                formStockInStockOut.setVisible(true);
                if (formStockInStockOut.isIcon()){
                    manager.deiconifyFrame(formStockInStockOut);
                }
                manager.activateFrame(formStockInStockOut);
            }
        }
        
    }
    
    /*public class InternalFrameLocation extends Thread implements Runnable {

        @Override
        public void run() {
            while (true){
                JInternalFrame currentFrame = mdiForm.getSelectedFrame();
                if (currentFrame != null){
                    Dimension mdiFormSize = mdiForm.getSize();
                    Point currentFrameLocation = currentFrame.getLocation();
                    
                    if (currentFrameLocation.getX() > (mdiFormSize.getWidth()-currentFrame.getWidth())){
                        currentFrame.setLocation((int) (mdiFormSize.getWidth()-currentFrame.getWidth()), (int) currentFrameLocation.getY());
                    }
                    if (currentFrameLocation.getY() > (mdiFormSize.getHeight()-currentFrame.getHeight())){ 
                        currentFrame.setLocation((int) currentFrameLocation.getX(), (int) (mdiFormSize.getHeight()-currentFrame.getHeight()));
                    }  
                    if (currentFrameLocation.getX() < 0){
                        currentFrame.setLocation(0, (int) currentFrameLocation.getY());
                    }
                    if (currentFrameLocation.getY() < 0){ 
                        currentFrame.setLocation((int) currentFrameLocation.getX(),0);
                    }
              
                } else {
                    //JOptionPane.showMessageDialog(null, "None of the Frames are active");
                }
                
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
        
    }*/
    
}
