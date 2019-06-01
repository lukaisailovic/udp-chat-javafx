package controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import client.ClientMessageListener;
import client.Message;
import shared.Notification;
import shared.NotificationStatus;
import view.ChatView;
import view.LoginView;

public class ClientController {
	
		private int notificationsPort = 32323;
		private String serverAddress = "localhost";
		
		private String username = null;
	
		private LoginView loginView;
		private ChatView chatView;

		private static ClientController instance = null;
		private ClientMessageListener clientListener = new ClientMessageListener();
		private boolean isMessageListenerInitialized = false;
		
		
		private ArrayList<String> onlineUsers = new ArrayList<>();
		
		private String recipient = null;
		
		private ClientController() {
			
		}
		
		
		public void setViews(LoginView lv, ChatView cv) {
			this.loginView = lv;
			this.chatView = cv;
		}
		
		public static ClientController getInstance() {
			if(instance == null) {
				instance = new ClientController();
			}
			return instance;
		}
		
		public void startMessageListener(){
			if (isMessageListenerInitialized) {
				return;
			}
			Thread clientListenerThread = new Thread(this.clientListener);
			clientListenerThread.start(); // start thread in the background
			this.isMessageListenerInitialized = true;
		}
		
		public void sendMessage(Message msg,String recipientPassed) throws Exception{
			String delimiter = ";";
			String data = "M"+delimiter+this.username+delimiter+this.recipient+delimiter+msg.getData();
			DatagramSocket socket = new DatagramSocket(); 
			byte[] buffer = data.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.serverAddress), this.notificationsPort); 																												// paketa
			socket.send(packet);	
			socket.close();
		}
		
		public void notifyServer(NotificationStatus type) throws Exception{
			DatagramSocket socket = new DatagramSocket(); 
			
			Notification n = new Notification(this.username, type,this.clientListener.getPort());
			
			String msg = n.serialize();
		
			byte[] buffer = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.serverAddress), this.notificationsPort); 																												// paketa
			socket.send(packet);	
			socket.close();
		}
		
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
		public void showChatView() {
			loginView.hide();
			chatView.loadData();
			chatView.show();
		}


		public LoginView getLoginView() {
			return loginView;
		}


		public void setLoginView(LoginView loginView) {
			this.loginView = loginView;
		}


		public ChatView getChatView() {
			return chatView;
		}


		public void setChatView(ChatView chatView) {
			this.chatView = chatView;
		}
		
		public void addUserToOnlineUsers(String username) {
			if (!username.isEmpty()) {
				this.onlineUsers.add(username);
				this.chatView.addUserToListOfOnlineUsers(username);
			}
		}
		public void removeUserFromOnlineUsers(String username) {
			if (!username.isEmpty()) {
				this.onlineUsers.remove(username);
				this.chatView.removeUserFromListOfOnlineUsers(username);
			}
			
		}


		public ArrayList<String> getOnlineUsers() {
			return onlineUsers;
		}

		
	
		
		public String getRecipient() {
			return recipient;
		}


		public void setRecipient(String recipient) {
			this.recipient = recipient;
			System.out.println("New recipient is "+this.recipient);
		}
		
		
		
		
		
}
