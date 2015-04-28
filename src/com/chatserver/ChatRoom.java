package com.chatserver;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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
		//Inform other clients of new client arrival.
		chat(client.getId(), client.getName() + " has joined the chatroom.");
	}
	
	//Remove a client from the ChatRoom
	public void removeClient(int clientId){
		Client client = clients.get(clientId);
		if(client != null){
			String left = "LEFT_CHATROOM: " + roomRef + "\nJOIN_ID: " + client.getId() + "\n";
			sendMessage(left, client);
			clients.remove(clientId);
		}
		else{
			//TODO Send error
		}
	}
	
	//Chat send a message to all clients subscribed to the ChatRoom
	public void chat(int clientId, String message){
		Iterator<Entry<Integer, Client>> iterator = clients.entrySet().iterator();
		Client sender = clients.get(clientId);
		if(sender != null){
			String sendString = "CHAT: " + roomRef + "\nCLIENT_NAME: " + sender.getName() + "\nMESSAGE: " + message + "\n";
			while (iterator.hasNext()){
				Client recepient = iterator.next().getValue();
				sendMessage(sendString, recepient);
			}
		}
		else{
			//TODO Send error
		}
	}
	
	//Send a message to a client
	public void sendMessage(String message, Client client){
		try{
			PrintWriter writer = new PrintWriter(client.getSocket().getOutputStream(), true);
			System.out.println(message);
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
