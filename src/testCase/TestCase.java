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
		try
		{
			System.out.println("Client is running. Port No. " + portNumber);
			System.out.println();
			sendMessage = new DataOutputStream(socket.getOutputStream());
			running = true;
			testJoin();
			testChat();
			testLeave();
		}
		catch(Exception e)
		{
			running = false;
			System.out.println("Cannot Connect With Server");
		}	
	}
	
	public void testJoin(){
		try
		{
			String join = "JOIN_CHATROOM: cats\nCLIENT_IP: 0\nPORT: 0\nCLIENT_NAME: Stephen\n";
			sendMessage.writeBytes(join);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(reader.readLine());
			System.out.println(reader.readLine());
			System.out.println(reader.readLine());
			System.out.println();
		}
		catch(Exception e)
		{
			running = false;
			System.out.println("Cannot Connect With Server");
		}
	}
	
	public void testChat(){
		try
		{
			String leave = "CHAT: 0\nJOIN_ID: 0\nCLIENT_NAME: Stephen\nMESSAGE: this is a test message\n\n";
			sendMessage.writeBytes(leave);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(reader.readLine());
			System.out.println(reader.readLine());
			System.out.println(reader.readLine());
			System.out.println();
		}
		catch(Exception e)
		{
			running = false;
			System.out.println("Cannot Connect With Server");
		}
	}
	
	public void testLeave(){
		try
		{
			String leave = "LEAVE_CHATROOM: 0\nJOIN_ID: 0\nCLIENT_NAME: Stephen\n";
			sendMessage.writeBytes(leave);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(reader.readLine());
			System.out.println(reader.readLine());
			System.out.println();
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
