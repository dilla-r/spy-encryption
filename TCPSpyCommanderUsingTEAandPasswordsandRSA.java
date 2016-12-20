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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author RebeccaD
 */
public class TCPSpyCommanderUsingTEAandPasswordsandRSA {

    public static void main(String args[]) {

        try {

            int serverPort = 7896; // the server port
            ServerSocket listenSocket = new ServerSocket(serverPort);
            Scanner scan = new Scanner(System.in);

            //Instanciate KLM Creator 
            KMLCreator kmlc = new KMLCreator();
            kmlc.writeFile(); //write initial kml file

            PasswordHash ph = new PasswordHash(); //Instanciate PasswordHash

            System.out.println("Waiting for Spies to Visit...");

            int visitCounter = 0; //Count number of client connections

            while (true) {

                Socket clientSocket = listenSocket.accept();
                visitCounter++; //increment counter when client visits
                Connection c = new Connection(clientSocket, ph, visitCounter, kmlc); //create a new connection for spy
            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}

class Connection extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    PasswordHash ph;
    int counter;
    KMLCreator kmlc;
    //Spy Commander's Public Key N
    BigInteger n = new BigInteger("2844891281665683488614871479725174564285088586176624811468277743220045548476519447861771890237119064564869788642572018813512601667408913421179671166260169402425164921211826448151489698499794657981216017027742055773790093190511059430868149989");
    //Spy Commander's Private Key D
    BigInteger d = new BigInteger("830846989198180905016839954864272718623321109288197489837844820562913648745602975907873628318941344519456303380057500954041624876520817310662443485066494762987187335042620171846486122322942596492640571370120191884221622783572936048872966273");
    //Spy Commander's Public Key E
    BigInteger e = new BigInteger("65537");
    TEA tea;

    /**
     * Instantiate objects in new Connection
     *
     * @param aClientSocket client socket
     * @param teaAlg TEA object
     * @param passH PasswordHash object
     * @param vc Spy Number Counter
     * @param kmlcreat KLMFileCreator Object
     */
    public Connection(Socket aClientSocket, PasswordHash passH, int vc, KMLCreator kmlcreat) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            ph = passH;
            counter = vc;
            kmlc = kmlcreat;

            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    /**
     * Dialog with spy
     */
    public void run() {
        try {

            System.out.print("Got visit " + counter); //Print number of visit

            //Receive Session Key from Spy
            int size = in.readInt(); //read length of response
            byte[] inMessage = new byte[size]; //set byte array to size of response
            size = in.read(inMessage); //read in message
            
            BigInteger session = new BigInteger(inMessage); //Convert session key to BigInteger
            BigInteger sessionKey = session.modPow(d, n); //RSA Decrypt Session Key
            byte [] skey = sessionKey.toByteArray(); //Convert session key back to byte array
            
            
            tea = new TEA(skey); //Create new TEA with session key
            

            //Create byte array of id prompt
            byte[] outMessage;
            outMessage = tea.encrypt("Enter your ID:".getBytes());
            out.writeInt(outMessage.length); //send length of prompt
            out.write(outMessage); //send prompt
            out.flush();

            // Read Username Response
            size = in.readInt(); //read length of response
            inMessage = new byte[size]; //set byte array to size of response
            size = in.read(inMessage); //read response to byte array
            String uname = new String(tea.decrypt(inMessage)); //decrypt response

            //Send password Prompt
            outMessage = tea.encrypt("Enter your Password:".getBytes());
            out.writeInt(outMessage.length);
            out.write(outMessage);
            out.flush();

            //Read Password Response
            size = in.readInt();
            inMessage = new byte[size];
            size = in.read(inMessage);
            String pword = new String(tea.decrypt(inMessage));

            //Check username to see if symmetric key is correct
            if (!checkKey(uname)) {
                clientSocket.close(); //if incorrect, close socket
                System.out.println(". Illegal Symmetric Key Used. This may be an attack"); //print warning 
                return; //exit thread
            }

            //Check if username and passwrod are valid
            if (!ph.validateUser(uname, pword)) {
                //if invalid, send feedback to spy
                outMessage = tea.encrypt("Your username and password are not valid".getBytes());
                out.writeInt(outMessage.length);
                out.write(outMessage);
                out.flush();

                //Print warning
                System.out.println(" from " + uname + ". Illegal Username or Password Attempt. This may be an attack");
                clientSocket.close(); //close Socket
                return; //exit thread
            }

            System.out.println(" from " + uname);

            //Send Location Prompt
            outMessage = tea.encrypt("Enter your Location:".getBytes());
            out.writeInt(outMessage.length);
            out.write(outMessage);
            out.flush();

            //Read Location Response
            size = in.readInt();
            inMessage = new byte[size];
            size = in.read(inMessage);
            String loca = new String(tea.decrypt(inMessage));
            kmlc.updateLocation(uname, loca);

            //Send Location Feedback
            out.writeUTF("Thank you. Your location was securely transmitted to Intelligence Headquarters.");
            out.flush();

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } finally {
            try {

                clientSocket.close();
            } catch (IOException e) {/*close failed*/
            }
        }

    }

    /**
     * Check if correct symmetric key was submitted by client
     *
     * @param message message to check
     * @return true if valid key
     */
    public boolean checkKey(String message) {
        boolean check = true;

        char[] messArray = message.toCharArray();//convert message to byte array

        //check to see if each char in message is valid ASCII character
        for (int i = 0; i < messArray.length; i++) {
            if (messArray[i] > 128) {
                check = false;
            }

        }
        return check;
    }
}
