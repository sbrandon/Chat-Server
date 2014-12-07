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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ChatServer {
	
	private ServerSocket serverSocket;
	private int clientCount;
	private int chatRoomCount;
	private HashMap<String, ChatRoom> chatrooms = new HashMap<String, ChatRoom>();
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
		ChatRoom chatRoom = chatrooms.get(chatroomName);
		//If the chat room exists add the client to it, else create a new one.
		if(chatRoom != null){
			chatRoom.addClient(client);
		}
		else{
			ChatRoom chat = new ChatRoom(chatroomName, chatRoomCount);
			chatrooms.put(chatroomName, chat);
			chat.addClient(client);
			chatRoomCount++;
		}
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
	
	//Main method
	public static void main(String[] args) throws IOException {
		int portNumber = Integer.parseInt(args[0]);
		ChatServer server = new ChatServer(new ServerSocket(portNumber));
		server.start();
	}
	
}