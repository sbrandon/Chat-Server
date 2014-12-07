/*
 * Class ChatWorker is the thread created by the ChatServer.
 * Stephen Brandon
 * October 2014
 */
package com.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatWorker implements Runnable {
	
	private Socket socket;
	private ChatServer server;
	private int joinId;
	private boolean killServer;
	
	//Constructor
	public ChatWorker(Socket socket, int clientId, ChatServer server){
		this.socket = socket;
		this.server = server;
		this.joinId = clientId;
		this.killServer = false;
	}
	
	//Join
	public void joinChatroom(String chatroom, String clientName){
		Client client = new Client(joinId, clientName, socket);
		server.joinChatRoom(chatroom, client);
	}
	
	//Leave
	public void leaveChatroom(){
		
	}
	
	//Chat
	public void Chat(){
		
	}
	
	public String heloResponse(String command){
		return command + "\nIP:" + socket.getLocalAddress().toString().substring(1) + "\nPort:" + socket.getLocalPort() + "\nStudentID:14303108\n";  
	}
	
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			ArrayList<String> lines;
			while(!killServer){
				lines = new ArrayList<String>();
				while(reader.ready()){
					lines.add(reader.readLine());
				}
				if(lines.isEmpty()){
					//return error
				}
				else if(lines.get(0).startsWith("JOIN_CHATROOM")){
					if(lines.size() >= 3){
						String[] line1 = lines.get(0).split(":");
						String chatroomName = line1[1].substring(1);
						String[] line4 = lines.get(3).split(":");
						String clientName = line4[1].substring(1);
						joinChatroom(chatroomName, clientName);
					}
					else{
						//return error
					}
				}
				else if(lines.get(0).startsWith("LEAVE_CHATROOM")){
					
				}
				else if(lines.get(0).startsWith("CHAT")){
					
				}
				else if(lines.get(0).startsWith("HELO")){
					writer.println(heloResponse(lines.get(0)));			
				}
				else if(lines.get(0).startsWith("KILL_SERVICE")){
					killServer = true;
					socket.close();
					server.killService();
				}
				else{
					//Do Nothing
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}