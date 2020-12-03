/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eats.possible.purchase;

/**
 *
 * @author IT-NEW
 */
public class NumberFunction {
    
    //This method put comma on numbers equals to or more than 1000 
    //For Example 1000.00 = 1,000.00
    public String _getFormattedNumber(String numberToFormat){
        String finalNumber = "";
        String[] number = numberToFormat.split("\\.");
        char[] digit = number[0].toCharArray();
        int x = 0;
        int y=3;
        for (int i=digit.length-1; i>=0; i--){
            if (x == y) {
                finalNumber = digit[i] + "," + finalNumber;
                y+=3;
            } else {
                finalNumber = digit[i] + finalNumber;
            }
            x++;
        }
        
        return finalNumber + "." + number[1];
    }
    
    //This method strips the "Php" and comma in the string
    //for example Php 1,000.00 = 1000.00
    public float _stripValue(String stringToStrip){
        float strippedValue;
        String word = "Php";
        if(stringToStrip.contains(word)){
            stringToStrip = stringToStrip.replace(word, "");
        }
        word = ",";
        if(stringToStrip.contains(word)){
            stringToStrip = stringToStrip.replace(word, "");
        }
        strippedValue = Float.parseFloat(stringToStrip);
        return strippedValue;
    }
}
