import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable
{

    // Made some more checges on new patch
    // MADE CHANGE on TEST BRACH
	JTextField textField;
	Socket socket;
	String name;
	public static JTextArea textArea;
	static PrintWriter sendMessage;
	public static Scanner reader;
	public static String text;
	public static String NAME, TBMESSAGE;
	public static int PORT;
	public static InetAddress IP;
	public static JTextField portField;
	public static JTextField IPField;
	public static JTextField nameField;
	public static JTextArea messageDialog;
	public static JTextField messageTF;
	public static JButton connectButton;
	
	public static void main(String[] args)
	{
		// Create new instance of the GUI
		new ChatClient().GUI();
	}
	
	public void GUI()
	{
		//Font setup
		Font font = new Font("sansserif", Font.PLAIN, 15);
		
		//Create Text Field for user to input the message which they want to send,
		//The user has a choice of pressing the enter key on their keyboard or pressing the send button.
		textField = new JTextField();
		textField.setFont(font);
		textField.addActionListener(l -> {

                    try {
                        TBMESSAGE = textField.getText(); //Save message in a variable.
                        textField.setText(""); //Clear the text field.
                        textField.requestFocus();
                        sendMessage.println(NAME + ": " + TBMESSAGE); //Send to server

                    } catch (Exception g) {
                        textArea.append("You must be connected to a server \n");
                    }
                });
		
		//Button and actionListener for the send button.
		//This does the same thing as the method above, when the user presses the send button instead.
		JButton sendButton = new JButton("SEND");
		sendButton.setFont(font);
		sendButton.addActionListener(l -> {


				try {
					TBMESSAGE = textField.getText();
					textField.setText("");
					textField.requestFocus();
					sendMessage.println(NAME + ": " + TBMESSAGE);

				} catch (Exception g) {
					textArea.append("You must be connected to a server \n");
				}

		});
		
		//Create a new container and add the textField and button which we made above.
		Container window = new JPanel();
		window.setLayout(new BorderLayout());
		window.add(BorderLayout.CENTER, textField);
		window.add(BorderLayout.EAST, sendButton);
		
		//Create a new textArea and create a new ScrollPane
		//Add textArea to the scrollPane to make it scrollAble.
		textArea = new JTextArea();
		textArea.setFont(font);
		JScrollPane scroll = new JScrollPane(textArea);
		
		//Create another new container with a GridLayout.
		Container windowN = new JPanel();
		windowN.setLayout(new GridLayout(4, 2));

		//Create a new JtextLabel called set Port
		JLabel portL = new JLabel("Set Port:");
		//Create a new JTextField and display default information.
		portField = new JTextField();
		portField.setText("1991");

		//Create a new JtextLabel called set IP
		JLabel IPL = new JLabel("Set IP:");
		//Create a new JTextField and display default information.
		IPField = new JTextField();
		IPField.setText("localhost");

		//Create a new JtextLabel called name
		JLabel nameL = new JLabel("Name:");
		//Create a new JText Field and display default information.
		nameField = new JTextField();
		nameField.setText("Annoymous");

		//Create a new button called Connect To server.
		//Add an action Listener to the button.
		connectButton = new JButton("Connect To Server");
		connectButton.addActionListener(l -> {
				
				setInfo(); //Execute setInto method

		});

		//adds the elements to a windowN container
		windowN.add(nameL);
		windowN.add(nameField);
		windowN.add(portL);
		windowN.add(portField);
		windowN.add(IPL);
		windowN.add(IPField);
		windowN.add(connectButton);
		
		//Add the 2 containers and the scollPane to the main contentPane.
		getContentPane().add(BorderLayout.NORTH, windowN);
		getContentPane().add(BorderLayout.CENTER, scroll);
		getContentPane().add(BorderLayout.SOUTH, window);
		
		setTitle("Client");
		setSize(400, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	// This is the thread
	public void run() {

		try {
			// setup the socket, printWriter for sending, and Scanner processing input.
			socket = new Socket(IP, PORT);
			sendMessage = new PrintWriter(socket.getOutputStream(), true);
			reader = new Scanner(socket.getInputStream());
			textArea.append("Client Connected To Server......\n");
			
			//prints the messages on the clients console
			while (true) {
				text = reader.nextLine();
				textArea.append(text+"\n");
			}

		} catch (Exception e) {
			textArea.append("No Server On This IP or Port \n");
		}
	}
	
	// gets the data in the interface textFields and stores them in a variable to be used later.
	// Starts the clientThread.
	public static void setInfo() {
		try {

			//get from user input of the interface, convert if needed and save in variable.
			NAME = nameField.getText(); 
			PORT = Integer.parseInt(portField.getText());
			IP = InetAddress.getByName(IPField.getText());

			//Start thread
			new Thread(new ChatClient()).start();
			
		} catch (Exception e) {
			textArea.append("Please enter a valid IP address and PORT \n");
		}

	}
}
