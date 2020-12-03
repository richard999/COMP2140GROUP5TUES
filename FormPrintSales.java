/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author IT-NEW
 */
public class FormPrintSales implements Printable {
    private PrinterJob job;
    int[] pageBreaks;
    String[] purchaseNo, purchaseDate, id, menuItem, order, subTotal;
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        
        Font headerFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
 
        if (pageBreaks == null) {
            int linesPerPage = (int)(pageFormat.getImageableHeight()/lineHeight) - 1;
            int numBreaks = (this.purchaseNo.length-1)/linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b=0; b<numBreaks; b++) {
                pageBreaks[b] = (b+1)*linesPerPage; 
            }
        }
 
        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }
 
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        graphics.setFont(headerFont);
        
        graphics.drawString("Purchase No.", 0, lineHeight);
        graphics.drawString("Purchase Date", 50, lineHeight);
        graphics.drawString("ID", 130, lineHeight);
        graphics.drawString("Menu Item", 180, lineHeight);
        graphics.drawString("# of Order", 370, lineHeight);
        graphics.drawString("Sub-Total", 410, lineHeight);
        
        graphics.setFont(font);
 
        int y = lineHeight; 
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
        int end   = (pageIndex == pageBreaks.length) ? this.purchaseNo.length : pageBreaks[pageIndex];
        for (int line=start; line<end; line++) {
            y += lineHeight;
            graphics.drawString(this.purchaseNo[line], 0, y);
            graphics.drawString(this.purchaseDate[line], 50, y);
            graphics.drawString(this.id[line], 130, y);
            graphics.drawString(this.menuItem[line], 180, y);
            graphics.drawString(this.order[line], 370, y);
            graphics.drawString(this.subTotal[line], 410, y);
        }
 
        return PAGE_EXISTS;
    }
    
    protected void _initTextLines(String[] purchaseNo, String[] purchaseDate, String[] id, String[] menuItem,
            String[] order, String[] subTotal) {
        if (this.purchaseNo == null) {
            this.purchaseNo = purchaseNo;
            this.purchaseDate = purchaseDate;
            this.id = id;
            this.menuItem = menuItem;
            this.order = order;
            this.subTotal = subTotal;
        }
    }
    
    protected void _goPrint(){
        job = PrinterJob.getPrinterJob();
        if(job.printDialog()){
            PageFormat pf = job.pageDialog(job.defaultPage());
            Paper paper = new Paper();
            paper.setImageableArea(pf.getImageableX(), pf.getImageableY(), paper.getImageableWidth(), paper.getImageableHeight());
            job.setPrintable(this, pf);
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}
