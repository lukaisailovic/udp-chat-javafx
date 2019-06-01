package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import controller.ClientController;

public class ClientMessageListener implements Runnable {
	
	private int port = 2019;
	private String onlineStatusUpdateStartPattern = "---START STATUS UPDATE---";
	private String onlineStatusUpdateEndPattern = "---END STATUS UPDATE---";
	private String delimiter = ";";
	
	
	@Override
	public void run() {
		this.initialize();

	}
	
	private void initialize() {
		DatagramSocket socket; // listener socket	

		while(true) {
			try {
				socket = new DatagramSocket(this.port);
				break;
			} catch (SocketException e) {
				this.port++;
			}
		}
		
		System.out.println("Client listener initialized on port "+port);
		while(true){
			
			byte[] buffer = new byte[1500]; // MTU = 1500 bytes
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			// primanje paketa
			try {
				socket.receive(packet);
				InetAddress senderAddress = packet.getAddress();
				int senderPort = packet.getPort();
				
				String receivedText = new String(buffer).trim();
				
				if(receivedText.startsWith(this.onlineStatusUpdateStartPattern)) {
					this.handleOnlineStatusUpdate(receivedText);
				} else {
					
					String[] strArr = receivedText.split(delimiter);
					String data = strArr[1];
					String recipient = strArr[0];
					System.out.println("NEW DATA: "+receivedText);
					if (recipient.equals(ClientController.getInstance().getRecipient())) {
						Message msg = new Message(data, recipient); // 				
						ClientController.getInstance().getChatView().addMessage(msg,false); // handle received msg
						System.out.println("New message from "+senderAddress+ " from port " +senderPort+ " ["+msg+"]");
					}
										
				}
				
				
			} catch (Exception e) {
				System.out.println("Fail");
				e.printStackTrace();
				continue;
			} 
		
			
		}
	}
	
	
	public void handleOnlineStatusUpdate(String data) {
		int startIndex = data.indexOf(this.onlineStatusUpdateStartPattern);
		int endIndex = data.indexOf(this.onlineStatusUpdateEndPattern);
		System.out.println(data);
		String truncatedData = data.substring(startIndex+this.onlineStatusUpdateStartPattern.length(), endIndex);
		
		String[] strArr = truncatedData.split(this.delimiter);
		String type = strArr[0];
		String username = strArr[1];
		
		if (type.equals("CN")) {
			//ClientController.getInstance().getChatView().addOnlineUser(username);
			System.out.println("Should add "+strArr[1]);
		} 
		if (type.equals("DC")) {
			//ClientController.getInstance().getChatView().removeOnlineUser(username);
			System.out.println("Should remove "+strArr[1]);
		}
		
		
	}

	public int getPort() {
		return port;
	}
	
	

}
