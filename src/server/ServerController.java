package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import shared.Notification;

public class ServerController {
	
	private static ServerController instance = null;
	private NotificationListener notificationListener = new NotificationListener();
	
	
	private ArrayList<User> users = new ArrayList<>();
	
	

	
	private ServerController() {
		this.initializeNotificationListener();
	}
	
	public static ServerController getInstance() {
		if (instance == null) {
			instance = new ServerController();
		}
		return instance;
	}
	
	
	private void initializeNotificationListener() {
		Thread notificationsThread = new Thread(this.notificationListener);
		notificationsThread.start();
	}
	
	
	
	
	
	public void sendMessage(User user, String msg) throws Exception { 
		DatagramSocket socket = new DatagramSocket(); 
		byte[] buffer = msg.getBytes();	
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, user.getAddress(), user.getPort()); 																												// paketa
		socket.send(packet);	
		socket.close();
	}

	public ArrayList<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		this.users.add(user);
		System.out.println(this.getUsers());
	}
	public void removeUser(User user) {
		this.users.remove(user);
	}
	
	
	
}
