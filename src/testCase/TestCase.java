package testCase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestCase {

	private DataOutputStream sendMessage;
	private BufferedReader reader;
	private int portNumber;
	private boolean running;
	private Socket socket;
	
	public TestCase(int portNumber){
		new BufferedReader(new InputStreamReader(System.in));
		this.portNumber = portNumber;
		try{
			this.socket = new Socket("localhost", portNumber);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void start(){
		System.out.println("Client is running. Port No. " + portNumber);
		running = true;
		testJoin();
	}
	
	public void testJoin(){
		try
		{
			String join = "JOIN_CHATROOM: cats\nCLIENT_IP: 0\nPORT: 0\nCLIENT_NAME: Stephen\n";
			sendMessage = new DataOutputStream(socket.getOutputStream());
			sendMessage.writeBytes(join);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("FROM SERVER: " + reader.readLine());
			System.out.println("FROM SERVER: " + reader.readLine());
			System.out.println("FROM SERVER: " + reader.readLine());
			System.out.println("FROM SERVER: " + reader.readLine());
			System.out.println("FROM SERVER: " + reader.readLine());
		}
		catch(Exception e)
		{
			running = false;
			System.out.println("Cannot Connect With Server");
		}
	}
	
	public static void main(String args[]) throws Exception {
		int portNumber = 6789;
		TestCase client = new TestCase(portNumber);
		client.start();
	}
}
