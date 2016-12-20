/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2task2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author RebeccaD
 */
public class TCPSpyUsingTEAandPasswordsandRSA {

    public static void main(String args[]) {
        
        Socket s = null;
        
        Scanner scan = new Scanner(System.in);

            //Spy Commander's Public Keys
            BigInteger n = new BigInteger("2844891281665683488614871479725174564285088586176624811468277743220045548476519447861771890237119064564869788642572018813512601667408913421179671166260169402425164921211826448151489698499794657981216017027742055773790093190511059430868149989");
            BigInteger e = new BigInteger("65537");
        try {
            
            int serverPort = 7896;
            s = new Socket(args[1], serverPort);

            //Generate Session Key
            Random rnd = new Random();
            BigInteger key = new BigInteger(16*8,rnd); 
            
            //RSA encrypt session key
            BigInteger c = key.modPow(e, n);
            
            byte [] sessionKey = c.toByteArray(); //convert sessionKey to Byte Array
            
            TEA tea = new TEA(key.toByteArray()); //create TEA instance with session key


            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            
            //Send RSA encryped session key to spy commander
            out.writeInt(sessionKey.length);
            out.write(sessionKey);
            out.flush();
            
            //Read ID Prompt
            int size = in.readInt();//read size of incoming message
            byte [] message = new byte[size];//initialize byte array to size of incoming message
            int messagesize = in.read(message); //read in incoming message
            String decoded = new String(tea.decrypt(message)); //decode incoming message
            System.out.println(decoded); //Print decoded text
            
            //Send ID Response
            byte [] secretMessage; //create byte array to hold response
            secretMessage = tea.encrypt(scan.nextLine().getBytes()); //encrypt response
            out.writeInt(secretMessage.length); //send length of response
            out.write(secretMessage); //send response
            out.flush();
            
            //Read password prompt
            size = in.readInt();
            message = new byte[size];
            messagesize = in.read(message);
            decoded = new String(tea.decrypt(message));
            System.out.println(decoded);
            
            //Send Password Response
            secretMessage = tea.encrypt(scan.nextLine().getBytes());
            out.writeInt(secretMessage.length);
            out.write(secretMessage);
            out.flush();            
            
            //Read Location Prompt
            size = in.readInt();
            message = new byte[size];
            messagesize = in.read(message);
            decoded = new String(tea.decrypt(message));
            System.out.println(decoded);
            
            //Send Location Response
            secretMessage = tea.encrypt(scan.nextLine().getBytes());
            out.writeInt(secretMessage.length);
            out.write(secretMessage);
            out.flush();     


            System.out.println(in.readUTF());	    // read a line of data from the stream
            
            
        } catch (UnknownHostException m) {
            System.out.println("Socket:" + m.getMessage());
        } catch (EOFException m) {
            System.out.println("EOF:" + m.getMessage());
        } catch (IOException m) {
            System.out.println("readline:" + m.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException m) {
                    System.out.println("close:" + m.getMessage());
                }
            }
        }
    }
    
}
