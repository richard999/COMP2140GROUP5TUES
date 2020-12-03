/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 *
 * @author IT-NEW
 */
public class FormPurchaseReceipt implements Printable{
    String menuItemName[];
    String purchaseNo, purchaseDate;
    String cashDown, cashT, cashC;
    int menuItemOrder[];
    String menuItemSubTotal[];
    String itemTotal;
    String discount;
    String vatableSales;
    String vatAmount;
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if(pageIndex > 0){
            return NO_SUCH_PAGE;
        }
        
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        
        graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 8));
        graphics.drawString("Eats Possible", 80, 10);
        graphics.drawString("Lapu-Lapu St., Tagum City", 60, 20);
        graphics.drawString("Purchase No: "+purchaseNo, 60, 30);
        graphics.drawString("Purchase Date: "+purchaseDate, 60, 40);
        graphics.drawString("=============================================", 0, 50);
        int y = 60;
        for(int i=0; i<menuItemName.length; i++){
            graphics.drawString(menuItemName[i], 20, y);
            graphics.drawString(Integer.toString(menuItemOrder[i]), 40, y+10);
            graphics.drawString(menuItemSubTotal[i], 120, y+10);
            y+=20;
        }
        graphics.drawString("=============================================", 0, y);
        graphics.drawString("Discount", 20, y+10);
        graphics.drawString(discount, 120, y+10);
        graphics.drawString("Total Amount", 20, y+20);
        graphics.drawString(itemTotal, 120, y+20);
        graphics.drawString("Down Payment", 20, y+30);
        graphics.drawString(cashDown, 120, y+30);
        graphics.drawString("Cash Tendered", 20, y+40);
        graphics.drawString(cashT, 120, y+40);
        graphics.drawString("Change", 20, y+50);
        graphics.drawString(cashC, 120, y+50);
        graphics.drawString("VATABLE Sales", 20, y+60);
        graphics.drawString(vatableSales, 120, y+60);
        graphics.drawString("VAT Amount", 20, y+70);
        graphics.drawString(vatAmount, 120, y+70);
        
        return PAGE_EXISTS;
    }
    
    protected void _setValues(String No, String date,String[] itemName, int[] itemOrder, String[] itemSubTotal,
            String Discount ,String total, String downPayment, String cashTendered, String cashChange){
        purchaseNo = No;
        purchaseDate = date;
        menuItemName = new String[itemName.length];
        menuItemOrder =  new int[itemOrder.length];
        menuItemSubTotal = new String[itemSubTotal.length];
        menuItemName = itemName;
        menuItemOrder = itemOrder;
        menuItemSubTotal = itemSubTotal;
        discount = Discount;
        itemTotal = total;
        cashDown = downPayment;
        cashT = cashTendered;
        cashC = cashChange;
        
        NumberFunction nFunction = new NumberFunction();
        float totalSales = nFunction._stripValue(itemTotal);
        float vatSales = (float) (totalSales/1.12);
        float vat = totalSales - vatSales;
        vatSales = (float) (Math.round(vatSales*100.00)/100.00);
        vat = (float) (Math.round(vat*100.00)/100.00);
        vatableSales = "Php " + nFunction._getFormattedNumber(Float.toString(vatSales));
        vatAmount = "Php " + nFunction._getFormattedNumber(Float.toString(vat));
    }
    
}
