/*
 * Class ChatServer
 * Stephen Brandon
 * November 2014
 */
package com.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ChatServer {
	
	private ServerSocket serverSocket;
	private int clientCount;
	private int chatRoomCount;
	private HashMap<Integer, ChatRoom> chatrooms = new HashMap<Integer, ChatRoom>();
	private static final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	
	//Constructor
	public ChatServer(ServerSocket serverSocket){
		this.serverSocket = serverSocket;
		this.clientCount = 0;
		this.chatRoomCount = 0;
	}
	
	//Start the server
	public void start(){
		try{
			while(true){
				if(executorService.getActiveCount() < executorService.getMaximumPoolSize()){
					Socket socket = serverSocket.accept();
					executorService.execute(new ChatWorker(socket, clientCount, this));
					addClient();
				}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	//Add a client to a chatRoom
	public void joinChatRoom(String chatroomName, Client client){
		ChatRoom chatroom = findChatroomByName(chatroomName);
		//If the chat room exists add the client to it, else create a new one.
		if(chatroom != null){
			chatroom.addClient(client);
		}
		else{
			chatroom = new ChatRoom(chatroomName, chatRoomCount);
			chatroom.addClient(client);
			chatrooms.put(chatroom.getRoomRef(), chatroom);
			chatRoomCount++;
		}
	}
	
	//Find chatroom by name
	public ChatRoom findChatroomByName(String name){
		Iterator<Entry<Integer, ChatRoom>> iterator = chatrooms.entrySet().iterator();
		while (iterator.hasNext()){
			ChatRoom chatroom = iterator.next().getValue();
			if(chatroom.getName().equals(name)){
				return chatroom;
			}
		}
		return null;
	}
	
	//Remove a client from a chatRoom
	public void leaveChatRoom(int chatroomId, int clientId){
		ChatRoom chatroom = chatrooms.get(chatroomId);
		chatroom.removeClient(clientId);
	}
	
	//Send message to a chatRoom
	public boolean chat(int chatroomId, int clientId, String message){
		ChatRoom chatroom = chatrooms.get(chatroomId);
		if(chatroom != null){
			chatroom.chat(clientId, message);
			return true;
		}
		return false;
	}
	
	//Increment client count
	public void addClient(){
		clientCount++;
	}
	
	//Stop the server and kill all the threads
	public void killService(){
		try {
			executorService.shutdownNow();
			serverSocket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ChatRoom getChatRoom(int chatRoomId){
		chatrooms.get(chatRoomId);
		return null;
	}
	
	//Main method
	public static void main(String[] args) throws IOException {
		int portNumber = Integer.parseInt(args[0]);
		ChatServer server = new ChatServer(new ServerSocket(portNumber));
		server.start();
	}
	
}