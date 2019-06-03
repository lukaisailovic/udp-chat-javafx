package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import shared.Notification;
import shared.NotificationStatus;

public class NotificationListener implements Runnable {
	

	private int notificationsPort = 32323;
	private String onlineStatusUpdateStartPattern = "---START STATUS UPDATE---";
	private String onlineStatusUpdateEndPattern = "---END STATUS UPDATE---";
	
	private String onlineUsersStartPattern = "---START ONLINE U UPDATE---";
	private String onlineUsersEndPattern = "---END ONLINE U UPDATE---";
	private String delimiter = ";";
	
	
	@Override
	public void run() {
		try {
			this.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void initialize() throws Exception {
		DatagramSocket socket = new DatagramSocket(this.notificationsPort);
		
		System.out.println("Server notification listener started on port "+this.notificationsPort);
		while(true){
			
			byte[] buffer = new byte[1500]; // MTU = 1500 bytes
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			InetAddress senderAddress = packet.getAddress();
			int senderPort = packet.getPort();
			
			String msg = new String(buffer).trim();
			if (msg.startsWith("N")) {
				this.handleNotification(msg,senderAddress,senderPort);
			} else if (msg.startsWith("M")) {
				try {
					this.handleMessage(msg, senderAddress,senderPort);
				} catch (Exception e) {
					System.out.println("Could not send message (Empty)");
				}
			}
			
					
		}
	}
	
	private void handleNotification(String msg, InetAddress senderAddress,int senderPort) throws Exception{
		ServerController controller = ServerController.getInstance();
		
		Notification n = Notification.deserialize(msg);
		
		User user = new User(n.getUsername(), senderAddress, n.getPort());
		
		NotificationStatus type = n.getStatus();
		if(type.equals(NotificationStatus.CONNECTED)) {
			controller.addUser(user);
		} else if (n.getStatus().equals(NotificationStatus.DISCONNECTED)) {
			controller.removeUser(user);	
		}
		
		for (User u : controller.getUsers()) {
			if (!u.equals(user)) {
				controller.sendMessage(u,this.buildNotificationMessage(user.getUsername(), type));
				System.out.println("Updating "+u.getUsername()+"'s list of online users");
			} 
			if (u.equals(user) && type.equals(NotificationStatus.CONNECTED)){
				System.out.println("sending online users list to "+u.getUsername());				
				controller.sendMessage(u, this.buildOnlineUsersList(user.getUsername()));
			}		
		}
				
		System.out.println("New notification from "+senderAddress+ " from port " +senderPort+n.toString());	
	}
	
	private String buildNotificationMessage(String username, NotificationStatus type) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.onlineStatusUpdateStartPattern);
		if (type.equals(NotificationStatus.CONNECTED)) {
			sb.append("CN");
		} else if (type.equals(NotificationStatus.DISCONNECTED)) {
			sb.append("DC");
		}
		sb.append(delimiter);
		sb.append(username);
		sb.append(this.onlineStatusUpdateEndPattern);
		return sb.toString();
	}
	
	private String buildOnlineUsersList(String username) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.onlineUsersStartPattern);
		for (User u : ServerController.getInstance().getUsers()) {
			if (!u.getUsername().equals(username)) {
				sb.append(u.getUsername());
				sb.append(";");
			}		
		}
		sb.append(this.onlineUsersEndPattern);
		return sb.toString();
	}
	
	private void handleMessage(String msg, InetAddress senderAddress,int senderPort) throws Exception{
		ServerController controller = ServerController.getInstance();
		String [] strArr = msg.split(delimiter);
		System.out.println("Server should forward data ["+strArr[3]+"] to "+strArr[2]+" sent by "+strArr[1]);
		String sender = strArr[1];
		String receiver = strArr[2];
		String data = strArr[3];
		
		for (User u : controller.getUsers()) {
			if (u.getUsername().equals(receiver)) {
				String msgToSend = sender + delimiter + data;
				controller.sendMessage(u, msgToSend);
				break;
			}
		}
		
	}

}
