package com.chatserver;

import java.io.PrintWriter;
import java.util.HashMap;

public class ChatRoom {

	private String name;
	private int roomRef;
	private HashMap<String, Client> clients = new HashMap<String, Client>();
	
	public ChatRoom(String name, int roomRef){
		this.name = name;
		this.roomRef = roomRef;
	}
	
	//Add a client to the ChatRoom
	public void addClient(Client client){
		clients.put(client.getName(), client);
		String joined = "JOINED_CHATROOM: " + name + "\nSERVER_IP: " + client.getSocket().getLocalAddress().toString().substring(1) + "\nPORT: " + client.getSocket().getLocalPort() + "\nROOM_REF: " + roomRef + "\nJOIN_ID: " + client.getId() + "\n";
		sendMessage(joined, client);
	}
	
	//Remove a client from the ChatRoom
	public void removeClient(String clientName){
		clients.remove(clientName);
	}
	
	//Send a message to a client
	public void sendMessage(String message, Client client){
		try{
			PrintWriter writer = new PrintWriter(client.getSocket().getOutputStream(), true);
			writer.println(message);
		}
		catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	//Getters & Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Client> getClients() {
		return clients;
	}

	public void setClients(HashMap<String, Client> clients) {
		this.clients = clients;
	}

	public int getRoomRef() {
		return roomRef;
	}

	public void setRoomRef(int roomRef) {
		this.roomRef = roomRef;
	}
	
}
