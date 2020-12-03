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
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author IT-NEW
 */
public class SubFormDownPayment extends JPanel{
    private final JTextField txtCashTender, txtOutput;
    public SubFormDownPayment(){
        this.setPreferredSize(new Dimension(210, 90));
        
        txtCashTender = new JTextField();
        txtCashTender.addKeyListener(new KeyAdapter() {
           @Override
           public void keyTyped(KeyEvent e) {
              TextFieldFilter textFieldFilter = new TextFieldFilter();
              textFieldFilter._numbersAndPeriod(e);
           }
        });
        Font newFont = txtCashTender.getFont();
        txtCashTender.setPreferredSize(new Dimension(200, 35));
        txtCashTender.setFont(new Font(newFont.getName(), Font.PLAIN, 20));
        txtCashTender.setBackground(Color.BLACK);
        txtCashTender.setForeground(Color.GREEN);
        txtCashTender.addKeyListener(new TextFieldFunction());
        
        txtOutput = new JTextField("JMD 0.00");
        txtOutput.setPreferredSize(new Dimension(200, 35));
        txtOutput.setFont(new Font(newFont.getName(), Font.PLAIN, 20));
        txtOutput.setBackground(Color.BLACK);
        txtOutput.setForeground(Color.GREEN);
        txtOutput.setFocusable(false);
        txtOutput.setEditable(false);
        
        add(txtCashTender);
        add(txtOutput);
    }
    
    protected String _getValue(){
        String value;
        value = txtOutput.getText();
        return value;
    }
    
    private class TextFieldFunction implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getSource() == txtCashTender){
                NumberFunction nFunction = new NumberFunction();
                String cash;
                
                if(!txtCashTender.getText().contains("\\.")){
                    cash = nFunction._getFormattedNumber(txtCashTender.getText() + ".00");
                } else {
                    cash = nFunction._getFormattedNumber(txtCashTender.getText());
                }
                String currency = "JMD ";
                txtOutput.setText(currency + cash);
                
                if(txtCashTender.getText().equals("")){
                    txtOutput.setText("JMD 0.00");
                }
            }
        }
        
    }
    
}
