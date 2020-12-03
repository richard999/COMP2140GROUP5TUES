/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormSearchPurchase extends JPanel{
    JTextField txtSearch;
    public SubFormSearchPurchase(){
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 35));
        txtSearch.addKeyListener(new KeyAdapter() {
           @Override
           public void keyTyped(KeyEvent e) {
              TextFieldFilter textFieldFilter = new TextFieldFilter();
              textFieldFilter._numbersOnly(e);
           }
        });
        Font newFont = txtSearch.getFont();
        txtSearch.setFont(new Font(newFont.getName(), Font.PLAIN, 15));
        txtSearch.setBackground(Color.BLACK);
        txtSearch.setForeground(Color.GREEN);
        
        add(txtSearch);
    }
    
    protected int _getPurchaseNo(){
        int number = 0;
        try{
            number = Integer.parseInt(txtSearch.getText());
        } catch(Exception ex){
            JOptionPane.showMessageDialog(this, "WRONG INPUT \n\n Numbers only", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
        return number;
    }
    
}
