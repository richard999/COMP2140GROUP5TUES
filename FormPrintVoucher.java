/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eatspossibleadministrator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author IT-NEW
 */
public class FormPrintVoucher implements Printable {

    PrinterJob job;
    JPanel printPanel;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        printPanel.printAll(graphics);

        return PAGE_EXISTS;
    }

    protected void _setPanel(JPanel panel) {
        printPanel = panel;
    }

    protected boolean _goPrint() {
        job = PrinterJob.getPrinterJob();
        if (job.printDialog()) {
            PageFormat pf = new PageFormat();
            Paper paper = new Paper();
            paper.setImageableArea(1, 1, paper.getWidth(), paper.getHeight());
            pf.setPaper(paper);
            job.setPrintable(this, pf);
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

}
