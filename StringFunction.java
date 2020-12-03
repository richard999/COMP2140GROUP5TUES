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
public class StringFunction {
    
    
    //This method makes the first letter of the string capital
    //example      boneless lechon belly = Boneless Lechon Belly
    //This methon can only be use for retrieving data from the database
    protected String _Capitalized(String StringToCapitalized){
        //JOptionPane.showMessageDialog(null, StringToCapitalized);
        String CapitalizedString = "";
        
        if (!"".equals(StringToCapitalized)){
            String[] word = StringToCapitalized.split(" ");
            
            for(int i=0; i<word.length; i++){
                if(!word[i].trim().equals("")){
                    word[i] = word[i].substring(0, 1).toUpperCase() + word[i].substring(1, word[i].length());
                    CapitalizedString += word[i] + " ";
                }
            }
        }
        
        return CapitalizedString;
    }
    
    //This method removes excess white spaces in a string
    //Example   bonless   lechon belly = bonless lechon belly
    //This method can only be used for storing data to the database
    protected String _removeWhiteSpaces(String stringToFormat){
        String stringToReturn = "";
        String[] word = stringToFormat.split(" ");
        
        if (!"".equals(stringToFormat)){
            for(int i=0; i<word.length; i++){
                if(!word[i].trim().equals("")){
                    stringToReturn += word[i] + " ";
                }
            }
        }
        return stringToReturn;
    }
    
}
