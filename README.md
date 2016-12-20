# spy-encryption

### This project uses symmetric and asymmetric key cryptograph to encrypt and decrypt location information sent between clients and servers over TCP sockets. The location information is saved to a .kml file that can display the location information on Google Earth. 

##### TCPSpyCommanderUsingTEAandRSA.java is the server in this interaction
##### TCPSpyUsingTEAandRSA.java is the client. It generates a random key, which is encypts using RSA to send to the server. The server then decypts the key, and they use this key to communicate using TEA for their session. 
##### The location information is sent from the "spy" to the "spy commander" over this secure connection. The spy commander can then generate a .kml file (example in SecretAgents.kml) that can be read by Google Earth.
