import ocsf.server.*;
import client.*;
import common.*;

import java.io.*;
import java.util.Scanner;

public class ServerConsole implements ChatIF{

    final public static int DEFAULT_PORT = 5555;

    EchoServer server;

    Scanner fromConsole;

    public ServerConsole(int port)
    {
        try
        {
            server = new EchoServer(port);

        }
        catch(Exception exception)
        {
            System.out.println("Error: Can't setup server!"
                    + " Terminating server.");
            System.exit(1);
        }

        // Create scanner object to read from console
        fromConsole = new Scanner(System.in);
    }

    public void accept()
    {
        try
        {

            String message;

            while (true)
            {
                message = fromConsole.nextLine();
                client.handleMessageFromServerUI(message);
            }
        }
        catch (Exception ex)
        {
            System.out.println
                    ("Unexpected error while reading from console!");
        }
    }

    public void display(String message)
    {
        System.out.println("> " + message);
    }

    public static void main(String[] args)
    {
        int port = 0;
        final int DEFAULT_PORT = 5555;

        try{
            port = Integer.parseInt(args[0]);
        }catch (Exception e){
            port = DEFAULT_PORT;
        }

        ServerConsole server= new ServerConsole(port);
        server.accept();   //Wait for console data
    }


}