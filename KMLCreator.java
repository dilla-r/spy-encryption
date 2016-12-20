package project2task2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author RebeccaD
 */
public class KMLCreator {

    File file;
    String spyC = "-79.945289,40.44431,0.00000";
    String spyJames;
    String spyJoe;
    String spyMike;

    public KMLCreator() {

        //Initialize locations as Hamburg Hall and file name
        
        file = new File("SecretAgents.kml"); //initialize file name
        spyJames = "-79.945389,40.444216,0.00000";
        spyMike = "-79.945389,40.444216,0.00000";
        spyJoe = "-79.945389,40.444216,0.00000";
    }

    /**
     * Write file with current spy locations
     * @throws IOException 
     */
    public void writeFile() throws IOException {

        FileWriter fw = new FileWriter(file);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                + "<kml xmlns=\"http://earth.google.com/kml/2.2\"\n"
                + "><Document>\n"
                + "<Style id=\"style1\">\n"
                + "<IconStyle>\n"
                + "<Icon>\n"
                + "<href>http://maps.gstatic.com/intl/en_ALL/mapfiles/ms/micons/bluedot.png</href>\n"
                + "</Icon>\n"
                + "</IconStyle>\n"
                + "</Style>\n"
                + "<Placemark>\n"
                + " <name>seanb</name>\n"
                + " <description>Spy Commander</description>\n"
                + " <styleUrl>#style1</styleUrl>\n"
                + " <Point>\n"
                + " <coordinates>"+ spyC +"</coordinates>\n"
                + " </Point>\n"
                + "</Placemark>\n"
                + "<Placemark>\n"
                + " <name>jamesb</name>\n"
                + " <description>Spy</description>\n"
                + " <styleUrl>#style1</styleUrl>\n"
                + " <Point>\n"
                + " <coordinates>" + spyJames +"</coordinates>\n"
                + " </Point>\n"
                + "</Placemark>\n"
                + "7\n"
                + "<Placemark>\n"
                + " <name>joem</name>\n"
                + " <description>Spy</description>\n"
                + " <styleUrl>#style1</styleUrl>\n"
                + " <Point>\n"
                + " <coordinates>" + spyJoe + "</coordinates>\n"
                + " </Point>\n"
                + "</Placemark>\n"
                + "<Placemark>\n"
                + " <name>mikem</name>\n"
                + " <description>Spy</description>\n"
                + " <styleUrl>#style1</styleUrl>\n"
                + " <Point>\n"
                + " <coordinates>" + spyMike + "</coordinates>\n"
                + " </Point>\n"
                + "</Placemark>\n"
                + "</Document>\n"
                + "</kml>");
        fw.flush();
        fw.close();

    }
    
    /**
     * Updates location of spy and called re-write of file
     * @param uname name of spy being updated
     * @param location updated spy location
     * @throws IOException 
     */
    public void updateLocation(String uname, String location) throws IOException{
        
        //Determine which's spy's location is to be updated, and updates this
        switch (uname) {
            case "joem":
                spyJoe = location;
                break;
            case "jamesb":
                spyJames = location;
                break;
            case "mikem":
                spyMike = location;
                break;
            default:
                break;
        }
        
        writeFile(); //call file re-write
    }

}
