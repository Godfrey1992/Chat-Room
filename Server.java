
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class Server extends JFrame implements Runnable{

	// MADE CHANGE on TEST BRACH
	public static JFrame frame;
	public static JLabel portLabel;
	public static JTextField portField;
	public static JLabel myIPLabel;
	public static JTextField myIP;
	public static JLabel serverConsolLabel;
	public static JTextArea serverConsol;
	public static JButton setPort;
	public static JButton startServerButton;
	public static InetAddress IP;
	public static String[] IP2;
	public static Font font;
	public static ServerSocket server;
	public static int x;
	public static int port = 1991;

	// This array stores all the user connections.
	static ArrayList<PrintWriter> clientsConnected = new ArrayList<PrintWriter>();
	
	
	public static void main(String[] args) throws UnknownHostException  {
		
		// get the IP address for your current machine.
		// insert it into an array
		IP = InetAddress.getLocalHost();
		IP2 = (IP.toString()).split("/");

		//Set font
		font = new Font("sansserif", Font.BOLD, 20);

		//Create a new JFrame and call it server.
		frame = new JFrame("Server");

		//Create a new container with a gridLayout.
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new GridLayout(4, 2));

		//Create a centered JLabel.
		portLabel = new JLabel("PORT: ",SwingConstants.CENTER);
		portLabel.setFont(font);
		//add it to the container.
		contentPane.add(portLabel);

		//Create a new TextField.
		//Initialize it will a default value of port.
		portField = new JTextField(port);
		contentPane.add(portField);
		portField.setEditable(true);
		portField.setText(port + "");

		//Create a centered JLabel.
		myIPLabel = new JLabel("IP: ", SwingConstants.CENTER);
		myIPLabel.setFont(font);
		contentPane.add(myIPLabel);

		//Display the IP address. it is held in a array.
		myIP = new JTextField(IP2[1]);
		contentPane.add(myIP);
		myIP.setEditable(false);

		//Create a centered JLabel.
		serverConsolLabel = new JLabel("Console: ", SwingConstants.CENTER);
		serverConsolLabel.setFont(font);
		contentPane.add(serverConsolLabel);

		//create a scrollable JTextArea as the server console.
		serverConsol = new JTextArea(5, 20);
		JScrollPane scrollPane = new JScrollPane(serverConsol);
		contentPane.add(scrollPane);
		serverConsol.setEditable(false);
		
		// define a set port button and add an actionListerner
		setPort = new JButton("Set Port");
		contentPane.add(setPort);
		setPort.addActionListener(l -> {

            public void actionPerformed(ActionEvent e)
            {
                setPort(); //execute setPort method on button press
            }
        });
		
		//Create a new button to start the server. with an actionListener
		Server.startServerButton = new JButton("start Server");
		contentPane.add(Server.startServerButton);
		Server.startServerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	// start thread on button press.
            	new Thread(new Server()).start();
            }
        });

		frame.setSize(400, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//Start of the Server thread.
	public void run(){
		try {
			//set the server socket.
			Server.server = new ServerSocket(port);
			serverConsol.append("Server Started....... \n");
			
			while(true){
				//Start clientThread call thread to process client input.
				Socket socket = server.accept();
				serverConsol.append("Client connected...... \n");
				new Thread(new ClientThread(socket)).start();
				PrintWriter clientStream = new PrintWriter(socket.getOutputStream());
				//Add client data to the array.
				clientsConnected.add(clientStream);

			}
		} catch (Exception e) {
			serverConsol.append("Server is already running.......\n");
		}
	}

	// this method sends the messages to every client which is in the array
	// text = the actual message the clients send
	static void sendToClients(String text) {
		for (PrintWriter eachMessage : clientsConnected) {
			try {
				eachMessage.println(text); //send the message.
				eachMessage.flush(); //Clear the print writer for the next message
			} catch (Exception e) {
				serverConsol.append("Thread error 3 \n");
				;
			}
		}
	}
	
	//sets a user specified port.
	public static void setPort() {
		try{
			x = Integer.parseInt(portField.getText()); //get what user entered
			port = x;
			serverConsol.append("Port is set to: " + port +".......\n"); //print on server console
			}
		
		catch(Exception e){
			serverConsol.append("Please Enter A Valid Port.....\n");
		}
	}
}

