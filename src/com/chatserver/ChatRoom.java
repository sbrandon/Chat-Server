package com.chatserver;

import java.io.PrintWriter;
import java.util.HashMap;

public class ChatRoom {

	private String name;
	private int roomRef;
	private HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	
	public ChatRoom(String name, int roomRef){
		this.name = name;
		this.roomRef = roomRef;
	}
	
	//Add a client to the ChatRoom
	public void addClient(Client client){
		clients.put(client.getId(), client);
		String joined = "JOINED_CHATROOM: " + name + "\nSERVER_IP: " + client.getSocket().getLocalAddress().toString().substring(1) + "\nPORT: " + client.getSocket().getLocalPort() + "\nROOM_REF: " + roomRef + "\nJOIN_ID: " + client.getId() + "\n";
		sendMessage(joined, client);
	}
	
	//Remove a client from the ChatRoom
	public void removeClient(int clientId){
		Client client = clients.get(clientId);
		clients.remove(clientId);
		String left = "LEFT_CHATROOM: " + roomRef + "\nJOIN_ID: " + client.getId() + "\n";
		sendMessage(left, client);
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

	public HashMap<Integer, Client> getClients() {
		return clients;
	}

	public void setClients(HashMap<Integer, Client> clients) {
		this.clients = clients;
	}

	public int getRoomRef() {
		return roomRef;
	}

	public void setRoomRef(int roomRef) {
		this.roomRef = roomRef;
	}
	
}
