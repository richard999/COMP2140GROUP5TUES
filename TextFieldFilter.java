/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

import java.awt.event.KeyEvent;

/**
 *
 * @author IT-NEW
 */
public class TextFieldFilter {
    protected void _numbersOnly(KeyEvent e){
        char input = e.getKeyChar();
        if(_isNotNumber(input)){
            e.consume();
        }
    }
    
    private boolean _isNotNumber(char input){
        String allowed = "0123456789";
        for(int i=0; i<allowed.length(); i++){
            if(input == allowed.charAt(i)){
                return false;
            }
        }
        return true;
    }
    
    protected void _numbersAndPeriod(KeyEvent e){
        char input = e.getKeyChar();
        if(_isNotNumberAndPeriod(input)){
            e.consume();
        }
    }
    
    private boolean _isNotNumberAndPeriod(char input){
        String allowed = "0123456789.";
        for(int i=0; i<allowed.length(); i++){
            if(input == allowed.charAt(i)){
                return false;
            }
        }
        return true;
    }
    
}
