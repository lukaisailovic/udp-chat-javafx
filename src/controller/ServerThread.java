package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerThread implements Runnable {
		
	@Override
	public void run() {
		try {
			//this.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void initialize() throws Exception  {
		DatagramSocket socket = new DatagramSocket(2019); // serverski socket
		Scanner tastatura = new Scanner(System.in);
		System.out.println("Server listening...");
		while(true){
			
			byte[] buffer = new byte[1500]; // MTU = 1500 bytes
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			// primanje paketa
			socket.receive(packet); // puni se buffer paketa
	
			// saznajemo info iz primljenog paketa:
			InetAddress adresa_sendera = packet.getAddress();
			int port_sendera = packet.getPort();
			
			String poruka = new String(buffer).trim();
			System.out.println("Sender " + adresa_sendera 
					+ " sa porta " + port_sendera + ": " + poruka);
			
			// slanje potvrde
			System.out.print("Unesi poruku: ");
			poruka = tastatura.nextLine();
			buffer = poruka.getBytes();
			packet = new DatagramPacket(buffer, buffer.length, adresa_sendera, port_sendera);
			socket.send(packet);
			
			System.out.println("Poslato: " + poruka);
		
		}

	}
	
	public void test(String msg) throws Exception {
		DatagramSocket socket = new DatagramSocket(); // klijentski socket
		byte[] buffer = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, 
				InetAddress.getByName("localhost"), 2019); // pakovanje																														// paketa
		socket.send(packet);	
		socket.close();
		
	}

}
