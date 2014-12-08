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
	public void leaveChatroom(String chatroomId, String clientId){
		int chatroom = Integer.parseInt(chatroomId);
		int client = Integer.parseInt(clientId);
		server.leaveChatRoom(chatroom, client);
	}
	
	//Chat
	public void chat(String chatroomId, String clientId, String message){
		int chatroom = Integer.parseInt(chatroomId);
		int client = Integer.parseInt(clientId);
		server.chat(chatroom, client, message);
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
					//Do nothing
				}
				else if(lines.get(0).startsWith("JOIN_CHATROOM")){
					if(lines.size() >= 3){
						String[] line0 = lines.get(0).split(":");
						String chatroomName = line0[1].substring(1);
						String[] line3 = lines.get(3).split(":");
						String clientName = line3[1].substring(1);
						joinChatroom(chatroomName, clientName);
					}
					else{
						//return error
					}
				}
				else if(lines.get(0).startsWith("LEAVE_CHATROOM")){
					String[] line0 = lines.get(0).split(":");
					String chatroomId = line0[1].substring(1);
					String[] line1 = lines.get(1).split(":");
					String joinId = line1[1].substring(1);
					leaveChatroom(chatroomId, joinId);
				}
				else if(lines.get(0).startsWith("CHAT")){
					String[] line0 = lines.get(0).split(":");
					String chatroomId = line0[1].substring(1);
					String[] line1 = lines.get(1).split(":");
					String joinId = line1[1].substring(1);
					String[] line3 = lines.get(3).split(":");
					String message = line3[1].substring(1);
					chat(chatroomId, joinId, message);
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