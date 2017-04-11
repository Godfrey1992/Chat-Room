

import java.net.Socket;

import java.util.Scanner;


// ADDED COMMENTS!!!!
// This class handles the inputs sent from the client.
public class ClientThread implements Runnable{

	Scanner input;

	public ClientThread(Socket socket)
	{
		try 
		{
			//get the inputs sent from the client.
			input = new Scanner(socket.getInputStream());
		} 
		catch (Exception e) 
		{
			Server.serverConsol.append("Thread error 1 \n");
		}
	}
	
	// This thread is started by the Server class
	public void run() {
		
		try
		{
			String text;
			// loop through the sent messages from the client
			while((text = input.nextLine()) != null)
			{
				Server.serverConsol.append(text + "\n"); //display to the server console
				Server.sendToClients(text); // execute this method which is in the server class
			}
		}
		catch(Exception e)
		{
			Server.serverConsol.append("Client Disconnected....... \n");
		}
	}
	
}
