package shared;

public class Notification {
	
		private static String delimiter = ";";
	
		private String username;
		private NotificationStatus status;
		private int port;
		public Notification(String username, NotificationStatus status, int port) {
			super();
			this.username = username;
			this.status = status;
			this.port = port;
		}
		
		
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			sb.append("N");
			sb.append(delimiter);
			sb.append(username);
			sb.append(delimiter);
			sb.append(this.status.toString());
			sb.append(delimiter);
			sb.append(port);
			return sb.toString();
		}
		
		public static Notification deserialize(String serialized) {
			String[] strArr  = serialized.split(delimiter);
			//System.out.println("Parsed = " +strArr.length);
			return new Notification(strArr[1], NotificationStatus.valueOf(strArr[2]),Integer.parseInt(strArr[3]));
		}

		@Override
		public String toString() {
			return "Notification [username=" + username + ", status=" + status + ", port=" + port + "]";
		}


		public String getUsername() {
			return username;
		}


		public void setUsername(String username) {
			this.username = username;
		}


		public NotificationStatus getStatus() {
			return status;
		}


		public void setStatus(NotificationStatus status) {
			this.status = status;
		}


		public int getPort() {
			return port;
		}


		public void setPort(int port) {
			this.port = port;
		}
		
		
		
		
		
}
