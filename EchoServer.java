// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    String message = msg.toString();
    if(message.startsWith("#login")){
      System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
      this.sendToAllClients(msg);
    } else {
      System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
      this.sendToAllClients(client.getInfo("loginId") + "> " + msg);
    }

  }

  public void handleMessageFromServerUI(String message){
    if (!message.charAt(0)=='#'){
      System.out.println(message);
      sendToAllClients(message);
    }
    switch (message){
      case("#getport"):
        System.out.println(getPort());
        break;
      case ("#start"):
        if(serverStarted==false){
          try{
            listen();
          } catch(Exception e){
            System.out.println("Could not start.");
          }
        }
        break;
      case ("#close"):
        try{
          sendToAllClients("server is closing");
          System.out.println("Server is closing.");
          close();
        } catch(Exception e){
          System.out.println("Unable to close.");
        }
        break;
      case ("#stop"):
        System.out.println("Server stopped listening to new clients.");
        stopListening();
        break;
      case("#quit"):
        System.out.println("Server is quitting.");
        System.exit(0);
        break;
    }

  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  boolean serverStarted;

  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    serverStarted = true;
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    serverStarted = false;
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("User " + client.getId() + " has connected");
  }

  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("User " + client.getInfo("loginID") + " has disconnected");
    sendToAllClients("User " + client.getInfo("loginID") + " has disconnected");
  }

  @Override
  synchronized protected void clientException(ConnectionToClient client, Throwable exception){
    System.out.println(client.getInfo("loginId") + " has disconnected.");
  }


  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
