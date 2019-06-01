package client;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
	private String data;
	private String sender;
	private String timestamp;
	
	public Message(String data, String sender) {
		super();
		this.data = data;
		this.sender = sender;
		LocalDateTime datetime = LocalDateTime.now();		
		this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(datetime);
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	@Override
	public String toString() {
		return "["+timestamp.toString()+"]"+" "+sender+": "+data;
	}
	
	
}
