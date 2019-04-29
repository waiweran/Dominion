package computerPlayer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Wrapper for the socket used for SocketPlayer neural nets. 
 * @author Nathaniel
 * @version 04-29-2019
 */
public class NetSocket {
	
	private static final String ADDRESS = "localhost";
	private static final int PORT = 12345;
		
	private Socket socket;
	private Scanner input;
	private PrintStream output;

	/**
	 * Initializes the NetSocket object.
	 * @throws IOException if the socket cannot connect.
	 */
	public NetSocket() throws IOException {
		socket = new Socket();
		socket.setSoTimeout(1000);
		socket.connect(new InetSocketAddress(ADDRESS, PORT));
		output = new PrintStream(socket.getOutputStream());
		input = new Scanner(socket.getInputStream());
	}
	
	/**
	 * Sends a String over the socket.
	 * @param jsontext the String to send.
	 */
	public synchronized void send(String jsontext) {
		output.println(jsontext);
		output.flush();
	}
	
	/**
	 * Blocking call-and-response over the socket.
	 * @param jsontext String to send.
	 * @return Response from socket.
	 */
	public synchronized String respond(String jsontext) {
		send(jsontext);
		while(!input.hasNextLine());
		return input.nextLine();
	}
	
	/**
	 * Closes the NetSocket.
	 */
	public void close() {
		output.close();
		input.close();
		try {
			socket.close();
		} catch (IOException e1) {}
	}

}
