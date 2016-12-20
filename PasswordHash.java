/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task2;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author RebeccaD
 */
public class PasswordHash {
    
    String jamesUser;
    String mikeUser;
    String joeUser;
    String jamesPass;
    String mikePass;
    String joePass;
    String salt;
    MessageDigest md;
    
    /**
     * Initializes username value hashed password values
     */
    public PasswordHash(){
        jamesUser = "jamesb"; //James's Username
        jamesPass = "264b4b5f038b7a511f08e9a5ff29ac21"; //James's hashed password
        joeUser = "joem"; //Joe's username
        joePass = "93bddde1c87e6f48b49c68a0e06eade4"; //Joe's hashed password
        mikeUser = "mikem"; //mike's username
        mikePass = "97eb2a6bac0657fd18948bdbed845f74"; //Mike's hashed password
        salt = "HTS@#47283487333^@#$&@"; //hash salt
        
        
        try {
            md = MessageDigest.getInstance("MD5"); //Initialize MessageDigest for MD5 hashing
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * checkes submitted password with stored password
     * @param uname submitted username
     * @param pword submitted password
     * @return true if valid username and password combination
     */
    public boolean validateUser(String uname, String pword){

       boolean valid = true;
       
       String combine = pword+salt; //combine submitted password with salt
       byte [] hashIt = combine.getBytes(); //create byte array of combined password and salt
       md.update(hashIt); //hash byte array
       byte [] digested = md.digest(); //return hashed and salted password
       String hashedPWord = DatatypeConverter.printHexBinary(digested).toLowerCase(); //convert to hex string

       // Test to see if username and hashed password equal stored values
       if(uname.equals(jamesUser)){
           if(!hashedPWord.equals(jamesPass)){
               valid = false;
           }
       }
       else if(uname.equals(mikeUser)){
           if(!hashedPWord.equals(mikePass)){
               valid = false;
           }
       }
       else if(uname.equals(joeUser)){
           if(!hashedPWord.equals(joePass)){
               valid = false;
           }
       }
       else{
           valid = false;
       }
        
       return valid;
    }
    
}
